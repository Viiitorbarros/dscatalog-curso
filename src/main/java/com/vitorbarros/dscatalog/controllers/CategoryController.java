package com.vitorbarros.dscatalog.controllers;

import com.vitorbarros.dscatalog.models.Category;
import com.vitorbarros.dscatalog.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity <List<Category>> findById(){
       return categoryService.findAll();
    }



    @PostMapping
    public  ResponseEntity<Category> save(@RequestBody Category category){
        return categoryService.save(category);
    }

}
