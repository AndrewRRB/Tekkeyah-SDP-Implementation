package com.tekkeyah.model.entity;

import com.tekkeyah.pattern.observer.Observer;
import com.tekkeyah.pattern.observer.Subject;
import com.tekkeyah.pattern.strategy.PaymentStrategy;
import jakarta.persistence.*;

@Entity
@Table(name = "ticket_payments")
public class TicketPayment extends Payment implements Observer {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public TicketPayment() {
        super();
    }

    public TicketPayment(Ticket ticket, PaymentStrategy method) {
        super();
        this.method = method;
        setTicket(ticket);
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        if (ticket != null) {
            this.amount = ticket.getPrice();
        }
    }

    @Override
    public void update(Subject source, String msg) {
        // refund when the event is cancelled
        if ("EVENT_CANCELLED".equals(msg) || (msg != null && msg.contains("CANCELLED"))) {
            refund();
        }
    }
}
