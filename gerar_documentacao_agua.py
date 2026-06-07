import os
import re
import sys

# Extensões conhecidas para linguagens
EXT_LINGUAGEM_PADRAO = {
    ".java": ("java", "//"),
    ".xml": ("xml", "block"),
    ".html": ("html", "block"),
    ".htm": ("html", "block"),
    ".yml": ("yaml", "#"),
    ".yaml": ("yaml", "#"),
    ".properties": ("properties", "#"),
    ".sql": ("sql", "--"),
    ".ts": ("typescript", "//"),
    ".js": ("javascript", "//"),
    ".vue": ("vue", "<!-- -->"),
    ".py": ("python", "#"),
    ".css": ("css", "/* */"),
    ".scss": ("scss", "/* */"),
    ".json": ("json", "//"),
}

# Extensões a ignorar (binários, imagens, grandes)
EXT_IGNORE = {".zip", 'mvnw', 'mvnw.cmd', ".pdn", ".txt", ".rar", ".tar", ".gz", ".7z", ".jar", ".class", ".exe",
              ".dll", ".png", ".jpg", ".jpeg", ".gif", ".csv", ".ico", ".bmp", ".pdf", ".doc", ".docx", ".md", ".svg",
              ".ico", ".webp", ".md", ".editorconfig", '.gitattributes', '.editorconfig', '.mvn', '.npm', '.DS_Store','.map','.ods','.sql','.sqlc', '.psd'}

DIRS_IGNORE = {'.git', 'target', '.editorconfig', 'out', 'dist', 'build', 'mvnw', 'mvnw.cmd', 'nginx', 'deckfiles', 'documentacao',
               'node_modules', 'venv', '__pycache__', 'data', 'logs', 'q8-data', 'q8-logs', 'q8-files', '.idea', '.vscode',
               'deckfiles', 'documentacao','2022', 'antigo', 'photoshop', 'scss', 'jquery-3.5.1', 'scrollreveral','googlecharts', 'fontawesome-free-5.13.0-web', 'echarts-5.0.0-dist', 'countdown', 'boxicons-2.0.7', 'bootstrap-5.0.0-beta1-dist' }

MAX_FILE_SIZE = 5 * 1024 * 1024  # 5MB

NOME_ARQUIVO_MD = "DOCUMENTACAO.md"
NOME_ARQUIVO_MD_CENTRAL = "DOCUMENTACAO_TODOS_MICROSSERVICOS.md"


def inferir_linguagem(ext):
    ext = ext.lower()
    if ext in EXT_LINGUAGEM_PADRAO:
        return EXT_LINGUAGEM_PADRAO[ext]
    if ext.startswith(".ts"):
        return ("typescript", "//")
    if ext.startswith(".js"):
        return ("javascript", "//")
    if ext.startswith(".html"):
        return ("html", "block")
    if ext.startswith(".css"):
        return ("css", "/* */")
    if ext.startswith(".scss"):
        return ("scss", "/* */")
    if ext.startswith(".json"):
        return ("json", "//")
    if ext.startswith(".vue"):
        return ("vue", "<!-- -->")
    if ext.startswith(".py"):
        return ("python", "#")
    if ext.startswith(".sql"):
        return ("sql", "--")
    return (ext[1:] if ext else "text", "#")


def is_text_file(file_path):
    ext = os.path.splitext(file_path)[1].lower()
    if ext in EXT_IGNORE:
        return False
    if os.path.getsize(file_path) > MAX_FILE_SIZE:
        print(f"Atenção: Ignorando arquivo grande {file_path}")
        return False
    return True


def gerar_documentacao_diretorio(projeto_dir, unico=False):
    conteudo_unico = []
    md_por_diretorio = []

    for root, dirs, files in os.walk(projeto_dir):
        dirs[:] = [d for d in dirs if d not in DIRS_IGNORE]
        arquivos_filtrados = [f for f in files if f.strip()]
        if not arquivos_filtrados:
            continue

        if unico:
            if conteudo_unico:
                conteudo_unico.append("---\n\n")
        else:
            caminho_md = os.path.join(root, NOME_ARQUIVO_MD)
            md_por_diretorio.append(os.path.relpath(caminho_md, projeto_dir))
            with open(caminho_md, "w", encoding="utf-8") as destino:
                for arquivo in arquivos_filtrados:
                    caminho_arquivo = os.path.join(root, arquivo)
                    if not is_text_file(caminho_arquivo):
                        continue
                    ext = os.path.splitext(arquivo)[1]
                    linguagem, tipo_comentario = inferir_linguagem(ext)
                    caminho_relativo = os.path.relpath(caminho_arquivo, projeto_dir)
                    try:
                        with open(caminho_arquivo, "r", encoding="utf-8", errors="ignore") as f:
                            conteudo = f.read()
                    except Exception as e:
                        print(f"Erro lendo {caminho_arquivo}: {e}")
                        continue
                    comentario_inicio = f"<!-- {caminho_relativo} -->" if tipo_comentario in ["block",
                                                                                              "<!-- -->"] else f"{tipo_comentario} {caminho_relativo}"
                    bloco = f"```{linguagem}\n{comentario_inicio}\n{conteudo}\n```\n\n"
                    destino.write(bloco)
            print(f"Gerado .md por diretório: {caminho_md}")

        if unico:
            for arquivo in arquivos_filtrados:
                caminho_arquivo = os.path.join(root, arquivo)
                if not is_text_file(caminho_arquivo):
                    continue
                ext = os.path.splitext(arquivo)[1]
                linguagem, tipo_comentario = inferir_linguagem(ext)
                caminho_relativo = os.path.relpath(caminho_arquivo, projeto_dir)
                try:
                    with open(caminho_arquivo, "r", encoding="utf-8", errors="ignore") as f:
                        conteudo = f.read()
                except Exception as e:
                    print(f"Erro lendo {caminho_arquivo}: {e}")
                    continue
                comentario_inicio = f"<!-- {caminho_relativo} -->" if tipo_comentario in ["block",
                                                                                          "<!-- -->"] else f"{tipo_comentario} {caminho_relativo}"
                bloco = f"```{linguagem}\n{comentario_inicio}\n{conteudo}\n```\n\n"
                conteudo_unico.append(bloco)

    if unico:
        caminho_md = os.path.join(projeto_dir, NOME_ARQUIVO_MD)
        with open(caminho_md, "w", encoding="utf-8") as f:
            f.write(f"# Documentação do projeto {os.path.basename(projeto_dir)}\n\n")
            f.writelines(conteudo_unico)
        print(f"Gerado .md único do projeto: {caminho_md}")

    return conteudo_unico, md_por_diretorio


def limpar_documentacao(raiz):
    """
    Apaga apenas os arquivos gerados pelo script:
    - DOCUMENTACAO.md
    - DOCUMENTACAO_TODOS_MICROSSERVICOS.md
    """
    for root, dirs, files in os.walk(raiz):
        dirs[:] = [d for d in dirs if d not in DIRS_IGNORE]
        for f in files:
            if f == NOME_ARQUIVO_MD or f == NOME_ARQUIVO_MD_CENTRAL:
                caminho = os.path.join(root, f)
                try:
                    os.remove(caminho)
                    print(f"Removido: {caminho}")
                except Exception as e:
                    print(f"Erro removendo {caminho}: {e}")


def main():
    # Se chamado com "limpar", executa apenas limpeza
    if len(sys.argv) > 1 and sys.argv[1] == "limpar":
        raiz = sys.argv[2] if len(sys.argv) > 2 else "."
        raiz = os.path.abspath(raiz)
        print(f"🗑 Limpando arquivos de documentação em: {raiz}")
        limpar_documentacao(raiz)
        sys.exit(0)

    raiz = sys.argv[1] if len(sys.argv) > 1 else "."
    raiz = os.path.abspath(raiz)
    conteudo_total = []

    for item in sorted(os.listdir(raiz)):
        item_path = os.path.join(raiz, item)
        if os.path.isdir(item_path) and item not in DIRS_IGNORE:
            print(f"\nProcessando microsserviço/projeto: {item_path}\n")
            gerar_documentacao_diretorio(item_path, unico=False)
            conteudo_projeto, _ = gerar_documentacao_diretorio(item_path, unico=True)
            conteudo_total.append(f"# Projeto: {item}\n\n")
            conteudo_total.extend(conteudo_projeto)
            conteudo_total.append("\n---\n\n")

    caminho_md_central = os.path.join(raiz, NOME_ARQUIVO_MD_CENTRAL)
    with open(caminho_md_central, "w", encoding="utf-8") as f:
        f.write("# Documentação Centralizada de Todos os Microsserviços\n\n")
        f.writelines(conteudo_total)
    print(f"\n📄 Documentação centralizada gerada em: {caminho_md_central}")


if __name__ == "__main__":
    main()
