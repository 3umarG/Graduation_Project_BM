package com.bm.graduationproject.controllers;

import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.enums.Currency;
import com.bm.graduationproject.services.BaseCurrencyService;
import com.bm.graduationproject.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {
    private BaseCurrencyService service;

    @Autowired
    public CurrencyController(CurrencyService service){
        this.service=service;
    }
    @GetMapping("/conversion")
    public ConversionResponseDto convertOrCompare(@RequestParam("from") Currency from
                                , @RequestParam("to1") Currency to1
                                , @RequestParam(value = "to2",required = false) Currency to2
                                , @RequestParam("amount") Double amount)
    {
        return this.service.convert(from.name(),to1.name(),amount);
    }

    @GetMapping()
    public List<CurrencyResponseDto> getAllCurrencies(){
        return service.getAllCurrencies();
    }
}
