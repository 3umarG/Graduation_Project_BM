package com.bm.graduationproject.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CachingService {
    public String concatEnumNames(List<Enum<?>> enums) {
        return enums.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }
}
