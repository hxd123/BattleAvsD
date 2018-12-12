package com.aequilibrium.assess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*******************************************************************************
 * AssessApplication.java - Spring boot start program
 * Date   : Dec 7, 2018
 * Version: 1.0
 * Author : Bill Huang
 *
 * Common:
 *    1) start the program and wait other requests
 ********************************************************************************/

//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
@SpringBootApplication
@ComponentScan(basePackages={"com"})
public class AssessApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssessApplication.class, args);
    }
}