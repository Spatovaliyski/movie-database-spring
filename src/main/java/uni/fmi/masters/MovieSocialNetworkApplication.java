package uni.fmi.masters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class MovieSocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieSocialNetworkApplication.class, args);
	}

}
