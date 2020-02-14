package com.rodarte.springreactor;

import com.rodarte.springreactor.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringReactorApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpringReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Flux.range: similar a range de rxjs
        Flux.just(1, 2, 3, 4)
                .map(i -> i * 2)
                .zipWith(Flux.range(0, 4), (i, j) -> String.format("Primer Flux: %d, Segundo Flux: %d", i, j))
                .subscribe(System.out::println);

    }

    public Usuario crearUsuario() {
        return new Usuario("John", "Doe");
    }

}
