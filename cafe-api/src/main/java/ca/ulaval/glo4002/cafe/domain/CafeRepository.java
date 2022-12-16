package ca.ulaval.glo4002.cafe.domain;

public interface CafeRepository {
    void saveOrUpdate(Cafe cafe);

    Cafe get();
}
