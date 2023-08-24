package com.bm.graduationproject.services;


import com.bm.graduationproject.dtos.CompareResponseDto;
import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.models.ConversionOpenApiResponse;

import java.util.List;

public interface BaseCurrencyService {

    ConversionResponseDto convert(String from, String to, double amount);

    List<CurrencyResponseDto> getAllCurrencies();

    CompareResponseDto compare(String src, String des1 , String des2 , Double amount);


}
