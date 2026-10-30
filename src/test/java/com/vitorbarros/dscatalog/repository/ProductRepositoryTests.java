package com.vitorbarros.dscatalog.repository;


import com.vitorbarros.dscatalog.Factory.Factory;
import com.vitorbarros.dscatalog.exeptions.ResourceNotFoundException;
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
    public void saveDeveriaSalvarDadosComAutoIncrementQuandoIdNull(){



        Product product = Factory.createProduct();
        product.setId(null);
        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(26, product.getId());


    }


    @Test
    public void  deleteDeveriaDeletarProdutoQuandoIdExistir(){

        Long idExistete = 1L;

        repository.deleteById(idExistete);

        Optional<Product> result = repository.findById(idExistete);

        Assertions.assertTrue(result.isEmpty());


    }

    @Test
    public void  findByIdDeveriaRetornarUmOptionalNaoVazioQuandoIdExistir(){

        Long idExistete = 1L;

        Optional<Product> product = repository.findById(idExistete);

        Assertions.assertTrue(product.isPresent());



    }

    @Test
    public void  findByIdDeveriaRetornarUmOptionalVazioQuandoIdNaoExistir(){

        Long idExistete = 1000L;

        Optional<Product> product = repository.findById(idExistete);

        Assertions.assertTrue(product.isEmpty());

    }




}
