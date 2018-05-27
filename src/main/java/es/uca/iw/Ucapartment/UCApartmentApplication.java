package es.uca.iw.Ucapartment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.uca.iw.Ucapartment.security.VaadinSessionSecurityContextHolderStrategy;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.Iva.Iva;
import es.uca.iw.Ucapartment.Iva.IvaRepository;
import es.uca.iw.Ucapartment.Usuario.Rol;
import es.uca.iw.Ucapartment.Usuario.UsuarioService;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class UCApartmentApplication {

	private static final Logger log = LoggerFactory.getLogger(UCApartmentApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(UCApartmentApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(UsuarioService service, IvaRepository ivaRepository) {
		return (args) -> {

			if (service.findAll().size() == 0) {
				// save a couple of users with default password: default
				service.save(new Usuario("Jack", "Bauer","11111111A"));
				service.save(new Usuario("Chloe", "O'Brian","22222222B"));
				service.save(new Usuario("Kim", "Bauer","33333333C"));
				service.save(new Usuario("David", "Palmer","44444444D"));
				service.save(new Usuario("Michelle", "Dessler","55555555E"));

				Usuario root = new Usuario("root", "root","98765432R");
				root.setPassword("rootroot");
				root.setRol(Rol.ADMINISTRADOR);
				service.save(root);

				ivaRepository.save(new Iva("es",21));
				// fetch all users
				log.info("Users found with findAll():");
				log.info("-------------------------------");
				for (Usuario usuario : service.findAll()) {
					log.info(usuario.toString());
				}
				log.info("");

				// fetch an individual user by ID
				Usuario usuario = service.findOne(1L);
				log.info("User found with findOne(1L):");
				log.info("--------------------------------");
				log.info(usuario.toString());
				log.info("");

				// fetch users by last name
				log.info("User found with findByApellidosStartsWithIgnoreCase('Bauer'):");
				log.info("--------------------------------------------");
				for (Usuario bauer : service.findByApellidosStartsWithIgnoreCase("Bauer")) {
					log.info(bauer.toString());
				}
				log.info("");
			}
		};
	}

	@Configuration
	@EnableGlobalMethodSecurity(securedEnabled = true)
	public static class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

		@Autowired
		private UserDetailsService userDetailsService;

		@Bean
		public PasswordEncoder encoder() {
			return new BCryptPasswordEncoder(11);
		}

		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
			authProvider.setUserDetailsService(userDetailsService);
			authProvider.setPasswordEncoder(encoder());
			return authProvider;
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {

			auth.authenticationProvider(authenticationProvider());

			// auth
			// .inMemoryAuthentication()
			// .withUser("admin").password("p").roles("ADMIN", "MANAGER",
			// "USER")
			// .and()
			// .withUser("manager").password("p").roles("MANAGER", "USER")
			// .and()
			// .withUser("user").password("p").roles("USER");
			
		}

		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return authenticationManager();
		}

		static {
			// Use a custom SecurityContextHolderStrategy
			SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
		}
	}
}
