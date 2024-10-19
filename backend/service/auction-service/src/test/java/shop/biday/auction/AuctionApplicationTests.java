package shop.biday.auction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuctionApplicationTests {

	@Autowired
	private Environment env;

	@Test
	void contextLoads() {
		String activeProfile = env.getActiveProfiles()[0];
		assertThat(activeProfile).isEqualTo("test");
	}
}
