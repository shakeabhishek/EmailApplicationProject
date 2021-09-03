package com.test.EmailApplicationProject.exceptions;

import com.test.EmailApplicationProject.models.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
   @ExceptionHandler({LoginException.class, TokenExpiredException.class})
    public ResponseEntity<?> handleLoginFailure(Exception ex) {
       ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), 400);
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
   }
//
//   @ExceptionHandler({Exception.class})
//    public String globalHandler(Exception ex) {
//       return ex.getMessage();
//   }
}
