package com.example.workdist.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.example.workdist"})
@EntityScan(basePackages = {"com.example.workdist.common.data.entity"})
@EnableJpaRepositories(basePackages = {"com.example.workdist.common.data.repository"})
@EnableTransactionManagement
public class WorkDistributionApi {
    public static void main(String[] args) {
        SpringApplication.run(WorkDistributionApi.class, args);
    }
}