package com.eventmgmt.controller;

import com.eventmgmt.entity.Event;
import com.eventmgmt.entity.User;
import com.eventmgmt.service.EventService;
import com.eventmgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/organizer")
public class OrganizerController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User user = getCurrentUser(principal);
        model.addAttribute("events", eventService.findByOrganizer(user.getId()));
        model.addAttribute("user", user);
        return "organizer/dashboard";
    }

    @GetMapping("/events/new")
    public String newEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "organizer/event-form";
    }

    @PostMapping("/events")
    public String createEvent(@ModelAttribute Event event, Principal principal) {
        User user = getCurrentUser(principal);
        event.setOrganizer(user);
        if (event.getAvailableSeats() == 0) {
            event.setAvailableSeats(event.getTotalSeats());
        }
        eventService.save(event);
        return "redirect:/organizer/dashboard";
    }

    @GetMapping("/events/{id}/edit")
    public String editEvent(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findById(id).orElseThrow());
        return "organizer/event-form";
    }

    @PostMapping("/events/{id}")
    public String updateEvent(@PathVariable Long id, @ModelAttribute Event event) {
        event.setId(id);
        eventService.save(event);
        return "redirect:/organizer/dashboard";
    }

    @GetMapping("/events/{id}/delete")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return "redirect:/organizer/dashboard";
    }

    private User getCurrentUser(Principal principal) {
        if (principal instanceof Authentication auth) {
            Object p = auth.getPrincipal();
            if (p instanceof UserDetails ud) {
                return userService.findByEmail(ud.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }
        }
        throw new RuntimeException("Not authenticated");
    }
}
