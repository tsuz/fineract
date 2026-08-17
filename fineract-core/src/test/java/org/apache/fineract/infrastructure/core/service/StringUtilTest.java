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
package org.apache.fineract.infrastructure.core.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilTest {

    @Test
    void testMaskValueWithDefaultUnmaskedLengthMasksMiddleCharacters() {
        // when
        String result = StringUtil.maskValue("001100000123456789");

        // then
        Assertions.assertEquals("0*************6789", result);
    }

    @Test
    void testMaskValueWithValueShorterThanUnmaskedLengthReturnsFullMask() {
        // when
        String result = StringUtil.maskValue("12");

        // then
        Assertions.assertEquals("****", result);
    }

    @Test
    void testMaskValueWithValueEqualToUnmaskedLengthReturnsFullMask() {
        // when
        String result = StringUtil.maskValue("1234");

        // then
        Assertions.assertEquals("****", result);
    }

    @Test
    void testMaskValueWithValueOneLongerThanUnmaskedLengthDoesNotLeakOriginalValue() {
        // when
        String result = StringUtil.maskValue("12345");

        // then
        Assertions.assertEquals("****", result);
        Assertions.assertNotEquals("12345", result);
    }

    @Test
    void testMaskValueWithValueTwoLongerThanUnmaskedLengthMasksAtLeastOneCharacter() {
        // when
        String result = StringUtil.maskValue("123456");

        // then
        Assertions.assertEquals("1*3456", result);
    }

    @Test
    void testMaskValueWithCustomUnmaskedLength() {
        // when
        String result = StringUtil.maskValue("user@example.com", 3);

        // then
        Assertions.assertEquals("u************com", result);
    }

    @Test
    void testMaskValueWithEmptyStringReturnsFullMask() {
        // when
        String result = StringUtil.maskValue("");

        // then
        Assertions.assertEquals("****", result);
    }

    @Test
    void testMaskValueWithNullReturnsNull() {
        // when
        String result = StringUtil.maskValue(null);

        // then
        Assertions.assertNull(result);
    }
}
