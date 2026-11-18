package com.vitorbarros.dscatalog.service;


import com.vitorbarros.dscatalog.dto.ProductDTO;
import com.vitorbarros.dscatalog.exeptions.ResourceNotFoundException;
import com.vitorbarros.dscatalog.models.Product;
import com.vitorbarros.dscatalog.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
        PageRequest pageRequest = PageRequest.of(0,10);

        ResponseEntity <Page<ProductDTO>> result = productService.findAllPaged(pageRequest);

        Assertions.assertFalse(result.getBody().isEmpty());
        Assertions.assertEquals(10, result.getBody().getSize());
        Assertions.assertEquals(25 , result.getBody().getTotalElements());
        Assertions.assertEquals(0 , result.getBody().getNumber());
    }

    @Test
    public void findAllPagedShouldNoReturnPageWhenNoExistPage(){
        PageRequest pageRequest = PageRequest.of(50,10);

        ResponseEntity <Page<ProductDTO>> result = productService.findAllPaged(pageRequest);

        Assertions.assertTrue(result.getBody().isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortByName(){

        PageRequest pageRequest = PageRequest.of(0,10, Sort.by("name"));

        ResponseEntity <Page<ProductDTO>> result = productService.findAllPaged(pageRequest);

        Assertions.assertFalse(result.getBody().isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getBody().getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getBody().getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa" , result.getBody().getContent().get(2).getName());


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
