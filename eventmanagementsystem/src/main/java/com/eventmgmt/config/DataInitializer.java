package com.eventmgmt.config;

import com.eventmgmt.entity.Event;
import com.eventmgmt.entity.User;
import com.eventmgmt.service.EventService;
import com.eventmgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * Seeds the database with demo data on first startup so the app is
 * immediately usable without manual data entry.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserService userService, EventService eventService) {
        return args -> {
            // Only seed if no users exist
            if (userService.count() > 0) return;

            // --- Users ---
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@eventhub.com");
            admin.setPassword("admin123");
            admin.setPhone("9876543210");
            admin.setRole(User.Role.ADMIN);
            userService.registerUser(admin);

            User organizer = new User();
            organizer.setName("Raj EventWorks");
            organizer.setEmail("organizer@eventhub.com");
            organizer.setPassword("admin123");
            organizer.setPhone("9876500001");
            organizer.setRole(User.Role.ORGANIZER);
            userService.registerUser(organizer);

            User participant = new User();
            participant.setName("Test User");
            participant.setEmail("user@eventhub.com");
            participant.setPassword("admin123");
            participant.setPhone("9876500002");
            participant.setRole(User.Role.PARTICIPANT);
            userService.registerUser(participant);

            // --- Events ---
            createEvent(eventService, organizer, "Sunset Music Festival 2026",
                    "An evening of live performances by top indie bands and DJs. Food stalls and fun activities included.",
                    "MUSIC", "Phoenix Marketcity, Bengaluru", LocalDateTime.of(2026, 10, 15, 18, 0),
                    999, 500);

            createEvent(eventService, organizer, "TechFest 2026 - College Tech Symposium",
                    "A two-day inter-college technical fest featuring hackathons, robotics, paper presentations and coding contests.",
                    "COLLEGE", "IIT Campus, Powai, Mumbai", LocalDateTime.of(2026, 9, 25, 9, 0),
                    499, 300);

            createEvent(eventService, organizer, "Inter-University Cricket Championship",
                    "Eight university teams compete for the trophy. Two days of thrilling T20 cricket action.",
                    "SPORTS", "Chinnaswamy Stadium, Bengaluru", LocalDateTime.of(2026, 11, 5, 10, 0),
                    299, 1000);

            createEvent(eventService, organizer, "AI & ML National Conference 2026",
                    "Leading researchers and industry experts discuss the future of AI, ML and data science. Networking lunch included.",
                    "CONFERENCE", "Hyatt Regency, New Delhi", LocalDateTime.of(2026, 10, 10, 9, 30),
                    2499, 200);

            createEvent(eventService, organizer, "Full-Stack Web Development Workshop",
                    "Hands-on workshop on building modern web apps with Spring Boot and React. Bring your laptop.",
                    "WORKSHOP", "TIE Hub, Hyderabad", LocalDateTime.of(2026, 9, 20, 10, 0),
                    799, 50);

            createEvent(eventService, organizer, "Classical Night - Saraswati Puja Special",
                    "An enchanting evening of Indian classical music featuring sitar and tabla maestros.",
                    "MUSIC", "Rabindra Sadan, Kolkata", LocalDateTime.of(2026, 10, 5, 18, 30),
                    599, 400);

            createEvent(eventService, organizer, "Startup Bootcamp & Pitch Day",
                    "Learn from founders, refine your pitch, and present to investors. Certificate of participation included.",
                    "WORKSHOP", "WeWork, Koramangala, Bengaluru", LocalDateTime.of(2026, 11, 12, 9, 0),
                    1499, 80);
        };
    }

    private void createEvent(EventService eventService, User organizer, String title,
                             String desc, String type, String venue,
                             LocalDateTime date, double price, int seats) {
        Event e = new Event();
        e.setTitle(title);
        e.setDescription(desc);
        e.setEventType(type);
        e.setVenue(venue);
        e.setEventDate(date);
        e.setTicketPrice(price);
        e.setTotalSeats(seats);
        e.setAvailableSeats(seats);
        e.setOrganizer(organizer);
        eventService.save(e);
    }
}
