package com.vitorbarros.dscatalog.service;


import com.vitorbarros.dscatalog.Factory.Factory;
import com.vitorbarros.dscatalog.dto.ProductDTO;
import com.vitorbarros.dscatalog.exeptions.DatabaseException;
import com.vitorbarros.dscatalog.exeptions.ResourceNotFoundException;
import com.vitorbarros.dscatalog.models.Product;
import com.vitorbarros.dscatalog.repository.CategoryRepository;
import com.vitorbarros.dscatalog.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductServiceTest {

    private Long idExistente;
    private Long idNaoExistente;
    private Long dependentId;
    private Product product;
    private ProductDTO productDTO;
    private PageImpl page;

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;



    @BeforeEach
    void setUP() throws  Exception{

        idExistente = 1L;
        idNaoExistente = 10L;
        dependentId = 100L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        productDTO = Factory.createProductDto();

        //FindAllPage

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);


        //Delete
        Mockito.when(repository.existsById(idExistente)).thenReturn(true);
        Mockito.when(repository.existsById(idNaoExistente)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        //Save

        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(product);


        //FindById

        Mockito.when(repository.findById(idExistente)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(idNaoExistente)).thenReturn(Optional.empty());
        Mockito.when(repository.findById(dependentId)).thenReturn(Optional.of(product));

    }


    @Test
    public void findAllPageShouldReturnPage(){

        Pageable pageable = PageRequest.of(0,10);
        ResponseEntity<Page<ProductDTO>> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);

    }


    @Test
    public void deleteDeveriaNaoLançarNenhumaExcecaoQuandoIdExistir(){


        Assertions.assertDoesNotThrow(() -> service.delete(idExistente));
        Mockito.verify(repository , Mockito.times(1)).deleteById(idExistente);

    }


    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdNoExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, ()->service.delete(idNaoExistente));
        Mockito.verify(repository, Mockito.times(0)).deleteById(idNaoExistente);


    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenHavedependentId(){

        Assertions.assertThrows(DatabaseException.class, () -> service.delete(dependentId));
        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);

    }


    @Test
    public void findByIdShouldReturnProductWhenIdExist(){

        ResponseEntity <ProductDTO> result = service.findById(idExistente);

        Assertions.assertEquals(product.getId(), result.getBody().getId());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());

    }



    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(idNaoExistente));

    }



    @Test
    public void saveShouldReturnSavedProduct(){

        ResponseEntity <ProductDTO> result = service.save(productDTO);


        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(productDTO.getName(), result.getBody().getName());



    }


}
