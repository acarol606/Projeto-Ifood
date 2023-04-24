package com.example;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private Item_cardapioRepository itemCardapioRepository;

    @GetMapping("/novo-restaurante")
    public String mostrarFormNovoRestaurante(Model model){
        model.addAttribute("restaurante", new Restaurante());

        return "novo-restaurante";
    }
    @GetMapping(value={"/index", "/"})
    public String mostrarListaRestaurante(Model model) {
        model.addAttribute("restaurantes", restauranteRepository.findAll());
        return "index";
    }
    @PostMapping("/adicionar-restaurante")
    public String adicionaRestaurante(@Valid Restaurante restaurante, BindingResult result) {
        if (result.hasErrors()) {
            return "/novo-restaurante";
        }
        restauranteRepository.save(restaurante);
        return "redirect:/index";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarRestaurante(@PathVariable("id") int id, @Valid Restaurante restaurante, BindingResult result, Model model) {
        restaurante.setId(id);
        if (result.hasErrors()) {
            return "atualizar-restaurante";
        }
        restauranteRepository.save(restaurante);
        return "redirect:/index";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormAtualizar(@PathVariable("id") int id, Model model) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "O id do restaurante é inválido:" + id));


        model.addAttribute("restaurante", restaurante);
        return "atualizar-restaurante";
    }

    @GetMapping("/remover-restaurante/{id}")
    public String removerRestaurante(@PathVariable("id") int id, HttpServletRequest request) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + id));
        restauranteRepository.delete(restaurante);

        List<Item_cardapio> cardapio = itemCardapioRepository.findByRestauranteId(id);

        itemCardapioRepository.deleteAll(cardapio);

        return "redirect:/index";
    }

}
