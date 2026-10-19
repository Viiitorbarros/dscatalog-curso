package com.vitorbarros.dscatalog.repository;

import com.vitorbarros.dscatalog.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
