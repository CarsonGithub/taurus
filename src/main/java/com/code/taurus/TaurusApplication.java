package com.code.taurus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启用服务
 *
 * @author 郑楷山
 **/

@SpringBootApplication
public class TaurusApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaurusApplication.class, args);
	}

}
