package com.bm.graduationproject.repositories;

import com.bm.graduationproject.dtos.ExchangeRateDto;
import com.bm.graduationproject.web.response.ConversionOpenApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "currency", url = "${base_url}")
public interface CurrencyRepository {

    @GetMapping("/pair/{fromCurrency}/{toCurrency}")
    ConversionOpenApiResponse getCurrencyPair(@PathVariable("fromCurrency") String fromCurrency,
                                              @PathVariable("toCurrency") String toCurrency);
    @GetMapping("/latest/{base}")
    ExchangeRateDto getExchangeRate(@PathVariable("base") String base);
}
