package com.gridnine.testing.rule;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.util.FlightBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

class RulesSetTest {
    final Predicate<Flight> DepartureIsBeforeNow = flights -> {
        LocalDateTime now = LocalDateTime.now();
        final List<Segment> segments = flights.getSegments();
        for (Segment segment : segments) {
            final LocalDateTime departureDate = segment.getDepartureDate();
            if (departureDate.isBefore(now)) {
                return true;
            }
        }

        return false;
    };
    final Predicate<Flight> arrivedIsBeforeDeparture = flights -> {
        final List<Segment> segments = flights.getSegments();
        for (Segment segment : segments) {
            if (segment.getArrivalDate().isBefore(segment.getDepartureDate())) {
                return true;
            }
        }
        return false;
    };
    final Predicate<Flight> moreTwoHoursOnGround = flights -> {
        final List<Segment> segments = flights.getSegments();
        int timeOnGround = 0;
        int timeTwoHours = 2 * 60 * 60;

        for (int i = 0; i < segments.size() - 1; i++) {
            LocalDateTime arrivalTime = segments.get(i).getArrivalDate();
            LocalDateTime departureTime = segments.get(i + 1).getDepartureDate();
            timeOnGround += Duration.between(arrivalTime, departureTime).abs().toSeconds();
            if (timeOnGround > timeTwoHours) {
                return true;
            }
        }

        return false;
    };
    List<Flight> flightList = FlightBuilder.createFlights();
    Rule rule5 = new RuleImpl(DepartureIsBeforeNow.negate(), "еще не улетели");
    Rule rule4 = new RuleImpl(arrivedIsBeforeDeparture.negate(), "дата прилета не раньше даты отлета");
    Rule rule6 = new RuleImpl(moreTwoHoursOnGround.negate(), "менее 2 часов на земле");

    Rule rule1 = new RuleImpl(DepartureIsBeforeNow, "уже улетели");
    Rule rule2 = new RuleImpl(arrivedIsBeforeDeparture, "дата прилета раньше даты отлета");
    Rule rule3 = new RuleImpl(moreTwoHoursOnGround, "больше 2 часов на земле");

    RulesSet<Rule> rulesSet = new RulesSet<>();


    @BeforeEach
    void setUp() {
        rulesSet.addRule(rule1);
        rulesSet.addRule(rule2);
        rulesSet.addRule(rule3);
    }

    @AfterEach
    void tearDown() {
        rulesSet.clear();
    }

    @Test
    void executeRules() {
        Assertions.assertEquals(4, rulesSet.executeRules(flightList).size());
    }

    @Test
    void executeRulesSeparate() {
        final Map<String, List<Flight>> rulesSeparateMap = rulesSet.executeRulesSeparate(flightList);
        Assertions.assertEquals(3, rulesSeparateMap.size());
    }
}