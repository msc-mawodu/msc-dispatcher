package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class StateCacheStore {
    // todo: this will hold, set and fetch application state.

    private Logger logger = LoggerFactory.getLogger(StateCacheStore.class);
    private JdbcTemplate jdbcTemplate;

    public StateCacheStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void changeState(String state) {
        // fixme: implement
    }

    public String currentState() {
        // fixme: implement
        return "running";
    }
}
