package msc.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Transactional
public class ProfilerStore {

    private Logger logger = LoggerFactory.getLogger(ProfilerStore.class);
    private JdbcTemplate jdbcTemplate;

    public ProfilerStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String profilerData) {
        jdbcTemplate.update("INSERT INTO profiling(metrics) VALUES (?)",
                profilerData
        );
    }

    public String fetchAllProfilerEntries() {
        StringBuilder profilerEntries = new StringBuilder();
        try {
            Connection jdbc = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement psSelect = jdbc.prepareStatement("SELECT * from profiling where dispatched=0");
            ResultSet rows = psSelect.executeQuery();
            while (rows.next()) {
                profilerEntries.append(rows.getString("metrics"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // handle...
        }

        return profilerEntries.toString();
    }
}
