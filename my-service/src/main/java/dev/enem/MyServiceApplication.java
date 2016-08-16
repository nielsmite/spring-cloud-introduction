package dev.enem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@SpringBootApplication
public class MyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyServiceApplication.class, args);
	}
}

@RefreshScope
@RestController
class MyServiceController {

    @Value("${message}")
    public String message;

    @RequestMapping(method = RequestMethod.GET, value = "/message")
    public String message(){
        return this.message;
    }
}


@Component
class BootstrapCLR implements CommandLineRunner {

    @Autowired
    ReservationRepository reservationRepository;

    @Override
    public void run(String... strings) throws Exception {
        Stream.of("Niels", "Ansgar", "Fede", "Alex").forEach(nom -> reservationRepository.save(new Reservation(nom)));

        reservationRepository.findAll().stream().forEach(System.out::println);
    }
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {}

@Entity
class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    public String name;

    public Reservation() {
    }

    public Reservation(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Reservation{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
