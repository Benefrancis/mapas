import os
import sys
from pathlib import Path

# =========================================================
# CONFIGURAÇÕES
# =========================================================

NOME_DOCUMENTACAO = "DOCUMENTACAO.md"
NOME_EXTENSOES = "extensoes_encontradas.md"

MAX_FILE_SIZE = 2 * 1024 * 1024  # 2MB

# ---------------------------------------------------------
# EXTENSÕES RELEVANTES PARA SOFTWARE
# ---------------------------------------------------------

EXTENSOES_PERMITIDAS = {
    ".java",
    ".kt",
    ".groovy",
    ".py",
    ".js",
    ".jsx",
    ".ts",
    ".tsx",
    ".vue",
    ".sql",
    ".json",
    ".yaml",
    ".yml",
    ".xml",
    ".properties",
    ".conf",
    ".env",
    ".ini",
    ".sh",
    ".ps1",
    ".txt",
    ".css",
    ".scss",
    ".html",
    ".gradle",
    ".kts",
    ".tf",
    ".toml",
    ".bat",
    ".c",
    ".cpp",
    ".h",
    ".hpp",
    ".cs",
    ".go",
    ".rs",
    ".php",
    ".rb",
    ".swift",
}

# ---------------------------------------------------------
# ARQUIVOS SEM EXTENSÃO PERMITIDOS
# ---------------------------------------------------------

ARQUIVOS_SEM_EXTENSAO_PERMITIDOS = {
    "Dockerfile",
    "Jenkinsfile",
    "Makefile",
}

# ---------------------------------------------------------
# ARQUIVOS ESPECÍFICOS A IGNORAR
# ---------------------------------------------------------

ARQUIVOS_IGNORE = {
    "package-lock.json",
    "yarn.lock",
    "pnpm-lock.yaml",
    ".DS_Store",
}

# ---------------------------------------------------------
# DIRETÓRIOS IGNORADOS
# ---------------------------------------------------------

DIRS_IGNORE = {
    "SUSEP",
    ".git",
    "node_modules",
    "target",
    "build",
    "dist",
    "out",
    ".idea",
    ".vscode",
    "__pycache__",
    "venv",
    ".mvn",
    ".gradle",
    "coverage",
    "logs",
    "tmp",
    "q8-data",
    "q8-files",
    "q8-logs",
    "docs",
    "documentacao",
    ".github"
}

# ---------------------------------------------------------
# MAPA DE LINGUAGEM
# ---------------------------------------------------------

MAPA_LINGUAGEM = {
    ".java": "java",
    ".kt": "kotlin",
    ".groovy": "groovy",
    ".py": "python",
    ".js": "javascript",
    ".jsx": "javascript",
    ".ts": "typescript",
    ".tsx": "typescript",
    ".vue": "vue",
    ".sql": "sql",
    ".json": "json",
    ".yaml": "yaml",
    ".yml": "yaml",
    ".xml": "xml",
    ".properties": "properties",
    ".conf": "conf",
    ".env": "bash",
    ".ini": "ini",
    ".sh": "bash",
    ".ps1": "powershell",
    ".md": "markdown",
    ".txt": "text",
    ".css": "css",
    ".scss": "scss",
    ".html": "html",
    ".gradle": "groovy",
    ".kts": "kotlin",
    ".tf": "terraform",
    ".toml": "toml",
    ".bat": "bat",
    ".c": "c",
    ".cpp": "cpp",
    ".h": "c",
    ".hpp": "cpp",
    ".cs": "csharp",
    ".go": "go",
    ".rs": "rust",
    ".php": "php",
    ".rb": "ruby",
    ".swift": "swift",
}


# =========================================================
# UTIL
# =========================================================

def obter_linguagem(extensao: str) -> str:
    return MAPA_LINGUAGEM.get(extensao.lower(), "text")


def arquivo_valido(path: Path) -> bool:

    if not path.is_file():
        return False

    # ignora arquivos gerados pelo próprio script
    if path.name in {NOME_DOCUMENTACAO, NOME_EXTENSOES}:
        return False

    # ignora arquivos específicos
    if path.name in ARQUIVOS_IGNORE:
        return False

    # ignora arquivos sem extensão não permitidos
    if (
            not path.suffix
            and path.name not in ARQUIVOS_SEM_EXTENSAO_PERMITIDOS
    ):
        return False

    # ignora extensões não permitidas
    if (
            path.suffix.lower() not in EXTENSOES_PERMITIDAS
            and path.name not in ARQUIVOS_SEM_EXTENSAO_PERMITIDOS
    ):
        return False

    # ignora arquivos minificados
    if path.name.endswith(".min.js"):
        return False

    if path.name.endswith(".bundle.js"):
        return False

    # ignora arquivos muito grandes
    try:
        if path.stat().st_size > MAX_FILE_SIZE:
            print(f"Ignorando arquivo grande: {path}")
            return False
    except Exception:
        return False

    return True


# =========================================================
# LEITURA RECURSIVA
# =========================================================

def listar_arquivos_recursivamente(diretorio: Path) -> list[Path]:

    arquivos = []

    for root, dirs, files in os.walk(diretorio):

        dirs[:] = [d for d in dirs if d not in DIRS_IGNORE]

        for nome_arquivo in files:

            path = Path(root) / nome_arquivo

            if arquivo_valido(path):
                arquivos.append(path)

    return sorted(arquivos)


# =========================================================
# GERAÇÃO MARKDOWN
# =========================================================

def gerar_markdown(diretorio: Path) -> None:

    arquivos = listar_arquivos_recursivamente(diretorio)

    if not arquivos:
        print(f"Nenhum arquivo relevante encontrado em: {diretorio}")
        return

    caminho_md = diretorio / NOME_DOCUMENTACAO

    with open(caminho_md, "w", encoding="utf-8") as md:

        md.write(f"# Documentação: {diretorio.name}\n\n")

        for arquivo in arquivos:

            extensao = arquivo.suffix.lower()

            # suporte para arquivos sem extensão
            if arquivo.name == "Dockerfile":
                linguagem = "dockerfile"
            elif arquivo.name == "Makefile":
                linguagem = "makefile"
            elif arquivo.name == "Jenkinsfile":
                linguagem = "groovy"
            else:
                linguagem = obter_linguagem(extensao)

            relativo = arquivo.relative_to(diretorio)

            try:
                conteudo = arquivo.read_text(
                    encoding="utf-8",
                    errors="ignore"
                )

            except Exception as e:
                print(f"Erro lendo {arquivo}: {e}")
                continue

            md.write(f"## {relativo}\n\n")

            md.write(f"```{linguagem}\n")
            md.write(conteudo)
            md.write("\n```\n\n")

    print(f"Gerado: {caminho_md}")


# =========================================================
# RELATÓRIO DE EXTENSÕES
# =========================================================

def gerar_relatorio_extensoes(raiz: Path) -> None:

    caminho_saida = raiz / NOME_EXTENSOES

    linhas = []

    for root, dirs, files in os.walk(raiz):

        dirs[:] = [d for d in dirs if d not in DIRS_IGNORE]

        for nome_arquivo in files:

            path = Path(root) / nome_arquivo

            if path.name in {NOME_DOCUMENTACAO, NOME_EXTENSOES}:
                continue

            extensao = path.suffix.lower() or "[sem_ext]"

            relativo = path.relative_to(raiz)

            linhas.append(f"{extensao} -> {relativo}")

    linhas = sorted(set(linhas))

    with open(caminho_saida, "w", encoding="utf-8") as f:
        f.write("\n".join(linhas))

    print(f"Gerado: {caminho_saida}")


# =========================================================
# LIMPEZA
# =========================================================

ARQUIVOS_GERADOS = {
    NOME_DOCUMENTACAO,
    NOME_EXTENSOES,
}


def limpar_documentacao(raiz: Path) -> None:

    removidos = 0

    for root, dirs, files in os.walk(raiz):

        dirs[:] = [d for d in dirs if d not in DIRS_IGNORE]

        for nome_arquivo in files:

            if nome_arquivo not in ARQUIVOS_GERADOS:
                continue

            caminho = Path(root) / nome_arquivo

            try:
                caminho.unlink()

                removidos += 1

                print(f"Removido: {caminho}")

            except Exception as e:
                print(f"Erro removendo {caminho}: {e}")

    print(f"\nTotal removido: {removidos}")


# =========================================================
# MAIN
# =========================================================

def main() -> None:

    raiz = Path(".").resolve()

    # -----------------------------------------------------
    # LIMPEZA
    # -----------------------------------------------------

    if len(sys.argv) > 1 and sys.argv[1].lower() == "limpar":

        print(f"Limpando documentação em: {raiz}")

        limpar_documentacao(raiz)

        return

    # -----------------------------------------------------
    # GERAÇÃO
    # -----------------------------------------------------

    print(f"Processando raiz: {raiz}")

    # documentação da raiz
    gerar_markdown(raiz)

    # documentação dos diretórios imediatos
    for item in raiz.iterdir():

        if not item.is_dir():
            continue

        if item.name in DIRS_IGNORE:
            continue

        gerar_markdown(item)

    # relatório central de extensões
    gerar_relatorio_extensoes(raiz)

    print("\nProcessamento concluído.")


if __name__ == "__main__":
    main()

