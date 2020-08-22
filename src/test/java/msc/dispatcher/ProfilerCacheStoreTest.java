package msc.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import java.io.InputStream;
import java.util.Scanner;

import static junit.framework.TestCase.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DispatcherConfig.class }, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProfilerCacheStoreTest {

    @Autowired
    ProfilerCacheStore profilerCacheStore;

    @Test
    public void givenEntryToSave_canSaveItFetchItAndDeleteAfterFetching() {
        String dataFromFile = getStubData();
        profilerCacheStore.save(dataFromFile);
        String dataFromDb = profilerCacheStore.fetchAllProfilerEntries();
        assertEquals(dataFromFile, dataFromDb);
        // NB after fetching entries should be automatically deleted.
        String emptyData = profilerCacheStore.fetchAllProfilerEntries();
        assertEquals("", emptyData);
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

