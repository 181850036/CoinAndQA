package org.njuse.nekocoin.nekocoin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.security.auth.login.Configuration;


@SpringBootApplication
public class NekocoinApplication {

    public static void main(String[] args) {


        ApplicationContext  applicationContext = SpringApplication.run(NekocoinApplication.class, args);

//        Object bean = applicationContext.getBean("Driver");
    }

}
