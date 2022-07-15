package com.tackout.system;

//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
//@MapperScan("com.system.wysystem.dao")
public class TackoutApplication {

    public static void main(String[] args) {SpringApplication.run(TackoutApplication.class, args);}

}
