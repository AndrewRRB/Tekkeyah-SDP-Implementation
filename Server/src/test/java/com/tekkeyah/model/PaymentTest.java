package com.tekkeyah.model;

import com.tekkeyah.model.entity.*;
import com.tekkeyah.pattern.factory.DonationPaymentCreator;
import com.tekkeyah.pattern.factory.TicketPaymentCreator;
import com.tekkeyah.pattern.observer.Observer;
import com.tekkeyah.pattern.observer.Subject;
import com.tekkeyah.pattern.strategy.CardPayment;
import com.tekkeyah.pattern.strategy.CashPayment;
import com.tekkeyah.pattern.strategy.CheckPayment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testCardPayment() {
        CardPayment card = new CardPayment("1234");
        assertEquals("1234", card.getCardLastFour());
        assertTrue(card.pay(100.0));
        assertFalse(card.pay(-5.0));
        assertTrue(card.refund(100.0));
        assertFalse(card.refund(0.0));
    }

    @Test
    void testCashPayment() {
        CashPayment cash = new CashPayment("Ahmed");
        assertEquals("Ahmed", cash.getReceivedBy());
        assertTrue(cash.pay(50.0));
        assertTrue(cash.refund(50.0));
    }

    @Test
    void testCheckPayment() {
        CheckPayment check = new CheckPayment("CHK-999", "National Bank");
        assertEquals("CHK-999", check.getCheckNumber());
        assertEquals("National Bank", check.getBankName());
        assertTrue(check.pay(250.0));
        assertTrue(check.refund(250.0));
    }

    @Test
    void testPaymentProcessAndObserverNotification() {
        DonationPayment payment = new DonationPayment();
        payment.setAmount(150.0);
        payment.setMethod(new CardPayment("4321"));

        List<String> messages = new ArrayList<>();
        Observer testObserver = (Subject source, String msg) -> messages.add(msg);
        payment.attach(testObserver);

        boolean processed = payment.process();

        assertTrue(processed);
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(1, messages.size());
        assertEquals("Payment COMPLETED", messages.get(0));

        boolean refunded = payment.refund();
        assertTrue(refunded);
        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
        assertEquals(2, messages.size());
        assertEquals("Payment REFUNDED", messages.get(1));
    }

    @Test
    void testTicketPaymentAndEventCancellation() {
        Ticket ticket = new Ticket("A-12", 75.0);
        TicketPayment ticketPayment = new TicketPayment(ticket, new CashPayment("Cashier 1"));

        assertEquals(75.0, ticketPayment.getAmount());
        assertEquals("A-12", ticketPayment.getTicket().getSeatNumber());

        ticketPayment.process();
        assertEquals(PaymentStatus.COMPLETED, ticketPayment.getStatus());

        // Simulate event cancellation notification
        ticketPayment.update(null, "EVENT_CANCELLED");
        assertEquals(PaymentStatus.REFUNDED, ticketPayment.getStatus());
    }

    @Test
    void testFactoryCreators() {
        DonationPaymentCreator donationCreator = new DonationPaymentCreator();
        Payment donation = donationCreator.createPayment();
        assertInstanceOf(DonationPayment.class, donation);

        TicketPaymentCreator ticketCreator = new TicketPaymentCreator();
        Payment ticketPay = ticketCreator.createPayment();
        assertInstanceOf(TicketPayment.class, ticketPay);
    }
}

