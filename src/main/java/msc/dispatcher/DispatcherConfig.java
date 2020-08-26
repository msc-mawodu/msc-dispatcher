package msc.dispatcher;

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
    StateCacheStore stateStore(JdbcTemplate jdbcTemplate) {
        return new StateCacheStore(jdbcTemplate);
    }


    @Bean
    Profiler profiler() {
        return new Profiler();
    }

    @Bean
    DispatcherClient dispatcherClient() {
        return new DispatcherClient();
    }

    @Bean
    FileWatcherExecutor fileWatcherExecutor() {
        return new FileWatcherExecutor();
    }

    @Bean
    @Autowired
    public ProfilerExecutor profilerExecutor(Profiler profiler, ProfilerCacheStore dataCache) {
        return new ProfilerExecutor(profiler, dataCache);
    }

    @Bean
    @Autowired
    public DispatcherExecutor dispatcherExecutor(ProfilerCacheStore cacheStore, DispatcherClient client) {
        return new DispatcherExecutor(cacheStore, client);
    }

    @Bean
    ApplicationCronJobs cronJobs(ProfilerExecutor profilerExecutor, DispatcherExecutor dispatcherExecutor, FileWatcherExecutor fileWatcherExecutor) {
        return new ApplicationCronJobs(profilerExecutor, dispatcherExecutor, fileWatcherExecutor);
    }
}
