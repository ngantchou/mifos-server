package org.apache.fineract.organisation.teller.domain;

import com.google.common.base.Splitter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;

@Entity
@Table(name = "m_cashier_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CashierSession extends AbstractPersistableCustom<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_id", nullable = false)
    private Cashier cashier;

    @Column(name = "opening_amount", nullable = false)
    private BigDecimal openingAmount;

    @Column(name = "closing_amount", nullable = true)
    private BigDecimal closingAmount;

    @Column(name = "reconciliation_data", nullable = true, length = 500)
    private String reconciliationData;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDate endDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = true)
    private LocalTime endTime;

    @Column(name = "notes", nullable = true, length = 500)
    private String notes;

    @Column(name = "status", nullable = false)
    private Boolean status;

    public static CashierSession fromJson(final Cashier cashier, final JsonCommand command) {
        final BigDecimal openingAmount = command.bigDecimalValueOfParameterNamed("openingAmount");
        final BigDecimal closingAmount = command.bigDecimalValueOfParameterNamed("closingAmount");
        final LocalDate startDate = command.localDateValueOfParameterNamed("startDate");
        final String notes = command.stringValueOfParameterNamed("notes");

        return new CashierSession()
                .setCashier(cashier)
                .setOpeningAmount(openingAmount)
                .setClosingAmount(closingAmount)
                .setStartDate(startDate)
                .setNotes(notes);
    }

    public Map<String, Object> update(final JsonCommand command) {
        final Map<String, Object> actualChanges = new LinkedHashMap<>(4);

        final BigDecimal closingAmount = command.bigDecimalValueOfParameterNamed("closingAmount");
        if (closingAmount != null && !closingAmount.equals(this.closingAmount)) {
            actualChanges.put("closingAmount", closingAmount);
            this.closingAmount = closingAmount;
        }

        final String reconciliationDataParamName = "reconciliationData";
        if (command.isChangeInStringParameterNamed(reconciliationDataParamName, this.reconciliationData)) {
            final String newValue = command.stringValueOfParameterNamed(reconciliationDataParamName);
            actualChanges.put(reconciliationDataParamName, newValue);
            this.reconciliationData = newValue;
        }

        final String notesParamName = "notes";
        if (command.isChangeInStringParameterNamed(notesParamName, this.notes)) {
            final String newValue = command.stringValueOfParameterNamed(notesParamName);
            actualChanges.put(notesParamName, newValue);
            this.notes = newValue;
        }

        return actualChanges;
    }
}
