package seedu.address.model.transaction.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

class WeightTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Weight(null));
    }

    @Test
    public void constructor_invalidWeight_throwsIllegalArgumentException() {
        String invalidWeight = "";
        assertThrows(IllegalArgumentException.class, () -> new Weight(invalidWeight));
    }

    @Test
    public void isValidWeight() {
        // null weight
        assertThrows(NullPointerException.class, () -> Weight.isValidWeight(null));

        // invalid weights
        assertFalse(Weight.isValidWeight("")); // empty string
        assertFalse(Weight.isValidWeight("a")); // non-number
        assertFalse(Weight.isValidWeight(" ")); // spaces only
        assertFalse(Weight.isValidWeight("-1")); // negative number
        assertFalse(Weight.isValidWeight(".0")); // decimal without 0
        assertFalse(Weight.isValidWeight("1/3")); // slash
        assertFalse(Weight.isValidWeight("1 ")); // trailing space
        assertFalse(Weight.isValidWeight("1. 0")); // spaces between

        // valid weights
        assertTrue(Weight.isValidWeight("100.99"));
        assertTrue(Weight.isValidWeight("0")); // zero
        assertTrue(Weight.isValidWeight("0.0")); // zero with decimal places
        assertTrue(Weight.isValidWeight("010")); // leading 0
        assertTrue(Weight.isValidWeight("010.00")); // leading 0 with decimal places
    }

    @Test
    public void hashcode() {
        Weight weight = new Weight("21.50");
        Weight otherWeight = new Weight("21.50");

        assertEquals(weight, otherWeight);
    }

    @Test
    public void equals() {
        Weight weight = new Weight("21.50");

        // same values -> returns true
        assertEquals(weight, new Weight("21.50"));

        // same object -> returns true
        assertEquals(weight, weight);

        // null -> returns false
        assertNotEquals(null, weight);

        // different types -> returns false
        assertFalse(weight.equals(5.0f));

        // different values -> returns false
        assertNotEquals(weight, new Weight("10"));
    }
}