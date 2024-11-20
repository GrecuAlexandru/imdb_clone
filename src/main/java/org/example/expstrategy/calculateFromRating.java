package org.example.expstrategy;

import org.example.ExperienceStrategy;

public class calculateFromRating implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 2;
    }
}
