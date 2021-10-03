package com.gridnine.testing.rule;

import com.gridnine.testing.model.Flight;

import java.util.*;

//Класс представляет набор правил
//которые можно динамически добавлять, удалять и выполнять над списком полетов
public class RulesSet<T extends Rule> {

    private Set<T> rules = new HashSet<>();

    public RulesSet() {
    }

    public RulesSet(Set<T> rules) {
        this.rules = rules;
    }

    public Set<T> getRules() {
        return rules;
    }

    public void setRules(Set<T> rules) {
        if (Objects.isNull(rules)) {
            throw new IllegalStateException("Set must not be null!");
        }
        this.rules.addAll(rules);
    }

    public void addRule(T rule) {
        if (Objects.isNull(rule)) {
            throw new IllegalStateException("Rule must not be null!");
        }
        rules.add(rule);
    }

    public void deleteRule(T rule) {
        rules.remove(rule);
    }

    public void clear() {
        rules.clear();
    }

    // К списку полетов применяются все правила
    public List<Flight> executeRules(List<Flight> flights) {
        List<Flight> result = new ArrayList<>();
        for (T rule : rules) {
            final List<Flight> listSoredWithRule = rule.execute(flights);
            result.addAll(listSoredWithRule);
        }
        return result;
    }

    //Каждое правило применяется отдельно к списку полетов
    public Map<String, List<Flight>> executeRulesSeparate(List<Flight> flights) {
        Map<String, List<Flight>> result = new HashMap<>();
        for (T rule : rules) {
            final List<Flight> listSoredWithRule = rule.execute(flights);
            result.put(rule.getName(), listSoredWithRule);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RulesSet<?> rulesSet = (RulesSet<?>) o;
        return rules.equals(rulesSet.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rules);
    }

    @Override
    public String toString() {
        return "RulesSet{" +
                "rules=" + rules +
                '}';
    }
}
