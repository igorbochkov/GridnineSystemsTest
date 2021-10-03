package com.gridnine.testing.rule;

import com.gridnine.testing.model.Flight;

import java.util.List;

public interface Rule {
    List<Flight> execute(List<Flight> flights);

    String getName();
}
