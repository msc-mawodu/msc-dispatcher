package msc.dispatcher;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.List;


import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DispatcherConfig.class }, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FilenameCacheStoreTest {

    @Autowired
    FilenameCacheStore cacheStore;

    @Test
    public void givenNewEntrySet_addsAndRetreivesToCache() {
        List<String> filenamesToSave = Arrays.asList("File1", "File2", "File3");

        cacheStore.save(filenamesToSave);
        List<String> retreivedFilenames = cacheStore.fetchAll();
        for(String path : filenamesToSave) {
            assertTrue(retreivedFilenames.contains(path));
        }
    }

    @Test
    public void givenAlreadyExistingEntrySet_DoesntAddDuplicates() {
        List<String> filenamesToSave = Arrays.asList("File1", "File2", "File3");
        List<String> newFilenamesToSave = Arrays.asList("File1", "File2", "File3", "File4", "File5");

        cacheStore.save(filenamesToSave);
        cacheStore.save(newFilenamesToSave);
        List<String> retreivedFilenames = cacheStore.fetchAll();
        for(String path : newFilenamesToSave) {
            assertTrue(retreivedFilenames.contains(path));
        }
    }
}
