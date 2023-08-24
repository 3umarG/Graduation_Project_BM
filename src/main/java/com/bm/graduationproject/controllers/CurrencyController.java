package com.bm.graduationproject.controllers;

import com.bm.graduationproject.dtos.CurrencyResponseDto;
import com.bm.graduationproject.enums.Currency;
import com.bm.graduationproject.models.ApiCustomResponse;
import com.bm.graduationproject.models.FavoritesResponseDto;
import com.bm.graduationproject.services.CurrencyService;
import com.bm.graduationproject.services.CurrencyServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {
    private CurrencyService service;

    @Autowired
    public CurrencyController(CurrencyServiceImp service) {
        this.service = service;
    }

    @GetMapping("/conversion")
    public ResponseEntity<ApiCustomResponse<?>> convertOrCompare(@RequestParam("from") Currency from
            , @RequestParam("to1") Currency to1
            , @RequestParam(value = "to2", required = false) Currency to2
            , @RequestParam("amount") Double amount) {
        return ResponseEntity.ok(ApiCustomResponse.builder()
                .statusCode(200)
                .isSuccess(true)
                .data(to2 == null
                        ? this.service.convert(from.name(), to1.name(), amount)
                        : this.service.compare(from.name(), to1.name(), to2.name(), amount))
                .build());
    }

    @GetMapping()
    public ResponseEntity<ApiCustomResponse<List<CurrencyResponseDto>>> getAllCurrencies() {
        return ResponseEntity.ok(ApiCustomResponse.<List<CurrencyResponseDto>>builder()
                        .data(service.getAllCurrencies())
                        .statusCode(200)
                        .isSuccess(true)
                        .build());
    }


    @PostMapping()
    public ResponseEntity<ApiCustomResponse<FavoritesResponseDto>> getExchangeRate(@RequestParam(name = "base") Currency baseCurrency, @RequestBody List<Currency> favorites){
        return ResponseEntity.ok(ApiCustomResponse.<FavoritesResponseDto>builder()
                        .data(service.getExchangeRate(baseCurrency, favorites))
                        .isSuccess(true)
                        .statusCode(200)
                .build());
    }
}
