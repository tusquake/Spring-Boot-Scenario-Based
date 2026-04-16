package com.interview.debug.scenario136;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/scenario136")
@RequiredArgsConstructor
public class IndexingController {

    private final IndexingService indexingService;
    private final UserActivityRepository userActivityRepository;

    @PostMapping("/populate")
    public String populate(@RequestParam(defaultValue = "50000") int count) {
        indexingService.populateData(count);
        return "Populated " + count + " records successfully.";
    }

    @GetMapping("/search")
    public IndexingService.SearchResult search(@RequestParam String trackingId) {
        return indexingService.searchByTrackingId(trackingId);
    }

    @GetMapping("/find")
    public List<UserActivity> find() {
        return userActivityRepository.findAll();
    }
}
