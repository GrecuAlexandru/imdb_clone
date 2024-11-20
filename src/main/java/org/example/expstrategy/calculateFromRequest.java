package org.example.expstrategy;

import org.example.ExperienceStrategy;

public class calculateFromRequest implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 5;
    }
}
