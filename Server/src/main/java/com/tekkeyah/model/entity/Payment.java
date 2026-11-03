package com.tekkeyah.model.entity;

import com.tekkeyah.pattern.strategy.PaymentStrategy;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Payment extends Observable {

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
        setChanged();
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
            setChanged();
            notifyObservers("Payment " + status);
        }
        return ok;
    }

    public void attach(Observer o) {
        this.addObserver(o);
    }

    public void detach(Observer o) {
        this.deleteObserver(o);
    }

    public void notifyObservers(String msg) {
        setChanged();
        super.notifyObservers(msg);
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
