package com.bm.graduationproject.models;

import com.bm.graduationproject.dtos.CurrencyResponseDto;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FavoritesResponseDto {
    //     "base": "KWD",
//    "destinations" : [
//		{
//			"code" : "EGP",
//			"country" : "Egyption Pound",
//			"imageUrl" : ".....",
//			"rate" : 100.5566
    private String base;
    private List<CurrencyResponseDto> currencies;
}

