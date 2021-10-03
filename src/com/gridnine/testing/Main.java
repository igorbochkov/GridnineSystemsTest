package com.gridnine.testing;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.rule.Rule;
import com.gridnine.testing.rule.RuleImpl;
import com.gridnine.testing.rule.RulesSet;
import com.gridnine.testing.util.FlightBuilder;
import com.gridnine.testing.util.UtilRule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
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

        Rule rule1 = new RuleImpl(DepartureIsBeforeNow, "уже улетели");
        Rule rule2 = new RuleImpl(arrivedIsBeforeDeparture, "дата прилета раньше даты отлета");
        Rule rule3 = new RuleImpl(moreTwoHoursOnGround, "больше 2 часов на земле");

        Rule rule5 = new RuleImpl(DepartureIsBeforeNow.negate(), "еще не улетели");
        Rule rule4 = new RuleImpl(arrivedIsBeforeDeparture.negate(), "дата прилета не раньше даты отлета");
        Rule rule6 = new RuleImpl(moreTwoHoursOnGround.negate(), "менее 2 часов на земле");

        RulesSet<Rule> rulesSet = new RulesSet<>();
        rulesSet.addRule(rule5);
        rulesSet.addRule(rule4);
        rulesSet.addRule(rule6);

        UtilRule.printFlight(rulesSet.executeRulesSeparate(FlightBuilder.createFlights()));
    }
}
