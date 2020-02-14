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

		// map: transforma los datos de un flujo en otro tipo
		Flux<Usuario> nombres =
			Flux
				.just("Andres", "Pedro", "Diego", "Juan")
				.map(nombre -> new Usuario(nombre, null))
				.doOnNext(usuario -> {

					if (usuario == null) {
						throw new RuntimeException("Usuario debe existir");
					}

					System.out.println(usuario.getNombre());

				})
				.map(usuario -> {
					usuario.setNombre(usuario.getNombre().toLowerCase());
					return usuario;
				});

		nombres.subscribe(
			usuario -> System.out.println(usuario.toString()),
			err -> logger.error(err.getMessage()),
			() -> System.out.println("observable completado")
		);

	}

}
