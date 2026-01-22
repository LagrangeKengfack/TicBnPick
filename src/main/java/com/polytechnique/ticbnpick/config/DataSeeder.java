package com.polytechnique.ticbnpick.config;

import com.polytechnique.ticbnpick.models.Client;
import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.repositories.ClientRepository;
import com.polytechnique.ticbnpick.repositories.PersonRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final ClientRepository clientRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking for existing clients...");
        clientRepository.count().flatMap(count -> {
            if (count == 0) {
                log.info("No clients found. Seeding test client...");
                Person person = new Person();
                person.setLast_name("Doe");
                person.setFirst_name("John");
                person.setPhone("+33612345678");
                person.setEmail("john.doe@example.com");
                person.setPassword("password");
                person.setNationalId("ID12345");
                person.setPhotoCard("photo.jpg");
                
                return personRepository.save(person).flatMap(savedPerson -> {
                    Client client = new Client();
                    client.setPersonId(savedPerson.getId());
                    client.setLoyaltyStatus("GOLD");
                    return clientRepository.save(client);
                });
            }
            return Mono.empty();
        }).subscribe(
            client -> log.info(" Test Client Created with ID: {}", client.getId()),
            error -> log.error("Error seeding data:", error),
            () -> log.info("Data seeding check completed.")
        );
    }
}
