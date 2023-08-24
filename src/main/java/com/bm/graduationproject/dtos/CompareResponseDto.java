package com.bm.graduationproject.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class CompareResponseDto {
    private String source;
    private String destination1;
    private String destination2;
    private Double amount1;
    private Double amount2;
}
