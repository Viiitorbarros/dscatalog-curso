package com.vitorbarros.dscatalog.service;

import com.vitorbarros.dscatalog.dto.CategoryDTO;
import com.vitorbarros.dscatalog.dto.ProductDTO;
import com.vitorbarros.dscatalog.exeptions.DataBaseException;
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


    private ProductRepository ProductRepository;

    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository ProductRepository,CategoryRepository categoryRepository ){
        this.ProductRepository = ProductRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
   public ResponseEntity <Page<ProductDTO>> findAllPaged(Pageable pageRequest){

        Page<Product> categoriesPeged = ProductRepository.findAll(pageRequest);

        Page<ProductDTO> categoriesDto = categoriesPeged.map(x->new ProductDTO(x));

        return ResponseEntity.ok(categoriesDto);

   }

   @Transactional(readOnly = true)
   public ResponseEntity<ProductDTO> findById(Long id){

        Product Product = ProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entidade Nao Encontrada "));

        ProductDTO productDTO = new ProductDTO(Product, Product.getCategories());

        return ResponseEntity.ok().body(productDTO);

   }

   @Transactional
   public ResponseEntity <ProductDTO> save (ProductDTO productDTO){


        Product entity = new Product();
        copyDtotoEnity(productDTO, entity);
        ProductRepository.save(entity);

        ProductDTO dto = new ProductDTO(entity);

       return ResponseEntity.ok().body(dto);

   }


   @Transactional
   public  ProductDTO update(Long id, ProductDTO productDTO){

        try {
            Product entity = ProductRepository.getReferenceById(id);
            copyDtotoEnity(productDTO, entity);;
            ProductRepository.save(entity);
            return new ProductDTO(entity);
        }catch (EntityNotFoundException e){

            throw new ResourceNotFoundException(id + "Não Encontrado");
        }
   }


   public void delete(Long id){

        if (!ProductRepository.existsById(id)){
            throw new ResourceNotFoundException("Id não Encontrado");
        }
        try {
            ProductRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DataBaseException("Falha na integridade");
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
