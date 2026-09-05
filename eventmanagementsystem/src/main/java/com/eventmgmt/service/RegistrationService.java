package com.eventmgmt.service;

import com.eventmgmt.entity.*;
import com.eventmgmt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Registration register(User user, Event event) {
        if (registrationRepository.existsByUserIdAndEventId(user.getId(), event.getId())) {
            throw new RuntimeException("Already registered for this event");
        }
        if (event.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        Registration reg = new Registration();
        reg.setUser(user);
        reg.setEvent(event);
        reg.setStatus(Registration.Status.REGISTERED);
        registrationRepository.save(reg);

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);

        return reg;
    }

    public List<Registration> findByUser(User user) {
        return registrationRepository.findByUserId(user.getId());
    }

    public List<Registration> findByEvent(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }

    public void markAttended(Long registrationId) {
        Registration reg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        reg.setStatus(Registration.Status.ATTENDED);
        registrationRepository.save(reg);
    }

    public void cancel(Long registrationId) {
        Registration reg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        reg.setStatus(Registration.Status.CANCELLED);
        registrationRepository.save(reg);

        Event event = reg.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventRepository.save(event);
    }

    // ===== Ticket / Payment =====

    public Ticket bookTicket(User user, Event event, String paymentMethod) {
        if (event.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available for this event");
        }

        Ticket ticket = new Ticket();
        ticket.setTicketCode("TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setSeatNumber("S-" + (event.getTotalSeats() - event.getAvailableSeats() + 1));
        ticket.setQrCodeData("EVENT:" + event.getId() + "|USER:" + user.getId()
                + "|TICKET:" + ticket.getTicketCode());
        ticketRepository.save(ticket);

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);

        Payment payment = new Payment();
        payment.setTicket(ticket);
        payment.setAmount(event.getTicketPrice());
        payment.setPaymentMethod(paymentMethod != null ? paymentMethod : "UPI");
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        payment.setStatus(Payment.Status.SUCCESS);
        paymentRepository.save(payment);

        return ticket;
    }

    public List<Ticket> findTicketsByUser(User user) {
        return ticketRepository.findByUserId(user.getId());
    }

    public List<Ticket> findTicketsByEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    public Optional<Ticket> findTicketByCode(String code) {
        return ticketRepository.findByTicketCode(code);
    }

    public void checkIn(Long ticketId) {
        Ticket t = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        t.setCheckedIn(true);
        ticketRepository.save(t);
    }

    // ===== Feedback =====

    public Feedback addFeedback(User user, Event event, int rating, String comment) {
        Feedback fb = new Feedback();
        fb.setUser(user);
        fb.setEvent(event);
        fb.setRating(rating);
        fb.setComment(comment);
        return feedbackRepository.save(fb);
    }

    public List<Feedback> findFeedbackByEvent(Long eventId) {
        return feedbackRepository.findByEventId(eventId);
    }
}
