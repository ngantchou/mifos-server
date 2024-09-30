package org.apache.fineract.organisation.teller.domain;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.portfolio.savings.data.SavingsAccountData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "m_billetage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Billetage extends AbstractPersistableCustom<Long>{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teller_id", nullable = false)
    private Teller teller;

    // Link to Cashier Session (for opening/closing cashier sessions)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_session_id", nullable = true)
    private CashierSession cashierSession;

    // Link to Cashier Transaction (for cashier transactions)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_transaction_id", nullable = true)
    private CashierTransaction cashierTransaction;

    // Link to Savings Account Transaction (for withdrawals and deposits)
    /**
     * Represents a many-to-one relationship with the SavingsAccountTransaction entity.
     * This field is lazily fetched and can be nullable.
     * 
     * @see SavingsAccountTransaction
     */
    @Column(name = "savings_transaction_id", nullable = true)
    private String savingsTransactionRef;

    @Column(name = "denomination_id", nullable = false)
    private Integer denominationId;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "teller_count")
    private Integer tellerCount;

    @Column(name = "cashier_count")
    private Integer cashierCount;

    @Column(name = "difference")
    private Integer difference;

    @Column(name = "status", nullable = false)
    private Integer status;
    
    public static List<Billetage> fromJson(JsonCommand command, Teller teller, CashierSession cashierSession, 
    CashierTransaction cashierTransaction, String savingsTransaction) {
        // Extract general fields
        final Integer status = 1;

        // Initialize list to hold billetage objects
        List<Billetage> billetages = new ArrayList<>();

        // Extract billetage array from JSON
        if (command.parameterExists("billetage")) {
            JsonArray billetageList = command.arrayOfParameterNamed("billetage");

            // Iterate over the billetage array and create Billetage objects
            for (JsonElement billetageEntry : billetageList) {
                JsonObject json = billetageEntry.getAsJsonObject();
                // Extract denomination and count from each billetage entry
                final BigDecimal denomination = json.get("denomination").getAsBigDecimal();
                final Integer count = json.get("count").getAsInt();
                final Integer tellerCount = json.get("tellerCount").getAsInt();
                final Integer cashierCount = json.get("cashierCount").getAsInt();
                final Integer difference = json.get("difference").getAsInt();
            
                // Create and add each Billetage object
                Billetage billetage = new Billetage()
                        .setTeller(teller)
                        .setDenominationId(denomination.intValue()) // Assuming denomination is stored as an Integer
                        .setCount(count)
                        .setTellerCount(tellerCount)   // Set as null or adjust based on logic
                        .setCashierCount(cashierCount)  // Set as null or adjust based on logic
                        .setDifference(difference)    // Set as null or calculate difference as needed
                        .setCashierSession(cashierSession)
                        .setCashierTransaction(cashierTransaction)
                        .setSavingsTransactionRef(savingsTransaction)
                        .setStatus(status);

                billetages.add(billetage);
            }
        }

        return billetages;
    }

    public void update(JsonCommand command) {
        if (command.isChangeInIntegerParameterNamed("denominationId", this.denominationId)) {
            this.denominationId = command.integerValueOfParameterNamed("denominationId");
        }
        if (command.isChangeInIntegerParameterNamed("count", this.count)) {
            this.count = command.integerValueOfParameterNamed("count");
        }
        if (command.isChangeInIntegerParameterNamed("tellerCount", this.tellerCount)) {
            this.tellerCount = command.integerValueOfParameterNamed("tellerCount");
        }
        if (command.isChangeInIntegerParameterNamed("cashierCount", this.cashierCount)) {
            this.cashierCount = command.integerValueOfParameterNamed("cashierCount");
        }
        if (command.isChangeInIntegerParameterNamed("difference", this.difference)) {
            this.difference = command.integerValueOfParameterNamed("difference");
        }
        if (command.isChangeInIntegerParameterNamed("status", this.status)) {
            this.status = command.integerValueOfParameterNamed("status");
        }
    }
}
