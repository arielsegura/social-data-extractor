package io.github.arielsegura.socialpersistor;

import io.github.arielsegura.socialpersistor.repositories.SearchTopicResultRepository;
import io.github.arielsegura.socialpersistor.repositories.TopicResultRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableCaching
@ComponentScan
@EnableAutoConfiguration
@EnableCassandraRepositories(basePackageClasses = TopicResultRepository.class)
@EnableElasticsearchRepositories(basePackageClasses = SearchTopicResultRepository.class)
public class Application extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name:searches}")
    String keyspace;

    @Value("${spring.data.cassandra.schema-action:CREATE_IF_NOT_EXISTS}")
    SchemaAction schemaAction;

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false"); //https://github.com/netty/netty/issues/6956
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        List<CreateKeyspaceSpecification> createKeyspaceSpecifications = new ArrayList<>();
        createKeyspaceSpecifications.add(getKeySpaceSpecification());
        return createKeyspaceSpecifications;
    }

    // Below method creates "my_keyspace" if it doesnt exist.
    private CreateKeyspaceSpecification getKeySpaceSpecification() {
        CreateKeyspaceSpecification createKeyspaceSpecification = CreateKeyspaceSpecification.createKeyspace(this.keyspace);
        createKeyspaceSpecification.ifNotExists(true).withSimpleReplication(3);
        return createKeyspaceSpecification;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return schemaAction;
    }

    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }
}
