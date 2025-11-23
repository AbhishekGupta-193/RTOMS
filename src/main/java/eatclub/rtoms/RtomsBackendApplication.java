package eatclub.rtoms;
import eatclub.rtoms.Config.DotEnvApplicationInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RtomsBackendApplication {
	public static void main(String[] args) {
//		SpringApplication.run(RtomsBackendApplication.class, args);
		SpringApplication application = new SpringApplication(RtomsBackendApplication.class);
		application.addInitializers(new DotEnvApplicationInitializer());
		application.run(args);
	}

}
