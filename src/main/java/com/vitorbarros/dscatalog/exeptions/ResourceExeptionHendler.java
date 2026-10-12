package com.vitorbarros.dscatalog.exeptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExeptionHendler {

   @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<StandardExeption> entityNotFound(ResourceNotFoundException e , HttpServletRequest request){
    HttpStatus status = HttpStatus.NOT_FOUND;

    StandardExeption err = new StandardExeption();

    err.setTimestamp(Instant.now());
    err.setStatus(status.value());
    err.setError("Não encontrado");
    err.setMenssagem(e.getMessage());
    err.setPath(request.getRequestURI());

    return ResponseEntity.status(status).body(err);
   }


    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardExeption> databaseException(DataBaseException e , HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardExeption err = new StandardExeption();

        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Error de integridade");
        err.setMenssagem(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }




 }
