package msc.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static junit.framework.TestCase.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DispatcherConfig.class }, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProfilerStoreTest {

    @Autowired
    ProfilerStore profilerStore;

    @Test
    public void shouldHandleInsertionAndDeletionCorrectly() throws IOException {
        String dataFromFile = getStubData();
        profilerStore.save(dataFromFile);
        String dataFromDb = profilerStore.fetchAllProfilerEntries();
        assertEquals(dataFromFile, dataFromDb);
    }

    @Test
    public void givenExistingEntries_marksAsRetreived() {
        // fixme: implement
    }

    private String getStubData() {
        InputStream metrics = getClass().getResourceAsStream("/stub-metrics.txt");

        Scanner scanner = new Scanner(metrics);
        StringBuffer buffer = new StringBuffer();
        while(scanner.hasNext()){
            buffer.append(scanner.nextLine());
        }
        return buffer.toString();
    }
}

