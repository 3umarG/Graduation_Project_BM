package com.bm.graduationproject.enums;

import lombok.Getter;

@Getter
public enum Currency {
    USD("US Dollar","https://flagcdn.com/w20/us.png"),
    EUR("EURO","https://api.exchangerate-api.com/flag-images/EU.gif"),
    GBP("Sterling Pound","https://flagcdn.com/w20/gb.png"),
    AED("UAE Dirham","https://flagcdn.com/w20/ae.png"),
    BHD("Bahrain Dinar","https://flagcdn.com/w20/bh.png"),
    JPY("Japan Yen","https://flagcdn.com/w20/jp.png"),
    KWD("Kuwaiti Dinar","https://flagcdn.com/w20/kw.png"),
    OMR("Oman Riyal","https://flagcdn.com/w20/om.png"),
    QAR("QATARI Riyal","https://flagcdn.com/w20/qa.png"),
    SAR("Saudi Riyal","https://flagcdn.com/w20/sa.png");

    private String country;
    private String flagImageUrl;

    Currency(String country, String flagImageUrl) {
        this.country = country;
        this.flagImageUrl = flagImageUrl;
    }
}
