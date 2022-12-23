package ca.ulaval.glo4002.cafe.small.cafe.domain.bill;

import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.bill.TipRate;
import ca.ulaval.glo4002.cafe.domain.exception.InvalidGroupTipRateException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TipRateTest {
    @Test
    public void whenCreatingTipRateWithValueUnder0_shouldThrowInvalidGroupTipRateException() {
        assertThrows(InvalidGroupTipRateException.class, () -> new TipRate(-2));
    }

    @Test
    public void whenCreatingTipRateWithValueOver100_shouldThrowInvalidGroupTipRateException() {
        assertThrows(InvalidGroupTipRateException.class, () -> new TipRate(120));
    }
}
