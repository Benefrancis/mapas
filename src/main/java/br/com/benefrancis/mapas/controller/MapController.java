package br.com.benefrancis.mapas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MapController {



    private static final Logger log = LoggerFactory.getLogger(MapController.class);

    @GetMapping("/")
    public ModelAndView showMap() {
        log.info("Usuário acessando o site");
        // Criar ModelAndView com caminho relativo
        ModelAndView mv = new ModelAndView("index"); // Caminho simplificado, já considerando o prefixo configurado
        return mv;
    }

    @ResponseBody
    @GetMapping("/update-position")
    public Map<String, Object> updatePosition() {
        // Lógica para obter a nova posição
        Map<String, Object> response = new HashMap<>();
        response.put("latitude", -23.5505); // exemplo de latitude
        response.put("longitude", -46.6333); // exemplo de longitude
        return response;
    }
}
