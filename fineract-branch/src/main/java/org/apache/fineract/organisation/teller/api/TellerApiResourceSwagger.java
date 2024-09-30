/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.organisation.teller.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import org.apache.fineract.infrastructure.core.service.Page;
import org.apache.fineract.organisation.monetary.data.CurrencyData;
import org.apache.fineract.organisation.staff.data.StaffData;
import org.apache.fineract.organisation.teller.data.BilletageCloseData;
import org.apache.fineract.organisation.teller.data.BilletageOpenData;
import org.apache.fineract.organisation.teller.data.CashierData;
import org.apache.fineract.organisation.teller.data.CashierTransactionData;
import org.apache.fineract.organisation.teller.domain.Billetage;
import org.apache.fineract.organisation.teller.domain.CashierTxnType;
import org.apache.fineract.organisation.teller.domain.TellerStatus;

/**
 * Created by sanyam on 20/8/17.
 */
final class TellerApiResourceSwagger {

    private TellerApiResourceSwagger() {

    }

    @Schema(description = "GetTellersResponse")
    public static final class GetTellersResponse {

        private GetTellersResponse() {

        }

        @Schema(example = "3")
        public Long id;
        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "0")
        public Long debitAccountId;
        @Schema(example = "0")
        public Long creditAccountId;
        @Schema(example = "Teller3")
        public String name;
        @Schema(example = "[2015,2,1]")
        public LocalDate startDate;
        @Schema(example = "ACTIVE")
        public TellerStatus status;
        @Schema(example = "Head Office")
        public String officeName;
    }

    @Schema(description = "PostTellersRequest")
    public static final class PostTellersRequest {

        private PostTellersRequest() {

        }

        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "Teller3")
        public String name;
        @Schema(example = "cash handling")
        public String description;
        @Schema(example = "ACTIVE")
        public TellerStatus status;
        @Schema(example = "en")
        public String locale;
        @Schema(example = "dd-MM-yyyy")
        public String dateFormat;
        @Schema(example = "01 February 2015")
        public LocalDate startDate;

    }

    @Schema(description = "PostTellersResponse")
    public static final class PostTellersResponse {

        private PostTellersResponse() {

        }

        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "5")
        public Long resourceId;
    }

    @Schema(description = "PutTellersRequest")
    public static final class PutTellersRequest {

        private PutTellersRequest() {

        }

        @Schema(example = "Teller3")
        public String name;
        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "teller cash handling")
        public String description;
        @Schema(example = "ACTIVE")
        public TellerStatus status;
        @Schema(example = "28 February 2015")
        public LocalDate endDate;
        @Schema(example = "01 February 2015")
        public LocalDate startDate;
        @Schema(example = "en")
        public String locale;
        @Schema(example = "dd-MM-yyyy")
        public String dateFormat;

    }

    @Schema(description = "PutTellersResponse")
    public static final class PutTellersResponse {

        private PutTellersResponse() {

        }

        static final class PutTellersResponseChanges {

            private PutTellersResponseChanges() {}

            @Schema(example = "teller cash handling")
            public String description;
            @Schema(example = "28 February 2015")
            public LocalDate endDate;
            @Schema(example = "01 February 2015")
            public LocalDate startDate;
            @Schema(example = "en")
            public String locale;
            @Schema(example = "dd-MM-yyyy")
            public String dateFormat;
        }

        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "5")
        public Long resourceId;
        public PutTellersResponseChanges changes;

    }

    @Schema(description = "GetTellersTellerIdCashiersResponse")
    public static final class GetTellersTellerIdCashiersResponse {

        private GetTellersTellerIdCashiersResponse() {

        }

        @Schema(example = "1")
        public Long tellerId;
        @Schema(example = "Teller1")
        public String tellerName;
        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "Head Office")
        public String officeName;
        public Collection<CashierData> cashiers;

    }

    @Schema(description = "PostTellersTellerIdCashiersRequest")
    public static final class PostTellersTellerIdCashiersRequest {

        private PostTellersTellerIdCashiersRequest() {

        }

        @Schema(example = "28 February 2015")
        public LocalDate endDate;
        @Schema(example = "teller cash handling")
        public String description;
        @Schema(example = "true")
        public Boolean isFullDay;
        @Schema(example = "3")
        public Long staffId;
        @Schema(example = "en")
        public String locale;
        @Schema(example = "dd-MM-yyyy")
        public String dateFormat;
        @Schema(example = "01 February 2015")
        public LocalDate startDate;

    }

    @Schema(description = "PostTellersTellerIdCashiersResponse")
    public static final class PostTellersTellerIdCashiersResponse {

        private PostTellersTellerIdCashiersResponse() {

        }

        @Schema(example = "1")
        public Long resourceId;
        @Schema(example = "2")
        public Long subResourceId;

    }

    @Schema(description = "GetTellersTellerIdCashiersCashierIdResponse")
    public static final class GetTellersTellerIdCashiersCashierIdResponse {

        private GetTellersTellerIdCashiersCashierIdResponse() {

        }

        @Schema(example = "1")
        public Long id;
        @Schema(example = "1")
        public Long tellerId;
        @Schema(example = "1")
        public Long staffId;
        @Schema(example = "")
        public String description;
        @Schema(example = "Feb 20, 2015 12:00:00 AM")
        public LocalDate startDate;
        @Schema(example = "Feb 27, 2015 12:00:00 AM")
        public LocalDate endDate;
        @Schema(example = "true")
        public Boolean isFullDay;
        @Schema(example = "")
        public String startTime;
        @Schema(example = "")
        public String endTime;
        @Schema(example = "Teller1")
        public String tellerName;
        @Schema(example = "Staff1, Test")
        public String staffName;

    }

    @Schema(description = "PutTellersTellerIdCashiersCashierIdRequest")
    public static final class PutTellersTellerIdCashiersCashierIdRequest {

        private PutTellersTellerIdCashiersCashierIdRequest() {

        }

        @Schema(example = "25 February 2015")
        public LocalDate endDate;
        @Schema(example = "Cashier updated.")
        public String description;
        @Schema(example = "true")
        public Boolean isFullDay;
        @Schema(example = "1")
        public Long staffId;
        @Schema(example = "en")
        public String locale;
        @Schema(example = "dd-MM-yyyy")
        public String dateFormat;
        @Schema(example = "01 February 2015")
        public LocalDate startDate;

    }

    @Schema(description = "PutTellersTellerIdCashiersCashierIdResponse")
    public static final class PutTellersTellerIdCashiersCashierIdResponse {

        private PutTellersTellerIdCashiersCashierIdResponse() {

        }

        static final class PutTellersTellerIdCashiersCashierIdResponseChanges {

            private PutTellersTellerIdCashiersCashierIdResponseChanges() {}

            @Schema(example = "25 February 2015")
            public LocalDate endDate;
            @Schema(example = "Cashier updated.")
            public String description;
            @Schema(example = "en")
            public String locale;
            @Schema(example = "dd-MM-yyyy")
            public String dateFormat;
        }

        @Schema(example = "1")
        public Long resourceId;
        @Schema(example = "2")
        public Long subResourceId;
        public PutTellersTellerIdCashiersCashierIdResponseChanges changes;
    }

    @Schema(description = "DeleteTellersTellerIdCashiersCashierIdResponse")
    public static final class DeleteTellersTellerIdCashiersCashierIdResponse {

        private DeleteTellersTellerIdCashiersCashierIdResponse() {

        }

        @Schema(example = "1")
        public Long resourceId;
    }

    @Schema(description = "GetTellersTellerIdCashiersTemplateResponse")
    public static final class GetTellersTellerIdCashiersTemplateResponse {

        private GetTellersTellerIdCashiersTemplateResponse() {

        }

        @Schema(example = "1")
        public Long tellerId;
        @Schema(example = "Teller1")
        public String tellerName;
        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "Head Office")
        public String officeName;
        public Collection<StaffData> staffOptions;
    }

    @Schema(description = "GetTellersTellerIdCashiersCashiersIdTransactionsResponse")
    public static final class GetTellersTellerIdCashiersCashiersIdTransactionsResponse {

        private GetTellersTellerIdCashiersCashiersIdTransactionsResponse() {

        }

        @Schema(example = "8")
        public Long id;
        @Schema(example = "15")
        public Long cashierId;
        public CashierTxnType txnType;
        @Schema(example = "1000")
        public BigDecimal txnAmount;
        @Schema(example = "[2015, 02, 25]")
        public LocalDate txnDate;
        @Schema(example = "2")
        public Long entityId;
        @Schema(example = "loans")
        public String entityType;
        @Schema(example = "Disbursement, Loan:2-000000002,Client:1-Test 1")
        public String txnNote;
        @Schema(example = "[2015, 02, 25, 12, 00, 00]")
        public LocalDateTime createdDate;
        @Schema(example = "1")
        public Long officeId;
        @Schema(example = "Head Office")
        public String officeName;
        @Schema(example = "0")
        public Long tellerId;
        @Schema(example = "B, Ramesh")
        public String cashierName;
    }

    @Schema(description = "PostTellersTellerIdCashiersCashierIdAllocateRequest")
    public static final class PostTellersTellerIdCashiersCashierIdAllocateRequest {

        private PostTellersTellerIdCashiersCashierIdAllocateRequest() {

        }

        @Schema(example = "USD")
        public String currencyCode;
        @Schema(example = "5000")
        public BigDecimal txnAmount;
        @Schema(example = "allocating cash")
        public String txnNote;
        @Schema(example = "en")
        public String locale;
        @Schema(example = "dd-MM-yyyy")
        public String dateFormat;
        @Schema(example = "[2015, 02, 01]")
        public LocalDate txnDate;
    }

    @Schema(description = "PostTellersTellerIdCashiersCashierIdAllocateResponse")
    public static final class PostTellersTellerIdCashiersCashierIdAllocateResponse {

        private PostTellersTellerIdCashiersCashierIdAllocateResponse() {

        }

        @Schema(example = "1")
        public Long resourceId;
        @Schema(example = "4")
        public Long subResourceId;
    }

    @Schema(description = "PostTellersTellerIdCashiersCashierIdSettleRequest")
    public static final class PostTellersTellerIdCashiersCashierIdSettleRequest {

        private PostTellersTellerIdCashiersCashierIdSettleRequest() {

        }

        @Schema(example = "USD")
        public String currencyCode;
        @Schema(example = "2000")
        public BigDecimal txnAmount;
        @Schema(example = "cash settlement")
        public String txnNote;
        @Schema(example = "en")
        public String locale;
        @Schema(example = "dd-MM-yyyy")
        public String dateFormat;
        @Schema(example = "[2015, 02, 20]")
        public LocalDate txnDate;

    }

    @Schema(description = "PostTellersTellerIdCashiersCashierIdSettleResponse")
    public static final class PostTellersTellerIdCashiersCashierIdSettleResponse {

        private PostTellersTellerIdCashiersCashierIdSettleResponse() {

        }

        @Schema(example = "1")
        public Long resourceId;
        @Schema(example = "5")
        public Long subResourceId;
    }

    @Schema(description = "GetTellersTellerIdCashiersCashiersIdSummaryAndTransactionsResponse")
    public static final class GetTellersTellerIdCashiersCashiersIdSummaryAndTransactionsResponse {

        private GetTellersTellerIdCashiersCashiersIdSummaryAndTransactionsResponse() {

        }

        @Schema(example = "7000.000000")
        public BigDecimal sumCashAllocation;
        @Schema(example = "0")
        public BigDecimal sumInwardCash;
        @Schema(example = "0")
        public BigDecimal sumOutwardCash;
        @Schema(example = "50.000000")
        public BigDecimal sumCashSettlement;
        @Schema(example = "6950.000000")
        public BigDecimal netCash;
        @Schema(example = "Head Office")
        public String officeName;
        @Schema(example = "1")
        public long tellerId;
        @Schema(example = "Teller1")
        public String tellerName;
        @Schema(example = "1")
        public long cashierId;
        @Schema(example = "Staff1, Test")
        public String cashierName;
        public Page<CashierTransactionData> cashierTransactions;

    }

    @Schema(description = "GetTellersTellerIdCashiersCashiersIdTransactionsTemplateResponse")
    public static final class GetTellersTellerIdCashiersCashiersIdTransactionsTemplateResponse {

        private GetTellersTellerIdCashiersCashiersIdTransactionsTemplateResponse() {

        }

        @Schema(example = "1")
        public Long cashierId;
        @Schema(example = "Head Office")
        public String officeName;
        @Schema(example = "0")
        public Long tellerId;
        @Schema(example = "Teller1")
        public String tellerName;
        @Schema(example = "Staff1, Test")
        public String cashierName;
        public CashierData cashierData;
        @Schema(example = "Feb 20, 2015 12:00:00 AM")
        public LocalDate startDate;
        @Schema(example = "Feb 27, 2015 12:00:00 AM")
        public LocalDate endDate;
        public Collection<CurrencyData> currencyOptions;
    }

    @Schema(description = "PostTellersTellerIdCashiersCashierIdOpenRequest")
    public static final class PostTellersTellerIdCashiersCashierIdOpenRequest {

        private PostTellersTellerIdCashiersCashierIdOpenRequest() {

        }
        public  class BilletageOpenDataRequest {
            
            private BilletageOpenDataRequest(){}

            @Schema(description = "Denomination of the currency", example = "100.00")
            private Double denomination;

            @Schema(description = "Quantity of the denomination", example = "5")
            private Integer quantity;
            // Difference between the cashier and teller counts
        }

        @Schema(description = "Opening amount", example = "1000")
        public Double openingAmount;
        //@Schema(description = "Billetage details", example = "[{'denomination': 100, 'quantity': 10}]")
        public Set<BilletageOpenDataRequest> billetage;
        @Schema(description = "Additional notes", example = "Opening cashier session for daily operations")
        public String notes;
        @Schema(example = "Feb 20, 2015 12:00:00 AM")
        public LocalDate startDate;
        @Schema(example = "12:00:00 AM")
        public String startTime;
    }

    // Response for opening a cashier session
    @Schema(description = "OpenCashierSessionResponse")
    public static class OpenCashierSessionResponse {

        private OpenCashierSessionResponse() {

        }
        @Schema(description = "Cashier ID", example = "123")
        public Long cashierId;

        @Schema(description = "Opening amount", example = "1000")
        public Double openingAmount;

        @Schema(description = "Status of the session opening", example = "Cashier session opened successfully")
        public String status;

        // Getters and setters
    }

    @Schema(description = "PostTellersTellerIdCashiersCashierIdCloseRequest")
    public static final class PostTellersTellerIdCashiersCashierIdCloseRequest {

        private PostTellersTellerIdCashiersCashierIdCloseRequest() {

        }

        public  class BilletageCloseDataRequest {
            
            private BilletageCloseDataRequest(){}

            @Schema(description = "Denomination of the currency", example = "100.00")
            private Double denomination;

            @Schema(description = "Quantity of the denomination", example = "5")
            private Integer quantity;
            // Difference between the cashier and teller counts
        }

        @Schema(description = "Closing amount", example = "800")
        public Double closingAmount;

        //@Schema(description = "Billetage details", example = "[{'denomination': 100, 'quantity': 8}]")
        public Set<BilletageCloseDataRequest> billetage;

        @Schema(description = "Reconciliation data", example = "[{'expected': 800, 'actual': 780}]")
        public String reconciliation;

        @Schema(description = "Additional notes", example = "Closing cashier session after operations")
        public String notes;
        @Schema(example = "Feb 20, 2015 12:00:00 AM")
        public LocalDate endDate;
        @Schema(example = "12:00:00 AM")
        public String endTime;
    }

    // Response for closing a cashier session
    @Schema(description = "CloseCashierSessionResponse")
    public static class CloseCashierSessionResponse {

        private CloseCashierSessionResponse() {

        }
        @Schema(description = "Cashier ID", example = "123")
        public Long cashierId;

        @Schema(description = "Closing amount", example = "800")
        public Double closingAmount;

        @Schema(description = "Status of the session closing", example = "Cashier session closed successfully")
        public String status;

        // Getters and setters
    }
}
