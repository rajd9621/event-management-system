package com.eventmgmt.controller;

import com.eventmgmt.entity.Registration;
import com.eventmgmt.entity.Ticket;
import com.eventmgmt.entity.User;
import com.eventmgmt.service.EventService;
import com.eventmgmt.service.RegistrationService;
import com.eventmgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/participant")
public class ParticipantController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User user = getCurrentUser(principal);
        model.addAttribute("registrations", registrationService.findByUser(user));
        model.addAttribute("tickets", registrationService.findTicketsByUser(user));
        model.addAttribute("user", user);
        return "participant/dashboard";
    }

    @GetMapping("/registrations/{id}/cancel")
    public String cancelRegistration(@PathVariable Long id) {
        registrationService.cancel(id);
        return "redirect:/participant/dashboard";
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
