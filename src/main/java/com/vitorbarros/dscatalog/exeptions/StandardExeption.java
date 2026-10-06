package com.vitorbarros.dscatalog.exeptions;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@Setter
@Getter
public class StandardExeption {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String menssagem;
    private String path;




}
