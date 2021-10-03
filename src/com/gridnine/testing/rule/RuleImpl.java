package com.gridnine.testing.rule;

import com.gridnine.testing.model.Flight;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RuleImpl implements Rule {

    private Predicate<Flight> rule;
    private String name;

    public RuleImpl(Predicate<Flight> rule, String name) {
        if (Objects.isNull(rule) || Objects.isNull(name)) {
            throw new IllegalStateException("rule and name must not be null!");
        }
        this.rule = rule;
        this.name = name;
    }

    public Predicate<Flight> getRule() {
        return rule;
    }

    public void setRule(Predicate<Flight> rule) {
        if (Objects.isNull(rule)) {
            throw new IllegalStateException("rule must not be null!");
        }
        this.rule = rule;
    }

    public String getName() {
        return name;
    }

    //выполнение данного правила над списком полетов
    //возвращает отфильтрованный список полетов
    @Override
    public List<Flight> execute(List<Flight> flights) {
        return flights.stream().filter(rule).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleImpl rule1 = (RuleImpl) o;
        return Objects.equals(rule, rule1.rule) && Objects.equals(name, rule1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule, name);
    }

    @Override
    public String toString() {
        return "RuleImpl{" +
                "rule=" + rule +
                ", name='" + name + '\'' +
                '}';
    }
}
