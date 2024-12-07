package informational_systems.lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("informational_systems.lab1.repository")
@EntityScan("informational_systems.lab1.items")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
