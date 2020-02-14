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

		// los observables son inmutables; cada operador devuelve una nueva instancia de Flux
		// el flujo original se conserva y nunca cambia
		Flux<String> nombres =
			Flux
				.just("Andres Guzman", "Pedro Fulano", "Diego Sultano", "Juan Mengano", "Bruce Lee", "Bruce Willis");

		Flux<Usuario> usuarios =
			nombres
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("Bruce"))
				.doOnNext(usuario -> {

					if (usuario == null) {
						throw new RuntimeException("Usuario debe existir");
					}

					System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));

				})
				.map(usuario -> {
					usuario.setNombre(usuario.getNombre().toLowerCase());
					return usuario;
				});

		usuarios.subscribe(
			usuario -> System.out.println(usuario.toString()),
			err -> logger.error(err.getMessage()),
			() -> System.out.println("observable completado")
		);

	}

}
