package com.vitorbarros.dscatalog.controllers;

import com.vitorbarros.dscatalog.models.Category;
import com.vitorbarros.dscatalog.service.CategoryService;
import com.vitorbarros.dscatalog.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity <Page<CategoryDTO>> findAllPaged(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy

    ){
        Pageable pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);


        return categoryService.findAllPaged(pageRequest);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){

       return categoryService.findById(id);

    }


    @PostMapping
    public  ResponseEntity<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO){

        categoryDTO = categoryService.save(categoryDTO).getBody();

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(categoryDTO);

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){

        categoryDTO = categoryService.update(id,categoryDTO);

        return ResponseEntity.ok().body(categoryDTO);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }




}
