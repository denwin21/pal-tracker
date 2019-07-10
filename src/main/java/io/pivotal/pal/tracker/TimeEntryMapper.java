package io.pivotal.pal.tracker;

import org.springframework.web.bind.annotation.Mapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeEntryMapper {
    public TimeEntry map(TimeEntry2 timeEntry2){
        String [] dateArray= timeEntry2.getDate().split("-");
        if(Integer.parseInt(dateArray[1])>12) {
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
           String text = timeEntry2.getDate();
           LocalDate parsedDate = LocalDate.parse(text, formatter);
           return new TimeEntry(timeEntry2.getId(), timeEntry2.getProjectId(), timeEntry2.getUserId(), parsedDate, timeEntry2.getHours());
       } else return new TimeEntry(timeEntry2.getId(), timeEntry2.getProjectId(),timeEntry2.getUserId(), LocalDate.parse(timeEntry2.getDate()), timeEntry2.getHours());
    }
}
