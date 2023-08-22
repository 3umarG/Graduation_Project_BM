package com.bm.graduationproject.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@FeignClient(name = "currency",url = "${base_url}")
public interface CurrencyRepository {
}
