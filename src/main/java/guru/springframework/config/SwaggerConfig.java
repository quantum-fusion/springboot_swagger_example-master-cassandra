package guru.springframework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("product")
                .select()
                .apis(RequestHandlerSelectors.basePackage("guru.springframework.controllers"))
                .paths(regex("/product.*"))
                .build()
                .apiInfo(metaData());
    }

    @Bean
    public Docket restaurantApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("restaurant")
                .select()
                .apis(RequestHandlerSelectors.basePackage("guru.springframework.controllers"))
                .paths(regex("/restaurant.*"))
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("My Apps API Title")
                .description("My Apps API Description")
                .termsOfServiceUrl("My Apps API terms of service")
                .contact("My Apps API Contact Email")
                .license("My Apps API Licence Type")
                .licenseUrl("My Apps API License URL")
                .build();
    }

}
