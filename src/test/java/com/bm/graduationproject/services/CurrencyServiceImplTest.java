package com.bm.graduationproject.services;
import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.repositories.CurrencyRepository;
import com.bm.graduationproject.web.response.ConversionOpenApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CurrencyServiceImplTest{


    @Test
    public void testConvert() {
        CurrencyRepository repository = Mockito.mock(CurrencyRepository.class);
        CurrencyServiceImpl currencyService = new CurrencyServiceImpl(repository);

        ConversionOpenApiResponse apiResponse = new ConversionOpenApiResponse();

        apiResponse.setConversion_rate(0.93);
        apiResponse.setConversion_result(46.5);

        Mockito.when(repository.getCurrencyPair(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()))
                .thenReturn(apiResponse);

        ConversionResponseDto result = currencyService.convert("USD", "EUR", 50.0);

        Assertions.assertEquals("USD", result.getSource());
        Assertions.assertEquals("EUR", result.getDestination());
        Assertions.assertEquals(46.5, result.getAmount());

    }

}
