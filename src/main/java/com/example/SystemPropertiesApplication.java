package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@ComponentScan
public class SystemPropertiesApplication{

	public static void main(String[] args) {
		SpringApplication.run(SystemPropertiesApplication.class, args);
	}

}

@RestController
@RefreshScope
@RequestMapping(value = "/keystore")
class KeystoreRestController {
	private final Environment env;

	@Autowired
	public KeystoreRestController(Environment env) {
		this.env = env;
	}


	@RequestMapping("/")
	public String getKeystore(){
		return System.getProperty("javax.net.ssl.keyStore");
	}

}

@Component
class DynamicSystemProperties implements ApplicationListener<EnvironmentChangeEvent>{

	private final Environment env;

	@Autowired
	public DynamicSystemProperties(Environment env) {
		this.env = env;

	}

	@Override
	public void onApplicationEvent(EnvironmentChangeEvent environmentChangeEvent) {

		if(env.containsProperty("system.javax.net.ssl.keyStore")) {
			String keystore = env.getProperty("system.javax.net.ssl.keyStore");
			System.out.println("system.javax.net.ssl.keyStore - " + keystore);

			System.getProperties().setProperty("javax.net.ssl.keyStore", keystore);
		}
	}
}
