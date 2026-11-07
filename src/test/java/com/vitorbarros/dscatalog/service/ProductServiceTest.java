package com.vitorbarros.dscatalog.service;


import com.vitorbarros.dscatalog.Factory.Factory;
import com.vitorbarros.dscatalog.dto.ProductDTO;
import com.vitorbarros.dscatalog.exeptions.DatabaseException;
import com.vitorbarros.dscatalog.exeptions.ResourceNotFoundException;
import com.vitorbarros.dscatalog.models.Category;
import com.vitorbarros.dscatalog.models.Product;
import com.vitorbarros.dscatalog.repository.CategoryRepository;
import com.vitorbarros.dscatalog.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tools.jackson.databind.cfg.ContextAttributes;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long idExist;
    private Long idNoExist;
    private Long dependentId;
    private Product product;
    private Category category;
    private ProductDTO productDTO;
    private PageImpl page;


    @BeforeEach
    void setUP(){

        idExist = 1L;
        idNoExist = 1000L;
        dependentId = 102L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        productDTO = Factory.createProductDto();
        page = new PageImpl(List.of(product));

    }



    @Test
    public void findAllPageShouldReturnPage(){

        Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        Pageable pageable = PageRequest.of(0,10);

        ResponseEntity<Page<ProductDTO>> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);

        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);





    }

    @Test
    public void deleteDeveriaNaoLançarNenhumaExcecaoQuandoIdExistir(){

        Mockito.when(repository.existsById(idExist)).thenReturn(true);

        service.delete(idExist);

        Mockito.verify(repository, Mockito.times(1)).deleteById(idExist);


    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdNoExist(){

        Mockito.when(repository.existsById(idNoExist)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class,() -> service.delete(idNoExist));


    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenHavedependentId(){

        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        Assertions.assertThrows(DatabaseException.class, ()-> service.delete(dependentId));


    }

    @Test
    public void findByIdShouldReturnProductWhenIdExist(){

        Mockito.when(repository.findById(idExist)).thenReturn(Optional.of(product));

        ResponseEntity<ProductDTO> result = service.findById(idExist);

        Assertions.assertNotNull(result);

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExist(){

        Mockito.when(repository.findById(idNoExist)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(idNoExist));


    }

    @Test
    public void saveShouldReturnSavedProduct(){

        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(product);

        ResponseEntity<ProductDTO> result = service.save(productDTO);


        Assertions.assertNotNull(result);
        Assertions.assertEquals(productDTO.getName(), result.getBody().getName());
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());


    }

    @Test
    public void updateShouldReturnProductDtoWhenExistId(){

        Mockito.when(repository.getReferenceById(idExist)).thenReturn(product);

        ProductDTO result = service.update(idExist,productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productDTO.getId(), result.getId());
        Mockito.verify(repository, Mockito.times(1)).save(product);

    }

    @Test
    public void updateShouldThrowReResourceNotFoundExceptionWhenIdNotExist(){

        Mockito.when(repository.getReferenceById(idNoExist)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(idNoExist, productDTO));

        Mockito.verify(repository, Mockito.times(0)).save(product);


    }








}
