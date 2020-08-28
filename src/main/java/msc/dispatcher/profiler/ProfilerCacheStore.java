package msc.dispatcher.profiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Transactional
public class ProfilerCacheStore {

    private Logger logger = LoggerFactory.getLogger(ProfilerCacheStore.class);
    private JdbcTemplate jdbcTemplate;

    public ProfilerCacheStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String profilerData) {
        logger.info("Saving profiling data batch.");
        jdbcTemplate.update("INSERT INTO profiling(metrics) VALUES (?)",
                profilerData
        );
    }

    public String fetchAllProfilerEntries() {
        StringBuilder profilerEntries = new StringBuilder();
        List<Integer> idsToMarkAsRead = new ArrayList<>();
        try {
            Connection jdbc = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement psSelect = jdbc.prepareStatement("SELECT * from profiling");
            ResultSet rows = psSelect.executeQuery();
            while (rows.next()) {
                profilerEntries.append(rows.getString("metrics"));
                int id = rows.getInt("id");
                logger.info(String.format("Fetched entry from db: %s", id));
                idsToMarkAsRead.add(id);
            }

            deleteDispatchedEntries(idsToMarkAsRead);

        } catch (SQLException e) {
            e.printStackTrace();
            // handle...
        }

        return profilerEntries.toString();
    }

    private void deleteDispatchedEntries(List<Integer> idsToDelete) {
        for(Integer id : idsToDelete) {
            logger.info(String.format("Deleting entry from db: %s", id));

            jdbcTemplate.update("DELETE FROM profiling WHERE id=?", id);
        }
    }
}
