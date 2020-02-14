package com.rodarte.springreactor;

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

        // un Flux es un observable
        // just: crea un observable a partir de los elementos proveidos
        // una vez emitidos, el observable se completa
        // doOnNext: el equivalente a tap en rxjs
        Flux<String> nombres =
                Flux
                        .just("Andres", "Pedro", "", "Diego", "Juan")
                        .doOnNext(nombre -> {

                            if (nombre.isEmpty()) {
                                throw new RuntimeException("Nombres no pueden ser vacios");
                            }

                            System.out.println(nombre);

                        });

        // suscripcion; metodos next y error
        // errores interrumpen el flujo y matan la suscripcion
        nombres.subscribe(
                logger::info,
                err -> logger.error(err.getMessage())
        );

    }

}
