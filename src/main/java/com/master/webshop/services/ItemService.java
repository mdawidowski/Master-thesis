package com.master.webshop.services;

import com.master.webshop.model.Item;
import com.master.webshop.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemService {
    @Autowired
    ItemRepository repository;

    @GetMapping("/api/item")
    public Iterable<Item> findAllItems(){
        return repository.findAll();
    }
}
