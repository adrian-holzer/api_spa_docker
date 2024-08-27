package com.adri.api_spa.Utils;

import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

public class ApiError {

    private Map<String, String> errores;


    public  ApiError(Errors errors){


        this.errores=   handleMethodArgumentNotValid(errors);

    }

    public Map<String, String> handleMethodArgumentNotValid(Errors ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return errors;
    }


    public Map<String, String> getErrores() {
        return errores;
    }
}
