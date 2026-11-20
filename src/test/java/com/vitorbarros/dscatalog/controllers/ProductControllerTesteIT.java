package com.vitorbarros.dscatalog.controllers;



import com.vitorbarros.dscatalog.Factory.Factory;
import com.vitorbarros.dscatalog.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Carregar o Contexto
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTesteIT {

    private Long idExist;
    private Long idNoExist;
    private ResultActions resultActions;
    private Integer count;

    private ProductDTO productDTO;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUP() throws  Exception{

        idExist = 1L;
        idNoExist = 100L;
        count = 25;
        productDTO = Factory.createProductDto();

    }
    @Test
    public void findAllPagedShouldReturnPageWhenSortByName() throws Exception{

       resultActions =  mockMvc.perform(get("/product?page=0&size=10&sort=name,asc")
               .accept(MediaType.APPLICATION_JSON));

       resultActions.andExpect(status().isOk());
       resultActions.andExpect(jsonPath("$.totalElements").value(count));
       resultActions.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));

    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExist() throws  Exception
    {

        String jsonBody = objectMapper.writeValueAsString(productDTO);
        String expectedValue = productDTO.getName();

        resultActions = mockMvc.perform(put("/product/{id}" , idExist)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));


        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.name").value(expectedValue));

    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws  Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);
        String expectedValue = productDTO.getName();

        resultActions = mockMvc.perform(put("/product/{id}", idNoExist)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));


        resultActions.andExpect(status().isNotFound());
    }

}
