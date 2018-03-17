package io.github.arielsegura.socialpersistor;

import io.github.arielsegura.socialpersistor.domain.TopicResult;
import io.github.arielsegura.socialpersistor.repositories.TopicResultRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        Application.class
})
public class TopicResultRepositoryTest {

    @Autowired
    TopicResultRepository topicResultRepository;

    @Test(expected = InvalidDataAccessApiUsageException.class) // can't retrieve with a single key
    public void simpleSaveTest() throws Exception {
        String id = UUID.randomUUID() + "";
        TopicResult topicResult = new TopicResult(id, "my-text", new Date(), "my-search", "myuserid");
        topicResultRepository.save(topicResult);
        TopicResult one = topicResultRepository.findById(id).get();
        assertEquals(topicResult, one);
    }

    @Test
    public void searchByMainSearchAndUser() throws Exception {
        String id = UUID.randomUUID() + "";
        String mainSearch = "my-search";
        String myuserid = "myuserid";
        TopicResult topicResult = new TopicResult(id, "my-text", new Date(), mainSearch, myuserid);
        topicResultRepository.save(topicResult);
        TopicResult one = topicResultRepository.findAllByMainSearchEqualsAndSearchUserIdEquals(mainSearch, myuserid).get(0);
        assertEquals(topicResult, one);
    }

    @Test
    public void saveTwice() throws Exception {
        String id = UUID.randomUUID() + "";
        String mainSearch = "my-search";
        String myuserid = "myuserid";
        TopicResult topicResult = new TopicResult(id, "my-text", new Date(), mainSearch, myuserid);
        topicResultRepository.save(topicResult);
        topicResultRepository.save(topicResult);
    }
}
