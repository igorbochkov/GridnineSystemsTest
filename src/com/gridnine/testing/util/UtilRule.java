package com.gridnine.testing.util;

import com.gridnine.testing.model.Flight;

import java.util.List;
import java.util.Map;

public class UtilRule {

    private UtilRule() {
    }

    //выводит в консоль список полетов согласно правилу
    public static void printFlight(Map<String, List<Flight>> map) {
        for (Map.Entry<String, List<Flight>> entry : map.entrySet()) {
            System.out.println("========================");
            System.out.println(entry.getKey());
            printFlight(entry.getValue());
        }
    }

    public static void printFlight(List<Flight> flights) {
        flights.forEach(System.out::println);
    }

}
