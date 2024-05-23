package com.zscmp.main.app;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.zscmp.main.app.mybatis")
@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(AppApplication.class, args);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
