package com.vitorbarros.dscatalog.service;

import com.vitorbarros.dscatalog.dto.ProductDTO;
import com.vitorbarros.dscatalog.exeptions.DataBaseException;
import com.vitorbarros.dscatalog.exeptions.ResourceNotFoundException;
import com.vitorbarros.dscatalog.models.Product;
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



    public ProductService(ProductRepository ProductRepository ){
        this.ProductRepository = ProductRepository;
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
   public ResponseEntity <ProductDTO> save (ProductDTO ProductDTO){


        Product Product = new Product();
        Product.setName(ProductDTO.getName());
        ProductRepository.save(Product);

        ProductDTO dto = new ProductDTO(Product);

       return ResponseEntity.ok().body(dto);

   }


   @Transactional
   public  ProductDTO update(Long id, ProductDTO ProductDTO){

        try {
            Product Product = ProductRepository.getReferenceById(id);
            Product.setName(ProductDTO.getName());
            ProductRepository.save(Product);
            return new ProductDTO(Product);
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


}
