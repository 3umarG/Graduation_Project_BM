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
    public DataDto getWhatWeNeed(String from, String to,double amount){
        DataDto dataDto = new DataDto();
        dataDto.setSource(from);
        dataDto.setDestination(to);
        dataDto.setAmount((this.getAll(from,to).getConversion_rate())* amount);
        return dataDto;
    }
}
