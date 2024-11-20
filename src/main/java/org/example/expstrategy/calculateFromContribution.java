package org.example.expstrategy;

import org.example.ExperienceStrategy;

public class calculateFromContribution implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 10;
    }
}
