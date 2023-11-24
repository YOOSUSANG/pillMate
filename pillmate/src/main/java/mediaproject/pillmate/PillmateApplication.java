package mediaproject.pillmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PillmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(PillmateApplication.class, args);
	}

}
