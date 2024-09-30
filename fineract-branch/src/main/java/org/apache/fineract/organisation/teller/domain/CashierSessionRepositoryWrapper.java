package org.apache.fineract.organisation.teller.domain;

import org.apache.fineract.organisation.teller.exception.CashierSessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CashierSessionRepositoryWrapper {

    private final CashierSessionRepository repository;

    @Autowired
    public CashierSessionRepositoryWrapper(final CashierSessionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public CashierSession findOneWithNotFoundDetection(final Long id) {
        return this.repository.findById(id).orElseThrow(() -> new CashierSessionNotFoundException(id));
    }

    public CashierSession save(final CashierSession cashier) {
        return this.repository.save(cashier);
    }

    public CashierSession saveAndFlush(final CashierSession cashier) {
        return this.repository.saveAndFlush(cashier);
    }

    public void delete(final CashierSession cashier) {
        this.repository.delete(cashier);
    }
}