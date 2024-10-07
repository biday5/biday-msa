package shop.biday.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import shop.biday.utils.BigIntegerToDecimal128Converter;
import shop.biday.utils.Decimal128ToBigIntegerConverter;

@Configuration
@EnableReactiveMongoRepositories(basePackages = {
        "shop.biday.model.repository"
})
public class ReactiveMongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "bidaydb";
    }

    @Override
    protected void configureConverters(MongoCustomConversions.MongoConverterConfigurationAdapter converterConfigurationAdapter) {
        converterConfigurationAdapter.registerConverter(new BigIntegerToDecimal128Converter());
        converterConfigurationAdapter.registerConverter(new Decimal128ToBigIntegerConverter());
    }
}
