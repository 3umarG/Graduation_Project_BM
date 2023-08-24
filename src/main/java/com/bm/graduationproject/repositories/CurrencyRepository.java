package com.bm.graduationproject.repositories;

import com.bm.graduationproject.models.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency", url = "${base_url}")
public interface CurrencyRepository {

    @GetMapping("/pair/{fromCurrency}/{toCurrency}")
    Data getCurrencyPair(@PathVariable("fromCurrency") String fromCurrency,
                         @PathVariable("toCurrency") String toCurrency);
}
