package com.bm.graduationproject.controllers;

import com.bm.graduationproject.dtos.DataDto;
import com.bm.graduationproject.models.Data;
import com.bm.graduationproject.services.BaseCurrencyService;
import com.bm.graduationproject.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {
    private BaseCurrencyService service;

    @Autowired
    public CurrencyController(CurrencyService service){
        this.service=service;
    }
    @GetMapping("/conversion")
    public DataDto convertOrCompare(@RequestParam("from") String from
                                ,@RequestParam("to1") String to1
                                ,@RequestParam("to2") String to2
                                ,@RequestParam("amount") Double amount)
    {
        return this.service.convert(from,to1,amount);
    }
}