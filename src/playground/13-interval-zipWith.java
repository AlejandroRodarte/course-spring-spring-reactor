package com.rodarte.springreactor;

import com.rodarte.springreactor.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootApplication
public class SpringReactorApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpringReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Flux<Integer> range = Flux.range(1, 12);

        // interval y delayElements son operadores asinronos: el metodo run() terminara de ejecutar
        // pero hilos secundarios seguiran trabajando con los datos de estos observables

        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

        Flux<Integer> delayElements =
                range
                        .delayElements(Duration.ofSeconds(1))
                        .doOnNext(r -> logger.info(r.toString()));

        range
                .zipWith(interval, (r, d) -> r)
                .doOnNext(r -> logger.info(r.toString()))
                .blockLast();

        delayElements.blockLast();

    }

    public Usuario crearUsuario() {
        return new Usuario("John", "Doe");
    }

}
