package com.bm.graduationproject.exceptions;

import com.bm.graduationproject.web.response.ApiCustomResponse;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@ResponseBody
public class ControllerHandler {

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiCustomResponse<?> conversionFailedBadRequestExceptionHandling(
            ConversionFailedException e,
            WebRequest request
    ) {
        return ApiCustomResponse.builder()
                .isSuccess(false)
                .message("Please Enter valid currency name.")
                .statusCode(400)
                .build();

    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiCustomResponse<?> handleNotReadableMessageException(
            HttpMessageNotReadableException e,
            WebRequest request
    ){
        return ApiCustomResponse.builder()
                .isSuccess(false)
                .message("Please Enter valid currency name.")
                .statusCode(400)
                .build();
    }

    @ExceptionHandler(NotValidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiCustomResponse<?> handleNegativeAmountException(
            NotValidAmountException e,
            WebRequest request
    ){
        return ApiCustomResponse.builder()
                .isSuccess(false)
                .message(e.getMessage())
                .statusCode(400)
                .build();
    }
}
