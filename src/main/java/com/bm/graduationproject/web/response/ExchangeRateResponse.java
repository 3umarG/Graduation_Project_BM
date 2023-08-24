package com.bm.graduationproject.web.response;

import com.bm.graduationproject.dtos.CurrenciesRateDto;
import lombok.*;

import java.util.List;
import java.util.Map;
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ExchangeRateResponse {
//     "base": "KWD",
//    "destinations" : [
//		{
//			"code" : "EGP",
//			"country" : "Egyption Pound",
//			"imageUrl" : ".....",
//			"rate" : 100.5566
    private String base;
    private List<CurrenciesRateDto> currencies;
}
