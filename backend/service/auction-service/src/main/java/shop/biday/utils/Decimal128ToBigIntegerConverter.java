package shop.biday.utils;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@ReadingConverter
public class Decimal128ToBigIntegerConverter implements Converter<Decimal128, BigInteger> {

    @Override
    public BigInteger convert(Decimal128 source) {
        return source.bigDecimalValue().toBigInteger();
    }
}
