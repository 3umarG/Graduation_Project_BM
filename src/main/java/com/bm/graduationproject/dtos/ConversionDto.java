package com.bm.graduationproject.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversionDto {
    String source;
    String destination;
    double amount;
}

