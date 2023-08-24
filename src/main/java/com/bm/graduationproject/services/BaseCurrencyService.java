package com.bm.graduationproject.services;


import com.bm.graduationproject.models.Data;
import com.bm.graduationproject.dtos.DataDto;

public interface BaseCurrencyService {

    Data getAll(String from, String to);

    DataDto getWhatWeNeed(String from, String to,double amount);

}
