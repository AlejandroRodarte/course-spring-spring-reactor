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

		List<String> usuariosList = new ArrayList<>();

		usuariosList.add("Andres Guzman");
		usuariosList.add("Pedro Fulano");
		usuariosList.add("Diego Sultano");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");

		// creando un observable a partir de un objecto que implementa la interfaz Iterable
		Flux<String> nombres = Flux.fromIterable(usuariosList);

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
