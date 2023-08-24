package com.bm.graduationproject.services;

import com.bm.graduationproject.models.Data;
import com.bm.graduationproject.dtos.DataDto;
import com.bm.graduationproject.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService implements BaseCurrencyService {

    private final CurrencyRepository repository;

    @Autowired
    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Data getAll(String from, String to) {
        return repository.getCurrencyPair(from,to);
    }
    @Override
    public ConversionDto convert(String from, String to,double amount){
        ConversionDto dataDto = new ConversionDto();
        dataDto.setSource(from.toUpperCase());
        dataDto.setDestination(to.toUpperCase());
        dataDto.setAmount((this.getAll(from.toUpperCase(),to.toUpperCase()).getConversion_rate())* amount);
        return dataDto;
    }
}
