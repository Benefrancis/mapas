package br.com.benefrancis.mapas.controller;

import br.com.benefrancis.mapas.service.UnidadeConservacaoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger; // Import Logger
import org.slf4j.LoggerFactory; // Import LoggerFactory
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class UnidadeConservacaoController {

    // Adiciona um Logger para logs mais estruturados
    private static final Logger logger = LoggerFactory.getLogger(UnidadeConservacaoController.class);

    private final UnidadeConservacaoService unidadeConservacaoService;
    // Considerar tornar o ExecutorService um bean gerenciado pelo Spring se a aplicação crescer
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final Random random = new Random();

    @Transactional
    // A anotação aqui pode não ter o efeito desejado em operações assíncronas. A transação principal está no Service.
    @GetMapping("/import")
    public ResponseEntity<String> importData(
            @RequestParam(defaultValue = "11880") int startId,
            @RequestParam(defaultValue = "11883") int endId) {

        logger.info("Iniciando submissão de tarefas para importação de IDs {} a {}", startId, endId);

        IntStream.rangeClosed(startId, endId)
                .forEach(id -> executorService.submit(() -> {
                    try {
                        // --- Rate Limiting movido para DENTRO da task ---
                        // Ajuste para ~10 requisições por minuto por thread:
                        // Atraso mínimo de 6 segundos (6000ms) por requisição
                        // Aleatoriedade entre 6000ms e 10000ms (10 segundos)
                        int delay = random.nextInt(4001) + 6000; // Intervalo: 6000 - 10000
                        logger.debug("Task para ID {} aguardando {} ms antes da execução.", id, delay);
                        TimeUnit.MILLISECONDS.sleep(delay);
                        // --- Fim do Rate Limiting ---

                        // Chamada ao serviço que executa a lógica principal
                        unidadeConservacaoService.processUnidadeConservacao(id);

                    } catch (InterruptedException ie) {
                        // Tratamento específico para InterruptedException do sleep
                        logger.warn("Thread para ID {} foi interrompida durante o sleep.", id);
                        // Re-estabelece o status de interrupção da thread
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        // Captura qualquer outra exceção que possa ocorrer na task ou ser relançada pelo serviço
                        // O serviço já loga os erros específicos (ApiFetchException, DataParseException, etc.)
                        // Este log é mais genérico para a execução da thread.
                        logger.error("Erro não tratado na thread para o ID {}: {}", id, e.getMessage(), e);
                        // System.err e printStackTrace são geralmente desencorajados em produção, prefira o logger.
                        // System.err.println("Erro na thread para o ID " + id + ": " + e.getMessage());
                        // e.printStackTrace();
                    }
                }));

        // Importante: shutdown() apenas sinaliza para não aceitar novas tarefas.
        // As tarefas já submetidas continuarão a executar.
        executorService.shutdown();
        logger.info("ExecutorService recebeu shutdown. Aguardando conclusão das tarefas existentes (ou timeout).");

        // O awaitTermination aqui pode bloquear a resposta HTTP por muito tempo.
        // Em uma API real, você geralmente retornaria 202 Accepted imediatamente
        // e o processamento continuaria em background.
        // Para um script de importação simples, pode ser aceitável.
        /*
        try {
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) { // Aumentar timeout se necessário
                 logger.warn("Timeout ao aguardar a finalização das tarefas de importação.");
                 executorService.shutdownNow(); // Tenta forçar a parada
            } else {
                 logger.info("Todas as tarefas de importação foram concluídas.");
            }
        } catch (InterruptedException e) {
            logger.error("Thread principal interrompida enquanto aguardava a finalização do executor.", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        */

        // Retorna imediatamente após submeter as tarefas
        return ResponseEntity.ok("Processo de importação iniciado em background para IDs de " + startId + " a " + endId + ". Verifique os logs para o progresso.");
    }
}