package com.vitorbarros.dscatalog.service;

import com.vitorbarros.dscatalog.dto.CategoryDTO;
import com.vitorbarros.dscatalog.dto.ProductDTO;
import com.vitorbarros.dscatalog.exeptions.DatabaseException;
import com.vitorbarros.dscatalog.exeptions.ResourceNotFoundException;
import com.vitorbarros.dscatalog.models.Category;
import com.vitorbarros.dscatalog.models.Product;
import com.vitorbarros.dscatalog.repository.CategoryRepository;
import com.vitorbarros.dscatalog.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductService {


    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository ProductRepository,CategoryRepository categoryRepository ){
        this.productRepository = ProductRepository;
        this.categoryRepository = categoryRepository;
    }


    @Transactional(readOnly = true)
   public ResponseEntity <Page<ProductDTO>> findAllPaged(Pageable pageable){

        Page<Product> productsPeged = productRepository.findAll(pageable);

        Page<ProductDTO> productsDto = productsPeged.map(x->new ProductDTO(x));

        return ResponseEntity.ok(productsDto);

   }

   @Transactional(readOnly = true)
   public ResponseEntity<ProductDTO> findById(Long id){

        Product Product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entidade Nao Encontrada "));

        ProductDTO productDTO = new ProductDTO(Product, Product.getCategories());

        return ResponseEntity.ok().body(productDTO);

   }

   @Transactional
   public ResponseEntity <ProductDTO> save (ProductDTO productDTO){


        Product entity = new Product();
        copyDtotoEnity(productDTO, entity);
        productRepository.save(entity);

        ProductDTO dto = new ProductDTO(entity);

       return ResponseEntity.ok().body(dto);

   }


   @Transactional
   public  ProductDTO update(Long id, ProductDTO productDTO){

        try {
            Product entity = productRepository.getReferenceById(id);
            copyDtotoEnity(productDTO, entity);;
            productRepository.save(entity);
            return new ProductDTO(entity);
        }catch (EntityNotFoundException e){

            throw new ResourceNotFoundException(id + "Não Encontrado");
        }
   }


   public void delete(Long id){

        if (!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Id não Encontrado");
        }
        try {
            productRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha na integridade");
        }

   }


   private void copyDtotoEnity(ProductDTO productDTO, Product entity){

        entity.setName(productDTO.getName());
        entity.setDate(productDTO.getDate());
        entity.setDescription(productDTO.getDescription());
        entity.setPrice(productDTO.getPrice());
        entity.setImgUrl(productDTO.getImgUrl());

        entity.getCategories().clear();
        for(CategoryDTO cat : productDTO.getCategories()){
            Category categorie = categoryRepository.getOne(cat.getId());
            entity.getCategories().add(categorie);

        }

   }

}
