package com.bm.graduationproject.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiCustomResponse<T> {
    private Integer statusCode;
    private Boolean isSuccess;
    private String message;

    private T data;
}
