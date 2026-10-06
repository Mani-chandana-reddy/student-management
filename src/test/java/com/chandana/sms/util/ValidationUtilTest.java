package com.chandana.sms.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationUtilTest {

    @Test
    void blankDetectsNullEmptyAndSpaces() {
        assertTrue(ValidationUtil.isBlank(null));
        assertTrue(ValidationUtil.isBlank(""));
        assertTrue(ValidationUtil.isBlank("   "));
        assertFalse(ValidationUtil.isBlank("abc"));
    }

    @Test
    void acceptsNormalEmail() {
        assertTrue(ValidationUtil.isValidEmail("aarav@example.com"));
        assertTrue(ValidationUtil.isValidEmail("first.last+tag@mail.co.in"));
    }

    @Test
    void rejectsNullAndBadEmail() {
        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail("no-at-sign.com"));
        assertFalse(ValidationUtil.isValidEmail("a@b"));
    }

    @Test
    void ageMustBeBetweenOneAndHundred() {
        assertFalse(ValidationUtil.isValidAge(0));
        assertTrue(ValidationUtil.isValidAge(1));
        assertTrue(ValidationUtil.isValidAge(100));
        assertFalse(ValidationUtil.isValidAge(101));
    }

    @Test
    void yearMustBeBetweenOneAndSix() {
        assertFalse(ValidationUtil.isValidYear(0));
        assertTrue(ValidationUtil.isValidYear(1));
        assertTrue(ValidationUtil.isValidYear(6));
        assertFalse(ValidationUtil.isValidYear(7));
    }

    @Test
    void marksMustBeBetweenZeroAndHundred() {
        assertFalse(ValidationUtil.isValidMarks(-0.5));
        assertTrue(ValidationUtil.isValidMarks(0));
        assertTrue(ValidationUtil.isValidMarks(100));
        assertFalse(ValidationUtil.isValidMarks(100.1));
    }
}
