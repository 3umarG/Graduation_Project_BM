package com.bm.graduationproject.models;

public enum Currencies {
    USD("US Dollar","https://flagcdn.com/w20/us.png"),
    EUR("EURO","https://api.exchangerate-api.com/flag-images/EU.gif"),
    GBP("Sterling Pound","https://flagcdn.com/w20/gb.png"),
    AED("UAE DIRHAM","https://flagcdn.com/w20/ae.png"),
    BHD("Bahrain Dinar","https://flagcdn.com/w20/bh.png"),
    JPY("Japan Yen","https://flagcdn.com/w20/jp.png"),
    KWD("Kwait Dinar","https://flagcdn.com/w20/kw.png"),
    OMR("Oman Riyal","https://flagcdn.com/w20/om.png"),
    QAR("QATARI Riyal","https://flagcdn.com/w20/qa.png"),
    SAR("Sudi Riyal","https://flagcdn.com/w20/sa.png");

    private String Currencies;
    private String flagImageUrl;

    public String getCurrencies() {
        return Currencies;
    }

    public String getFlagImageUrl() {
        return flagImageUrl;
    }

    Currencies(String currencies, String flagImageUrl) {
        Currencies = currencies;
        this.flagImageUrl = flagImageUrl;
    }
}
