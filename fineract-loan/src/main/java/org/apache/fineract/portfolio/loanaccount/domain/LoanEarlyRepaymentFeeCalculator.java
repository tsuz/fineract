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

import java.math.BigDecimal;
import java.math.MathContext;
import org.apache.fineract.infrastructure.core.service.MathUtil;

/**
 * Computes the fee charged when a loan product configures an optional early repayment fee. The result is expressed at
 * the precision of the supplied {@link MathContext}; callers are responsible for rounding it to the loan currency's
 * scale before applying it to a transaction.
 */
public final class LoanEarlyRepaymentFeeCalculator {

    private LoanEarlyRepaymentFeeCalculator() {}

    public static BigDecimal calculateFee(final boolean enableEarlyRepaymentFee,
            final LoanEarlyRepaymentFeeCalculationType earlyRepaymentFeeCalculationType, final BigDecimal earlyRepaymentFeeAmount,
            final BigDecimal outstandingPrincipal, final MathContext mc) {
        if (!enableEarlyRepaymentFee || earlyRepaymentFeeCalculationType == null || !MathUtil.isGreaterThanZero(earlyRepaymentFeeAmount)
                || !MathUtil.isGreaterThanZero(outstandingPrincipal)) {
            return BigDecimal.ZERO;
        }
        return switch (earlyRepaymentFeeCalculationType) {
            case FLAT -> earlyRepaymentFeeAmount;
            case PERCENT_OF_OUTSTANDING_PRINCIPAL -> MathUtil.percentageOf(outstandingPrincipal, earlyRepaymentFeeAmount, mc);
        };
    }
}
