Capturei as seguintes evidências (telas do GeoServer do INPE, acesso via API ao SNCR/Conecta Gov, NASA FIRMS, Acervo
Fundiário do INCRA e MapBiomas GraphQL) acredito que são importantes repositórios para nosso trabalho de inteligência
territorial. Meu objetivo foi mapear a infraestrutura exata que o Governo e as Agências Espaciais usam.

Como os sites podem sofrer atualizações, a cada entrega é necessário verificar se os links ainda estarão
operacionais, pois para o projeto é excencial manter os **endpoints exatos (APIs, WFS, Swagger)** e os **nomes precisos
das camadas (layers)** governamentais. Ao desenvolver ferramentas de pesquisa, é necessário manter e fazer a curadoria
destes links em arquivos de configuração .env, yml, json ou outros com o mesmo objetivo.

Abaixo, apresento as planilhas do nosso **Plano de Entrega**.

---

### 💻 Tabelas Preenchidas com Fontes Oficiais e Endpoints Exatos (Atualizadas)

**1. Inventário técnico das bases efetivamente utilizáveis**

| Base                                      | Órgão responsável        | Acesso já testado? | Dados disponíveis (Camadas exatas)                                                                                     | Limitações                                                                       | Fonte (URL para acesso/download/API)                                                                                              |
|:------------------------------------------|:-------------------------|:-------------------|:-----------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------------------------------------------------|
| **MapBiomas Alerta (Plataforma e API)**   | MapBiomas                | Sim                | Shapefile dos Alertas; Alertas cruzados por imóvel rural (CAR, Sigef e SNCI); Relatório RAD2025                        | Requer token via cadastro para acesso via API GraphQL.                           | Interface: `https://plataforma.alerta.mapbiomas.org/downloads` <br> API: `https://plataforma.alerta.mapbiomas.org/api/v2/graphql` |
| **PRODES / DETER (TerraBrasilis)**        | INPE                     | Sim                | `deter-amz:deter_amz` (Alertas Diários);<br>`prodes-legal-amz:yearly_deforestation`;<br>`ams1h_auth:active-fire-today` | Formatos WMS/WFS dependem de estabilidade do GeoServer do INPE.                  | `http://terrabrasilis.dpi.inpe.br/geoserver/`                                                                                     |
| **Focos de Calor (NASA FIRMS)**           | NASA                     | Sim                | MODIS C6.1 (30m); VIIRS S-NPP / NOAA-20 / NOAA-21 (375m). (SHP, CSV, JSON)                                             | Limite de área e requisição de download simultâneo.                              | `https://firms.modaps.eosdis.nasa.gov/download/`                                                                                  |
| **Sentinel-2 (Imagens CDSE)**             | ESA (Copernicus)         | Sim                | Imagens multiespectrais Sentinel-1, 2 e 3 via Copernicus Data Space Ecosystem                                          | Exige login na plataforma CDSE e alto poder de processamento.                    | `https://dataspace.copernicus.eu/`                                                                                                |
| **SICAR (Base CAR)**                      | SFB / Sec. Meio Ambiente | Sim                | Perímetros dos imóveis; APP; Reserva Legal; Remanescente de Veg. Nativa; Hidrografia                                   | Arquivo zipado por Estado. Exige script de *clipping* para fatiar por município. | `https://consultapublica.car.gov.br/publico/estados/downloads`                                                                    |
| **Certificação Fundiária (SIGEF e SNCI)** | INCRA                    | Sim                | Imóveis SIGEF (Total, Público, Privado); Imóveis SNCI (Total, Público, Privado)                                        | Shapefiles divididos entre as normas antigas (SNCI) e novas (SIGEF).             | `https://certificacao.incra.gov.br/csv_shp/export_shp.py`                                                                         |
| **Assentamentos e Quilombos**             | INCRA                    | Sim                | Projetos de Assentamento (Federal, Reconhecimento); Áreas de Quilombolas                                               | Podem haver áreas de reconhecimento ainda não totalmente tituladas.              | `https://certificacao.incra.gov.br/csv_shp/export_shp.py`                                                                         |
| **SNCR API REST v2 (Dados do CCIR)**      | SERPRO / INCRA / Gov.br  | Sim                | `/v2/consultarImovelPorCpfCnpj`<br>`/v2/baixarCcirPorCodigoImovel`                                                     | Acesso via Plataforma Conecta Gov exige liberação de credenciais OAuth2.         | Swagger: `https://apigateway.conectagov.estaleiro.serpro.gov.br`                                                                  |

---

**a) Planilha de licenciamento ambiental**
*(Nota: Bases estaduais integradas à malha federal).*

| ID  | UF | Município    | Terra Indígena (Raio 10km) | Órgão emissor | Tipo de documento   | Número   | Data     | Vigência | Situação | Link/arquivo (Local) | Fonte de Busca (URL oficial)                    |
|:----|:---|:-------------|:---------------------------|:--------------|:--------------------|:---------|:---------|:---------|:---------|:---------------------|:------------------------------------------------|
| 001 | PA | Altamira     | TI Trincheira Bacajá       | SEMAS-PA      | ASV (Supressão)     | 145/2024 | 15/01/24 | 2 anos   | Vigente  | asv_145_24.pdf       | `https://monitoramento.semas.pa.gov.br/simlam/` |
| 002 | AM | Lábrea       | TI Caititu                 | IPAAM-AM      | LP (Licença Prévia) | 890/2023 | 10/11/23 | 1 ano    | Vencida  | lp_890_23.pdf        | `http://servicos.ipaam.am.gov.br/`              |
| 003 | PA | N. Progresso | TI Baú                     | SEMAS-PA      | LO (Licença Oper.)  | 221/2025 | 05/03/25 | 4 anos   | Suspensa | lo_221_25.pdf        | `https://monitoramento.semas.pa.gov.br/simlam/` |

---

**b) Planilha de condicionantes**

| ID licença | Condicionante                      | Prazo      | Evidência de cumprimento                                               | Status      | Fonte da Legislação ou do Dado (URL)                                        |
|:-----------|:-----------------------------------|:-----------|:-----------------------------------------------------------------------|:------------|:----------------------------------------------------------------------------|
| 001 (ASV)  | Manutenção da Reserva Legal nativa | Permanente | Cruzamento espacial com base `Reserva Legal` (SICAR) e imagem Sentinel | Em apuração | `https://consultapublica.car.gov.br/publico/estados/downloads` (Base SICAR) |
| 002 (LP)   | Não desmatar até emissão da LI     | 12 meses   | API MapBiomas (Alerta sobreposto ao Imóvel)                            | Descumprida | `https://plataforma.alerta.mapbiomas.org/api/v2/graphql`                    |
| 003 (LO)   | Proibição de acesso/estrada na TI  | Permanente | Satélite Planet e MapBiomas                                            | Em apuração | `https://plataforma.alerta.mapbiomas.org/`                                  |

---

**c) Planilha fundiária**

| ID  | Município    | TI Relacionada       | Tipo de dado    | Identificador (Código / CPF / CNPJ) | Área (ha) | Observação                                             | Fonte (Link para download/consulta)                                                   |
|:----|:-------------|:---------------------|:----------------|:------------------------------------|:----------|:-------------------------------------------------------|:--------------------------------------------------------------------------------------|
| 001 | Altamira     | TI Trincheira Bacajá | CAR (Perímetro) | PA-1500602...                       | 1.250     | Sobreposição integral com TI. Base baixada por estado. | `https://consultapublica.car.gov.br/publico/estados/downloads`                        |
| 002 | Lábrea       | TI Caititu           | SIGEF Privado   | 1234.5678...                        | 800       | Imóvel certificado no limite da TI.                    | `https://certificacao.incra.gov.br/csv_shp/export_shp.py`                             |
| 003 | N. Progresso | TI Baú               | SNCI Público    | T-556677/PA                         | 3.500     | Cadastro de norma antiga (SNCI) em área federal.       | `https://certificacao.incra.gov.br/csv_shp/export_shp.py`                             |
| 004 | Apuí         | TI Tenharim          | CCIR (SNCR)     | 9510990856428                       | 1.100     | CCIR baixado automaticamente via API REST v2 (PDF).    | `https://apigateway.conectagov.estaleiro.serpro.gov.br/v2/baixarCcirPorCodigoImovel/` |

---

**d) Planilha de alertas geoespaciais**

| ID  | Fonte do Alerta | Data     | Município    | TI Afetada / Buffer  | Área       | Coordenadas       | Arquivo (Local)  | Fonte (Servidor / URL de Download)                                                             |
|:----|:----------------|:---------|:-------------|:---------------------|:-----------|:------------------|:-----------------|:-----------------------------------------------------------------------------------------------|
| 001 | MapBiomas       | 15/05/26 | Altamira     | TI Trincheira Bacajá | 18,5 ha    | -3.5512, -51.7799 | alerta_rural.zip | `https://plataforma.alerta.mapbiomas.org/downloads` (Alertas por imóvel rural)                 |
| 002 | DETER (INPE)    | 02/06/26 | N. Progresso | TI Baú               | 45,2 ha    | -7.1234, -55.4321 | deter_amz.shp    | `http://terrabrasilis.dpi.inpe.br/geoserver/` (Layer: `deter-amz:deter_amz`)                   |
| 003 | FIRMS (NASA)    | 04/06/26 | Apuí         | TI Tenharim          | N/A (Foco) | -7.1999, -60.0123 | VIIRS_C2.csv     | `https://firms.modaps.eosdis.nasa.gov/download/list.php`                                       |
| 004 | PRODES (INPE)   | Anual    | Lábrea       | TI Caititu (Entorno) | 120 ha     | -7.2589, -64.7890 | prodes_2025.shp  | `http://terrabrasilis.dpi.inpe.br/geoserver/` (Layer: `prodes-legal-amz:yearly_deforestation`) |

---

**e) Planilha de metadados**

| Arquivo/Serviço                         | Origem               | Data obtenção | Responsável | EPSG (Sistema) | Observação Técnica                                                          | Fonte (URL de Conexão ou Download Direto)                      |
|:----------------------------------------|:---------------------|:--------------|:------------|:---------------|:----------------------------------------------------------------------------|:---------------------------------------------------------------|
| **Imóvel_certificado_SIGEF_Brasil.zip** | INCRA                | 04/06/2026    | Benefrancis | EPSG:4674      | Contém todos os perímetros SIGEF do Brasil no Datum SIRGAS 2000.            | `https://certificacao.incra.gov.br/csv_shp/export_shp.py`      |
| **PA_Perimetros_dos_Imoveis.zip**       | SICAR/SFB            | 04/06/2026    | Benefrancis | EPSG:4674      | Base do Pará. Necessita filtro por município pós-download.                  | `https://consultapublica.car.gov.br/publico/estados/downloads` |
| **API_SNCR_REST_v2**                    | SERPRO / Conecta Gov | 04/06/2026    | ESMPU       | N/A (JSON)     | Coleta automatizada de Situação Jurídica, CCIR e Titularidade via CPF/CNPJ. | `https://apigateway.conectagov.estaleiro.serpro.gov.br/v2/`    |
| **FIRMS_VIIRS_S-NPP**                   | NASA                 | 04/06/2026    | ESMPU       | EPSG:4326      | WGS84. Recorte enviado por e-mail no formato SHP/CSV. Requer reprojeção.    | `https://firms.modaps.eosdis.nasa.gov/alerts/create.php`       |

---

### 💡 Nota Estratégica:

> *"Dra. Denise, além de catalogarmos as bases tradicionais, fiz um trabalho de catalogação dos servidores GeoServer (
WFS/WMS) do INPE (TerraBrasilis) e do INCRA (SIGEF/SNCI), permitindo o consumo direto das camadas sem precisar baixar
arquivos manualmente a todo momento.*
>
> *A grande inovação que trago é a integração com
a **API REST v2 do SNCR (Sistema Nacional de Cadastro Rural) via ConectaGov**, o que nos permitirá consultar a situação
de imóveis e baixar PDFs do CCIR automaticamente via CPF/CNPJ ou Código do Imóvel, e a utilização
da **API GraphQL do MapBiomas** e do **NASA FIRMS**, alimentando a nossa IA com alertas de fogo e desmatamento validados
quase em tempo real."*

