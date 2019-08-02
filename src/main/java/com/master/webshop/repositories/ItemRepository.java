package com.master.webshop.repositories;

import com.master.webshop.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Integer> {


}
