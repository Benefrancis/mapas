package br.com.benefrancis.mapas.service;

import br.com.benefrancis.mapas.entity.*;
import br.com.benefrancis.mapas.exceptions.ApiFetchException;
import br.com.benefrancis.mapas.exceptions.DataParseException;
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
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UnidadeConservacaoService {

    // ... (Logger, baseUrl, httpClient, objectMapper, formatters - sem alterações) ...
    @Value("${app.base-url}")
    private String baseUrl;

    private static final Logger logger = LoggerFactory.getLogger(UnidadeConservacaoService.class);

    private final UnidadeConservacaoRepository unidadeConservacaoRepository;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Set<DateTimeFormatter> DATE_TIME_FORMATTERS = Set.of(
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    );

    private static final Set<DateTimeFormatter> DATE_FORMATTERS = Set.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ISO_LOCAL_DATE
    );


    @Async
    @Transactional
    public void processUnidadeConservacao(long id) {
        try {
            logger.info("Iniciando processamento para UC ID: {}", id);
            JsonNode ucData = fetchUnidadeConservacao(id);

            if (ucData != null && !ucData.isEmpty()) {
                logger.debug("Dados recebidos para UC ID {}, iniciando parsing.", id);
                UnidadeConservacao uc = parseUnidadeConservacao(ucData);
                unidadeConservacaoRepository.save(uc);
                logger.info("UC com ID {} processada e salva/atualizada com sucesso.", id);
            } else if (ucData == null) {
                // Log WARN já feito em fetchUnidadeConservacao para 404
            } else {
                logger.warn("Dados vazios retornados para UC ID: {}", id);
            }

        } catch (ApiFetchException e) {
            logger.error("Falha final (após retries) ao buscar dados da API para UC ID: {}. Causa: {}", id, e.getMessage());
        } catch (DataParseException e) {
            logger.error("Falha ao parsear dados para UC ID: {}. Causa: {}", id, e.getMessage(), e);
        } catch (DataAccessException e) {
            logger.error("Erro de persistência no banco de dados para UC ID: {}", id, e);
        } catch (Exception e) {
            logger.error("Erro inesperado e não classificado ao processar UC ID: {}", id, e);
        }
    }

    @Retryable(value = {ApiFetchException.class, IOException.class, InterruptedException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000))
    private JsonNode fetchUnidadeConservacao(long id) throws ApiFetchException {
        // ... (método fetchUnidadeConservacao sem alterações) ...
        String url = baseUrl + id;
        logger.debug("Fetching data from URL: {}", url);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode == 200) {
                logger.debug("Dados recebidos com sucesso (200 OK) para UC ID: {}", id);
                return objectMapper.readTree(response.body());
            } else if (statusCode == 404) {
                logger.warn("UC com ID {} não encontrada (404 Not Found).", id);
                return null;
            } else {
                logger.error("Falha ao buscar dados para UC ID: {}. Status: {}, Body: {}", id, statusCode, response.body());
                throw new ApiFetchException("Falha na API. Status code: " + statusCode, new RuntimeException("HTTP Status Code: " + statusCode));
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Erro de rede ou interrupção ao buscar UC ID: {}", id, e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new ApiFetchException("Erro de comunicação com a API para UC ID: " + id, e);
        } catch (Exception e) {
            logger.error("Erro inesperado durante fetch para UC ID: {}", id, e);
            throw new ApiFetchException("Erro inesperado durante fetch para UC ID: " + id, e);
        }
    }


    // --- Métodos de Parsing ---

    private UnidadeConservacao parseUnidadeConservacao(JsonNode ucData) throws DataParseException {
        UnidadeConservacao uc = new UnidadeConservacao();
        long currentId = -1;
        try {
            currentId = ucData.path("uc_id").asLong(-1);
            if (currentId == -1) {
                throw new DataParseException("Campo 'uc_id' ausente ou inválido no JSON.", null);
            }
            uc.setId(currentId);

            // --- Campos simples da UnidadeConservacao (sem alterações aqui) ---
            uc.setUcCodCnuc(getTextValue(ucData, "uc_cod_cnuc"));
            uc.setUcNom(getTextValue(ucData, "uc_nom"));
            uc.setUcObjetivo(getTextValue(ucData, "uc_objetivo"));
            uc.setDtCertificacao(parseDateTime(ucData, "dt_certificacao"));
            uc.setDtAprovacao(parseDateTime(ucData, "dt_aprovacao"));
            uc.setUcPlanManejoAprov(parseBooleanValue(ucData, "uc_plan_manejo_aprov"));
            uc.setUcExistenciaConselho(parseBooleanValue(ucData, "uc_existencia_conselho"));
            uc.setUcOutrosInstrumentos(parseBooleanValue(ucData, "uc_outros_instrumentos")); // Mantido, pois existe na entidade
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
            uc.setGeoAreaHectaresUc(getTextValue(ucData, "geo_area_hectares_uc"));
            uc.setGeoAreaHectaresZa(getTextValue(ucData, "geo_area_hectares_za"));
            uc.setUcSitFund(getTextValue(ucData, "uc_sit_fund"));
            uc.setUcPopTradBenOuRes(getTextValue(ucData, "uc_pop_trad_ben_ou_res"));
            uc.setAreaAmazonia(getTextValue(ucData, "Amazônia (ha)"));
            uc.setAreaCaatinga(getTextValue(ucData, "Caatinga (ha)"));
            uc.setAreaCerrado(getTextValue(ucData, "Cerrado (ha)"));
            uc.setAreaMataAtlantica(getTextValue(ucData, "Mata Atlântica (ha)"));
            uc.setAreaPampa(getTextValue(ucData, "Pampa (ha)"));
            uc.setAreaPantanal(getTextValue(ucData, "Pantanal (ha)"));
            uc.setAreaMarinha(getTextValue(ucData, "Área Marinha (ha)"));

            // --- Campos de strings simples que eram de plano de manejo (REMOVIDOS DA UC) ---
            // uc.setPlanoManejoFullName(getTextValue(ucData, "plano_manejo_full_name")); // REMOVIDO
            // uc.setPlanoManejoLinks(getTextValue(ucData, "plano_manejo_links"));       // REMOVIDO

            // --- Campos de strings simples restantes ---
            uc.setAtoLegalCriacaoFullName(getTextValue(ucData, "ato_legal_criacao_full_name"));
            uc.setAtoLegalCriacaoLink(getTextValue(ucData, "ato_legal_criacao_link"));
            uc.setAreaUcAtoLegalCriacao(getTextValue(ucData, "area_uc_ato_legal_criacao"));
            uc.setAreaUcAtoLegalRecente(getTextValue(ucData, "area_uc_ato_legal_recente"));
            uc.setOutrosAtosLegaisFullName(getTextValue(ucData, "outros_atos_legais_full_name"));
            uc.setOutrosAtosLegaisLink(getTextValue(ucData, "outros_atos_legais_link"));


            // --- Parsing de Objetos Aninhados e Listas ---

            // Contatos (OneToOne)
            if (hasNonNull(ucData, "contatos")) {
                Contatos contato = parseContato(ucData.get("contatos"), uc);
                uc.setContatos(contato);
            }

            // Atos Legais (OneToMany)
            if (hasNonNull(ucData, "atos_legais") && ucData.get("atos_legais").isArray()) {
                Set<AtosLegais> atosLegaisList = new LinkedHashSet<>();
                for (JsonNode atoLegalNode : ucData.get("atos_legais")) {
                    if (isNodeMissingOrNull(atoLegalNode, "docleg_id")) continue; // Pula se ID inválido
                    AtosLegais atoLegal = parseAtoLegal(atoLegalNode, uc);
                    atosLegaisList.add(atoLegal);
                }
                uc.setAtosLegais(atosLegaisList);
            }

            // Documentos do Conselho (OneToMany)
            if (hasNonNull(ucData, "doc_conselho") && ucData.get("doc_conselho").isArray()) {
                Set<DocumentosConselho> docConselhoList = new LinkedHashSet<>();
                for (JsonNode docConselhoNode : ucData.get("doc_conselho")) {
                    if (isNodeMissingOrNull(docConselhoNode, "docleg_id")) continue; // Pula se ID inválido
                    DocumentosConselho docConselho = parseDocConselho(docConselhoNode, uc);
                    docConselhoList.add(docConselho);
                }
                uc.setDocsConselho(docConselhoList);
            } else if (hasNonNull(ucData, "doc_conselho")) {
                logger.warn("Campo 'doc_conselho' existe mas não é um array para UC ID {}. Valor: {}", currentId, ucData.get("doc_conselho").asText());
            }

            // Outros Instrumentos de Gestão (OneToMany)
            if (hasNonNull(ucData, "uc_outros_instrumentos") && ucData.get("uc_outros_instrumentos").isArray()) {
                Set<OutrosInstrumentosGestao> outrosInstrumentosList = parseOutrosInstrumentosGestao(ucData.get("uc_outros_instrumentos"), uc);
                uc.setOutrosInstrumentosGestao(outrosInstrumentosList);
            } else if (hasNonNull(ucData, "uc_outros_instrumentos")) {
                logger.warn("Campo 'uc_outros_instrumentos' existe mas não é um array para UC ID {}. Valor: {}", currentId, ucData.get("uc_outros_instrumentos").asText());
            }

            // *** NOVO: Planos de Manejo (OneToMany) ***
            //    -> Assumindo que a chave no JSON é "planos_manejo". **VERIFICAR JSON REAL!**
            String planosManejoKey = "planos_manejo"; // <-- CONFIRMAR ESTA CHAVE
            if (hasNonNull(ucData, planosManejoKey) && ucData.get(planosManejoKey).isArray()) {
                Set<PlanosManejo> planosList = new LinkedHashSet<>();
                for (JsonNode planoNode : ucData.get(planosManejoKey)) {
                    if (isNodeMissingOrNull(planoNode, "docleg_id")) continue; // Pula se ID inválido
                    PlanosManejo plano = parsePlanoManejo(planoNode, uc); // Chama o novo método de parse
                    planosList.add(plano);
                }
                uc.setPlanosDeManejo(planosList); // Define a lista na UC
            } else if (hasNonNull(ucData, planosManejoKey)) {
                logger.warn("Campo '{}' existe mas não é um array para UC ID {}. Valor: {}", planosManejoKey, currentId, ucData.get(planosManejoKey).asText());
            } else {
                // Log se a chave nem existir (pode ser normal se não houver planos)
                logger.debug("Campo '{}' não encontrado ou nulo para UC ID {}.", planosManejoKey, currentId);
                // Verifica se os campos antigos (agora em PlanosManejo) existem no nível raiz
                String rootPlanoName = getTextValue(ucData, "plano_manejo_full_name");
                String rootPlanoLink = getTextValue(ucData, "plano_manejo_links");
                if (rootPlanoName != null || rootPlanoLink != null) {
                    logger.warn("Encontrados campos 'plano_manejo_full_name'/'plano_manejo_links' no nível raiz," +
                            " mas o array '{}' está ausente/inválido para UC ID {}. Dados podem estar inconsistentes.", planosManejoKey, currentId);
                }
            }

            // Limites UC (contém Mapa e várias listas OneToMany)
            if (hasNonNull(ucData, "limites_uc")) {
                parseLimitesUc(ucData.get("limites_uc"), uc);
            }

            // Socio Antro Fund (contém strings e lista OneToMany GruSociais)
            if (hasNonNull(ucData, "socio_antro_fund")) {
                parseSocioAntroFund(ucData.get("socio_antro_fund"), uc);
            }

            // Outras Inf (contém listas OneToMany: Videos, RedesSociais, Visitas, Ppgr)
            if (hasNonNull(ucData, "outras_inf")) {
                parseOutrasInf(ucData.get("outras_inf"), uc);
            }

            // UcSiteLinks e SocialNetworksLinks (OneToMany) - (lógica anterior mantida)
            if (hasNonNull(ucData, "uc_site_links") && ucData.get("uc_site_links").isArray()) {
                Set<UcSiteLinks> siteLinks = new LinkedHashSet<>();
                for (JsonNode linkNode : ucData.get("uc_site_links")) {
                    String link = null;
                    if (linkNode.isObject() && linkNode.fields().hasNext()) {
                        link = getTextValue(linkNode, linkNode.fields().next().getKey());
                    } else if (linkNode.isTextual()) {
                        link = getTextValue(linkNode, null);
                    }
                    if (link != null) {
                        UcSiteLinks siteLink = new UcSiteLinks();
                        // siteLink.setLink(link); // Assumindo campo 'link'
                        siteLink.setUnidadeConservacao(uc);
                        siteLinks.add(siteLink);
                    }
                }
                uc.setUcSiteLinks(siteLinks);
            }
            if (hasNonNull(ucData, "social_networks_links") && ucData.get("social_networks_links").isArray()) {
                Set<SocialNetworksLinks> socialLinks = new LinkedHashSet<>();
                for (JsonNode linkNode : ucData.get("social_networks_links")) {
                    String link = null;
                    if (linkNode.isObject() && linkNode.fields().hasNext()) {
                        link = getTextValue(linkNode, linkNode.fields().next().getKey());
                    } else if (linkNode.isTextual()) {
                        link = getTextValue(linkNode, null);
                    }
                    if (link != null) {
                        SocialNetworksLinks socialLink = new SocialNetworksLinks();
                        // socialLink.setLink(link); // Assumindo campo 'link'
                        socialLink.setUnidadeConservacao(uc);
                        socialLinks.add(socialLink);
                    }
                }
                uc.setSocialNetworksLinks(socialLinks);
            }


        } catch (Exception e) {
            logger.error("Erro detalhado ao parsear UC com ID {}: {}", currentId, e.getMessage(), e);
            throw new DataParseException("Erro ao parsear dados da UC ID " + currentId + ". Verifique os logs.", e);
        }
        return uc;
    }


    // *** NOVO MÉTODO DE PARSE PARA PlanosManejo ***
    private PlanosManejo parsePlanoManejo(JsonNode planoNode, UnidadeConservacao uc) {
        Long id = planoNode.path("docleg_id").asLong(-1);
        if (id == -1) {
            // Log ou lança exceção, pois ID é esperado
            logger.error("ID inválido encontrado para item de Plano de Manejo na UC ID {}", uc.getId());
            throw new DataParseException("ID inválido para item de Plano de Manejo", null);
        }

        return PlanosManejo.builder()
                .id(id) // ID vem do JSON
                .unidadeConservacao(uc) // Associa à UC
                // Campos específicos de PlanosManejo
                .planoManejoFullName(getTextValue(planoNode, "plano_manejo_full_name")) // Campo existe na entidade
                .planoManejoLinks(getTextValue(planoNode, "plano_manejo_links"))       // Campo existe na entidade
                // Campos comuns (docleg, tipo, etc.)
                .doclegClassdocId(getIntValue(planoNode, "docleg_classdoc_id"))
                .doclegNumero(getTextValue(planoNode, "docleg_numero"))
                .doclegLink(getTextValue(planoNode, "docleg_link"))
                .doclegLink2(getTextValue(planoNode, "docleg_link_2"))
                .doclegDtDoc(parseDate(planoNode, "docleg_dt_doc")) // Usa parseDate (LocalDate -> Date)
                .doclegDtPublicacao(parseDate(planoNode, "docleg_dt_publicacao")) // Usa parseDate
                .doclegNuAreaDocumento(getTextValue(planoNode, "docleg_nu_area_documento"))
                .tipodocNome(getTextValue(planoNode, "tipodoc_nome"))
                .instpubNome(getTextValue(planoNode, "instpub_nome"))
                .docfinNome(getTextValue(planoNode, "docfin_nome"))
                .docfinId(getIntValue(planoNode, "docfin_id"))
                .tipoInstNome(getTextValue(planoNode, "tipo_inst_nome"))
                .build();
    }

    // --- Métodos de parse para outras entidades (parseContato, parseAtoLegal, etc.) ---
    // Manter os métodos parseContato, parseAtoLegal, parseDocConselho,
    // parseOutrosInstrumentosGestao, parseLimitesUc, parseSocioAntroFund,
    // parseOutrasInf como estavam na versão anterior, pois as entidades
    // correspondentes não foram alteradas nesta rodada.
    // Certifique-se de que eles estejam presentes no seu código final.

    private Set<OutrosInstrumentosGestao> parseOutrosInstrumentosGestao(JsonNode outrosInstrumentosArrayNode, UnidadeConservacao uc) {
        Set<OutrosInstrumentosGestao> ois = new LinkedHashSet<>();
        if (outrosInstrumentosArrayNode == null || !outrosInstrumentosArrayNode.isArray()) {
            logger.warn("parseOutrosInstrumentosGestao chamado com nó nulo ou não-array para UC ID {}", uc.getId());
            return Collections.emptySet();
        }
        for (JsonNode instNode : outrosInstrumentosArrayNode) {
            if (isNodeMissingOrNull(instNode, "docleg_id")) continue;
            Long id = instNode.path("docleg_id").asLong();

            OutrosInstrumentosGestao oi = OutrosInstrumentosGestao.builder()
                    .id(id)
                    .unidadeConservacao(uc)
                    .doclegClassdocId(getIntValue(instNode, "docleg_classdoc_id"))
                    .doclegNumero(getTextValue(instNode, "docleg_numero"))
                    .doclegLink(getTextValue(instNode, "docleg_link"))
                    .doclegLink2(getTextValue(instNode, "docleg_link_2"))
                    .doclegDtDoc(parseDate(instNode, "docleg_dt_doc")) // Usa Date
                    .doclegDtPublicacao(parseDate(instNode, "docleg_dt_publicacao")) // Usa Date
                    .doclegNuAreaDocumento(getTextValue(instNode, "docleg_nu_area_documento"))
                    .tipodocNome(getTextValue(instNode, "tipodoc_nome"))
                    .instpubNome(getTextValue(instNode, "instpub_nome"))
                    .docfinNome(getTextValue(instNode, "docfin_nome"))
                    .docfinId(getIntValue(instNode, "docfin_id"))
                    .tipoInstNome(getTextValue(instNode, "tipo_inst_nome"))
                    .build();
            ois.add(oi);
        }
        return ois;
    }

    private Contatos parseContato(JsonNode contatoNode, UnidadeConservacao uc) {
        Contatos contato = new Contatos();
        contato.setUnidadeConservacao(uc);
        contato.setEndCep(getTextValue(contatoNode, "end_cep"));
        contato.setEndUf(getTextValue(contatoNode, "end_uf"));
        contato.setEndMunicipio(getTextValue(contatoNode, "end_municipio"));
        contato.setEndBairro(getTextValue(contatoNode, "end_bairro"));
        contato.setEndLogradouro(getTextValue(contatoNode, "end_logradouro"));
        contato.setFullAddress(getTextValue(contatoNode, "full_address"));
        contato.setUcSite(getTextValue(contatoNode, "uc_site"));

        if (hasNonNull(contatoNode, "telefones") && contatoNode.get("telefones").isArray()) {
            Set<TelefoneContato> telefones = new LinkedHashSet<>();
            for (JsonNode telNode : contatoNode.get("telefones")) {
                String numero = getTextValue(telNode, "uctel_numero");
                if (numero != null) {
                    TelefoneContato tel = new TelefoneContato();
                    tel.setNumero(numero);
                    tel.setContato(contato);
                    telefones.add(tel);
                }
            }
            if (!telefones.isEmpty()) contato.setTelefones(telefones);
        }
        if (hasNonNull(contatoNode, "emails") && contatoNode.get("emails").isArray()) {
            Set<EmailContato> emails = new LinkedHashSet<>();
            for (JsonNode emailNode : contatoNode.get("emails")) {
                String emailAddr = getTextValue(emailNode, "ucema_email");
                if (emailAddr != null) {
                    EmailContato email = new EmailContato();
                    email.setEmail(emailAddr);
                    email.setContato(contato);
                    emails.add(email);
                }
            }
            if (!emails.isEmpty()) contato.setEmails(emails);
        }
        return contato;
    }

    private AtosLegais parseAtoLegal(JsonNode atoLegalNode, UnidadeConservacao uc) {
        Long id = atoLegalNode.path("docleg_id").asLong(-1);
        if (id == -1) throw new DataParseException("ID inválido para Ato Legal", null);

        AtosLegais atoLegal = new AtosLegais(); // Assumindo construtor padrão
        atoLegal.setId(id);
        atoLegal.setUnidadeConservacao(uc);
        atoLegal.setDoclegClassdocId(getIntValue(atoLegalNode, "docleg_classdoc_id"));
        atoLegal.setDoclegNumero(getTextValue(atoLegalNode, "docleg_numero"));
        atoLegal.setDoclegLink(getTextValue(atoLegalNode, "docleg_link"));
        atoLegal.setDoclegLink2(getTextValue(atoLegalNode, "docleg_link_2"));
        atoLegal.setDoclegDtDoc(parseLocalDate(atoLegalNode, "docleg_dt_doc")); // LocalDate
        atoLegal.setDoclegDtPublicacao(parseLocalDate(atoLegalNode, "docleg_dt_publicacao")); // LocalDate
        atoLegal.setDoclegNuAreaDocumento(getTextValue(atoLegalNode, "docleg_nu_area_documento"));
        atoLegal.setTipodocNome(getTextValue(atoLegalNode, "tipodoc_nome"));
        atoLegal.setInstpubNome(getTextValue(atoLegalNode, "instpub_nome"));
        atoLegal.setDocfinNome(getTextValue(atoLegalNode, "docfin_nome"));
        atoLegal.setDocfinId(getIntValue(atoLegalNode, "docfin_id"));
        atoLegal.setTipoInstNome(getTextValue(atoLegalNode, "tipo_inst_nome"));
        atoLegal.setTipo(getTextValue(atoLegalNode, "tipo"));
        return atoLegal;
    }

    private DocumentosConselho parseDocConselho(JsonNode docConselhoNode, UnidadeConservacao uc) {
        Long id = docConselhoNode.path("docleg_id").asLong(-1);
        if (id == -1) throw new DataParseException("ID inválido para Documento Conselho", null);

        DocumentosConselho docConselho = new DocumentosConselho(); // Assumindo construtor padrão
        docConselho.setId(id);
        docConselho.setUnidadeConservacao(uc);
        docConselho.setDoclegClassdocId(getIntValue(docConselhoNode, "docleg_classdoc_id"));
        docConselho.setDoclegNumero(getTextValue(docConselhoNode, "docleg_numero"));
        docConselho.setDoclegLink(getTextValue(docConselhoNode, "docleg_link"));
        docConselho.setDoclegLink2(getTextValue(docConselhoNode, "docleg_link_2"));
        docConselho.setDoclegDtDoc(parseLocalDate(docConselhoNode, "docleg_dt_doc")); // LocalDate
        docConselho.setDoclegDtPublicacao(parseLocalDate(docConselhoNode, "docleg_dt_publicacao")); // LocalDate
        docConselho.setDoclegNuAreaDocumento(getTextValue(docConselhoNode, "docleg_nu_area_documento"));
        docConselho.setTipodocNome(getTextValue(docConselhoNode, "tipodoc_nome"));
        docConselho.setInstpubNome(getTextValue(docConselhoNode, "instpub_nome"));
        docConselho.setDocfinNome(getTextValue(docConselhoNode, "docfin_nome"));
        docConselho.setDocfinId(getIntValue(docConselhoNode, "docfin_id"));
        docConselho.setTipoInstNome(getTextValue(docConselhoNode, "tipo_inst_nome"));
        return docConselho;
    }

    private void parseLimitesUc(JsonNode limitesNode, UnidadeConservacao uc) {
        Mapa mapa = uc.getMapa() == null ? new Mapa() : uc.getMapa();
        mapa.setUnidadeConservacao(uc);
        mapa.setLinkMapaUc(getTextValue(limitesNode, "mapa_uc"));
        mapa.setQualDadoGeoespacial(getTextValue(limitesNode, "qual_dado_geoespacial"));
        uc.setMapa(mapa);

        if (hasNonNull(limitesNode, "biomas") && limitesNode.get("biomas").isArray()) {
            Set<Bioma> biomas = new LinkedHashSet<>();
            for (JsonNode biomaNode : limitesNode.get("biomas")) {
                String nomeBioma = getTextValue(biomaNode, "bioma");
                if (nomeBioma != null) {
                    Bioma bioma = new Bioma();
                    bioma.setBioma(nomeBioma);
                    bioma.setAreaBiomaHa(getTextValue(biomaNode, "area_bioma_ha"));
                    bioma.setUnidadeConservacao(uc);
                    biomas.add(bioma);
                }
            }
            if (!biomas.isEmpty()) uc.setBiomas(biomas);
        }

        if (hasNonNull(limitesNode, "leg_solos") && limitesNode.get("leg_solos").isArray()) {
            Set<LegSolos> solos = new LinkedHashSet<>();
            for (JsonNode soloNode : limitesNode.get("leg_solos")) {
                String soloDesc = getTextValue(soloNode, "solo");
                if (soloDesc != null) {
                    LegSolos solo = new LegSolos();
                    solo.setSolo(soloDesc);
                    solo.setUnidadeConservacao(uc);
                    solos.add(solo);
                }
            }
            if (!solos.isEmpty()) uc.setSolos(solos); // Usando o campo 'solos'
        }

        if (hasNonNull(limitesNode, "climas") && limitesNode.get("climas").isArray()) {
            Set<Climas> climas = new LinkedHashSet<>();
            for (JsonNode climaNode : limitesNode.get("climas")) {
                String climaDesc = getTextValue(climaNode, "clima");
                if (climaDesc != null) {
                    Climas clima = new Climas();
                    clima.setClima(climaDesc);
                    clima.setUnidadeConservacao(uc);
                    climas.add(clima);
                }
            }
            if (!climas.isEmpty()) uc.setClimas(climas);
        }

        if (hasNonNull(limitesNode, "rios") && limitesNode.get("rios").isArray()) {
            Set<Rios> rios = new LinkedHashSet<>();
            for (JsonNode rioNode : limitesNode.get("rios")) {
                String rioNome = getTextValue(rioNode, "rio");
                if (rioNome != null) {
                    Rios rio = new Rios();
                    rio.setRio(rioNome);
                    rio.setUnidadeConservacao(uc);
                    rios.add(rio);
                }
            }
            if (!rios.isEmpty()) uc.setRios(rios);
        }

        if (hasNonNull(limitesNode, "bacias") && limitesNode.get("bacias").isArray()) {
            Set<Bacias> bacias = new LinkedHashSet<>();
            for (JsonNode baciaNode : limitesNode.get("bacias")) {
                String baciaNome = getTextValue(baciaNode, "bacia");
                if (baciaNome != null) {
                    Bacias bacia = new Bacias();
                    bacia.setBacia(baciaNome);
                    bacia.setUnidadeConservacao(uc);
                    bacias.add(bacia);
                }
            }
            if (!bacias.isEmpty()) uc.setBacias(bacias);
        }

        if (hasNonNull(limitesNode, "uf_abrang") && limitesNode.get("uf_abrang").isArray()) {
            Set<UfAbrang> ufs = new LinkedHashSet<>();
            for (JsonNode ufNode : limitesNode.get("uf_abrang")) {
                String nomeUf = getTextValue(ufNode, "nome_uf");
                Integer geocode = getIntValue(ufNode, "geocode_uf");
                if (nomeUf != null && geocode != null) {
                    UfAbrang uf = new UfAbrang();
                    uf.setNomeUf(nomeUf);
                    uf.setGeocodeUf(geocode);
                    uf.setUnidadeConservacao(uc);
                    ufs.add(uf);
                }
            }
            if (!ufs.isEmpty()) uc.setUfAbrang(ufs);
        }

        if (hasNonNull(limitesNode, "mun_abrang") && limitesNode.get("mun_abrang").isArray()) {
            Set<MunAbrang> municipios = new LinkedHashSet<>();
            for (JsonNode munNode : limitesNode.get("mun_abrang")) {
                String nomeMun = getTextValue(munNode, "nome_municipio");
                Integer geocode = getIntValue(munNode, "geocode_mun");
                if (nomeMun != null && geocode != null) {
                    MunAbrang mun = new MunAbrang();
                    mun.setNomeMunicipio(nomeMun);
                    mun.setGeocodeMun(geocode);
                    mun.setUnidadeConservacao(uc);
                    municipios.add(mun);
                }
            }
            if (!municipios.isEmpty()) uc.setMunAbrang(municipios);
        }
    }

    private void parseSocioAntroFund(JsonNode socioNode, UnidadeConservacao uc) {
        // Campos string já tratados em parseUnidadeConservacao
        if (hasNonNull(socioNode, "gru_sociais") && socioNode.get("gru_sociais").isArray()) {
            Set<GruSociais> grupos = new LinkedHashSet<>();
            for (JsonNode grupoNode : socioNode.get("gru_sociais")) {
                String grupoNome = getTextValue(grupoNode, "grusocial_nome");
                if (grupoNome != null) {
                    GruSociais grupo = new GruSociais();
                    grupo.setGrusocialNome(grupoNome);
                    grupo.setUnidadeConservacao(uc);
                    grupos.add(grupo);
                }
            }
            if (!grupos.isEmpty()) uc.setGruSociais(grupos);
        }
    }

    private void parseOutrasInf(JsonNode outrasInfNode, UnidadeConservacao uc) {
        if (hasNonNull(outrasInfNode, "videos") && outrasInfNode.get("videos").isArray()) {
            Set<Videos> videos = new LinkedHashSet<>();
            for (JsonNode videoNode : outrasInfNode.get("videos")) {
                String nome = getTextValue(videoNode, "ucvideos_nome");
                String link = getTextValue(videoNode, "ucvideos_link");
                if (nome != null) {
                    Videos video = new Videos();
                    video.setUcvideosNome(nome);
                    video.setUcvideosLink(link);
                    video.setUnidadeConservacao(uc);
                    videos.add(video);
                }
            }
            if (!videos.isEmpty()) uc.setVideos(videos);
        }

        if (hasNonNull(outrasInfNode, "redes_sociais") && outrasInfNode.get("redes_sociais").isArray()) {
            Set<RedesSociais> redes = new LinkedHashSet<>();
            for (JsonNode redeNode : outrasInfNode.get("redes_sociais")) {
                String nome = getTextValue(redeNode, "ucsocial_nome");
                String link = getTextValue(redeNode, "ucsocial_link");
                if (nome != null) {
                    RedesSociais rede = new RedesSociais();
                    rede.setUcsocialNome(nome);
                    rede.setUcsocialLink(link);
                    rede.setUnidadeConservacao(uc);
                    redes.add(rede);
                }
            }
            if (!redes.isEmpty()) uc.setRedesSociais(redes);
        }

        if (hasNonNull(outrasInfNode, "visitas") && outrasInfNode.get("visitas").isArray()) {
            Set<Visitas> visitas = new LinkedHashSet<>();
            for (JsonNode visitaNode : outrasInfNode.get("visitas")) {
                Integer numero = getIntValue(visitaNode, "ucvisitas_numero");
                Integer ano = getIntValue(visitaNode, "ucvisitas_ano");
                if (numero != null && ano != null) {
                    Visitas visita = new Visitas();
                    visita.setUcvisitasNumero(numero);
                    visita.setUcvisitasAno(ano);
                    visita.setUnidadeConservacao(uc);
                    visitas.add(visita);
                } else if (numero != null || ano != null) {
                    logger.warn("Dados de visita parciais para UC ID {}: numero={}, ano={}", uc.getId(), numero, ano);
                }
            }
            if (!visitas.isEmpty()) uc.setVisitas(visitas);
        }

        if (hasNonNull(outrasInfNode, "ppgr") && outrasInfNode.get("ppgr").isArray()) {
            Set<Ppgr> ppgrs = new LinkedHashSet<>();
            for (JsonNode ppgrNode : outrasInfNode.get("ppgr")) {
                String fullName = getTextValue(ppgrNode, "full_name_ppgr");
                if (fullName != null) {
                    Ppgr ppgr = new Ppgr();
                    ppgr.setUnidadeConservacao(uc);
                    ppgr.setFullNamePpgr(fullName);
                    ppgr.setNomePpgiri(getTextValue(ppgrNode, "nome_ppgiri"));
                    ppgr.setDescricao(getTextValue(ppgrNode, "descricao"));
                    ppgr.setNomeInstrumento(getTextValue(ppgrNode, "nome_instrumento"));
                    ppgr.setDataDocLegal(parseLocalDate(ppgrNode, "data_doc_legal")); // LocalDate
                    ppgr.setDocLegal(getTextValue(ppgrNode, "doc_legal"));
                    ppgr.setNomeContato1(getTextValue(ppgrNode, "nome_contato_1"));
                    ppgr.setTel1(getTextValue(ppgrNode, "tel_1"));
                    ppgr.setEmail1(getTextValue(ppgrNode, "email_1"));
                    ppgr.setWebsite1(getTextValue(ppgrNode, "website_1"));
                    ppgr.setNomeContato2(getTextValue(ppgrNode, "nome_contato_2"));
                    ppgr.setTel2(getTextValue(ppgrNode, "tel_2"));
                    ppgr.setEmail2(getTextValue(ppgrNode, "email_2"));
                    ppgr.setLogo(getTextValue(ppgrNode, "logo"));
                    ppgr.setTipoNome(getTextValue(ppgrNode, "tipo_nome"));
                    ppgr.setOgNomePpgr(getTextValue(ppgrNode, "og_nome_ppgr"));
                    ppgr.setEsfNomePpgr(getTextValue(ppgrNode, "esf_nome_ppgr"));
                    ppgrs.add(ppgr);
                }
            }
            if (!ppgrs.isEmpty()) uc.setPpgr(ppgrs);
        }
    }

    // --- Métodos Utilitários de Parsing ---

    /**
     * Helper para verificar se um nó está ausente, é nulo ou não tem um ID válido (não é Long > 0)
     */
    private boolean isNodeMissingOrNull(JsonNode parentNode, String idFieldName) {
        if (!hasNonNull(parentNode, idFieldName)) {
            logger.warn("Campo de ID '{}' ausente ou nulo no nó: {}", idFieldName, parentNode);
            return true;
        }
        JsonNode idNode = parentNode.get(idFieldName);
        // Verifica se é um número Long válido (ou pode ser convertido de string)
        if (idNode.isNumber() && idNode.canConvertToLong() && idNode.asLong() > 0) {
            return false; // ID válido
        }
        if (idNode.isTextual()) {
            try {
                long id = Long.parseLong(idNode.asText());
                if (id > 0) return false; // ID válido convertido de string
            } catch (NumberFormatException ignored) {
            }
        }
        logger.warn("Campo de ID '{}' com valor inválido ('{}') no nó: {}", idFieldName, idNode.asText(), parentNode);
        return true;
    }


    private boolean hasNonNull(JsonNode parentNode, String fieldName) {
        return parentNode != null && parentNode.hasNonNull(fieldName);
    }

    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode targetNode = (fieldName == null) ? node : node.path(fieldName);
        if (targetNode != null && !targetNode.isMissingNode() && !targetNode.isNull()) {
            String value = targetNode.asText().trim();
            if (value.isEmpty() || "Sem informação.".equalsIgnoreCase(value) || "null".equalsIgnoreCase(value)) {
                return null;
            }
            return value;
        }
        return null;
    }

    private Integer getIntValue(JsonNode node, String fieldName) {
        if (hasNonNull(node, fieldName)) {
            JsonNode fieldNode = node.get(fieldName);
            if (fieldNode.isInt()) return fieldNode.asInt();
            if (fieldNode.isTextual()) {
                String textValue = getTextValue(fieldNode, null);
                if (textValue != null) {
                    try {
                        return Integer.parseInt(textValue);
                    } catch (NumberFormatException e) {
                        logger.warn("Não foi possível converter '{}' ({}) para Integer na UC ID {}.", textValue, fieldName, node.path("uc_id").asText("N/A"));
                    }
                }
            }
        }
        return null;
    }

    private BigDecimal getBigDecimalValue(JsonNode node, String fieldName) {
        // ... (mantido sem alterações) ...
        if (hasNonNull(node, fieldName)) {
            JsonNode fieldNode = node.get(fieldName);
            if (fieldNode.isNumber()) {
                return fieldNode.decimalValue();
            } else if (fieldNode.isTextual()) {
                return parseDecimalString(fieldNode.asText(), fieldName, node);
            }
        }
        return null;
    }


    private BigDecimal parseDecimalString(String decimalString, String fieldName, JsonNode contextNode) {
        // ... (mantido sem alterações) ...
        if (decimalString != null) {
            String cleanedString = decimalString.trim();
            if (cleanedString.isEmpty() || "Sem informação.".equalsIgnoreCase(cleanedString) || "null".equalsIgnoreCase(cleanedString)) {
                return null;
            }
            try {
                String parsableString = cleanedString.replace(".", "").replace(",", ".");
                if (!parsableString.isEmpty()) {
                    return new BigDecimal(parsableString);
                }
            } catch (NumberFormatException e) {
                logger.warn("Não foi possível parsear '{}' ({}) para BigDecimal na UC ID {}.", decimalString, fieldName, contextNode.path("uc_id").asText("N/A"));
            }
        }
        return null;
    }


    private LocalDateTime parseDateTime(JsonNode node, String fieldName) {
        // ... (mantido sem alterações) ...
        String dateTimeString = getTextValue(node, fieldName);
        if (dateTimeString != null) {
            for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
                try {
                    return LocalDateTime.parse(dateTimeString, formatter);
                } catch (DateTimeParseException e) {
                    logger.trace("Falha ao parsear datahora '{}' ({}) com formato {}: {}", dateTimeString, fieldName, formatter, e.getMessage());
                }
            }
            logger.warn("Não foi possível parsear datahora '{}' ({}) na UC ID {}.", dateTimeString, fieldName, node.path("uc_id").asText("N/A"));
        }
        return null;
    }


    private LocalDate parseLocalDate(JsonNode node, String fieldName) {
        // ... (mantido sem alterações) ...
        String dateString = getTextValue(node, fieldName);
        if (dateString != null) {
            for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                try {
                    return LocalDate.parse(dateString, formatter);
                } catch (DateTimeParseException e) {
                    logger.trace("Falha ao parsear data '{}' ({}) com formato {}: {}", dateString, fieldName, formatter, e.getMessage());
                }
            }
            logger.warn("Não foi possível parsear data '{}' ({}) na UC ID {}.", dateString, fieldName, node.path("uc_id").asText("N/A"));
        }
        return null;
    }


    private Date parseDate(JsonNode node, String fieldName) {
        // ... (mantido sem alterações) ...
        LocalDate localDate = parseLocalDate(node, fieldName);
        if (localDate != null) {
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    private Boolean parseBooleanValue(JsonNode node, String fieldName) {
        // ... (mantido sem alterações) ...
        if (hasNonNull(node, fieldName)) {
            JsonNode fieldNode = node.get(fieldName);
            if (fieldNode.isBoolean()) return fieldNode.asBoolean();
            if (fieldNode.isTextual()) {
                String textValue = fieldNode.asText().trim();
                if ("true".equalsIgnoreCase(textValue)) return Boolean.TRUE;
                if ("false".equalsIgnoreCase(textValue)) return Boolean.FALSE;
                logger.warn("Valor texto inesperado para booleano '{}' ({}) na UC ID {}.", textValue, fieldName, node.path("uc_id").asText("N/A"));
            } else if (fieldNode.isNumber()) {
                int intValue = fieldNode.asInt();
                if (intValue == 1) return Boolean.TRUE;
                if (intValue == 0) return Boolean.FALSE;
                logger.warn("Valor numérico inesperado para booleano '{}' ({}) na UC ID {}.", fieldNode.asText(), fieldName, node.path("uc_id").asText("N/A"));
            } else {
                logger.warn("Tipo inesperado ({}) para booleano '{}' ({}) na UC ID {}.", fieldNode.getNodeType(), fieldNode.asText(), fieldName, node.path("uc_id").asText("N/A"));
            }
        }
        return null;
    }
}