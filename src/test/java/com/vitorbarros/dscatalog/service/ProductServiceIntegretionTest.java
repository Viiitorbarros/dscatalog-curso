package com.vitorbarros.dscatalog.service;


import com.vitorbarros.dscatalog.exeptions.ResourceNotFoundException;
import com.vitorbarros.dscatalog.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;

@SpringBootTest
@Transactional
public class ProductServiceIntegretionTest {



    private Long idExist;
    private Long noExistId;
    private Long dependetId;
    private Integer count;


    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUP(){
        idExist = 1L;
        noExistId = 1000L;
        count = 25;

    }


    @Test
    public void findAllPagedShouldReturnPageWhenExistPage(){

    }
    @Test
    public void findAllPagedShouldNoReturnPageWhenNoExistPage(){

    }
    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortByName(){

    }

    @Test
    public void deleteShoudDeleteProductWhenIdExist(){

        productService.delete(idExist);
        Assertions.assertEquals(count - 1, productRepository.count());

    }

    @Test
    public void deleteShoudThrowsResouceNotFoundExpetionWhenIdNoExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.delete(noExistId));


    }



}
