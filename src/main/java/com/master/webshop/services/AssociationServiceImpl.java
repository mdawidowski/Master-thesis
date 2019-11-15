package com.master.webshop.services;

import com.master.webshop.model.Association;
import com.master.webshop.model.Product;
import com.master.webshop.repositories.AssociationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssociationServiceImpl implements AssociationService {

    private AssociationRepository associationRepository;

    public AssociationServiceImpl(AssociationRepository associationRepository) {
        this.associationRepository = associationRepository;
    }


    @Override
    public List<Association> findAll() {
        return associationRepository.findAll();
    }

    @Override
    public List<Association> findBySelectedProductOrderByOccurences(Product product){
        return associationRepository.findBySelectedProductOrderByOccurences(product);
    }

    @Override
    public void save(Association association){
        associationRepository.save(association);
    }

}

