package shop.biday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SmsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsServiceApplication.class, args);
	}

}
