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
    @GetMapping("/conversion/{from}/{to}/{amount}")
    public DataDto getWhatWeNeed(@PathVariable("from") String from
                                ,@PathVariable("to") String to
                                ,@PathVariable("amount") double amount)
    {
        return this.service.getWhatWeNeed(from,to,amount);
    }
}
