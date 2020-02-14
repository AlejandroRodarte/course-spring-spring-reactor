package com.rodarte.springreactor;

import com.rodarte.springreactor.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

		// flatMap: operador que recibe una funcion que retorna un Observable
		// las emisiones de ese Observable interno de aplanan para que todas esten al mismo nivel
		// Mono.just: Observable para un solo elemento
		// Mono.empty: Observable vacio
		Flux
			.fromIterable(usuariosList)
			.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
			.flatMap(usuario -> {

				if (usuario.getNombre().equalsIgnoreCase("bruce")) {
					return Mono.just(usuario);
				} else {
					return Mono.empty();
				}

			})
			.map(usuario -> {
				usuario.setNombre(usuario.getNombre().toLowerCase());
				return usuario;
			})
			.subscribe(usuario -> logger.info(usuario.toString()));

	}

}
