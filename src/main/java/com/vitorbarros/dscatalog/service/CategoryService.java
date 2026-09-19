package com.vitorbarros.dscatalog.service;

import com.vitorbarros.dscatalog.models.Category;
import com.vitorbarros.dscatalog.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {


    private CategoryRepository categoryRepository;



    public CategoryService(CategoryRepository categoryRepository ){
        this.categoryRepository = categoryRepository;
    }


   public ResponseEntity <List<Category>> findAll(){
         List<Category> listCategories = categoryRepository.findAll();
         return ResponseEntity.ok(listCategories);
   }

   public ResponseEntity <Category> save (Category category){

        if(category.getName().isBlank()){
           throw new RuntimeException("Esta vazio o nome ");
        }

       return ResponseEntity.ok(categoryRepository.save(category));
   }


}
