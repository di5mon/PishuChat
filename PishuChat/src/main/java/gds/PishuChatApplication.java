package gds;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { 
		DataSourceAutoConfiguration.class //отключили базу данных
})
public class PishuChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(PishuChatApplication.class, args);
	}

}
