package com.vitorbarros.dscatalog.service;

import com.vitorbarros.dscatalog.exeptions.DataBaseException;
import com.vitorbarros.dscatalog.exeptions.ResourceNotFoundException;

import com.vitorbarros.dscatalog.models.Category;
import com.vitorbarros.dscatalog.repository.CategoryRepository;
import com.vitorbarros.dscatalog.dto.CategoryDTO;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CategoryService {


    private CategoryRepository categoryRepository;



    public CategoryService(CategoryRepository categoryRepository ){
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
   public ResponseEntity <Page<CategoryDTO>> findAllPaged(Pageable pageable){

        Page<Category> categoriesPeged = categoryRepository.findAll(pageable);

        Page<CategoryDTO> categoriesDto = categoriesPeged.map(x->new CategoryDTO(x));

        return ResponseEntity.ok(categoriesDto);

   }

   public ResponseEntity<CategoryDTO> findById(Long id){

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entidade Nao Encontrada "));

        CategoryDTO categoryDTO = new CategoryDTO(category);

        return ResponseEntity.ok().body(categoryDTO);

   }

   @Transactional
   public ResponseEntity <CategoryDTO> save (CategoryDTO categoryDTO){


        Category category = new Category();
        category.setName(categoryDTO.getName());
        categoryRepository.save(category);

        CategoryDTO dto = new CategoryDTO(category);

       return ResponseEntity.ok().body(dto);

   }


   @Transactional
   public  CategoryDTO update(Long id, CategoryDTO categoryDTO){

        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(categoryDTO.getName());
            categoryRepository.save(category);
            return new CategoryDTO(category);
        }catch (EntityNotFoundException e){

            throw new ResourceNotFoundException(id + "Não Encontrado");
        }
   }


   public void delete(Long id){

        if (!categoryRepository.existsById(id)){
            throw new ResourceNotFoundException("Id não Encontrado");
        }
        try {
            categoryRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DataBaseException("Falha na integridade");
        }

   }


}
