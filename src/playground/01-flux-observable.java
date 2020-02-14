package com.rodarte.springreactor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringReactorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // un Flux es un observable
        // just: crea un observable a partir de los elementos proveidos
        // una vez emitidos, el observable se completa
        // doOnNext: el equivalente a tap en rxjs
        Flux<String> nombres =
                Flux
                        .just("Andres", "Pedro", "Diego", "Juan")
                        .doOnNext(System.out::println);

        // suscripcion
        nombres.subscribe();

    }

}
