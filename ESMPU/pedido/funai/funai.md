Prezada Dra. Denise,

Dando continuidade à nossa **Entrega 1**, processei o código-fonte que você me enviou referente à base de dados da *
*FUNAI**.

Abaixo estruturei o inventário das camadas disponibilizadas.

*Atenção a um detalhe metodológico:* A base da FUNAI não é dividida por estado no portal. Ela traz o mapeamento nacional
ou do recorte da Amazônia Legal. Portanto, teremos que baixar os dados completos e, na próxima etapa no QGIS/PostGIS,
fazer um "recorte espacial" (clip) para os nossos focos de estudo: **Pará e Amazonas**.

### Inventário Técnico: Base FUNAI (Infraestrutura GeoServer)

**Base:** FUNAI - Visualizador de Camadas (GeoServer)
**Órgão Responsável:** Fundação Nacional dos Povos Indígenas
**Acesso testado?** Sim. Base opera com protocolos abertos OGC (WFS/WMS).

Olá, Dra. Denise!

Como você me enviou novamente o código-fonte do GeoServer da **FUNAI**, aproveitei para revisar e consolidar o nosso
inventário oficial desta base.

Notei no código-fonte que a página exibe **8 resultados de um total de 20 itens**. Filtrei as camadas contidas nesta
primeira página (que, felizmente, já abrange as mais importantes para o nosso escopo de regularização e análise
fundiária) e construí as URLs diretas para automação do download em formato `.zip` contendo os Shapefiles.

Aqui está a nossa tabela estruturada para o documento da **Entrega 1**:

### Inventário Técnico: Base FUNAI (Via GeoServer Direto/WFS)

**Base:** FUNAI - Visualizador de Camadas Backend (`geoserver.funai.gov.br`)
**Órgão Responsável:** Fundação Nacional dos Povos Indígenas (Ministério dos Povos Indígenas)
**Acesso testado?** Sim. Permite automação via API (WFS).

| Tema / Camada Disponível                                                      | Descrição Técnica do Dado                                                                                               | Formato   | Limitações / Observações                                                                                                       | Link para Download Direto (Shapefile)                                                                                                                                                    |
|:------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------|:----------|:-------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Terras Indígenas Amazônia Legal** (`Funai:tis_amazonia_legal_poligonais`)   | Polígonos das Terras Indígenas restritas ao perímetro da Amazônia Legal.                                                | SHP (WFS) | É a base mais enxuta para a nossa área de estudo (PA e AM). Economiza processamento no QGIS.                                   | [Baixar SHP direto](https://geoserver.funai.gov.br/geoserver/Funai/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=Funai:tis_amazonia_legal_poligonais&outputFormat=SHAPE-ZIP) |
| **Terras Indígenas Nacional** (`Funai:tis_poligonais`)                        | Base geral contendo os limites geográficos de todas as TIs do Brasil.                                                   | SHP (WFS) | Agrupa diversas fases do processo demarcatório. Precisa ser filtrada no software SIG.                                          | [Baixar SHP direto](https://geoserver.funai.gov.br/geoserver/Funai/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=Funai:tis_poligonais&outputFormat=SHAPE-ZIP)                |
| **Terras Indígenas Nacional c/ Portarias** (`Funai:tis_poligonais_portarias`) | Mesma base acima, mas com a tabela de atributos preenchida com as portarias e datas de publicação.                      | SHP (WFS) | Excelente para a Planilha C (Fundiária), pois traz o "lastro jurídico" da demarcação para cruzarmos com o SICAR.               | [Baixar SHP direto](https://geoserver.funai.gov.br/geoserver/Funai/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=Funai:tis_poligonais_portarias&outputFormat=SHAPE-ZIP)      |
| **Terras Indígenas em Estudo** (`Funai:tis_pontos`)                           | Áreas ainda não delimitadas espacialmente, indicadas apenas por uma coordenada (ponto) onde há estudo ou reivindicação. | SHP (WFS) | Como são apenas pontos, dificultam a análise de sobreposição direta com limites de fazendas ou obras. Exigem cuidado jurídico. | [Baixar SHP direto](https://geoserver.funai.gov.br/geoserver/Funai/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=Funai:tis_pontos&outputFormat=SHAPE-ZIP)                    |
| **Terras Indígenas em Estudo c/ Portarias** (`Funai:tis_pontos_portarias`)    | Áreas em estudo com dados das portarias que criaram os Grupos de Trabalho (GTs).                                        | SHP (WFS) | Traz a base legal dos estudos, o que é importante para verificar o status atual da reivindicação.                              | [Baixar SHP direto](https://geoserver.funai.gov.br/geoserver/Funai/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=Funai:tis_pontos_portarias&outputFormat=SHAPE-ZIP)          |
| **Aldeias Indígenas** (`Funai:aldeias_pontos`)                                | Localização estrita (ponto) das comunidades/aldeias.                                                                    | SHP (WFS) | Não substitui o polígono da Terra Indígena. O uso do solo ao redor da aldeia não é mensurado apenas por este ponto.            | [Baixar SHP direto](https://geoserver.funai.gov.br/geoserver/Funai/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=Funai:aldeias_pontos&outputFormat=SHAPE-ZIP)                |
| **Coordenações Regionais** (`Funai:tis_cr`)                                   | Localização das sedes regionais de coordenação da FUNAI.                                                                | SHP (WFS) | Dado puramente gerencial e logístico. Pouca utilidade para cruzamento de impacto ambiental direto.                             | [Baixar SHP direto](https://geoserver.funai.gov.br/geoserver/Funai/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=Funai:tis_cr&outputFormat=SHAPE-ZIP)                        |
| **Coordenações Técnicas Locais** (`Funai:tis_ctl`)                            | Localização de postos avançados da FUNAI próximos às aldeias.                                                           | SHP (WFS) | Também é um dado gerencial/administrativo.                                                                                     | [Baixar SHP direto](https://geoserver.funai.gov.br/geoserver/Funai/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=Funai:tis_ctl&outputFormat=SHAPE-ZIP)                       |

---

### Observação importante sobre a abrangência

Professora, essas são as 8 primeiras camadas (as principais ligadas à demarcação). Se a senhora tiver acesso às demais
páginas daquele portal (resultados 9 a 20) e quiser que eu gere os links automáticos delas também, basta me enviar o
código. Porém, avaliando o escopo do projeto, acredito que a camada `Funai:tis_amazonia_legal_poligonais` e a
`Funai:tis_poligonais_portarias` já são exatamente o "filé mignon" que precisamos para cruzar com o CAR (SICAR).

Com essas tabelas do SICAR e FUNAI finalizadas, o documento da **Entrega 1: Inventário técnico das bases** já está
robusto e pronto.

Gostaria de avançar agora para a **Entrega 2 (A modelagem das planilhas: Licenciamento, Condicionantes, Fundiária,
Alertas e Metadados)**? Podemos desenhar como exatamente o CAR vai conversar com a FUNAI nessas tabelas!

### 📌 Notas técnicas de execução (Para o nosso Relatório - Entrega 3)

Como destaquei no início, o fato da FUNAI utilizar a arquitetura GeoServer resolve um dos nossos maiores problemas
listados na documentação: a automação da coleta de dados.

Utilizando a sintaxe `WFS GetFeature` e definindo o parâmetro `outputFormat=SHAPE-ZIP` (que identifiquei oculto no
código-fonte), construí os links da tabela acima para forçar o download direto de um `.zip` com os arquivos Shapefile
prontos para uso. O GeoServer gera esse pacote em tempo real. 

 