package io.pivotal.pal.tracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    HashMap<Long, TimeEntry> timeEntryHashMap = new HashMap<Long, TimeEntry>();
    long maxSize = 1L;

    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(maxSize);
        timeEntryHashMap.put(timeEntry.getId(), timeEntry);
        maxSize += 1L;
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return timeEntryHashMap.get(id);
    }


    public TimeEntry update(long id, TimeEntry timeEntry) {

        if (timeEntryHashMap.get(id)==null){
            return null;
        }
        timeEntryHashMap.put(id, timeEntry);
        timeEntry.setId(id);
        return timeEntry;
    }

    public void delete(long id) {
        timeEntryHashMap.remove(id);
    }

    public List<TimeEntry> list() {
        List<TimeEntry> timeEntries = new ArrayList<TimeEntry>();
        for(long i = 1; i<=timeEntryHashMap.size(); i++){
           timeEntries.add(timeEntryHashMap.get(i));
        }
        return timeEntries;
    }
}
