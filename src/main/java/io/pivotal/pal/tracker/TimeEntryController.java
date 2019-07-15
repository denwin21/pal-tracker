package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private final Counter actionCounter;
    private final DistributionSummary timeEntrySummary;

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry registry) {
        this.timeEntryRepository = timeEntryRepository;
        this.actionCounter = registry.counter("timeEntry.actionCounter");
        this.timeEntrySummary = registry.summary("timeEntry.summary");
    }

    @PostMapping()
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        actionCounter.increment();
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        return getTimeEntryResponseEntity(timeEntry);
    }


    @GetMapping()
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        try {

            TimeUnit.SECONDS.sleep(2);
        }
        catch(Exception ex){}
        return new ResponseEntity<List<TimeEntry>>(timeEntryRepository.list(), HttpStatus.OK);
    }


    @PutMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable long timeEntryId, @RequestBody TimeEntry updateEntry) {
        actionCounter.increment();
        TimeEntry timeEntry = timeEntryRepository.update(timeEntryId,updateEntry);
        return getTimeEntryResponseEntity(timeEntry);
    }

    @DeleteMapping("/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<TimeEntry> getTimeEntryResponseEntity(@RequestBody TimeEntry timeEntry) {
        if (timeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
    }
}
