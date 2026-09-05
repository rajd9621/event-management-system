package com.eventmgmt.controller;

import com.eventmgmt.entity.Event;
import com.eventmgmt.entity.Feedback;
import com.eventmgmt.entity.User;
import com.eventmgmt.service.EventService;
import com.eventmgmt.service.RegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationService registrationService;

    @GetMapping
    public String listEvents(@RequestParam(value = "type", required = false) String type, Model model) {
        if (type != null && !type.isEmpty()) {
            model.addAttribute("events", eventService.findByType(type.toUpperCase()));
            model.addAttribute("activeType", type);
        } else {
            model.addAttribute("events", eventService.findAll());
        }
        return "events";
    }

    @GetMapping("/category/{type}")
    public String eventsByCategory(@PathVariable String type, Model model) {
        model.addAttribute("events", eventService.findByType(type.toUpperCase()));
        model.addAttribute("activeType", type.toUpperCase());
        model.addAttribute("categoryTitle", prettyCategory(type));
        return "events";
    }

    @GetMapping("/{id}")
    public String eventDetails(@PathVariable Long id, Model model, Principal principal) {
        Event event = eventService.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);
        List<Feedback> feedbacks = registrationService.findFeedbackByEvent(id);
        model.addAttribute("feedbacks", feedbacks);
        double avg = feedbacks.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
        model.addAttribute("avgRating", Math.round(avg * 10.0) / 10.0);
        model.addAttribute("qrCode", null);
        return "event-details";
    }

    @PostMapping("/{id}/register")
    public String registerForEvent(@PathVariable Long id, Principal principal, Model model) {
        try {
            User user = getCurrentUser(principal);
            Event event = eventService.findById(id).orElseThrow();
            registrationService.register(user, event);
            return "redirect:/events/" + id + "?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/events/" + id + "?error=" + e.getMessage();
        }
    }

    @PostMapping("/{id}/feedback")
    public String submitFeedback(@PathVariable Long id,
                                 @RequestParam int rating,
                                 @RequestParam String comment,
                                 Principal principal) {
        User user = getCurrentUser(principal);
        Event event = eventService.findById(id).orElseThrow();
        registrationService.addFeedback(user, event, rating, comment);
        return "redirect:/events/" + id;
    }

    @GetMapping("/music")
    public String musicEvents(Model model) {
        model.addAttribute("events", eventService.findByType("MUSIC"));
        model.addAttribute("categoryTitle", "Music Events");
        return "events";
    }

    @GetMapping("/college")
    public String collegeEvents(Model model) {
        model.addAttribute("events", eventService.findByType("COLLEGE"));
        model.addAttribute("categoryTitle", "College Events");
        return "events";
    }

    @GetMapping("/sports")
    public String sportsEvents(Model model) {
        model.addAttribute("events", eventService.findByType("SPORTS"));
        model.addAttribute("categoryTitle", "Sports Events");
        return "events";
    }

    @GetMapping("/conferences")
    public String conferences(Model model) {
        model.addAttribute("events", eventService.findByType("CONFERENCE"));
        model.addAttribute("categoryTitle", "Conferences");
        return "events";
    }

    @GetMapping("/workshops")
    public String workshops(Model model) {
        model.addAttribute("events", eventService.findByType("WORKSHOP"));
        model.addAttribute("categoryTitle", "Workshops");
        return "events";
    }

    private User getCurrentUser(Principal principal) {
        if (principal instanceof Authentication auth) {
            Object principalObj = auth.getPrincipal();
            if (principalObj instanceof org.springframework.security.core.userdetails.UserDetails ud) {
                return userServiceFindByEmail(ud.getUsername());
            }
        }
        throw new RuntimeException("Not authenticated");
    }

    @Autowired
    private com.eventmgmt.service.UserService userService;

    private User userServiceFindByEmail(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private String prettyCategory(String type) {
        return switch (type.toUpperCase()) {
            case "MUSIC" -> "Music Events";
            case "COLLEGE" -> "College Events";
            case "SPORTS" -> "Sports Events";
            case "CONFERENCE" -> "Conferences";
            case "WORKSHOP" -> "Workshops";
            default -> "Events";
        };
    }
}
