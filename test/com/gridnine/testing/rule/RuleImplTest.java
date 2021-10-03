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
import java.util.function.Predicate;

class RuleImplTest {

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

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void execute() {
        final List<Flight> execute = rule4.execute(flightList);
        Assertions.assertEquals(5, execute.size());
        Assertions.assertEquals(5, rule5.execute(flightList).size());
        Assertions.assertEquals(4, rule6.execute(flightList).size());

        final List<Flight> execute1 = rule1.execute(flightList);
        Flight flight = flightList.get(2);
        Assertions.assertEquals(1, execute1.size());
        Assertions.assertTrue(flight.equals(execute1.get(0)));

    }
}