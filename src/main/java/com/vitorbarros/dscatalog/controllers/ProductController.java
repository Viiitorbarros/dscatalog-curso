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
    public ResponseEntity <Page<ProductDTO>> findAllPaged(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy

    ){
        Pageable pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);


        return productService.findAllPaged(pageRequest);

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
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO ProductDTO){

        ProductDTO = productService.update(id,ProductDTO);

        return ResponseEntity.ok().body(ProductDTO);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }




}
