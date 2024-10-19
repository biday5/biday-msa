package shop.biday.auction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuctionApplicationTests {

	@Autowired
	private Environment env;

	@Test
	void contextLoads() {
		String activeProfile = System.getProperty("spring.profiles.active", "default");
		assertThat(activeProfile).isEqualTo("test");
	}

	@Test
	void testConfigServerConnection() {
		boolean fetchRegistry = Boolean.parseBoolean(env.getProperty("eureka.client.fetch-registry"));
		boolean registerWithEureka = Boolean.parseBoolean(env.getProperty("eureka.client.register-with-eureka"));

		// 프로퍼티 값이 기대한 값인지 확인
		assertThat(fetchRegistry).isFalse();
		assertThat(registerWithEureka).isFalse();
	}

}
