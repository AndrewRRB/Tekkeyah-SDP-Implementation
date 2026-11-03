package com.tekkeyah.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Depends on Donor from outside the donation classes section (User hierarchy)
// import com.tekkeyah.model.entity.Donor;

@Entity
@Table(name = "tax_documents")
public class TaxDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int documentID;

    // Depends on Donor from outside the donation classes section (User hierarchy)
    // private Donor donor;

    private int taxYear;

    @OneToMany
    @JoinTable(
        name = "tax_document_receipts",
        joinColumns = @JoinColumn(name = "document_id"),
        inverseJoinColumns = @JoinColumn(name = "receipt_id")
    )
    private List<Receipt> receipts = new ArrayList<>();

    private double totalDeductible;

    public TaxDocument() {}

    public TaxDocument(int taxYear) {
        this.taxYear = taxYear;
    }

    public int getDocumentID() {
        return documentID;
    }

    public void setDocumentID(int documentID) {
        this.documentID = documentID;
    }

    // Depends on Donor from outside the donation classes section (User hierarchy)
    // public Donor getDonor() {
    //     return donor;
    // }
    // public void setDonor(Donor donor) {
    //     this.donor = donor;
    // }

    public int getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(int taxYear) {
        this.taxYear = taxYear;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public double getTotalDeductible() {
        return totalDeductible;
    }

    public void setTotalDeductible(double totalDeductible) {
        this.totalDeductible = totalDeductible;
    }

    public void generate() {
        double sum = 0.0;
        if (receipts != null) {
            for (Receipt r : receipts) {
                if (r.getDonation() != null) {
                    sum += r.getDonation().getAmount();
                }
            }
        }
        this.totalDeductible = sum;
        System.out.println("Generated TaxDocument #" + documentID + " for year " + taxYear + " with total deductible $" + totalDeductible);
    }

    public void download() {
        System.out.println("Downloading TaxDocument #" + documentID + " [Year: " + taxYear + ", Total Deductible: $" + totalDeductible + "]");
    }
}
