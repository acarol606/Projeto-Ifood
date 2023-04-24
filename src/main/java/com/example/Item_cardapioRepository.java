package com.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Item_cardapioRepository extends JpaRepository<Item_cardapio,Integer> {

    List<Item_cardapio> findByRestauranteId(int idRestaurante);
}

