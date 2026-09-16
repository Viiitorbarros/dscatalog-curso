package com.vitorbarros.dscatalog.models;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Category {

    private Long id;
    private String name;

    //Constructor vazio
    public Category(){
    }
    //Constructor
    public Category(Long id , String name){
        this.id = id;
        this.name = name;
    }

    //GETTERs e SETTERs criados com lombok
    //Equals And HashCode Criados com lombok

}
