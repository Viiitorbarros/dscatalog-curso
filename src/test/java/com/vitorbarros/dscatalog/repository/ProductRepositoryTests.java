package com.vitorbarros.dscatalog.repository;


import com.vitorbarros.dscatalog.models.Product;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    ProductRepository repository;



    @Test
    public void  deleteDeveriaDeletarProdutoQuandoIdExistir(){

        Long idExistete = 1L;

        repository.deleteById(idExistete);

        Optional<Product> result = repository.findById(idExistete);

        Assertions.assertTrue(result.isEmpty());


    }


}
