package com.jackalhan.ualr;


import com.jackalhan.ualr.constant.GenericConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;


@SpringBootApplication
@EnableScheduling
public class UalrEparApplication {

	private static final Logger log = LoggerFactory.getLogger(UalrEparApplication.class);

	@Inject
	private Environment env;

	@PostConstruct
	public void initApplication() {
		if (env.getActiveProfiles().length == 0) {
			log.warn("No Spring profile configured, running with default configuration");
		} else {
			log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
			Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
			if (activeProfiles.contains(GenericConstant.PROFILE_DEVELOPMENT) && activeProfiles.contains(GenericConstant.PROFILE_PRODUCTION)) {
				log.error("You have misconfigured your application! " +
						"It should not run with both the 'dev' and 'prod' profiles at the same time.");
			}
		}
	}

	/**
	 * Main method, used to run the application.
	 *
	 * @param args the command line arguments
	 * @throws UnknownHostException if the local host name could not be resolved into an address
	 */
	public static void main(String[] args) throws UnknownHostException {
		SpringApplication app = new SpringApplication(UalrEparApplication.class);
		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
		addDefaultProfile(app, source);
		Environment env = app.run(args).getEnvironment();
		log.info("\n----------------------------------------------------------\n\t" +
						"Application '{}' is running! Access URLs:\n\t" +
						"Local: \t\thttp://127.0.0.1:{}\n\t" +
						"External: \thttp://{}:{}\n----------------------------------------------------------",
				env.getProperty("spring.application.name"),
				env.getProperty("server.port"),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"));

	}
/*

	public static void main(String[] args) {
		SpringApplication.run(UalrEparApplication.class, args);
	}
*/

	/**
	 * If no profile has been configured, set by default the "dev" profile.
	 */
	private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
		if (!source.containsProperty("spring.profiles.active") &&
				!System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {

			app.setAdditionalProfiles(GenericConstant.PROFILE_DEVELOPMENT);
		}
	}


	// Accept the Locale from Headers e.g
	// @RequestHeader("Accept-Language") Locale locale

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US); // Set default Locale as US
		return slr;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("i18n/messages");  // name of the resource bundle
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}


	//SFTP
/*
	@Bean
	public DefaultSftpSessionFactory sftpSessionFactory() {
		DefaultSftpSessionFactory defaultSftpSessionFactory= new DefaultSftpSessionFactory();
		defaultSftpSessionFactory.setHost(env.getProperty("sftp.host"));
		defaultSftpSessionFactory.setUser(env.getProperty("sftp.username"));
		defaultSftpSessionFactory.setPassword(env.getProperty("sftp.passphrase"));
		defaultSftpSessionFactory.setPrivateKey(new ClassPathResource(env.getProperty("sftp.private.keyfile")));
		defaultSftpSessionFactory.setPrivateKeyPassphrase(env.getProperty("sftp.passphrase"));
		defaultSftpSessionFactory.setPort(Integer.parseInt(env.getProperty("sftp.port")));
		return defaultSftpSessionFactory;
	}
*/

	//In order to display custom error message please uncomment it.

/*	@Bean(name = "validator")
	public LocalValidatorFactoryBean beanValidation() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource());
		return localValidatorFactoryBean;
	}

	@Bean
	public Validator getValidator()
	{
		return beanValidation();
	}*/

}
