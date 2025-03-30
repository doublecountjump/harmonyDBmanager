package harmony.dbproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class DbprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbprojectApplication.class, args);
	}

}
