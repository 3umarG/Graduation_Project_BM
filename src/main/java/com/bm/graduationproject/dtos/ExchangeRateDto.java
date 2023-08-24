package com.bm.graduationproject.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
@Setter
@Getter
@NoArgsConstructor
public class ExchangeRateDto {
    private String result;
    private String base_code;
    private Map<String, Double> conversion_rates;
}
