package com.bm.graduationproject.services;
import com.bm.graduationproject.dtos.CompareResponseDto;
import com.bm.graduationproject.dtos.ConversionResponseDto;
import com.bm.graduationproject.repositories.CurrencyRepository;
import com.bm.graduationproject.web.response.ConversionOpenApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CurrencyServiceImplTest {


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

    @Test
    public void testcompare() {
        CurrencyRepository repository = Mockito.mock(CurrencyRepository.class);
        CurrencyServiceImpl currencyService = new CurrencyServiceImpl(repository);

        ConversionOpenApiResponse apiResponse1 = new ConversionOpenApiResponse();
        ConversionOpenApiResponse apiResponse2 = new ConversionOpenApiResponse();


        apiResponse1.setConversion_result(46.5);
        apiResponse1.setTarget_code("EUR");
        apiResponse2.setTarget_code("KWD");
        apiResponse2.setConversion_result(15.5);

        Mockito.when(repository.getCurrencyPair(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()))
                .thenReturn(apiResponse1,apiResponse2);

        CompareResponseDto result = currencyService.compare("USD", "EUR", "KWD", 50.0);

        Assertions.assertEquals("USD", result.getSource());
        Assertions.assertEquals(15.5, result.getAmount2());
        Assertions.assertEquals(46.5, result.getAmount1());
        Assertions.assertEquals("EUR", result.getDestination1());
        Assertions.assertEquals("KWD", result.getDestination2());
    }
}
