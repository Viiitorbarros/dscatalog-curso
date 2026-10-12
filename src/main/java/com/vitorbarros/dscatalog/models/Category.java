package com.vitorbarros.dscatalog.models;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String name;


    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updateAt;


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

    @PrePersist
    public void prePersist(){
        createdAt = Instant.now();
    }
    @PreUpdate
    public void preUpdate(){
        updateAt = Instant.now();
    }



}
