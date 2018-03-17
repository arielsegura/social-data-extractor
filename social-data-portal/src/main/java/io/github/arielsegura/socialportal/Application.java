package io.github.arielsegura.socialportal;

import io.github.arielsegura.social.redis.connection.SocialRedisUsersConnectionRepository;
import io.github.arielsegura.social.redis.repository.SocialConnectionRepository;
import io.github.arielsegura.socialportal.clients.PersistorClient;
import io.github.arielsegura.socialportal.clients.SearchClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;

@SpringBootApplication
@EnableCaching
@ComponentScan
@EnableAutoConfiguration //(exclude = TwitterAutoConfiguration.class)

public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PersistorClient persistorClient(){
        return PersistorClient.connect();
    }

    @Bean
    public SearchClient searchClient(){
        return SearchClient.connect();
    }

    @Bean
    public UsersConnectionRepository usersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor, SocialConnectionRepository socialConnectionRepository) {
        return new SocialRedisUsersConnectionRepository(connectionFactoryLocator, textEncryptor, socialConnectionRepository);
    }

}
