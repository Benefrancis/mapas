import os
import sys

# ===============================
# CONFIGURAÇÕES
# ===============================

EXT_LINGUAGEM_PADRAO = {
    ".cs": ("csharp", "//"),
    ".ts": ("typescript", "//"),
    ".html": ("html", "block"),
    ".scss": ("scss", "/* */"),
    ".css": ("css", "/* */"),
    ".json": ("json", "//"),
    ".sql": ("sql", "--"),
}

DIRS_IGNORE = {
    "bin", "obj", ".vs",
    "node_modules", "dist", ".angular", "coverage",
    ".git", "__pycache__", "logs"
}

EXT_IGNORE = {
    ".dll", ".exe", ".png", ".jpg", ".jpeg",
    ".gif", ".zip", ".rar", ".7z", ".pdf"
}

MAX_FILE_SIZE = 5 * 1024 * 1024

NOME_ARQUIVO_MD = "DOCUMENTACAO.md"
NOME_ARQUIVO_MD_CENTRAL = "DOCUMENTACAO_TODOS_PROJETOS.md"

total_linhas = 0
total_arquivos = 0


# ===============================
# AUXILIARES
# ===============================

def inferir_linguagem(ext):
    ext = ext.lower()
    if ext in EXT_LINGUAGEM_PADRAO:
        return EXT_LINGUAGEM_PADRAO[ext]
    if ext.startswith(".cs"):
        return ("csharp", "//")
    if ext.startswith(".ts"):
        return ("typescript", "//")
    if ext.startswith(".html"):
        return ("html", "block")
    return ("text", "#")


def detectar_tipo_projeto(dir_path):
    arquivos = os.listdir(dir_path)
    if any(f.endswith(".csproj") for f in arquivos):
        return "backend"
    if "angular.json" in arquivos:
        return "frontend"
    return "outro"


def classificar_backend(caminho):
    caminho_lower = caminho.lower()

    if "controller" in caminho_lower:
        return "Controllers"
    if "application" in caminho_lower:
        return "Application"
    if "domain" in caminho_lower:
        return "Domain"
    if "infrastructure" in caminho_lower:
        return "Infrastructure"
    if "dto" in caminho_lower:
        return "DTOs"
    return "Outros"


def classificar_frontend(caminho):
    caminho_lower = caminho.lower()

    if "component" in caminho_lower:
        return "Components"
    if "service" in caminho_lower:
        return "Services"
    if "guard" in caminho_lower:
        return "Guards"
    if "interceptor" in caminho_lower:
        return "Interceptors"
    if "model" in caminho_lower:
        return "Models"
    return "Outros"


def is_text_file(file_path):
    global total_arquivos

    nome = os.path.basename(file_path)

    if nome.endswith(".spec.ts"):
        return False
    if nome.endswith(".module.ts"):
        return False
    if nome.startswith("environment"):
        return False

    ext = os.path.splitext(file_path)[1].lower()

    if ext in EXT_IGNORE:
        return False

    if os.path.getsize(file_path) > MAX_FILE_SIZE:
        return False

    total_arquivos += 1
    return True


# ===============================
# GERAÇÃO
# ===============================

def gerar_documentacao_diretorio(projeto_dir):
    global total_linhas

    tipo = detectar_tipo_projeto(projeto_dir)

    estrutura = {}

    for root, dirs, files in os.walk(projeto_dir):
        dirs[:] = [d for d in dirs if d not in DIRS_IGNORE]

        for arquivo in files:
            caminho_arquivo = os.path.join(root, arquivo)

            if not is_text_file(caminho_arquivo):
                continue

            ext = os.path.splitext(arquivo)[1]
            linguagem, tipo_comentario = inferir_linguagem(ext)
            caminho_relativo = os.path.relpath(caminho_arquivo, projeto_dir)

            try:
                with open(caminho_arquivo, "r", encoding="utf-8", errors="ignore") as f:
                    conteudo = f.read()
                    total_linhas += conteudo.count("\n")
            except:
                continue

            if tipo == "backend":
                camada = classificar_backend(caminho_relativo)
            elif tipo == "frontend":
                camada = classificar_frontend(caminho_relativo)
            else:
                camada = "Geral"

            if camada not in estrutura:
                estrutura[camada] = []

            if tipo_comentario == "block":
                comentario_inicio = f"<!-- {caminho_relativo} -->"
            else:
                comentario_inicio = f"{tipo_comentario} {caminho_relativo}"

            bloco = (
                f"### {caminho_relativo}\n\n"
                f"```{linguagem}\n"
                f"{comentario_inicio}\n"
                f"{conteudo}\n"
                f"```\n\n"
            )

            estrutura[camada].append(bloco)

    caminho_md = os.path.join(projeto_dir, NOME_ARQUIVO_MD)

    with open(caminho_md, "w", encoding="utf-8") as f:
        f.write(f"# Documentação do Projeto {os.path.basename(projeto_dir)}\n\n")
        f.write(f"**Tipo:** {tipo.upper()}\n\n")

        for camada in sorted(estrutura.keys()):
            f.write(f"## {camada}\n\n")
            for bloco in estrutura[camada]:
                f.write(bloco)

    print(f"📄 Gerado estruturado: {caminho_md}")

    return estrutura


# ===============================
# LIMPEZA
# ===============================

def limpar_documentacao(raiz):
    for root, dirs, files in os.walk(raiz):
        dirs[:] = [d for d in dirs if d not in DIRS_IGNORE]

        for f in files:
            if f == NOME_ARQUIVO_MD or f == NOME_ARQUIVO_MD_CENTRAL:
                try:
                    os.remove(os.path.join(root, f))
                except:
                    pass


# ===============================
# MAIN
# ===============================

def main():
    global total_linhas, total_arquivos

    if len(sys.argv) > 1 and sys.argv[1] == "limpar":
        raiz = sys.argv[2] if len(sys.argv) > 2 else "."
        limpar_documentacao(raiz)
        print("🗑 Documentação removida.")
        sys.exit(0)

    raiz = sys.argv[1] if len(sys.argv) > 1 else "."
    raiz = os.path.abspath(raiz)

    conteudo_total = []

    for item in sorted(os.listdir(raiz)):
        item_path = os.path.join(raiz, item)

        if os.path.isdir(item_path) and item not in DIRS_IGNORE:
            print(f"\n🚀 Processando: {item}\n")

            estrutura = gerar_documentacao_diretorio(item_path)

            conteudo_total.append(f"# Projeto: {item}\n\n")

            for camada in sorted(estrutura.keys()):
                conteudo_total.append(f"## {camada}\n\n")
                conteudo_total.extend(estrutura[camada])

            conteudo_total.append("\n---\n\n")

    caminho_md_central = os.path.join(raiz, NOME_ARQUIVO_MD_CENTRAL)

    with open(caminho_md_central, "w", encoding="utf-8") as f:
        f.write("# Documentação Centralizada\n\n")
        f.writelines(conteudo_total)
        f.write("\n---\n")
        f.write(f"\nTotal de arquivos: {total_arquivos}\n")
        f.write(f"Total de linhas: {total_linhas}\n")

    print(f"\n📘 Central gerado em: {caminho_md_central}")
    print(f"📊 Arquivos: {total_arquivos} | Linhas: {total_linhas}")


if __name__ == "__main__":
    main()