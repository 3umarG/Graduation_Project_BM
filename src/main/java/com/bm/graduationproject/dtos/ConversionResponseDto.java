package com.bm.graduationproject.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversionResponseDto {
    String source;
    String destination;
    double amount;
}

