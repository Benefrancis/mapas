OK, vamos integrar seus dados do IBGE, FUNAI e INCRA no GeoServer, para que possam ser exibidos na sua aplicação.

**Passos:**

1.  **Acesso ao GeoServer:**
    *   Acesse a interface web do GeoServer através do seu navegador em `http://localhost:8080/geoserver` (ou o endereço configurado, caso tenha alterado a porta).  Utilize as credenciais configuradas no `docker-compose.yml`: `admin` (usuário) e `geoserver` (senha), a menos que você tenha alterado.

2.  **Criação de um Workspace:**
    *   No GeoServer, vá em "Data" -> "Workspaces" e clique em "Add new workspace".
    *   Defina um nome (ex: `mapas_brasil`) e um namespace URI (ex: `http://www.example.com/mapas_brasil`).  O namespace URI é um identificador único, não precisa ser um endereço web real.  Clique em "Submit".

3.  **Criação de um Data Store (para dados do PostGIS):**
    *   Vá em "Data" -> "Stores" e clique em "Add new Store".
    *   Escolha "PostGIS" na lista de fontes de dados vetoriais.
    *   Preencha os dados de conexão com o seu banco PostGIS:
        *   **Workspace:** Selecione o workspace que você criou (`mapas_brasil`).
        *   **Data Source Name:** Um nome para identificar este Data Store (ex: `postgis_mapas`).
        *   **Connection Parameters:**
            *   `host`: `db` (nome do container do banco no Docker Compose)
            *   `port`: `5432`
            *   `database`: `mapas`
            *   `user`: `postgres`
            *   `passwd`: `postgres`
            *   `schema`: `mapas`  (importante especificar o schema)
            *   marque a opção "validate connections"
    *   Clique em "Save".

4.  **Publicação das Camadas (Layers) do PostGIS:**
    *   Após salvar o Data Store, o GeoServer listará as tabelas disponíveis no schema `mapas` do seu banco de dados.
    *   Para cada tabela (ex: `tb_uf`), clique em "Publish".
    *   Na página de edição da camada:
        *   **Data:**  Verifique se os campos estão corretos (nome da camada, título, etc.).
        *   **Coordinate Reference Systems:**
            *   `Native SRS`:  `EPSG:4326` (de acordo com a sua tabela).
            *   `Declared SRS`:  `EPSG:4326`.
            *   Marque "Reproject Native to Declared".
            *   `SRS Handling`: Deixe em "Force declared".
        *   **Bounding Boxes:**  Clique em "Compute from data" e "Compute from native bounds" para preencher os campos de bounding box (caixa delimitadora).  Isso é crucial para o GeoServer saber a extensão espacial dos seus dados.
        *   **Publishing:**
            *   Configure os estilos que deseja usar.  O GeoServer oferece alguns estilos padrão, ou você pode criar seus próprios (ver abaixo).
    *   Clique em "Save".
    *   Repita este processo para as outras tabelas (FUNAI, INCRA) que você deseja publicar.

5.  **Criação de um Data Store (para Shapefiles):**

    *   Vá em "Data" -> "Stores" e clique em "Add new Store".
    *   Escolha "Shapefile" na lista de fontes de dados vetoriais.
    *   Preencha os dados:
        *   **Workspace:** Selecione o workspace que você criou (`mapas_brasil`).
        *   **Data Source Name:** Um nome para identificar este Data Store (ex: `shapefiles_ibge`).
        *   **Connection Parameters:**
            *   **URL:**  Aqui é onde fica a parte importante.  Você precisa montar um volume no container do GeoServer para que ele possa acessar os seus shapefiles.  No seu `docker-compose.yml`, adicione um volume para o GeoServer:

```yaml
geoserver:
  # ... outras configurações
  volumes:
    - ./geoserver/logs:/var/log/geoserver
    - ./geoserver/datadir:/var/lib/geoserver_data
    - ./geoserver/gwc_config:/var/lib/geowebcache/config
    - ./geoserver/gwc:/var/lib/geowebcache/cache
    - ./geoserver/netcfd:/var/lib/netcdf
    - ./geoserver/grib_cache:/var/lib/grib
    - ./geoserver/context.xml:/opt/tomcat/conf/Catalina/localhost/geoserver.xml # mapeamento para o seu context.xml
    - ./data_shapefiles:/usr/share/geoserver/shapefiles  # Novo volume!
```

        *   Crie uma pasta chamada `data_shapefiles` no mesmo diretório onde está o seu `docker-compose.yml`.  Coloque seus shapefiles (o `.shp`, `.shx`, `.dbf`, `.prj` e outros arquivos associados) dentro dessa pasta.

        *   No campo "URL" do GeoServer, coloque algo como: `file:///usr/share/geoserver/shapefiles/nome_do_seu_shapefile.shp`.  Substitua `nome_do_seu_shapefile.shp` pelo nome do arquivo `.shp` do seu shapefile.

    *   Clique em "Save".

6.  **Publicação das Camadas (Layers) dos Shapefiles:**

    *   Após salvar o Data Store, o GeoServer listará o shapefile.
    *   Clique em "Publish".
    *   Siga as mesmas instruções do passo 4 para configurar o CRS, bounding boxes e estilos.

7.  **Estilos (SLD):**
    *   Se você quiser personalizar a aparência das suas camadas, vá em "Data" -> "Styles".
    *   Você pode criar novos estilos (usando SLD, Styled Layer Descriptor) ou editar os estilos existentes.
    *   Os estilos definem cores, larguras de linha, símbolos, etc.
    *   Depois de criar ou editar um estilo, você pode associá-lo a uma camada na página de edição da camada (passo 4).

8.  **Visualização das Camadas:**
    *   Vá em "Data" -> "Layer Preview".
    *   Encontre a camada que você publicou e clique em "OpenLayers".  Isso abrirá um mapa com a sua camada.  Verifique se a geometria está sendo exibida corretamente.

9.  **Integração na sua aplicação (index.html):**
    *   Agora, altere o seu arquivo `index.html` para usar as camadas do GeoServer.  Use o seguinte código de exemplo (substitua os valores pelos seus):

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">

<head>
    <title>Mapa</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/leaflet.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/leaflet.css"/>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        #map {
            height: 500px;
            width: 100%;
        }

        #layer-controls {
            display: flex;
            flex-wrap: wrap;
            justify-content: flex-start; /* Alinha os itens à esquerda */
            margin-bottom: 10px;
        }

        .layer-control {
            margin-right: 10px; /* Espaçamento entre os checkboxes */
            margin-bottom: 5px;   /* Espaçamento abaixo dos checkboxes */
        }
    </style>
</head>
<body>
<div id="map"></div>

<div id="layer-controls">
    <!-- Os controles de camada serão gerados aqui dinamicamente -->
</div>

<script>
    var map = L.map('map').setView([-23.5505, -46.6333], 7);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // Função para buscar as camadas do GeoServer
    function getLayersFromGeoServer() {
        var geoserverURL = "http://localhost:8080/geoserver/rest/workspaces/mapas_brasil/layers.json";
        var username = "admin";  // Substitua pelo seu usuário do GeoServer
        var password = "geoserver"; // Substitua pela sua senha do GeoServer

        $.ajax({
            url: geoserverURL,
            type: "GET",
            dataType: "json",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
            },
            success: function (data) {
                var layers = data.layers.layer;
                createLayerControls(layers);
            },
            error: function (xhr, status, error) {
                console.error("Erro ao buscar as camadas do GeoServer:", error);
            }
        });
    }

    // Função para criar os controles de camada dinamicamente
    function createLayerControls(layers) {
        var layerControlsDiv = document.getElementById('layer-controls');

        layers.forEach(function (layer) {
            var layerName = layer.name;
            var layerTitle = layer.name; // Você pode buscar o título da camada separadamente, se precisar

            // Cria o elemento checkbox
            var checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.id = layerName + '-checkbox';
            checkbox.checked = true; // Camada visível por padrão

            // Cria o label para o checkbox
            var label = document.createElement('label');
            label.htmlFor = layerName + '-checkbox';
            label.textContent = layerTitle;

            // Cria o div para o controle de camada (checkbox + label)
            var layerControlDiv = document.createElement('div');
            layerControlDiv.classList.add('layer-control');
            layerControlDiv.appendChild(checkbox);
            layerControlDiv.appendChild(label);

            // Adiciona o controle de camada ao div principal
            layerControlsDiv.appendChild(layerControlDiv);

            // Cria a camada WMS
            var wmsLayer = L.tileLayer.wms("http://localhost:8080/geoserver/mapas_brasil/wms", {
                layers: 'mapas_brasil:' + layerName,
                format: 'image/png',
                transparent: true,
                version: '1.1.0',
                tiled: true
            }).addTo(map);  // Adiciona a camada ao mapa inicialmente

            // Adiciona o event listener para o checkbox
            checkbox.addEventListener('change', function () {
                if (this.checked) {
                    map.addLayer(wmsLayer);
                } else {
                    map.removeLayer(wmsLayer);
                }
            });
        });
    }

    // Chama a função para buscar as camadas do GeoServer quando a página carregar
    $(document).ready(function () {
        getLayersFromGeoServer();
    });

    var marker = L.marker([-23.5505, -46.6333]).addTo(map);

    function updatePosition() {
        $.ajax({
            url: "/update-position",
            success: function (data) {
                var lat = data.latitude;
                var lon = data.longitude;
                marker.setLatLng([lat, lon]);
                map.panTo([lat, lon]);
            }
        });
    }

    setInterval(updatePosition, 5000);
</script>
</body>
</html>
```

**Observações importantes:**

*   **Dockerfile e `docker-compose.yml`:** Não é necessário fazer alterações significativas no seu `Dockerfile` ou `docker-compose.yml`, exceto pela adição do volume para os shapefiles no `docker-compose.yml` como mostrado acima.  Certifique-se de reconstruir os containers Docker após fazer essa alteração: `docker-compose down && docker-compose up --build`
*   **Nomes de camadas e workspaces:**  Substitua os nomes de camadas e workspaces nos exemplos acima pelos nomes que você realmente criou no GeoServer.
*   **CRS:** Certifique-se de que o CRS (sistema de referência de coordenadas) das suas camadas no GeoServer e na sua aplicação (Leaflet) sejam consistentes (geralmente `EPSG:4326`).
*   **Firewall:** Se você tiver um firewall habilitado, verifique se ele permite o tráfego na porta 8080 (para acessar o GeoServer) e na porta 80 (para acessar a sua aplicação).
*   **Logs:** Verifique os logs do GeoServer (na pasta `/geoserver/logs` dentro do container) se você tiver problemas.
*   **Shapefile Structure:** A estrutura dos seus shapefiles precisa estar correta (com os arquivos `.shp`, `.shx`, `.dbf`, `.prj` e outros no mesmo diretório).
*   **CORS:** Se você estiver enfrentando problemas de CORS (Cross-Origin Resource Sharing), você precisará configurar o GeoServer para permitir solicitações de origem cruzada.  Isso pode ser feito adicionando um filtro no `web.xml` do GeoServer.

Problemas de CORS (Cross-Origin Resource Sharing) ocorrem quando um script rodando em um domínio (por exemplo, `http://localhost`) tenta acessar recursos de um domínio diferente (por exemplo, `http://localhost:8080`, onde o GeoServer está rodando). Por padrão, os navegadores bloqueiam essas requisições por motivos de segurança.

Para configurar o GeoServer (executando em Tomcat) para aceitar requisições CORS, você precisa adicionar um filtro ao arquivo `web.xml` do GeoServer. Aqui estão os passos:

1.  **Localize o arquivo `web.xml`:**

    Dentro do container Docker do GeoServer, o arquivo `web.xml` está localizado em:

    `/opt/tomcat/webapps/geoserver/WEB-INF/web.xml`

2.  **Edite o arquivo `web.xml`:**

    Você pode editar o arquivo diretamente no container Docker (não recomendado para persistência) ou mapear o arquivo `web.xml` como um volume no seu `docker-compose.yml`. A melhor prática é mapear o arquivo como um volume, para que as alterações persistam mesmo após reiniciar o container.

    No seu `docker-compose.yml`, você já tem uma linha que mapeia o `context.xml`:

    ```yaml
    - ./geoserver/context.xml:/opt/tomcat/conf/Catalina/localhost/geoserver.xml
    ```

    Você pode fazer o mesmo para o `web.xml`. Adicione a seguinte linha ao bloco `volumes` do serviço `geoserver`:

    ```yaml
    - ./geoserver/web.xml:/opt/tomcat/webapps/geoserver/WEB-INF/web.xml
    ```

    Isso mapeará um arquivo chamado `web.xml` na pasta `./geoserver/` do seu host para o local correto dentro do container.

3.  **Crie o arquivo `web.xml` (se não existir) e adicione o filtro CORS:**

    Crie um arquivo chamado `web.xml` na pasta `./geoserver/` (se ele ainda não existir). Copie o conteúdo original do arquivo `web.xml` do container para este arquivo (você pode usar `docker cp` para copiar o arquivo). Em seguida, adicione o seguinte código para configurar o filtro CORS:

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                                 http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
             version="4.0">

        <!-- ... (Outras configurações existentes) ... -->

        <!-- Filtro CORS -->
        <filter>
            <filter-name>CorsFilter</filter-name>
            <filter-class>org.apache.catalina.filters.CorsFilter</filter-class>
            <init-param>
                <param-name>cors.allowed.origins</param-name>
                <param-value>*</param-value>  <!-- Permitir de qualquer origem (NÃO recomendado para produção) -->
            </init-param>
            <init-param>
                <param-name>cors.allowed.methods</param-name>
                <param-value>GET,POST,PUT,DELETE,HEAD,OPTIONS</param-value>
            </init-param>
            <init-param>
                <param-name>cors.allowed.headers</param-name>
                <param-value>Content-Type,X-Requested-With,Accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization</param-value>
            </init-param>
            <init-param>
                <param-name>cors.exposed.headers</param-name>
                <param-value>Access-Control-Allow-Origin,Access-Control-Allow-Credentials</param-value>
            </init-param>
            <init-param>
                <param-name>cors.support.credentials</param-name>
                <param-value>true</param-value>
            </init-param>
        </filter>

        <filter-mapping>
            <filter-name>CorsFilter</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>

        <!-- ... (Outras configurações existentes) ... -->

    </web-app>
    ```

    **Explicação:**

    *   **`cors.allowed.origins`:** Define as origens permitidas.  `*` permite requisições de qualquer origem, o que **NÃO é recomendado para produção**.  Em vez disso, especifique os domínios exatos que você quer permitir (por exemplo, `http://localhost,http://meudominio.com`).
    *   **`cors.allowed.methods`:** Define os métodos HTTP permitidos.
    *   **`cors.allowed.headers`:** Define os headers permitidos nas requisições.
    *   **`cors.exposed.headers`:** Define os headers que o servidor expõe ao cliente.
    *   **`cors.support.credentials`:** Define se as credenciais (cookies, headers de autorização) devem ser suportadas.

4.  **Reinicie o container do GeoServer:**

    ```bash
    docker-compose down
    docker-compose up --build
    ```

    Isso irá aplicar as alterações no arquivo `web.xml`.

**Considerações de segurança:**

*   **`cors.allowed.origins`:** NUNCA use `*` em ambientes de produção. Especifique explicitamente as origens permitidas para evitar vulnerabilidades de segurança.
*   **HTTPS:** Se sua aplicação e GeoServer estiverem rodando em HTTPS, certifique-se de que as origens especificadas em `cors.allowed.origins` também usem HTTPS.
*   **Revise as configurações:** Verifique se as outras configurações do filtro CORS são adequadas para sua aplicação.

**Alternativas:**

*   **Proxy reverso:** Em vez de configurar o CORS no GeoServer, você pode usar um proxy reverso (como Nginx ou Apache) para servir tanto a sua aplicação quanto o GeoServer no mesmo domínio. Isso elimina a necessidade de configurar o CORS.

Após seguir esses passos, o GeoServer deverá aceitar requisições CORS da sua aplicação. Se você ainda estiver enfrentando problemas, verifique os logs do Tomcat para ver se há algum erro relacionado ao filtro CORS.




**Resumo:**

O processo envolve configurar o GeoServer para acessar seus dados (PostGIS e Shapefiles), publicar as camadas, definir estilos e, finalmente, usar essas camadas em sua aplicação Leaflet.  Certifique-se de verificar os logs do GeoServer e inspecionar o tráfego de rede no seu navegador (ferramentas de desenvolvedor) para diagnosticar quaisquer problemas.

Lembre-se de que este é um guia passo a passo. Podem surgir problemas específicos com seus dados ou configuração, mas essas etapas devem fornecer um bom ponto de partida.

Boa sorte!
