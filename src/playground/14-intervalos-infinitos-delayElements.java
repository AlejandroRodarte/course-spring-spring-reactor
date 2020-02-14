package com.rodarte.springreactor;

import com.rodarte.springreactor.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class SpringReactorApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpringReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Flux<Long> infinito = Flux.interval(Duration.ofSeconds(1));

        CountDownLatch latch = new CountDownLatch(1);

        // doOnTerminate: permite al stream ejecutar un efecto secundario cuando completa
        // Flux.error: retorna un observable con un error, lo atrapa el callback de error en el subscribe
        // retry: se reintenta hacer la suscripcion despues de un error n cantidad de veces
        infinito
                .doOnTerminate(latch::countDown)
                .flatMap(i -> i >= 5 ? Flux.error(new InterruptedException("Solo hasta 5")) : Flux.just(i))
                .map(i -> "Hola " + i)
                .retry(2)
                .subscribe(
                        logger::info,
                        e -> logger.error(e.getMessage())
                );

        latch.await();

    }

    public Usuario crearUsuario() {
        return new Usuario("John", "Doe");
    }

}
