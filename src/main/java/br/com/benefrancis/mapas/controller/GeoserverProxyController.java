package br.com.benefrancis.mapas.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Enumeration;

@RestController
@RequestMapping("/proxy/geoserver")
public class GeoserverProxyController {

    @Value("${geoserver.internal.url}")
    private String geoserverInternalUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/{workspace}/wms")
    public ResponseEntity<byte[]> proxyWmsRequest(@PathVariable String workspace, HttpServletRequest request) {

        String targetUrl = geoserverInternalUrl + "/" + workspace + "/wms";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(targetUrl)
                .query(request.getQueryString()); // Maneira mais robusta de passar todos os parâmetros

        URI targetUri = uriBuilder.build(true).toUri();

        // Copia os cabeçalhos da requisição original para a nova requisição
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            // Evita copiar cabeçalhos que podem causar problemas
            if (!headerName.equalsIgnoreCase(HttpHeaders.HOST) &&
                    !headerName.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)) {
                headers.set(headerName, request.getHeader(headerName));
            }
        }

        // Cria a entidade da requisição
        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, targetUri);

        // Executa a requisição e retorna a resposta como um array de bytes (funciona para imagens e JSON)
        return restTemplate.exchange(requestEntity, byte[].class);
    }
}