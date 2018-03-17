package io.github.arielsegura.social.redis.connection;

import io.github.arielsegura.social.redis.repository.SocialConnectionRepository;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;

import java.util.Objects;

public class SocialRedisUsersConnectionRepository extends InMemoryUsersConnectionRepository {

    private SocialConnectionRepository socialConnectionRepository;
    private ConnectionFactoryLocator connectionFactoryLocator;
    private TextEncryptor textEncryptor;
    private ConnectionSignUp connectionSignUp;

    public SocialRedisUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor, SocialConnectionRepository socialConnectionRepository) {
        super(connectionFactoryLocator);
        this.connectionFactoryLocator = Objects.requireNonNull(connectionFactoryLocator, "Connection factory locator should be non-null");
        this.textEncryptor = Objects.requireNonNull(textEncryptor, "TextEncryptor should be non-null");
        this.socialConnectionRepository = Objects.requireNonNull(socialConnectionRepository, "Social Connection repository should be non-null");
    }

    @Override
    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        super.setConnectionSignUp(connectionSignUp);
        this.connectionSignUp = connectionSignUp;
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        return new SocialRedisConnectionRepository(connectionFactoryLocator, textEncryptor, socialConnectionRepository, userId);
    }
}
