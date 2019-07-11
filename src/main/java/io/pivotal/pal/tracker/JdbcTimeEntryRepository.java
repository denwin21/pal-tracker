package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final JdbcTemplate template;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public TimeEntry create(TimeEntry timeEntry) {
        String sql = "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, RETURN_GENERATED_KEYS);

                    ps.setLong(1, timeEntry.getProjectId());
                    ps.setLong(2, timeEntry.getUserId());
                    ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                    ps.setInt(4, timeEntry.getHours());

                    return ps;
                }, keyHolder);

        timeEntry.setId(keyHolder.getKey().longValue());
        return timeEntry;
    }

    public TimeEntry find(long timeEntryId) {
        String sql = "SELECT * FROM time_entries WHERE id = ?";
        try {
            Map<String, Object> foundEntry = template.queryForMap(sql, timeEntryId);
            return new TimeEntry((long)foundEntry.get("id"), (long) foundEntry.get("project_id"),(long) foundEntry.get("user_id"),LocalDate.parse(foundEntry.get("date").toString()),(int)foundEntry.get("hours"));
        }
        catch(EmptyResultDataAccessException ex){
            return null;
        }

    }

    public List<TimeEntry> list() {
        String sql = "SELECT * FROM time_entries";
        try {
            List<TimeEntry> timeEntries = template.query(sql, new TimeEntryMapper());
            return timeEntries;
        }
        catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        String sql = "UPDATE time_entries SET project_id=?, user_id=?, date=?, hours=? WHERE id=?";

        template.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql);

                    ps.setLong(1, timeEntry.getProjectId());
                    ps.setLong(2, timeEntry.getUserId());
                    ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                    ps.setInt(4, timeEntry.getHours());
                    ps.setLong(5, id);

                    return ps;
                });

        timeEntry.setId(id);
        return timeEntry;
    }

    public void delete(long timeEntryId) {
        String sql = "DELETE FROM time_entries WHERE id=?";

        template.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql);

                    ps.setLong(1, timeEntryId);

                    return ps;
                });
    }
}
