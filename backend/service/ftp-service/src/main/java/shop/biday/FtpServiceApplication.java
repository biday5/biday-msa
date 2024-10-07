package shop.biday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class FtpServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtpServiceApplication.class, args);
    }

}
