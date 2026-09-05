package com.eventmgmt.controller;

import com.eventmgmt.entity.Event;
import com.eventmgmt.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private EventService eventService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        var upcoming = eventService.findAll();
        if (upcoming.size() > 6) {
            upcoming = upcoming.subList(0, 6);
        }
        model.addAttribute("upcomingEvents", upcoming);
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "gallery";
    }
}
