package com.vitorbarros.dscatalog.controllers;

import com.vitorbarros.dscatalog.models.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity <List<Category>> findById(){

        List<Category> list = new ArrayList<>();

        list.add(new Category(1L, "Eletronicos"));
        list.add(new Category(2L, "Cozinha"));

        return ResponseEntity.ok(list);

    }

}
