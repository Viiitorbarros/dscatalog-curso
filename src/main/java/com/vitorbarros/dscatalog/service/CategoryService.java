package com.vitorbarros.dscatalog.service;

import com.vitorbarros.dscatalog.models.Category;
import com.vitorbarros.dscatalog.repository.CategoryRepository;
import com.vitorbarros.dscatalog.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {


    private CategoryRepository categoryRepository;



    public CategoryService(CategoryRepository categoryRepository ){
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
   public ResponseEntity <List<CategoryDTO>> findAll(){

        List<Category> categories = categoryRepository.findAll();

        List<CategoryDTO> categoriesDto = categories.stream()
                .map(x-> new CategoryDTO(x))
                .toList();

        return ResponseEntity.ok(categoriesDto);

   }

   public ResponseEntity<CategoryDTO> findById(Long id){

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria nao encontrada"));

        CategoryDTO categoryDTO = new CategoryDTO(category);

        return ResponseEntity.ok().body(categoryDTO);

   }

   public ResponseEntity <Category> save (Category category){

        if(category.getName().isBlank()){
           throw new RuntimeException("Esta vazio o nome ");
        }

       return ResponseEntity.ok(categoryRepository.save(category));
   }


}
