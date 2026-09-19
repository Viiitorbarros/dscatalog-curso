package com.vitorbarros.dscatalog.models;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Category {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tb_category")
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
