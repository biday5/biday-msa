package shop.biday.auction;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuctionApplicationTests {

	@Test
	void contextLoads() {
		String activeProfile = System.getProperty("spring.profiles.active", "default");
		assertThat(activeProfile).isEqualTo("test");
	}
}
