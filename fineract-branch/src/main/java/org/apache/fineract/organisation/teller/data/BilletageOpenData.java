package org.apache.fineract.organisation.teller.data;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents the billetage record for a specific currency denomination.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public final class BilletageOpenData implements Serializable{

    @Schema(description = "Denomination of the currency", example = "100.00")
    private Double denomination;

    @Schema(description = "Quantity of the denomination", example = "5")
    private Integer quantity;
    // Difference between the cashier and teller counts
}

