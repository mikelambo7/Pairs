package com.mlambo.pairs;

public enum SkillLevel {
        NOVICE("NOVICE"),
        COMPETENT("COMPETENT"),
        EXPERT("EXPERT"),
        UNKNOWN("UNKNOWN");

        public final String level;

        SkillLevel(String level) {
                this.level = level;
        }

        public static boolean isValidSkillLevel(String input) {
                for (SkillLevel skillLevel : SkillLevel.values()) {
                        if (skillLevel.level.equals(input.toUpperCase())) {
                                return true;
                        }
                }
                return false;
        }
}
