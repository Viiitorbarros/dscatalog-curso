package com.vitorbarros.dscatalog.controllers;


import com.vitorbarros.dscatalog.Factory.Factory;
import com.vitorbarros.dscatalog.dto.ProductDTO;
import com.vitorbarros.dscatalog.models.Product;
import com.vitorbarros.dscatalog.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import java.util.List;


@WebMvcTest(ProductController.class)

public class ProductControllerTest {


    private Page<ProductDTO> page;
    private ProductDTO productDTO;
    private Long idExistente;
    private URI uri;



    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;




    @BeforeEach
    void setUP(){
        productDTO = Factory.createProductDto();
        page = new PageImpl<>(List.of(productDTO));
        idExistente = 1L;
        uri = URI.create("/product/1");

    }



    @Test
    public void findAllPagedShouldReturnPage() throws Exception {

        Mockito.when(service.findAllPaged(ArgumentMatchers.any(Pageable.class))).thenReturn(ResponseEntity.ok(page));

        mockMvc.perform(get("/product")).andExpect(status().isOk());

    }

    @Test
    public void findByIdShouldReturnProductDto() throws Exception{
        Mockito.when(service.findById(idExistente)).thenReturn(ResponseEntity.ok(productDTO));

        mockMvc.perform(get("/product/{id}",idExistente)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()).
                andExpect(jsonPath("$.id").value(1));

    }


    @Test
    public void saveShouldReturnStatusCreatedAndProductDto() throws Exception{


         Mockito.when(service.save(productDTO)).thenReturn(ResponseEntity.created(uri).body(productDTO));

         mockMvc.perform(post("/product")).andExpect(status().isCreated());

    }
}
