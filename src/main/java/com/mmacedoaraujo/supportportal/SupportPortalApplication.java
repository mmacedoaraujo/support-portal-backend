package com.mmacedoaraujo.supportportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

import static com.mmacedoaraujo.supportportal.constant.FileConstant.USER_FOLDER;

@SpringBootApplication
public class SupportportalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupportportalApplication.class, args);
        new File(USER_FOLDER).mkdirs();
    }

}
