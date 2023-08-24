package com.bm.graduationproject.repositories;

import com.bm.graduationproject.models.ConversionOpenApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency", url = "${base_url}")
public interface CurrencyRepository {

    @GetMapping("/pair/{fromCurrency}/{toCurrency}")
    ConversionOpenApiResponse getCurrencyPair(@PathVariable("fromCurrency") String fromCurrency,
                                              @PathVariable("toCurrency") String toCurrency);
}
