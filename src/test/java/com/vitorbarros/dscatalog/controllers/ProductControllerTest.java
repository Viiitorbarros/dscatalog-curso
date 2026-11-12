package com.vitorbarros.dscatalog.controllers;


import com.vitorbarros.dscatalog.Factory.Factory;
import com.vitorbarros.dscatalog.dto.ProductDTO;
import com.vitorbarros.dscatalog.exeptions.DatabaseException;
import com.vitorbarros.dscatalog.exeptions.ResourceNotFoundException;
import com.vitorbarros.dscatalog.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import java.util.List;


@WebMvcTest(ProductController.class)

public class ProductControllerTest {


    private Page<ProductDTO> page;
    private ProductDTO productDTO;
    private Long idExistente;
    private Long idNaoExistente;
    private Long dependetId;
    private URI uri;
    private ResultActions resultActions;



    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUP(){
        productDTO = Factory.createProductDto();
        page = new PageImpl<>(List.of(productDTO));
        idExistente = 1L;
        idNaoExistente = 1000L;
        dependetId = 500L;
        uri = URI.create("/product/1");

    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdNoExist() throws Exception{
        Mockito.doThrow(DatabaseException.class).when(service).delete(idNaoExistente);

        resultActions = mockMvc.perform(delete("/product/{id}", idNaoExistente));


        resultActions.andExpect(status().isBadRequest());


    }

    @Test
    public void deleteShouldReturnDatabaseExceptionWhenDependentId() throws Exception{

        Mockito.doThrow(DatabaseException.class).when(service).delete(dependetId);
        resultActions = mockMvc.perform(delete("/product/{id}", dependetId));

        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    public void deleteShouldReturnIsNoContentWhenIdExist() throws  Exception{

        resultActions = mockMvc.perform(delete("/product/{id}", idExistente));

        resultActions.andExpect(status().isNoContent());
    }


    @Test
    public void updateShouldReturnProductDtoWhenIdExist() throws Exception{

        Mockito.when(service.update(eq(idExistente), any())).thenReturn(productDTO);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        resultActions = mockMvc.perform(put("/product/{id}", idExistente)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.id").value(idExistente));


    }

    @Test
    public void updateShouldReturnNotFoundWhenIdNotExist() throws Exception{

        Mockito.when(service.update(eq(idNaoExistente), any())).thenThrow(ResourceNotFoundException.class);
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        resultActions = mockMvc.perform(put("/product/{id}", idNaoExistente)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.id").doesNotExist());

    }



    @Test
    public void findAllPagedShouldReturnPage() throws Exception {

        Mockito.when(service.findAllPaged(any(Pageable.class))).thenReturn(ResponseEntity.ok(page));

        mockMvc.perform(get("/product")).andExpect(status().isOk());

    }

    @Test
    public void findByIdShouldReturnNotFoundExceptionWhenIdNaoExistente() throws  Exception{

        Mockito.when(service.findById(idNaoExistente)).thenThrow(ResourceNotFoundException.class);


        resultActions = mockMvc.perform(get("/product/{id}", idNaoExistente));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.id").doesNotExist());

    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExist() throws Exception{
        Mockito.when(service.findById(idExistente)).thenReturn(ResponseEntity.ok(productDTO));

        mockMvc.perform(get("/product/{id}",idExistente)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()).
                andExpect(jsonPath("$.id").value(idExistente));

    }


    @Test
    public void saveShouldReturnStatusCreatedAndProductDto() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        Mockito.when(service.save(any())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(productDTO));

        resultActions = mockMvc.perform(post("/product")
                         .content(jsonBody)
                         .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.id").value(idExistente));

    }
}
