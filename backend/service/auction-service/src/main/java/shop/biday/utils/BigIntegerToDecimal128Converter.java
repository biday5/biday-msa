package shop.biday.utils;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

@Component
@WritingConverter
public class BigIntegerToDecimal128Converter implements Converter<BigInteger, Decimal128> {

    @Override
    public Decimal128 convert(BigInteger source) {
        return Decimal128.parse(new BigDecimal(source).toPlainString());
    }
}
