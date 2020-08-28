package msc.dispatcher;

import msc.dispatcher.filesystem.FileDispatcherExecutor;
import msc.dispatcher.filesystem.FileWatcherExecutor;
import msc.dispatcher.filesystem.FilenameCacheStore;
import msc.dispatcher.filesystem.FilesystemClient;
import msc.dispatcher.profiler.*;
import msc.dispatcher.state.StateClient;
import msc.dispatcher.state.StateReporterExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableTransactionManagement
@EnableScheduling
public class DispatcherConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(4);
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }

    @Autowired
    Environment env;

    @Bean public PlatformTransactionManager txManager() { return new DataSourceTransactionManager(dataSource()); }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    ProfilerCacheStore profilerStore(JdbcTemplate jdbcTemplate) {
        return new ProfilerCacheStore(jdbcTemplate);
    }

    @Bean
    FilenameCacheStore filenameCacheStore(JdbcTemplate jdbcTemplate) {
        return new FilenameCacheStore(jdbcTemplate);
    }

    @Bean
    Profiler profiler() {
        return new Profiler();
    }

    @Bean
    ProfilerClient dispatcherClient() {
        return new ProfilerClient();
    }

    @Bean
    StateClient stateClient() {
        return new StateClient();
    }

    @Bean
    FilesystemClient client() {
        return new FilesystemClient();
    }

    @Bean
    FileWatcherExecutor fileWatcherExecutor(FilenameCacheStore filenameCacheStore) {
        return new FileWatcherExecutor(filenameCacheStore);
    }

    @Bean
    @Autowired
    public ProfilerGathererExecutor profilerExecutor(Profiler profiler, ProfilerCacheStore dataCache) {
        return new ProfilerGathererExecutor(profiler, dataCache);
    }

    @Bean
    @Autowired
    public ProfilerDispatcherExecutor dispatcherExecutor(ProfilerCacheStore cacheStore, ProfilerClient client) {
        return new ProfilerDispatcherExecutor(cacheStore, client);
    }

    @Bean
    @Autowired
    public StateReporterExecutor stateReporterExecutor(StateClient client) {
        return new StateReporterExecutor(client);
    }

    @Bean
    @Autowired
    public FileDispatcherExecutor fileDispatcherExecutor(FilenameCacheStore filenameCacheStore, FilesystemClient client) {
        return new FileDispatcherExecutor(filenameCacheStore, client);
    }

    @Bean
    Scheduler scheduler(ProfilerGathererExecutor profilerGathererExecutor, ProfilerDispatcherExecutor dispatcherExecutor, FileWatcherExecutor fileWatcherExecutor, StateReporterExecutor stateReporterExecutor, FileDispatcherExecutor fileDispatcherExecutor) {
        return new Scheduler(profilerGathererExecutor, dispatcherExecutor, fileWatcherExecutor, stateReporterExecutor, fileDispatcherExecutor);
    }
}
