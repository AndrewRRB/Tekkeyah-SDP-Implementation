package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Payment;
import com.tekkeyah.model.entity.TicketPayment;
import com.tekkeyah.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketPaymentCreator extends PaymentCreator {

    public TicketPaymentCreator() {
        super();
    }

    @Autowired
    public TicketPaymentCreator(PaymentRepository paymentRepository) {
        super(paymentRepository);
    }

    @Override
    public Payment createPayment() {
        return new TicketPayment();
    }
}
