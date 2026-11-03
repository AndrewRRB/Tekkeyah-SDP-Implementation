package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Payment;
import com.tekkeyah.repository.PaymentRepository;

public abstract class PaymentCreator {

    protected PaymentRepository paymentRepository;

    public PaymentCreator() {}

    public PaymentCreator(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment checkout() {
        Payment p = createPayment();
        p.process();
        save(p);
        return p;
    }

    public abstract Payment createPayment();

    protected Payment save(Payment p) {
        if (paymentRepository != null) {
            return paymentRepository.save(p);
        }
        return p;
    }

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
