package com.rodarte.springreactor;

import com.rodarte.springreactor.models.Comentarios;
import com.rodarte.springreactor.models.Usuario;
import com.rodarte.springreactor.models.UsuarioComentarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringReactorApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpringReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // observable de usuario
        Mono<Usuario> usuarioMono = Mono.fromCallable(this::crearUsuario);

        // observable de comentarios
        Mono<Comentarios> comentariosMono = Mono.fromCallable(() -> {

            Comentarios comentarios = new Comentarios();

            comentarios.addComentario("Hola pepe");
            comentarios.addComentario("Voy a la playa");
            comentarios.addComentario("Tomo un curso");

            return comentarios;

        });

        // se usa flatMap para transformar el Mono<Usuario> en un Mono<UsuarioComentarios>
        // la idea es retornar Mono<Comentarios> en flatMap pero aplica en el un map para crear una nueva instancia
        // de UsuarioComentarios
        usuarioMono
                .flatMap(usuario -> comentariosMono.map(comentarios -> new UsuarioComentarios(usuario, comentarios)))
                .subscribe(System.out::println);

    }

    public Usuario crearUsuario() {
        return new Usuario("John", "Doe");
    }

}
