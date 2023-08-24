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
    private String base;
    private List<CurrencyResponseDto> currencies;
}

