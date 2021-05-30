package ru.olegr.accountapp.configuration;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@ComponentScan(basePackages = "ru.olegr.accountapp")
public class AccountAppApplication {

    private final DataSource dataSource;

    @Autowired
    public AccountAppApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(AccountAppApplication.class, args);
    }

    @Bean("accountAppValidator")
    public Validator accountAppValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public DSLContext dslContext() {
        return new DefaultDSLContext(jooqConfiguration());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant("/api/transactions"))
            .build();
    }

    private Configuration jooqConfiguration() {
        DefaultConfiguration defaultConfiguration = new DefaultConfiguration();

        DataSourceConnectionProvider connectionProvider = new DataSourceConnectionProvider(
            new TransactionAwareDataSourceProxy(dataSource)
        );
        defaultConfiguration.set(connectionProvider);

        SQLDialect dialect = SQLDialect.POSTGRES;
        defaultConfiguration.set(dialect);

        return defaultConfiguration;
    }
}
