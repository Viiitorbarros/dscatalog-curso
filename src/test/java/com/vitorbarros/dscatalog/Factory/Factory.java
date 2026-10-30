package com.vitorbarros.dscatalog.Factory;

import com.vitorbarros.dscatalog.dto.ProductDTO;
import com.vitorbarros.dscatalog.models.Category;
import com.vitorbarros.dscatalog.models.Product;

import java.time.Instant;

public class Factory {


    public static Product createProduct(){

        Product product = new Product(1L, "Phone","God Phone", 800.0,"https://raw.githubusercontent.com/devsuperior/dscatalog", Instant.parse("2020-07-14T10:00:00Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;

    }

    public static ProductDTO createProductDto(){

        Product product = createProduct();
        return new  ProductDTO(product,product.getCategories());

    }



}
