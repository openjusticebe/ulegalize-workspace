package com.ulegalize.lawfirm.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;


@Configuration
@Profile({"dev", "devDocker", "integrationtest", "test", "prod"})
public class DataSourceConfig {

    @Value("${app.datasource.driverClassName}")
    String driverClassName;
    @Value("${app.datasource.url}")
    String url;
    @Value("${app.datasource.username}")
    String username;
    @Value("${app.datasource.password}")
    String password;


    @Bean
    @Primary
    public DataSource devGetDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }

}