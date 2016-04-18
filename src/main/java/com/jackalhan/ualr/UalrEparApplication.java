package com.jackalhan.ualr;


import com.jackalhan.ualr.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
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
			if (activeProfiles.contains(Constants.PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.PROFILE_PRODUCTION)) {
				log.error("You have misconfigured your application! " +
						"It should not run with both the 'dev' and 'prod' profiles at the same time.");
			}
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(UalrEparApplication.class, args);
	}

	/**
	 * If no profile has been configured, set by default the "dev" profile.
	 */
	private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
		if (!source.containsProperty("spring.profiles.active") &&
				!System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {

			app.setAdditionalProfiles(Constants.PROFILE_DEVELOPMENT);
		}
	}

}
