package com.bm.graduationproject.web.controllers;

import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.services.CurrencyService;
import com.bm.graduationproject.web.response.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {
    private CurrencyService service;

    @Autowired
    public CurrencyController(CurrencyService service){
        this.service=service;
    }
    @GetMapping("/conversion")
    public ConversionResponseDto convertOrCompare(@RequestParam("from") String from
                                , @RequestParam("to1") String to1
                                , @RequestParam(value = "to2",required = false) String to2
                                , @RequestParam("amount") Double amount)
    {
        return this.service.convert(from,to1,amount);
    }

    @GetMapping()
    public List<CurrencyResponseDto> getAllCurrencies(){
        return service.getAllCurrencies();
    }
    @PostMapping()
    public ExchangeRateResponse getExchangeRate(@RequestParam String baseCurrency, @RequestParam List<String> favorites){
        return service.getExchangeRate(baseCurrency, favorites);
    }
}
