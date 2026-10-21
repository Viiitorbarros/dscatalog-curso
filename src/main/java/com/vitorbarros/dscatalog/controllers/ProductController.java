package com.vitorbarros.dscatalog.controllers;

import com.vitorbarros.dscatalog.dto.ProductDTO;
import com.vitorbarros.dscatalog.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(value = "/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService ProductService){
        this.productService = ProductService;
    }


    @GetMapping
    public ResponseEntity <Page<ProductDTO>> findAllPaged(Pageable pageable){

        return productService.findAllPaged(pageable);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){

       return productService.findById(id);

    }


    @PostMapping
    public  ResponseEntity<ProductDTO> save(@RequestBody ProductDTO ProductDTO){

        ProductDTO = productService.save(ProductDTO).getBody();

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ProductDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(ProductDTO);

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productDTO){

        productDTO = productService.update(id,productDTO);

        return ResponseEntity.ok().body(productDTO);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }




}
