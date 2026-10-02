package com.vitorbarros.dscatalog.dto;


import com.vitorbarros.dscatalog.models.Category;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class CategoryDTO {

    private Long id;
    private String name;


    public CategoryDTO(){

    }


    public CategoryDTO(Long id, String name){
        this.id = id;
        this.name = name;

    }


    //criado para facilitar minha conversão para DTO
    public CategoryDTO(Category entity){
        this.id = entity.getId();
        this.name = entity.getName();

    }


    public Long getId(){
        return id;
    }


    public String getName(){
        return name;
    }


    public void setId(Long id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }







}
