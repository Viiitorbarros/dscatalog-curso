package com.vitorbarros.dscatalog.controllers;

import com.vitorbarros.dscatalog.models.Category;
import com.vitorbarros.dscatalog.service.CategoryService;
import com.vitorbarros.dscatalog.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity <List<CategoryDTO>> findById(){

        return categoryService.findAll();

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){

       return categoryService.findById(id);

    }


    @PostMapping
    public  ResponseEntity<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO){

        categoryDTO = categoryService.save(categoryDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(categoryDTO);


    }

}
