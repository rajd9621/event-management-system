package com.eventmgmt.service;

import com.eventmgmt.entity.Event;
import com.eventmgmt.entity.User;
import com.eventmgmt.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> findAll() {
        return eventRepository.findByOrderByEventDateAsc();
    }

    public List<Event> findByType(String type) {
        return eventRepository.findByEventTypeIgnoreCase(type);
    }

    public List<Event> findByOrganizer(Long organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Event save(Event event) {
        if (event.getAvailableSeats() == 0 && event.getTotalSeats() > 0) {
            event.setAvailableSeats(event.getTotalSeats());
        }
        return eventRepository.save(event);
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    public Event update(Event event) {
        return eventRepository.save(event);
    }

    public void decrementAvailableSeats(Event event) {
        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);
    }

    public long count() {
        return eventRepository.count();
    }
}
