package br.com.benefrancis.mapas.service;

import br.com.benefrancis.mapas.entity.AtosLegais;
import br.com.benefrancis.mapas.entity.Contatos;
import br.com.benefrancis.mapas.entity.UnidadeConservacao;
import br.com.benefrancis.mapas.exceptions.ApiFetchException;
import br.com.benefrancis.mapas.exceptions.DataParseException;
import br.com.benefrancis.mapas.exceptions.DataPersistenceException;
import br.com.benefrancis.mapas.repository.UnidadeConservacaoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadeConservacaoService {


    @Value("${app.base-url}")
    private final String baseUrl = "https://cnuc-backend.mma.gov.br/api/v1/uc/";


    private static final Logger logger = LoggerFactory.getLogger(UnidadeConservacaoService.class);

    private final UnidadeConservacaoRepository unidadeConservacaoRepository;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();



    @Async
    @Transactional
    public void processUnidadeConservacao(int id) {
        try {
            JsonNode ucData = fetchUnidadeConservacao(id);
            if (ucData != null) {
                UnidadeConservacao uc = parseUnidadeConservacao(ucData);
                unidadeConservacaoRepository.save(uc);
                logger.info("UC with ID {} processed and saved/updated.", id);
            } else {
                logger.warn("No data fetched for UC ID: {}", id);
            }
        } catch (ApiFetchException e) {
            logger.error("Error fetching data for UC ID: {}", id, e);
            throw e; // Re-lança a exceção para o handler global
        } catch (DataParseException e) {
            logger.error("Error parsing data for UC ID: {}", id, e);
            throw e; // Re-lança a exceção para o handler global
        } catch (DataAccessException e) {
            logger.error("Error accessing database for UC ID: {}", id, e);
            throw new DataPersistenceException("Error persisting data", e);
        } catch (Exception e) {
            logger.error("Unexpected error processing UC ID: {}", id, e);
        }
    }


    private Contatos parseContato(JsonNode contatoNode, UnidadeConservacao uc) {
        Contatos contato = new Contatos();
        try {
            contato.setEndCep(getTextValue(contatoNode, "end_cep"));
            contato.setEndUf(getTextValue(contatoNode, "end_uf"));
            contato.setEndMunicipio(getTextValue(contatoNode, "end_municipio"));
            contato.setEndBairro(getTextValue(contatoNode, "end_bairro"));
            contato.setEndLogradouro(getTextValue(contatoNode, "end_logradouro"));
            contato.setFullAddress(getTextValue(contatoNode, "full_address"));
            contato.setUcSite(getTextValue(contatoNode, "uc_site"));
            contato.setUnidadeConservacao(uc);
        } catch (Exception e) {
            logger.error("Erro ao fazer o parsing do contato", e);
            throw new DataParseException("Erro ao fazer o parsing do contato", e);
        }

        return contato;
    }

    private String getTextValue(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName)) {
            JsonNode fieldNode = node.get(fieldName);
            if (!fieldNode.isNull()) {
                return fieldNode.asText();
            }
        }
        return null;
    }

    private LocalDateTime parseDateTime(JsonNode ucData, String fieldName) {
        String dateTimeString = getTextValue(ucData, fieldName);
        if (dateTimeString != null && !dateTimeString.isEmpty()) {
            List<DateTimeFormatter> formatters = Arrays.asList(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
            );

            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDateTime.parse(dateTimeString, formatter);
                } catch (DateTimeParseException e) {
                    // Tentar o próximo formato
                    logger.debug("Tentativa de parse da data {} com formato {} falhou: {}", dateTimeString, formatter.toString(), e.getMessage());
                }
            }
            logger.warn("Não foi possível fazer o parse da data para o campo {}: {}", fieldName, dateTimeString);
        }
        return null;
    }


    //Dentro da classe UnidadeConservacaoService
    @Retryable(value = {ApiFetchException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    private JsonNode fetchUnidadeConservacao(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + id))
                    .header("Content-Type", "application/json")
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readTree(response.body());
            } else {
                logger.error("Failed to fetch UC data for ID: {}. Status code: {}", id, response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching UC data for ID: {}", id, e);
            Thread.currentThread().interrupt();  // Restore the interrupted status
            throw new ApiFetchException("Error fetching data from API", e);
        }
    }

    private UnidadeConservacao parseUnidadeConservacao(JsonNode ucData) {
        UnidadeConservacao uc = new UnidadeConservacao();
        try {
            uc.setUcId(ucData.get("uc_id").asInt());
            uc.setUcCodCnuc(getTextValue(ucData, "uc_cod_cnuc"));
            uc.setUcNom(getTextValue(ucData, "uc_nom"));
            uc.setUcObjetivo(getTextValue(ucData, "uc_objetivo"));
            uc.setDtCertificacao(parseDateTime(ucData, "dt_certificacao"));
            uc.setDtAprovacao(parseDateTime(ucData, "dt_aprovacao"));
            uc.setUcPlanManejoAprov(ucData.get("uc_plan_manejo_aprov").asBoolean());
            uc.setUcExistenciaConselho(ucData.get("uc_existencia_conselho").asBoolean());
            uc.setUcOutrosInstrumentos(ucData.get("uc_outros_instrumentos").asBoolean());
            uc.setAnoCriacao(getTextValue(ucData, "ano_criacao"));
            uc.setCodWdpa(getTextValue(ucData, "cod_wdpa"));
            uc.setOgNome(getTextValue(ucData, "og_nome"));
            uc.setEsfNome(getTextValue(ucData, "esf_nome"));
            uc.setCatmanejNome(getTextValue(ucData, "catmanej_nome"));
            uc.setCatiucnNome(getTextValue(ucData, "catiucn_nome"));
            uc.setGrumanejNome(getTextValue(ucData, "grumanej_nome"));
            uc.setSitViewSituacao(getTextValue(ucData, "sit_view_situacao"));
            uc.setEffectivity(getTextValue(ucData, "effectivity"));
            uc.setInShapeHabilitado(getTextValue(ucData, "in_shape_habilitado"));
            uc.setPaginaUc(getTextValue(ucData, "pagina_uc"));
            uc.setApiUc(getTextValue(ucData, "api_uc"));
            uc.setLogoUc(getTextValue(ucData, "logo_uc"));
            uc.setAtoLegalCriacaoFullName(getTextValue(ucData, "ato_legal_criacao_full_name"));
            uc.setAtoLegalCriacaoLink(getTextValue(ucData, "ato_legal_criacao_link"));
            uc.setAreaUcAtoLegalCriacao(getTextValue(ucData, "area_uc_ato_legal_criacao"));
            uc.setAreaUcAtoLegalRecente(getTextValue(ucData, "area_uc_ato_legal_recente"));
            uc.setOutrosAtosLegaisFullName(getTextValue(ucData, "outros_atos_legais_full_name"));
            uc.setOutrosAtosLegaisLink(getTextValue(ucData, "outros_atos_legais_link"));
            uc.setPlanoManejoFullName(getTextValue(ucData, "plano_manejo_full_name"));
            uc.setPlanoManejoLinks(getTextValue(ucData, "plano_manejo_links"));
            uc.setDocConselhoFullName(getTextValue(ucData, "doc_conselho_full_name"));
            uc.setDocConselhoLink(getTextValue(ucData, "doc_conselho_link"));
            uc.setOutInstGestFullName(getTextValue(ucData, "out_inst_gest_full_name"));
            uc.setOutInstGestLinks(getTextValue(ucData, "out_inst_gest_links"));
            uc.setGeoAreaHectaresUc(getTextValue(ucData, "geo_area_hectares_uc"));
            uc.setGeoAreaHectaresZa(getTextValue(ucData, "geo_area_hectares_za"));
            uc.setUcSitFund(getTextValue(ucData, "uc_sit_fund"));
            uc.setUcPopTradBenOuRes(getTextValue(ucData, "uc_pop_trad_ben_ou_res"));

            //Parse dos contatos
            if (ucData.has("contatos")) {
                JsonNode contatoNode = ucData.get("contatos");
                Contatos contato = parseContato(contatoNode, uc);
//                uc.setContatos(contato);
            }

            // Parsing dos atos legais
            if (ucData.has("atos_legais") && ucData.get("atos_legais").isArray()) {
                List<AtosLegais> atosLegaisList = new ArrayList<>();
                for (JsonNode atoLegalNode : ucData.get("atos_legais")) {
                    AtosLegais atoLegal = parseAtoLegal(atoLegalNode, uc);
                    atosLegaisList.add(atoLegal);
                }
//                uc.setAtosLegais(atosLegaisList);
            }

        } catch (Exception e) {
            logger.error("Erro ao fazer o parsing da UC", e);
            throw new DataParseException("Erro ao fazer o parsing da UC", e);
        }

        return uc;
    }

    private AtosLegais parseAtoLegal(JsonNode atoLegalNode, UnidadeConservacao uc) {
        AtosLegais atoLegal = new AtosLegais();
        try {
            atoLegal.setDoclegId(atoLegalNode.get("docleg_id").asInt());
            atoLegal.setDoclegClassdocId(atoLegalNode.get("docleg_classdoc_id").asInt());
            atoLegal.setDoclegNumero(getTextValue(atoLegalNode, "docleg_numero"));
            atoLegal.setDoclegLink(getTextValue(atoLegalNode, "docleg_link"));
            atoLegal.setDoclegLink2(getTextValue(atoLegalNode, "docleg_link_2"));
            atoLegal.setDoclegDtDoc(parseLocalDate(atoLegalNode, "docleg_dt_doc"));
            atoLegal.setDoclegDtPublicacao(parseLocalDate(atoLegalNode, "docleg_dt_publicacao"));
            atoLegal.setDoclegNuAreaDocumento(getTextValue(atoLegalNode, "docleg_nu_area_documento"));
            atoLegal.setTipodocNome(getTextValue(atoLegalNode, "tipodoc_nome"));
            atoLegal.setInstpubNome(getTextValue(atoLegalNode, "instpub_nome"));
            atoLegal.setDocfinNome(getTextValue(atoLegalNode, "docfin_nome"));

            if (atoLegalNode.has("docfin_id") && !atoLegalNode.get("docfin_id").isNull()) {
                atoLegal.setDocfinId(atoLegalNode.get("docfin_id").asInt());
            }

            atoLegal.setTipoInstNome(getTextValue(atoLegalNode, "tipo_inst_nome"));
            atoLegal.setTipo(getTextValue(atoLegalNode, "tipo"));
            atoLegal.setUnidadeConservacao(uc); // MUITO IMPORTANTE: setar a UC no ato legal
        } catch (Exception e) {
            logger.error("Erro ao fazer o parsing do ato legal", e);
            throw new DataParseException("Erro ao fazer o parsing do ato legal", e);
        }

        return atoLegal;
    }

    private LocalDate parseLocalDate(JsonNode node, String fieldName) {
        String dateString = getTextValue(node, fieldName);
        if (dateString != null && !dateString.isEmpty()) {
            List<DateTimeFormatter> formatters = Arrays.asList(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                    DateTimeFormatter.ISO_LOCAL_DATE
            );

            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDate.parse(dateString, formatter);
                } catch (DateTimeParseException e) {
                    // Tentar o próximo formato
                    logger.debug("Tentativa de parse da data {} com formato {} falhou: {}", dateString, formatter.toString(), e.getMessage());
                }
            }
            logger.warn("Não foi possível fazer o parse da data para o campo {}: {}", fieldName, dateString);
        }
        return null;
    }
}