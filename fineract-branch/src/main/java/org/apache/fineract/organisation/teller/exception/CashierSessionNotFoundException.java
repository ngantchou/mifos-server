package org.apache.fineract.organisation.teller.exception;

import org.apache.fineract.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;

public class CashierSessionNotFoundException extends AbstractPlatformResourceNotFoundException {
    
    private static final String ERROR_MESSAGE_CODE = "error.msg.cashier.not.found";
    private static final String DEFAULT_ERROR_MESSAGE = "Cashier with identifier {0,number,long} not found!";

    /**
     * Creates a new instance.
     *
     * @param cashierId
     *            the primary key of the cashier
     */
    public CashierSessionNotFoundException(Long cashierId) {
        super(ERROR_MESSAGE_CODE, DEFAULT_ERROR_MESSAGE, cashierId);
    }
    
    public CashierSessionNotFoundException(Long id, EmptyResultDataAccessException e) {
        super("error.msg.staff.id.invalid", "Staff with identifier " + id + " does not exist", id, e);
    }

}
