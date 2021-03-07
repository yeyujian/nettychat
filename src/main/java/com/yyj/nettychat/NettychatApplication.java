package com.yyj.nettychat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@Import(FdfsClientConfig.class)
@EnableAsync
public class NettychatApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettychatApplication.class, args);
	}

}
