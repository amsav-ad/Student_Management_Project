package SMP.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import SMP.dto.CalendarEventRequest;
import SMP.dto.CalendarEventResponse;
import SMP.service.CalendarEventService;

@RestController
@RequestMapping("/api/calendar")
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @PostMapping
    public CalendarEventResponse createEvent(
            @RequestBody CalendarEventRequest request,
            Authentication authentication) {

        return calendarEventService.createEvent(
                request, authentication.getName());
    }

    @GetMapping
    public List<CalendarEventResponse> getAllEvents() {

        return calendarEventService.getAllEvents();
    }

    @GetMapping("/range")
    public List<CalendarEventResponse> getEventsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return calendarEventService.getEventsBetweenDates(
                startDate, endDate);
    }

    @GetMapping("/type/{eventType}")
    public List<CalendarEventResponse> getEventsByType(
            @PathVariable String eventType) {

        return calendarEventService.getEventsByType(eventType);
    }

    @PutMapping("/{id}")
    public CalendarEventResponse updateEvent(
            @PathVariable Long id,
            @RequestBody CalendarEventRequest request,
            Authentication authentication) {

        return calendarEventService.updateEvent(
                id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(
            @PathVariable Long id,
            Authentication authentication) {

        calendarEventService.deleteEvent(
                id, authentication.getName());
    }
}