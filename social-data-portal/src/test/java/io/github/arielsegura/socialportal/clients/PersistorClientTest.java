package io.github.arielsegura.socialportal.clients;

import io.github.arielsegura.socialextractor.model.RestTopicResult;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class PersistorClientTest {

    PersistorClient persistorClient = PersistorClient.connect();

    @Test
    public void store() throws Exception {
    }

    @Test
    @Ignore
    public void getTopicResults() throws Exception {
        List<RestTopicResult> topicResults =
                persistorClient.getTopicResults(0, 50, "my-seach", UUID.randomUUID().toString());
        assertTrue(topicResults.isEmpty());
    }

}
