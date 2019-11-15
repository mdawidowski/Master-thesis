package com.master.webshop.repositories;

import com.master.webshop.model.Association;
import com.master.webshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AssociationRepository  extends JpaRepository<Association, Long> {

    @Query("select a from Association a where selected_product = :product ORDER BY occurences DESC")
    List<Association> findBySelectedProductOrderByOccurences(@Param("product") Product product);

    @Query(nativeQuery = true, value = "select * from Association where selected_product_id = :product ORDER BY occurences DESC limit 5")
    List<Association> findBySelectedProductOrderByOccurencesLimit5(@Param("product") long id);

}