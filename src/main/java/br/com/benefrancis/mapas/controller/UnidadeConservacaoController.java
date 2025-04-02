package br.com.benefrancis.mapas.controller;

import br.com.benefrancis.mapas.service.UnidadeConservacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class UnidadeConservacaoController {

    private final UnidadeConservacaoService unidadeConservacaoService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5); // 5 threads

    @GetMapping("/import")
    public ResponseEntity<String> importData(
            @RequestParam(defaultValue = "19569") int startId,
            @RequestParam(defaultValue = "19570") int endId) {

        IntStream.rangeClosed(startId, endId)
                .forEach(id -> executorService.submit(() -> {
                    try {
                        unidadeConservacaoService.processUnidadeConservacao(id);
                    } catch (Exception e) {
                        System.err.println("Erro na thread para o ID " + id + ": " + e.getMessage());
                    }
                }));

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return ResponseEntity.ok("Importação iniciada para IDs de " + startId + " a " + endId);
    }
}