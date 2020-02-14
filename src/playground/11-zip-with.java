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

        Mono<Usuario> usuarioMono = Mono.fromCallable(this::crearUsuario);

        Mono<Comentarios> comentariosMono = Mono.fromCallable(() -> {

            Comentarios comentarios = new Comentarios();

            comentarios.addComentario("Hola pepe");
            comentarios.addComentario("Voy a la playa");
            comentarios.addComentario("Tomo un curso");

            return comentarios;

        });

        // zipWith (primera forma): pasamos el segundo observable; el callback (BiFunction) recibe los resultados
        // emitidos por tanto el observable fuente como el anidado, pudiendo retornar una nueva instancia de un objeto
        // combinado
        Mono<UsuarioComentarios> usuarioComentariosMono =
                usuarioMono
                        .zipWith(comentariosMono, (usuario, comentarios) -> new UsuarioComentarios(usuario, comentarios));

        // zipWith (segunda forma): solo pasar el segundo observable; el siguiente operador trabajara con un tuple
        // con los resultados de los dos observables dentro de el; se puede usar map para personalizar la salida del
        // flujo
        Mono<UsuarioComentarios> usuarioComentariosMono1 =
                usuarioMono
                        .zipWith(comentariosMono)
                        .map(tuple -> {

                            Usuario usuario = tuple.getT1();
                            Comentarios comentarios = tuple.getT2();

                            return new UsuarioComentarios(usuario, comentarios);

                        });

        usuarioComentariosMono.subscribe(System.out::println);
        usuarioComentariosMono1.subscribe(System.out::println);

    }

    public Usuario crearUsuario() {
        return new Usuario("John", "Doe");
    }

}
