package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.DonationPayment;
import com.tekkeyah.model.entity.Payment;
import com.tekkeyah.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DonationPaymentCreator extends PaymentCreator {

    public DonationPaymentCreator() {
        super();
    }

    @Autowired
    public DonationPaymentCreator(PaymentRepository paymentRepository) {
        super(paymentRepository);
    }

    @Override
    public Payment createPayment() {
        return new DonationPayment();
    }
}
