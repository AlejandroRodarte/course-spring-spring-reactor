package com.rodarte.springreactor;

import com.rodarte.springreactor.models.Usuario;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
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

        // manejando contrapresion (forma 1): implementando Suscriber
        Flux
                .range(1, 10)
                .log()
                .subscribe(new Subscriber<Integer>() {

                    private Subscription s;
                    private final Integer LIMIT = 2;
                    private Integer consumido = 0;

                    // onSuscribe: nos permite ejecutar un codigo al momento de la suscripcion
                    // podemos pedirle un lote de datos por medio de request()
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.s = subscription;
                        s.request(this.LIMIT);
                    }

                    // next handler: usamos el contador `consumido` para ir pidiendo lotes de 2 en dos
                    // por medio de request()
                    @Override
                    public void onNext(Integer integer) {

                        logger.info(integer.toString());

                        this.consumido++;

                        if (this.consumido == this.LIMIT) {
                            this.consumido = 0;
                            s.request(this.LIMIT);
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }

                });

        // segunda forma: usando operador limitRate()
        Flux
                .range(1, 10)
                .log()
                .limitRate(2)
                .subscribe(i -> logger.info(i.toString()));

    }

    public Usuario crearUsuario() {
        return new Usuario("John", "Doe");
    }

}
