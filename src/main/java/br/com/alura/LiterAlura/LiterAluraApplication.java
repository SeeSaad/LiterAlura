package br.com.alura.LiterAlura;

import br.com.alura.LiterAlura.main.Principal;
import br.com.alura.LiterAlura.repository.AutorRepository;
import br.com.alura.LiterAlura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	@Autowired
	private LivroRepository r1;
	@Autowired
	private AutorRepository r2;

	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(r1, r2);
		principal.menu();
	}
}
