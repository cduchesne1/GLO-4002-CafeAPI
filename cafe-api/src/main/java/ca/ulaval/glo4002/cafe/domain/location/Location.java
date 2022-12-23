package ca.ulaval.glo4002.cafe.domain.location;

import java.util.Optional;

public record Location(Country country, Optional<Province> province, Optional<State> state) {
    public static Location fromDetails(String countryString, String provinceString, String stateString) {
        Country country = Country.fromString(countryString);
        Optional<Province> province = Optional.empty();
        Optional<State> state = Optional.empty();

        switch (country) {
            case CA -> province = Optional.of(Province.fromString(provinceString));
            case US -> state = Optional.of(State.fromString(stateString));
        }

        return new Location(country, province, state);
    }
}
