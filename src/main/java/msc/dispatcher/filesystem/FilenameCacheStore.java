package msc.dispatcher.filesystem;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class FilenameCacheStore {

    private Logger logger = LoggerFactory.getLogger(FilenameCacheStore.class);
    private JdbcTemplate jdbcTemplate;

//    t_name=filename id/path/timeUpdated

    public FilenameCacheStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void save(List<String> filenamesToSave) {

        List<String> existingFiles = fetchAll();
        List<String> newFilenamesToSave = filenamesToSave.stream().filter(f -> !existingFiles.contains(f)).collect(Collectors.toList());

        jdbcTemplate.batchUpdate("INSERT INTO filename (path, timeUpdated) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {

                String filename = newFilenamesToSave.get(i);
                ps.setString(1, filename);
                ps.setDate(2, Date.valueOf(LocalDate.now()));
            }

            @Override
            public int getBatchSize() {
                return newFilenamesToSave.size();
            }
        });

    }

    public List<String> fetchAll() {
        List<String> filenames = new ArrayList<>();
        try {
            Connection jdbc = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement psSelect = jdbc.prepareStatement("SELECT * from filename");
            ResultSet rows = psSelect.executeQuery();
            while (rows.next()) {
                String path = rows.getString("path");
                filenames.add(path);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // handle...
        }

        return filenames;
    }

    public List<String> fetchAllUndispatched() {
        List<String> filenames = new ArrayList<>();
        try {
            Connection jdbc = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement psSelect = jdbc.prepareStatement("SELECT * from filename WHERE dispatched=0");
            ResultSet rows = psSelect.executeQuery();
            while (rows.next()) {
                String path = rows.getString("path");
                filenames.add(path);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // handle...
        }

        return filenames;
    }

    public void markAsDispatched(List<String> dispatchedFiles) {

        List<String> existingFiles = fetchAll();
        List<String> dispatchedFilesChecked = dispatchedFiles.stream().filter(f -> existingFiles.contains(f)).collect(Collectors.toList());

        jdbcTemplate.batchUpdate("UPDATE filename set dispatched=? WHERE path=?", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {

                String filename = dispatchedFilesChecked.get(i);
                ps.setString(2, filename);
                ps.setInt(1, 1);
            }

            @Override
            public int getBatchSize() {
                return dispatchedFilesChecked.size();
            }
        });
    }
}
