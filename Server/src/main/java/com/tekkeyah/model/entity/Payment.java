package com.tekkeyah.model.entity;

import com.tekkeyah.pattern.observer.Observer;
import com.tekkeyah.pattern.observer.Subject;
import com.tekkeyah.pattern.strategy.PaymentStrategy;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Payment implements Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    protected int paymentID;

    @Column(name = "amount", nullable = false)
    protected double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    protected PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "timestamp")
    protected LocalDateTime timestamp;

    @Transient
    protected PaymentStrategy method;

    @Transient
    private List<Observer> observers = new ArrayList<>();

    public Payment() {
        this.timestamp = LocalDateTime.now();
    }

    public Payment(double amount, PaymentStrategy method) {
        this();
        this.amount = amount;
        this.method = method;
    }

    public boolean process() {
        if (method == null) {
            throw new IllegalStateException("Payment method strategy not set");
        }
        boolean ok = method.pay(amount);
        status = ok ? PaymentStatus.COMPLETED : PaymentStatus.FAILED;
        notifyObservers("Payment " + status);
        return ok;
    }

    public boolean refund() {
        if (method == null) {
            throw new IllegalStateException("Payment method strategy not set");
        }
        boolean ok = method.refund(amount);
        if (ok) {
            status = PaymentStatus.REFUNDED;
            notifyObservers("Payment " + status);
        }
        return ok;
    }

    @Override
    public void attach(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String msg) {
        for (Observer o : new ArrayList<>(observers)) {
            o.update(this, msg);
        }
    }

    // Getters and Setters
    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public PaymentStrategy getMethod() {
        return method;
    }

    public void setMethod(PaymentStrategy method) {
        this.method = method;
    }
}
