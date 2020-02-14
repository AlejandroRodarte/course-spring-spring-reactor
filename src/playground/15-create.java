package com.rodarte.springreactor;

import com.rodarte.springreactor.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class SpringReactorApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpringReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Flux.create: crear un nuevo Observable
        // se pasa de argumento una funcion Consumer que representa a nuestro suscriptor
        // dentro de alli llamamos a metodos next, complete y error como le hacemos en rxjs

        // los handlers de estos tres casos pueden estar al nivel del subscribe o en los operadores
        // doOnNext, doOnError o doOnComplete
        Flux
                .create(emitter -> {

                    Timer timer = new Timer();

                    timer.schedule(new TimerTask() {

                        private Integer counter = 0;

                        @Override
                        public void run() {

                            emitter.next(counter++);

                            if (counter == 10) {
                                timer.cancel();
                                emitter.complete();
                            }

                            if (counter == 5) {
                                timer.cancel();
                                emitter.error(new InterruptedException("Error perro"));
                            }

                        }

                    }, 1000, 1000);

                })
                .doOnNext(next -> logger.info(next.toString()))
                .doOnError(e -> logger.error(e.getMessage()))
                .doOnComplete(() -> logger.info("observable completado"))
                .subscribe();

    }

    public Usuario crearUsuario() {
        return new Usuario("John", "Doe");
    }

}
