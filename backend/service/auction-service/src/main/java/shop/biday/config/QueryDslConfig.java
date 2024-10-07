package shop.biday.config;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableSpringDataWebSupport
public class QueryDslConfig {
    @PersistenceContext
    private EntityManager entityManager;


    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

}
