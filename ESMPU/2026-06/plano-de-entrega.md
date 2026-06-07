### 🔎 1. Análise Contextual e Territorial

**Parte 1 de 10 — Análise do Desafio Territorial:**
O projeto visa a detecção de fraudes em Terras Indígenas (TIs) na Amazônia Sul-Ocidental (foco no Pará e Amazonas)
requer, nesta fase inicial, a consolidação de matrizes de dados em planilhas tabulares. Atendendo exatamente ao pedido
da sua chefia de pesquisa, vamos estruturar o "Inventário Documental e Técnico" preenchendo as
planilhas solicitadas no PDF, garantindo que sirvam de base segura para a futura IA. O diferencial aqui será a injeção
imediata de rigor geodésico e a expansão da busca documental para autarquias além do eixo tradicional.

### 📖 2. Glossário Didático Imediato

**Parte 2 de 10 — Glossário de Descompressão:**

* **ICMBio:** Instituto Chico Mendes de Conservação da Biodiversidade (órgão federal responsável pelas Unidades de
  Conservação, que fazem muitas vezes fronteira ou sobrepõem TIs).
* **SEMAS-PA / IPAAM-AM:** Secretaria de Meio Ambiente e Sustentabilidade do Pará / Instituto de Proteção Ambiental do
  Amazonas (órgãos estaduais que emitem as licenças ambientais locais).
* **SNIRH / ANA:** Sistema Nacional de Informações sobre Recursos Hídricos / Agência Nacional de Águas (bases
  fundamentais, pois rios são vetores de invasão garimpeira em TIs).
* **INDE:** Infraestrutura Nacional de Dados Espaciais (catálogo do governo federal que padroniza como mapas oficiais
  devem ser publicados).
* **Metadados:** "Dados sobre os dados". Informações que explicam quem fez o mapa, em que data, e sob qual sistema de
  coordenadas.
* **Outorga de Água:** Autorização legal para uso de recursos hídricos, muitas vezes usada como documento acessório em
  fraudes para justificar ocupações.

### 🗺️ 3. Governança, Bases Oficiais e Legislação

**Parte 3 de 10 — Malha Jurídica e Fundiária:**
O licenciamento ambiental no Brasil é tripartite (União, Estado, Município). Nossa pesquisa deve cruzar:

1. **Esfera Federal:** IBAMA (licenciamento de grande impacto), INCRA/SIGEF (certificação de fazendas), FUNAI (TIs),
   ICMBio (Unidades de Conservação), SPU (Secretaria de Patrimônio da União).
2. **Esfera Estadual:** SEMAS (Pará) e IPAAM (Amazonas). São eles que emitem a maioria das licenças (LP, LI, LO) e
   as Autorizações de Supressão de Vegetação (ASV) que costumam ser fraudadas ou descumpridas.
3. **Esfera Municipal:** Planos Diretores Municipais e Secretarias Municipais de Meio Ambiente (muitos municípios
   ganharam delegação para licenciar atividades de impacto local, criando "pontos cegos" na fiscalização federal).
4. **Dados Abertos Federais:** O Portal Brasileiro de Dados Abertos e a INDE.

### 🛰️ 4. Estratégia de Geodados e Sensoriamento

**Parte 4 de 10 — Geodésia e Precisão Posicional:**
Adicionei a coluna "Sistema/Origem" e "EPSG" para garantir o controle geodésico (ancorado no SIRGAS2000) exigido para a
pesquisa não perder validade legal.

**A) INVENTÁRIO DOCUMENTAL JURÍDICO (Expandido)**

| Documento       | Onde normalmente é encontrado                                                                      | Utilidade para a pesquisa                                   |
|:----------------|:---------------------------------------------------------------------------------------------------|:------------------------------------------------------------|
| LP, LI, LO      | Portal de Licenciamento (SEMAS-PA / IPAAM-AM)                                                      | Verificar viabilidade, autorização e operação.              |
| ASV / AUTEF     | Sisflora (PA), DOF (Federal), Sinaflor (IBAMA)                                                     | Verificar fraude na autorização de desmatamento em  TI.     |
| Outorga de Água | SNIRH / ANA / Órgãos estaduais                                                                     | Identificar fazendas ou garimpos ilegais ativos.            |
| CAR / Matrícula | SICAR (Estadual/Federal) / Cartórios via ONR (Operador Nacional do Sistema de Registro Eletrônico) | Histórico de grilagem e fraudes no Cadastro Ambiental.      |
| CCIR            | INCRA (Sistema Nacional de Cadastro Rural)                                                         | Comprova a tentativa de regularização fundiária da invasão. |

**B) INVENTÁRIO TÉCNICO DAS BASES (Preenchido para o PDF)**

| Base                  | Órgão Responsável       | Acesso        | Dados Disponíveis              | Limitações (Rigor da Pesquisa)                                                                  |
|:----------------------|:------------------------|:--------------|:-------------------------------|:------------------------------------------------------------------------------------------------|
| **MapBiomas Alerta**  | MapBiomas               | Público (API) | Laudos de supressão, imagens   | Pode ter defasagem de dias; necessita validação de nuvens.                                      |
| **PRODES**            | INPE                    | Público (FTP) | Histórico anual (corte raso)   | Não pega degradação fina (fogo de sub-bosque/extração seletiva).                                |
| **DETER**             | INPE                    | Público       | Alertas diários/semanais       | Resolução de 64m (AWIFS); não serve para medir área com precisão jurídica, apenas para alertar. |
| **SICAR**             | SFB / Ministério Gestão | Público (Shp) | Perímetros auto-declarados     | Altíssimo volume de dados falsos auto-declarados; exige limpeza.                                |
| **Limites TI**        | FUNAI                   | Público (Geo) | Polígonos Oficiais e Fases     | Nem todas as TIs em estudo estão homologadas; há variação jurídica.                             |
| **Unid. Conservação** | ICMBio                  | Público (Geo) | Limites de Parna, Flona, Resex | Sobreposição complexa com TIs e licenças do estado.                                             |

**C) PLANILHAS DE ORGANIZAÇÃO (Exemplo aprimorado para o seu grupo)**
*(Sugiro adicionar a coluna "EPSG" na "Planilha e" do seu PDF)*:

| .Arquivo       | Origem    | Data de Obtenção | Responsável | EPSG (Sistema Original)   | Observação                                                     |
|:---------------|:----------|:-----------------|:------------|:--------------------------|:---------------------------------------------------------------|
| alerta_001.pdf | MapBiomas | 25/05/2026       | Benefrancis | EPSG:4326 (WGS84 Lat/Lon) | Necessário reprojetar para EPSG: 4674 (SIRGAS2000) no PostGIS. |

### 🧮 5. Modelagem Matemática, Geoestatística e IA

**Parte 5 de 10 — Seleção de Sensores e Aquisição (APIs/Drones):**
Para as planilhas do PDF, a fonte de dados (DETER/MapBiomas) baseia-se prioritariamente nos satélites Landsat (EUA) e
CBERS (Brasil/China). A recomendação "além da curva" para a sua chefe é propor que, a partir do mês 6 do projeto, a
coleta de dados passe a incluir **APIs do STAC (SpatioTemporal Asset Catalogs - Catálogos de Ativos Espaço-Temporais)**,
permitindo puxar imagens instantâneas sem depender do clique manual nos portais governamentais.

**Parte 6 de 10 — Processamento Digital e Espectral:**
Enquanto o grupo preenche as planilhas com "Cumprida" ou "Pendente" para as condicionantes (conforme a "Planilha b" do
PDF), a equipe deve guardar o ID da licença. A visão de longo prazo é usar esse ID para buscar automaticamente o NDVI (
Índice de Diferença Normalizada da Vegetação) histórico na área daquela licença específica.

**Parte 7 de 10 — Modelagem Preditiva / Estatística (IA/ML):**
O PDF não exige modelos de IA hoje, apenas a base empírica. No "texto com suas observações" para a chefe, indique: *"Os
dados estruturados nestas planilhas servirão de variável alvo (ground truth) para treinar um modelo de Random Forest (
Floresta Aleatória - algoritmo de IA que vota para decidir se há fraude) na fase 2 da pesquisa."*

### 🧱 6. Arquitetura Computacional e Reprodutibilidade

**Parte 8 de 10 — Arquitetura de Dados e Rastreabilidade:**
Atendendo ao subitem 3 da página 9 do seu PDF ("Organização dos dados"):
A estrutura de pastas "Dados Brutos", "Dados Organizados" e "Produtos" deve receber um acompanhamento rigoroso. Sugiro
propor à sua chefia a criação de um arquivo `hash_registro.txt`. Toda vez que o grupo baixar um Shapefile da FUNAI ou um
edital da SEMAS, gera-se um código criptográfico (Hash SHA-256). Isso garante a "cadeia de custódia" mencionada na
página 3, blindando a pesquisa contra alegações de adulteração de provas (muito comum em defesas de crimes ambientais).

### 💻 7. Implementação ou Script Proposto (Se aplicável)

**Parte 9 de 10 — Código / Execução Prática:**
Para o seu relatório ir além, mostre como automatizar o item de "viabilidade de integração" (página 7 do PDF). Aqui está
um micro-script em Python que você pode anexar ao seu texto, provando que é possível ler o CAR, ler a TI, checar a
coordenada oficial e exportar diretamente para a "Planilha c" que a chefia quer:

```python
import geopandas as gpd
import pandas as pd

# 1. Este script lê o dado geográfico e preenche automaticamente a 'Planilha Fundiária' do PDF
# Carregando TI (FUNAI) e CAR (SICAR) já convertidos para SIRGAS2000
tis = gpd.read_file("dados_brutos/funai_tis_sirgas2000.shp")
cars = gpd.read_file("dados_brutos/sicar_pa_sirgas2000.shp")

# 2. Interseção Topológica (Cruzar mapas)
sobreposicoes = gpd.overlay(cars, tis, how='intersection')

# 3. Extrair os dados para o formato EXATO da planilha C (página 6/7 do PDF)
planilha_c = pd.DataFrame({
    'ID': range(1, len(sobreposicoes) + 1),
    'Município': sobreposicoes['municipio_car'],
    'TI': sobreposicoes['nome_ti'],
    'Tipo de dado': 'CAR',
    'Identificador': sobreposicoes['recibo_car'],
    'Área': sobreposicoes.geometry.area / 10000,  # Converter m² para Hectares
    'Observação': 'Sobreposição detectada via script espacial.'
})

# Exporta para CSV para entregar à chefia de pesquisa
planilha_c.to_csv("produtos/planilha_c_automatizada.csv", index=False)
```

### 🚨 8. Auditoria de Risco e Conclusão Executiva

**Parte 10 de 10 — Laudo Final e Defesa Técnica:**
Conforme sua exigência de máxima excelência, aqui está o **Mapeamento de 30 Riscos e Planos de Mitigação**, desenhado
para um projeto de integridade socioambiental envolvendo Terras Indígenas. Você pode anexar este bloco como o seu
*Overdelivery*.

**A. Riscos Geodésicos e Qualidade de Dados (1 a 10)**

1. **Risco:** Uso de dados fora do SIRGAS2000. **Mitigação:** Criação de pipeline obrigatório de conversão de
   coordenadas (EPSG:4674).
2. **Risco:** Polígonos de TIs não atualizados (Fases: Declarada, Homologada). **Mitigação:** Consumo direto do
   servidor (WFS) da FUNAI via script semanal, não download estático.
3. **Risco:** Baixa precisão posicional em licenças antigas (feitas com GPS de mão). **Mitigação:** Tolerância de erro
   geográfico ("buffer" de 10 a 15 metros) antes de acusar sobreposição.
4. **Risco:** Imagens de satélite com alta cobertura de nuvens (comum na Amazônia). **Mitigação:** Utilização de
   composição de pixels e dados de radar (SAR - Sentinel-1), que enxerga através das nuvens.
5. **Risco:** Falsos positivos gerados por sombras de montanhas/nuvens no NDVI. **Mitigação:** Aplicação de máscara de
   nuvens (QA_PIXEL ou algoritmo Fmask).
6. **Risco:** Assimetria de escala (cruzar mapa municipal 1:5.000 com estadual 1:100.000). **Mitigação:** Padronizar e
   registrar a Escala de Análise nos Metadados da pesquisa.
7. **Risco:** Mudança do leito do rio alterando o limite físico da TI. **Mitigação:** Análise temporal cruzando limites
   teóricos oficiais com a imagem de satélite contemporânea.
8. **Risco:** Fragmentação do CAR (desmembramento para fugir da fiscalização de grandes áreas). **Mitigação:**
   Agrupamento de CARs contíguos pelo CPF/CNPJ do responsável.
9. **Risco:** Falta de rastreabilidade (Data Lineage) da licença em PDF. **Mitigação:** Geração de Hash criptográfico no
   momento da coleta.
10. **Risco:** Descontinuidade de APIs governamentais (o site cai). **Mitigação:** Backup frio mensal de todo o banco de
    dados.

**B. Riscos Jurídicos e Institucionais (11 a 20)**

11. **Risco:** Licenças municipais omitidas do banco estadual. **Mitigação:** Amostragem de municípios estratégicos e
    requisição via Lei de Acesso à Informação (LAI).
12. **Risco:** Validação de CAR sobreposto à TI sem cancelamento pelo órgão. **Mitigação:** Cruzar o status do CAR (
    Ativo, Pendente, Cancelado) no SICAR.
13. **Risco:** Desmatamento legal (ASV) ocorrendo fora do polígono autorizado. **Mitigação:** IA treinada para medir
    especificamente a expansão para fora da borda do polígono aprovado.
14. **Risco:** Alegação de nulidade da prova por quebra de cadeia de custódia. **Mitigação:** Registrar data, hora, IP,
    e servidor de origem na "Planilha e" de Metadados.
15. **Risco:** Conflito entre limite do SIGEF (INCRA) e limites da FUNAI. **Mitigação:** Elaborar nota técnica apontando
    que, pela Constituição (Art. 231), TIs têm prevalência (indigenato).
16. **Risco:** Sigilo processual de licenças (EIA/RIMA não publicados). **Mitigação:** Uso de prerrogativa institucional
    do Ministério Público (MPF) para requisição dos processos.
17. **Risco:** Termos de Ajustamento de Conduta (TACs) mascarando novas infrações. **Mitigação:** Inventariar TACs na "
    Planilha a" de licenciamentos.
18. **Risco:** Mudança na legislação ambiental durante a vigência dos 24 meses do projeto. **Mitigação:** Versionamento
    temporal das normas aplicáveis no repositório do projeto.
19. **Risco:** Condicionantes descritas de forma subjetiva ("plantar algumas árvores"). **Mitigação:** Categorizar as
    condicionantes em "Auditáveis via Satélite" (ex: tamanho de reserva legal) e "Auditáveis in loco".
20. **Risco:** Vazamento de dados sensíveis de indígenas isolados. **Mitigação:** Anonimização absoluta de coordenadas
    de aldeias em territórios com presença de isolados, limitando o mapa à fronteira exterior.

**C. Riscos Operacionais, Metodológicos e de IA (21 a 30)**

21. **Risco:** Planilhas tornarem-se intrabalháveis por excesso de linhas (ex: 500.000 CARs no Pará). **Mitigação:**
    Implantação progressiva do banco espacial PostGIS a partir do Mês 3.
22. **Risco:** Curva de aprendizado da equipe com mapas. **Mitigação:** Capacitações mensais (como a sua iniciativa
    agora).
23. **Risco:** Viés algorítmico da IA (confundir roça tradicional indígena com desmatamento ilegal). **Mitigação:**
    Treinamento supervisionado da IA (Machine Learning) com amostras de roças, incorporando a variável antropológica.
24. **Risco:** Perda histórica de dados por falta de versionamento. **Mitigação:** Utilização de ferramentas como DVC (
    Data Version Control) para mapas.
25. **Risco:** Dispersão do escopo (querer auditar o Brasil todo). **Mitigação:** Fixar rigorosamente Pará e Amazonas
    como Zona de Teste Inicial.
26. **Risco:** Isolamento acadêmico (a IA é boa na teoria, não processável pelo promotor/técnico do MPF). **Mitigação:**
    O produto final deve ser um "Dossiê Explicável" (como pedido no seu PDF, página 4), não um código de computador.
27. **Risco:** Falta de capacidade computacional local para processar imagens orbitais pesadas. **Mitigação:** Migração
    do processamento raster para Nuvem (Google Earth Engine / Microsoft Planetary Computer).
28. **Risco:** Sobreposição de ferramentas redundantes na ESMPU. **Mitigação:** Integração obrigatória com o projeto "
    Amazônia Protege" (já citado na sua página 5).
29. **Risco:** Falsos negativos de desmatamento devido ao tipo de satélite (ex: extração de madeira de alto valor que
    mantém o dossel superior). **Mitigação:** Incorporar índices avançados como NDFI (Normalized Difference Fraction
    Index - mede fração de solo exposto dentro da floresta verde).
30. **Risco:** Interpretação isolada dos mapas sem a variável humana. **Mitigação:** Todo alerta vermelho gerado pela
    ferramenta, antes de virar denúncia judicial, deve passar por um antropólogo ou analista ambiental para confirmar o
    contexto territorial e fundiário.

**Próximos Passos:**
Bene, você tem em mãos a resposta exata ao PDF da sua chefe (Partes 1 a 4), um script prático (Parte 9) e o plano de
mitigação de 30 pontos (Parte 10). Ao apresentar o inventário preenchido seguido destas observações, você assegura a
execução impecável do cronograma inicial enquanto instiga a ESMPU a alcançar um patamar institucional e tecnológico de
ponta. Como deseja estruturar a próxima frente?