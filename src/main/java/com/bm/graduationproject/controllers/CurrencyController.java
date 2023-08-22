package com.bm.graduationproject.controllers;

import com.bm.graduationproject.services.BaseCurrencyService;
import com.bm.graduationproject.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {
    private BaseCurrencyService service;

    @Autowired
    public CurrencyController(CurrencyService service){
        this.service=service;
    }
}
