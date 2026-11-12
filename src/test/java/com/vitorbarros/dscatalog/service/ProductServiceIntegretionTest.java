package com.vitorbarros.dscatalog.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceIntegretionTest {



    private Long idExist;
    private Long noExistId;
    private Long dependetId;
    private Integer count;


    @BeforeEach
    void setUP(){
        idExist = 1L;
        noExistId = 1000L;
        count = 25;

    }

    @Test
    public void deleteShoudDeleteProductWhenIdExist(){

    }

    @Test
    public void deleteShoudThrowsResouceNotFoundExpetionWhenIdNoExist(){

    }



}
