package com.example;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class Item_cardapioController {
    @Autowired
    private Item_cardapioRepository itemCardapiorepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

    @GetMapping(value={"/lista-restaurantes"})
    public String voltarParaListaDeRestaurantes() {

        return "index";
    }
    @GetMapping(value={"/cardapio/{idRestaurante}"})
    public String mostrarListaItem_cardapio(Model model, @PathVariable("idRestaurante") int idRestaurante) {
        List<Item_cardapio> itens = itemCardapiorepository.findByRestauranteId(idRestaurante);
        Restaurante restaurante = restauranteRepository.findById(idRestaurante).orElseThrow();

        model.addAttribute("itens", itens);
        model.addAttribute("restaurante", restaurante);
        return "cardapio";
    }
    @PostMapping("/adicionar-cardapio/{idRestaurante}")
    public String adicionaItem_cardapio(@Valid Item_cardapio itemcardapio, BindingResult result, @PathVariable("idRestaurante") int idRestaurante) {

        Restaurante restaurante = restauranteRepository.findById(idRestaurante).orElseThrow();
        if (result.hasErrors()) {
            return "novo-item/".concat(String.valueOf(restaurante));
        }

        itemcardapio.setRestaurante(restaurante);
        itemCardapiorepository.save(itemcardapio);
        return "redirect:/cardapio/".concat(String.valueOf(restaurante.getId()));
    }

    @PostMapping("/atualizar-item-cardapio/{id}")
    public String atualizarItem_cardapio(@PathVariable("id") int id, @Valid Item_cardapio item_cardapio, BindingResult result, Model model) {
        Item_cardapio itemCardapio = itemCardapiorepository.findById(id).orElse(null);

        if (result.hasErrors() || item_cardapio == null) {
            return "atualizar-item_cardapio";
        }
        item_cardapio.setId(id);
        item_cardapio.setRestaurante(itemCardapio.getRestaurante());
        itemCardapiorepository.save(item_cardapio);
        assert itemCardapio != null;
        return "redirect:/cardapio/".concat(String.valueOf(itemCardapio.getRestaurante().getId()));
    }

    @GetMapping("/novo-item/{idRestaurante}")
    public String mostrarFormNovoItem(Model model, @PathVariable("idRestaurante") int idRestaurante) {
        Restaurante restaurante = restauranteRepository.findById(idRestaurante).orElseThrow();

        model.addAttribute("restaurante", restaurante);
        model.addAttribute("itemcardapio", new Item_cardapio());

        return "novo-item-cardapio";
    }

    @GetMapping("/editar-cardapio/{id}")
    public String mostrarFormAtualizar(@PathVariable("id") int id, Model model) {
        Item_cardapio itemCardapio = itemCardapiorepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "O id do item é inválido:" + id));


        model.addAttribute("item_cardapio", itemCardapio);
        return "atualizar-cardapio";
    }

    @GetMapping("/remover-cardapio/{id}")
    public String removerItem_cardapio(@PathVariable("id") int id, HttpServletRequest request) {
        Item_cardapio itemCardapio = itemCardapiorepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do item cardapio é inválido:" + id));
        itemCardapiorepository.delete(itemCardapio);

        return "redirect:/cardapio/".concat(String.valueOf(itemCardapio.getRestaurante().getId()));
    }

}
