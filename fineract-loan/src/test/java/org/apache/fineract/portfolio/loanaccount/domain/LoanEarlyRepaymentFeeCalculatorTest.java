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
package org.apache.fineract.portfolio.loanaccount.domain;

import static org.apache.fineract.portfolio.loanaccount.domain.LoanEarlyRepaymentFeeCalculationType.FLAT;
import static org.apache.fineract.portfolio.loanaccount.domain.LoanEarlyRepaymentFeeCalculationType.PERCENT_OF_OUTSTANDING_PRINCIPAL;
import static org.apache.fineract.portfolio.loanaccount.domain.LoanEarlyRepaymentFeeCalculator.calculateFee;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;

class LoanEarlyRepaymentFeeCalculatorTest {

    private static final MathContext MC = new MathContext(19, RoundingMode.HALF_EVEN);

    @Test
    void returnsZero_whenFeeDisabled() {
        BigDecimal fee = calculateFee(false, FLAT, BigDecimal.TEN, BigDecimal.valueOf(1000), MC);

        assertEquals(BigDecimal.ZERO, fee);
    }

    @Test
    void returnsFlatAmount_whenFlatFeeEnabledAndPrincipalOutstanding() {
        BigDecimal fee = calculateFee(true, FLAT, BigDecimal.valueOf(25), BigDecimal.valueOf(1000), MC);

        assertEquals(BigDecimal.valueOf(25), fee);
    }

    @Test
    void returnsZero_whenFlatFeeEnabledButNoPrincipalOutstanding() {
        BigDecimal fee = calculateFee(true, FLAT, BigDecimal.valueOf(25), BigDecimal.ZERO, MC);

        assertEquals(BigDecimal.ZERO, fee);
    }

    @Test
    void returnsZero_whenOutstandingPrincipalIsNegative() {
        BigDecimal fee = calculateFee(true, FLAT, BigDecimal.valueOf(25), BigDecimal.valueOf(-100), MC);

        assertEquals(BigDecimal.ZERO, fee);
    }

    @Test
    void returnsPercentageOfOutstandingPrincipal() {
        BigDecimal fee = calculateFee(true, PERCENT_OF_OUTSTANDING_PRINCIPAL, BigDecimal.valueOf(5), BigDecimal.valueOf(1000), MC);

        assertEquals(0, BigDecimal.valueOf(50).compareTo(fee));
    }

    @Test
    void roundsPercentageFee_usingSuppliedMathContext() {
        BigDecimal fee = calculateFee(true, PERCENT_OF_OUTSTANDING_PRINCIPAL, BigDecimal.valueOf(1, 1), BigDecimal.valueOf(3), MC);

        // 0.1% of 3.00 = 0.003, rounded to the MathContext precision
        assertEquals(0, BigDecimal.valueOf(3, 3).compareTo(fee));
    }

    @Test
    void returnsZero_whenPercentageFeeEnabledButNoPrincipalOutstanding() {
        BigDecimal fee = calculateFee(true, PERCENT_OF_OUTSTANDING_PRINCIPAL, BigDecimal.valueOf(5), BigDecimal.ZERO, MC);

        assertEquals(BigDecimal.ZERO, fee);
    }

    @Test
    void returnsZero_whenCalculationTypeIsNull() {
        BigDecimal fee = calculateFee(true, null, BigDecimal.TEN, BigDecimal.valueOf(1000), MC);

        assertEquals(BigDecimal.ZERO, fee);
    }

    @Test
    void returnsZero_whenFeeAmountIsNull() {
        BigDecimal fee = calculateFee(true, FLAT, null, BigDecimal.valueOf(1000), MC);

        assertEquals(BigDecimal.ZERO, fee);
    }

    @Test
    void returnsZero_whenFeeAmountIsZero() {
        BigDecimal fee = calculateFee(true, PERCENT_OF_OUTSTANDING_PRINCIPAL, BigDecimal.ZERO, BigDecimal.valueOf(1000), MC);

        assertEquals(BigDecimal.ZERO, fee);
    }

    @Test
    void returnsZero_whenFeeAmountIsNegative() {
        BigDecimal fee = calculateFee(true, FLAT, BigDecimal.valueOf(-25), BigDecimal.valueOf(1000), MC);

        assertEquals(BigDecimal.ZERO, fee);
    }
}
