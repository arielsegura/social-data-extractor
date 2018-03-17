package io.github.arielsegura.social.redis;

import io.github.arielsegura.social.redis.connection.SocialRedisUsersConnectionRepository;
import io.github.arielsegura.social.redis.repository.SocialConnectionRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.social.TwitterAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import java.util.Optional;

@Configuration
@AutoConfigureBefore(TwitterAutoConfiguration.class)
@ImportAutoConfiguration(RedisAutoConfiguration.class)
public class SocialRedisSessionAutoConfiguration {

    @Configuration
    @EnableSocial
    @EnableRedisRepositories
    @ConditionalOnWebApplication
    protected static class CustomTwitterConfigurerAdapter extends SocialConfigurerAdapter {

        @Bean
        public TextEncryptor textEncryptor(){
            return Encryptors.noOpText();
        }

        @Bean
        public UsersConnectionRepository usersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor, SocialConnectionRepository socialConnectionRepository) {
            return new SocialRedisUsersConnectionRepository(connectionFactoryLocator, textEncryptor, socialConnectionRepository);
        }

        @Bean
        @Primary
        @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
        public ConnectionRepository connectionRepository(UsersConnectionRepository usersConnectionRepository) {
            return usersConnectionRepository.createConnectionRepository(Optional.ofNullable(super.getUserIdSource()).map(userIdSource -> userIdSource.getUserId()).orElse("userid")); // TODO fixme doesn't work with many users
        }

        @Bean
        public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate redisTemplate = new RedisTemplate<byte[], byte[]>();

            redisTemplate.setConnectionFactory(redisConnectionFactory);

            return redisTemplate;
        }

    }

}
