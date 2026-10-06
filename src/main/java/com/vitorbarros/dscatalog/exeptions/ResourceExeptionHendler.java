package com.vitorbarros.dscatalog.exeptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExeptionHendler {

   @ExceptionHandler(EntityNotFound.class)
   public ResponseEntity<StandardExeption> entityNotFound(EntityNotFound e , HttpServletRequest request){

    StandardExeption err = new StandardExeption();


    err.setTimestamp(Instant.now());
    err.setStatus(HttpStatus.NOT_FOUND.value());
    err.setError("Não encontrado");
    err.setMenssagem(e.getMessage());
    err.setPath(request.getRequestURI());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
   }

   @ExceptionHandler(InvalidResourceException.class)
   public ResponseEntity<StandardExeption> invalidResource(InvalidResourceException e , HttpServletRequest request){

       StandardExeption err = new StandardExeption();

       err.setTimestamp(Instant.now());
       err.setStatus(HttpStatus.BAD_REQUEST.value());
       err.setError("Error ao salvar");
       err.setMenssagem(e.getMessage());
       err.setPath(request.getRequestURI());


       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

   }

 }
