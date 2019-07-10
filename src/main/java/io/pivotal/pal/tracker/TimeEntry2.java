package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.Objects;

public class TimeEntry2 {

    private long id;

    public long getProjectId() {
        return projectId;
    }

    private long projectId;

    public long getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public int getHours() {
        return hours;
    }

    private long userId;
    private String date;
    private int hours;

    public TimeEntry2(long projectId, long userId, String date, int hours) {
        this.id = 1;
        this.projectId = projectId;
        this.userId = userId;
        this.date = date;
        this.hours = hours;
    }

    public TimeEntry2(TimeEntry timeEntry) {
        this.id = timeEntry.getId();
        this.projectId = timeEntry.getProjectId();
        this.userId = timeEntry.getUserId();
        this.date = timeEntry.getDate().toString();
        this.hours = timeEntry.getHours();
    }


    public TimeEntry2() {

    }


    public Long getId() {
        return this.id;
    }



    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TimeEntry2 timeEntry = (TimeEntry2) o;
        return id == timeEntry.id &&
                projectId == timeEntry.projectId &&
                userId == timeEntry.userId &&
                hours == timeEntry.hours &&
                Objects.equals(date, timeEntry.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId, userId, date, hours);
    }
}
