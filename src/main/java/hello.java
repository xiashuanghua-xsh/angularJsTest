import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class hello {
    @Bean
    public void printHello() {
        System.out.println("hello wordd");
    }
}
