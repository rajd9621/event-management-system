package com.eventmgmt.repository;

import com.eventmgmt.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventTypeIgnoreCase(String eventType);
    List<Event> findByOrderByEventDateAsc();
    List<Event> findByOrganizerId(Long organizerId);
}
