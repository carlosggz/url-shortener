package org.example.urlshortener.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigInteger;

@Component
public class BaseEncoder implements StringEncoder {
    private final BigInteger base;
    private final String characterSet;

    public BaseEncoder(
            @Value("${app.encoder.base}") int base,
            @Value("${app.encoder.character-set}") String characterSet) {
        this.base = new BigInteger(String.valueOf(base));
        this.characterSet = characterSet;
    }

    @Override
    public String encode(final String input) {
        Assert.isTrue(StringUtils.isNotBlank(input), "Input must not be blank");
        Assert.isTrue(StringUtils.isNumeric(input), "Input must be numeric");

        var number = new BigInteger(input);
        StringBuilder stringBuilder = new StringBuilder(1);
        do {
            stringBuilder.insert(0, characterSet.charAt((number.mod(base)).intValue()));
            number = number.divide(base);
        } while (number.compareTo(BigInteger.ZERO) > 0);
        return stringBuilder.toString();
    }
}
