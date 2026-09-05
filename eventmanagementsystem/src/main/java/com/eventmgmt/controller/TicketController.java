package com.eventmgmt.controller;

import com.eventmgmt.entity.Ticket;
import com.eventmgmt.entity.User;
import com.eventmgmt.service.EventService;
import com.eventmgmt.service.QrCodeService;
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
@RequestMapping("/book")
public class TicketController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private UserService userService;

    @GetMapping("/{eventId}")
    public String showBookingPage(@PathVariable Long eventId, Model model) {
        model.addAttribute("event", eventService.findById(eventId).orElseThrow());
        return "ticket-booking";
    }

    @PostMapping("/{eventId}")
    public String bookTicket(@PathVariable Long eventId,
                            @RequestParam(defaultValue = "UPI") String paymentMethod,
                            Principal principal,
                            Model model) {
        try {
            User user = getCurrentUser(principal);
            var event = eventService.findById(eventId).orElseThrow();
            Ticket ticket = registrationService.bookTicket(user, event, paymentMethod);
            String qr = qrCodeService.generateQrCodeDataUri(ticket.getQrCodeData());

            model.addAttribute("ticket", ticket);
            model.addAttribute("event", event);
            model.addAttribute("qrCode", qr);
            model.addAttribute("user", user);
            return "ticket-confirmation";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/events/" + eventId + "?error=" + e.getMessage();
        }
    }

    @GetMapping("/my-tickets")
    public String myTickets(Model model, Principal principal) {
        User user = getCurrentUser(principal);
        model.addAttribute("tickets", registrationService.findTicketsByUser(user));
        return "participant/my-tickets";
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
