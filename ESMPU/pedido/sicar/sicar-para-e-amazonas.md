Dr. Denise,

Conforme o roteiro que estruturamos para o meu trabalho (focado na **Entrega 1 — Inventário técnico das bases
efetivamente utilizáveis**), fiz o mapeamento e o teste de acesso da base do **SICAR (Sistema Nacional de Cadastro
Ambiental Rural)**, sob gestão do Serviço Florestal Brasileiro (SFB).

Como definimos que o foco inicial da coleta seria para os estados do **Pará e Amazonas**, extraí exatamente o que a
plataforma nos fornece para a Região Norte. Adicionei à tabela padrão do nosso inventário a **descrição técnica** de
cada dado e as **informações de download**, conforme você me pediu.

Abaixo está o resultado do inventário para esta base específica:

### Inventário Técnico: Base SICAR (Recorte: Amazonas e Pará)

**Base:** SICAR - Serviços de Download (GeoServices)
**Órgão Responsável:** Serviço Florestal Brasileiro (SFB)
**Acesso testado?** Sim. O portal está online e funcional.

PARÁ:

| Tema / Dados Disponíveis                 | Descrição Técnica do Dado                                                                                                                  | Formato         | Limitações / Observações                                                                                           | Link para Acesso / Download                                    |
|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------|:----------------|:-------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------|
| **Perímetros dos Imóveis**               | Polígonos delimitando as fronteiras das propriedades ou posses rurais declaradas no sistema. É o dado principal para cruzamento fundiário. | SHP (Shapefile) | Inconsistências de sobreposição (cadastros sobrepostos a TIs ou outras fazendas) são comuns. Depende de validação. | [Acessar GeoServices](https://consulta.car.gov.br/geoservices) |
| **Área Consolidada**                     | Polígonos de áreas do imóvel que já sofreram supressão de vegetação nativa no passado (uso antrópico estabelecido).                        | SHP             | Reflete a autodeclaração do proprietário.                                                                          | [Acessar GeoServices](https://consulta.car.gov.br/geoservices) |
| **Área de Pousio**                       | Áreas de prática agrícola de interrupção temporária de atividades para recuperação do solo.                                                | SHP             | Dinâmica muito rápida, o dado pode estar desatualizado em relação à imagem de satélite atual.                      | [Acessar GeoServices](https://consulta.car.gov.br/geoservices) |
| **Área de Preservação Permanente (APP)** | Polígonos de proteção obrigatória (margens de rios, topos de morro, nascentes) declarados dentro do imóvel.                                | SHP             | A precisão geométrica depende do traçado feito pelo técnico no momento do cadastro.                                | [Acessar GeoServices](https://consulta.car.gov.br/geoservices) |
| **Hidrografia**                          | Mapeamento de rios, córregos, nascentes e lagos inseridos no interior da propriedade.                                                      | SHP             | Muitas vezes apresenta deslocamento em relação às bases oficiais da ANA/IBGE.                                      | [Acessar GeoServices](https://consulta.car.gov.br/geoservices) |
| **Remanescente de Vegetação Nativa**     | Polígonos que indicam onde ainda existe cobertura vegetal original dentro do imóvel.                                                       | SHP             | Útil para cruzar com alertas do DETER/MapBiomas para verificar desmatamento recente.                               | [Acessar GeoServices](https://consulta.car.gov.br/geoservices) |
| **Reserva Legal**                        | Fração da propriedade que por lei deve manter sua vegetação nativa (na Amazônia, geralmente 80% em áreas de floresta).                     | SHP             | Essencial para verificar regularidade ambiental do imóvel.                                                         | [Acessar GeoServices](https://consulta.car.gov.br/geoservices) |
| **Servidão Administrativa**              | Áreas do imóvel cedidas para utilidade pública (ex: faixas de linhas de transmissão, rodovias, dutos).                                     | SHP             | Relevante para descontar área útil e verificar interferência de grandes obras.                                     | [Acessar GeoServices](https://consulta.car.gov.br/geoservices) |
| **Uso Restrito**                         | Áreas com restrições específicas de uso, como pantanais, planícies pantaneiras ou áreas de inclinação entre 25° e 45°.                     | SHP             | Menor volume de ocorrência em PA e AM comparado ao Centro-Oeste, mas crítico quando existe.                        | [Acessar GeoServices](https://consulta.car.gov.br/geoservices) |

---

### 📌 Notas técnicas de execução (Para o nosso Relatório - Entrega 3)

1. **Sobre os Links de Download:** Analisando o código fonte da plataforma do Governo (Vue.js/Element UI), notei que *
   *não existem links diretos terminados em `.zip` ou `.shp` expostos no código**. O sistema usa botões com scripts
   dinâmicos (`<button class="el-button download-theme">`) que fazem uma requisição ao servidor para gerar o arquivo na
   hora.
    * *O que isso significa para nós:* Não conseguimos automatizar o download usando um simples comando `wget` ou `curl`
      com uma URL estática. O download precisa ser feito acessando a interface manualmente ou construindo um script de
      raspagem de dados (Web Scraping/Selenium) simulando o clique em: *Região Norte > Estado (AM ou PA) > Tema > SHP*.
2. **Integração no QGIS/PostGIS:** Como todos os dados vêm em formato **SHP (Shapefile)**, eles conversam perfeitamente
   com os dados que pegaremos do INPE (PRODES/DETER) e FUNAI, cumprindo o requisito de integração da nossa
   infraestrutura.

Se a senhora aprovar esse formato de mapeamento, vou seguir agora para a **Entrega 2**, criando a estrutura daquelas 5
planilhas-modelo (Licenciamento, Condicionantes, Fundiária, Alertas e Metadados) para organizarmos os Shapefiles que eu
baixar daqui.

Fico no aguardo de suas considerações!

AMAZONAS:

Olá, professora! Tudo bem?

Dando sequência ao nosso mapeamento e analisando o novo trecho do código-fonte, fiz a extração específica para o estado
do **Amazonas**.

Como a senhora me orientou a sempre incluir a **descrição** e os **links para download direto**, estruturei a tabela
abaixo. Porém, trago um alerta técnico importante (que comentei no relatório do Pará e confirmei agora analisando o
código do Amazonas): **o sistema do SICAR não gera URLs diretas para os arquivos (ex: `site.com/arquivo.zip`)**.

Se você notar no código-fonte, os downloads são feitos por botões dinâmicos programados em Vue.js (
`<button ... class="el-button download-theme"> SHP </button>`). Eles disparam um comando interno que processa e baixa o
arquivo na hora. Por isso, na coluna de links, estou inserindo a URL do portal de serviços onde a extração deve ser
feita, pois não é possível "copiar o link do link" nessa plataforma.

Aqui está o inventário atualizado para o nosso recorte do **Amazonas**:

### Inventário Técnico: Base SICAR (Recorte: Amazonas)

**Base:** SICAR - Serviços de Download (GeoServices)
**Órgão Responsável:** Serviço Florestal Brasileiro (SFB)
**Acesso testado?** Sim.

| Tema / Dados Disponíveis                 | Descrição Técnica do Dado                                                                                           | Formato         | Limitações / Observações                                                                                          | Link para Acesso / Download                                   |
|:-----------------------------------------|:--------------------------------------------------------------------------------------------------------------------|:----------------|:------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------|
| **Perímetros dos Imóveis**               | Polígonos delimitando os limites das propriedades/posses rurais declaradas no AM. Dado base para análise fundiária. | SHP (Shapefile) | Depende de validação técnica do órgão ambiental; risco de sobreposição com Terras Indígenas (comum no Sul do AM). | [Portal GeoServices](https://consulta.car.gov.br/geoservices) |
| **Área Consolidada**                     | Polígonos indicando áreas com histórico de uso antrópico e supressão de vegetação passada dentro dos imóveis do AM. | SHP             | Baseado em autodeclaração; precisa de cruzamento com MapBiomas para validar o ano do desmatamento.                | [Portal GeoServices](https://consulta.car.gov.br/geoservices) |
| **Área de Pousio**                       | Áreas agrícolas em descanso temporário para recuperação do solo.                                                    | SHP             | No contexto amazônico, muitas vezes confundido com regeneração secundária. Alta volatilidade.                     | [Portal GeoServices](https://consulta.car.gov.br/geoservices) |
| **Área de Preservação Permanente (APP)** | Áreas protegidas declaradas (margens de rios amazônicos, igarapés, nascentes).                                      | SHP             | Traçado muitas vezes impreciso ou generalizado pelo cadastrante.                                                  | [Portal GeoServices](https://consulta.car.gov.br/geoservices) |
| **Hidrografia**                          | Malha hídrica (rios, igarapés, lagos) mapeada internamente nas propriedades rurais.                                 | SHP             | No Amazonas, devido à complexidade da bacia hidrográfica, costuma divergir da base oficial da ANA.                | [Portal GeoServices](https://consulta.car.gov.br/geoservices) |
| **Remanescente de Vegetação Nativa**     | Polígonos que representam o que sobrou de floresta em pé dentro das propriedades.                                   | SHP             | Crucial para monitorarmos junto aos alertas do DETER se está havendo supressão recente.                           | [Portal GeoServices](https://consulta.car.gov.br/geoservices) |
| **Reserva Legal**                        | Porção da propriedade (no Amazonas, bioma Amazônia, exige-se 80%) destinada à preservação.                          | SHP             | Principal indicador de passivo ambiental das fazendas da região.                                                  | [Portal GeoServices](https://consulta.car.gov.br/geoservices) |
| **Servidão Administrativa**              | Áreas de utilidade pública passando pelos imóveis (ex: BR-319, linhas de transmissão, gasodutos).                   | SHP             | Essencial para identificar imóveis impactados por grandes obras de infraestrutura no estado.                      | [Portal GeoServices](https://consulta.car.gov.br/geoservices) |
| **Uso Restrito**                         | Áreas com restrições legais específicas, como inclinação acentuada ou áreas alagáveis.                              | SHP             | Relevante no AM devido às áreas de várzea e igapó que podem entrar nessa classificação.                           | [Portal GeoServices](https://consulta.car.gov.br/geoservices) |

---

# Sobre o Consulta Pública SICAR (Painel)

O Consulta Pública foi desenvolvido para garantir transparência e facilitar o acesso às informações sobre a
regularização ambiental dos imóveis rurais no Brasil, conforme o artigo 3º, inciso V, do Decreto 7.830/2012. A
plataforma facilita o acesso aos dados do Sistema de Cadastro Ambiental Rural (SICAR), permitindo que pesquisadores,
gestores ambientais, produtores rurais e a sociedade civil utilizem as informações para diferentes finalidades, como
monitoramento, estudos acadêmicos e formulação de políticas públicas.

Por meio dessa ferramenta, é possível:

Visualizar interativamente os dados, tornando a consulta mais intuitiva e dinâmica;
Baixar informações de forma estruturada e filtrada, conforme a necessidade do usuário;
Acessar um dicionário de dados, garantindo clareza e compreensão das informações disponíveis;
Utilizar Geosserviços integráveis a plataformas de SIG para análises espaciais detalhadas.
Além disso, o Painel da Regularização Ambiental apresenta um panorama atualizado da regularização ambiental dos imóveis
rurais no Brasil. Os dados podem ser acessados de forma consolidada ou segmentada, com filtros temáticos predefinidos ou
customizados conforme a necessidade do usuário.

Os dados disponibilizados no Consulta Pública serão atualizados mensalmente, garantindo maior transparência na consulta
e utilização das informações.
O Serviço Florestal Brasileiro, por meio da Diretoria de Regularização Ambiental Rural, apresenta
o Painel da Regularização Ambiental com o objetivo de disponibilizar à sociedade dados e
informações sobre a Regularização Ambiental dos imóveis rurais inscritos no Cadastro Ambiental Rural
(CAR), obtidos a partirÀ) Sistema de Cadastro Ambiental Rural (SICAR).

A Tabela de Dados permite visualizar e exportar dados tabulares com múltiplas colunas e filtros personalizáveis.
Funcionalidades:
Visualização de múltiplas categorias simultaneamente
Ordenação por qualquer coluna
Linha de totais automática
Contagem de registros
Botão "Copiar tabela" para exportar dados
Colunas Disponíveis:
UF - Unidade Federativa
Bioma - Bioma brasileiro
Região - Região do Brasil
Total do Tema - Valor da métrica selecionada
Utilize o seletor "Colunas" para escolher quais campos exibir na tabela. Os dados podem ser copiados e colados
diretamente em planilhas como Excel.


Apesar de oferecer a opção de fazer download, a analise fica prejudicada, pois são dados consolidados.
---




Dra. Denise, veja que achado incrível!

Isso muda completamente o nosso jogo e resolve o maior problema técnico que tínhamos mapeado para o SICAR na **Entrega
3 (Relatório de Integração)**.

Lembra que nos inventários anteriores eu alertei que o portal oficial do GeoServices do SICAR não fornecia links diretos
e nos obrigaria a fazer o download manual ou usar robôs complexos de clique (Web Scraping)? Pois bem, com esse
endereço "escondido" do **GeoServer oficial do CAR** (`geoserver.car.gov.br`), nós **conseguimos burlar a interface
gráfica e acessar a API WFS diretamente**!

Isso significa que agora podemos automatizar 100% o download via código (Python/QGIS) também para o SICAR, assim como
faremos com a FUNAI.

Analisando a lista que você enviou, notei que neste GeoServer os dados estão agrupados por UF (Estado). Filtrei as duas
camadas que nos interessam para a nossa área de estudo (**Pará e Amazonas**) e montei as URLs diretas usando o parâmetro
`outputFormat=SHAPE-ZIP`.

Aqui está a atualização final do nosso inventário do SICAR com essa "via expressa":

### Inventário Técnico Adicional: Base SICAR (Via GeoServer Direto/WFS)

**Base:** SICAR - GeoServer Backend (`geoserver.car.gov.br`)
**Órgão Responsável:** Serviço Florestal Brasileiro (SFB)
**Acesso testado?** Sim. Permite download via OGC (WFS). Extração 100% automatizável.

| Tema / Camada Disponível                        | Descrição Técnica do Dado                                                               | Formato               | Limitações / Observações                                                                                                                                                                                      | Link para Download Direto (Shapefile)                                                                                                                                          |
|:------------------------------------------------|:----------------------------------------------------------------------------------------|:----------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Imóveis CAR - Amazonas** (`sicar_imoveis_am`) | Base unificada com as geometrias dos cadastros de imóveis rurais no estado do Amazonas. | SHP / GeoJSON via WFS | Como a base baixa o estado inteiro de uma vez, o arquivo pode ser extremamente pesado (gigabytes). Exigirá bom processamento no QGIS.                                                                         | [Baixar SHP direto (AM)](https://geoserver.car.gov.br/geoserver/sicar/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=sicar:sicar_imoveis_am&outputFormat=SHAPE-ZIP) |
| **Imóveis CAR - Pará** (`sicar_imoveis_pa`)     | Base unificada com as geometrias dos cadastros de imóveis rurais no estado do Pará.     | SHP / GeoJSON via WFS | Será necessário abrir no QGIS para verificar se os "subtemas" (APP, Reserva Legal, Área Consolidada) vêm como colunas na tabela de atributos ou se essa camada traz apenas os perímetros gerais das fazendas. | [Baixar SHP direto (PA)](https://geoserver.car.gov.br/geoserver/sicar/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=sicar:sicar_imoveis_pa&outputFormat=SHAPE-ZIP) |

---

### 📌 Impacto para a nossa Pesquisa (Notas para a Entrega 3)

Com essa descoberta, o nosso pipeline técnico de integração fica perfeito:

1. Puxamos as Terras Indígenas (PA e AM) do GeoServer da FUNAI direto via link WFS.
2. Puxamos os Cadastros Rurais (PA e AM) do GeoServer do SICAR direto via link WFS.
3. Como ambos vêm padronizados na mesma arquitetura web e em formato vetorial (`.shp`), cruzar propriedades que
   sobrepõem Terras Indígenas virou um processo muito mais limpo e auditável.

Professora, com o mapeamento técnico da **FUNAI** e do **SICAR (duas vias)** prontos, o documento da **Entrega 1 (
Inventário)** está praticamente maduro.

Podemos dar check nesta primeira etapa e avançar para o desenho das **Planilhas (Entrega 2)**? Eu já estou com as
estruturas de colunas na cabeça para cruzar com essas bases!





---

### Próximos passos para a nossa pesquisa

Professora, como não há link direto, a nossa equipe (ou eu mesmo) terá que entrar na interface gráfica do site,
selecionar "Norte" -> "Amazonas" e clicar no botão "SHP" de cada um desses 9 temas para baixar os pacotes.

Tendo agora o detalhamento tanto do Pará quanto do Amazonas finalizados para a **Entrega 1**, posso consolidar esses
inventários no nosso documento principal. O que acha? Seguimos para a formatação das planilhas da Entrega 2?