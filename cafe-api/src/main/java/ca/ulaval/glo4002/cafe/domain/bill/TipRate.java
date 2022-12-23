package ca.ulaval.glo4002.cafe.domain.bill;

import ca.ulaval.glo4002.cafe.domain.exception.InvalidGroupTipRateException;

public record TipRate(float value) {
    public TipRate {
        if (value < 0 || value > 1) {
            throw new InvalidGroupTipRateException();
        }
    }
}
