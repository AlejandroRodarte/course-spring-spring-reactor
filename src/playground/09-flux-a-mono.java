package com.rodarte.springreactor;

import com.rodarte.springreactor.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringReactorApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpringReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<Usuario> usuariosList = new ArrayList<>();

        usuariosList.add(new Usuario("Andres", "Guzman"));
        usuariosList.add(new Usuario("Pedro", "Fulano"));
        usuariosList.add(new Usuario("Diego", "Sultano"));
        usuariosList.add(new Usuario("Bruce", "Lee"));
        usuariosList.add(new Usuario("Bruce", "Willis"));

        // convirtiendo un Flux a un Mono (de List)
        // collect list: transforma un Flux<T> a un Mono<List<T>>
        Flux
                .fromIterable(usuariosList)
                .collectList()
                .subscribe(System.out::println);

    }

}
