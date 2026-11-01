package com.vitorbarros.dscatalog.service;


import com.vitorbarros.dscatalog.exeptions.DatabaseException;
import com.vitorbarros.dscatalog.exeptions.ResourceNotFoundException;
import com.vitorbarros.dscatalog.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductServiceTest {

    private Long idExistente;
    private Long idNaoExistente;
    private Long dependentId;

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @BeforeEach
    void setUP() throws Exception{

        idExistente = 1L;
        idNaoExistente = 1000L;
        dependentId = 2L;

        Mockito.when(repository.existsById(idExistente)).thenReturn(true);
        Mockito.when(repository.existsById(idNaoExistente)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
        Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(idNaoExistente);


        Mockito.doNothing().when(repository).deleteById(idExistente);
        Mockito.doNothing().when(repository).deleteById(dependentId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }



    @Test
    public void deleteDeveriaNaoFazerNadaQuandoIdExistir(){

        Assertions.assertDoesNotThrow(()-> service.delete(idExistente));

        Mockito.verify(repository,Mockito.times(1)).deleteById(idExistente);


    }

    @Test
    public void deleteDeveriaLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(idNaoExistente);
        });

        Mockito.verify(repository, Mockito.times(1)).existsById(idNaoExistente);
    }

    @Test
    public void deleteDeveriaLancarDatabaseExceptionQuandoDependentIdExistir() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }



}
