package com.cabinet.radiology.exam.Enum;

import java.util.Locale;

/**
 * Granularité temporelle utilisée par les statistiques chronologiques.
 *
 * <ul>
 *     <li>{@link #key()} fournit l'unité attendue par {@code DATE_TRUNC} de PostgreSQL.</li>
 *     <li>{@link #pattern()} fournit le format lisible produit par {@code TO_CHAR}.</li>
 * </ul>
 */
public enum PeriodType {

    DAY("day", "YYYY-MM-DD"),
    MONTH("month", "YYYY-MM"),
    YEAR("year", "YYYY");

    private final String key;
    private final String pattern;

    PeriodType(final String key, final String pattern) {
        this.key = key;
        this.pattern = pattern;
    }

    public String key() {
        return key;
    }

    public String pattern() {
        return pattern;
    }

    /**
     * Convertit une chaîne libre ({@code day}, {@code JOUR}, {@code mois}, {@code année}...)
     * en granularité. Retourne {@link #MONTH} par défaut.
     */
    public static PeriodType from(final String value) {
        if (value == null || value.isBlank()) {
            return MONTH;
        }
        return switch (value.trim().toUpperCase(Locale.ROOT)) {
            case "DAY", "JOUR", "DAILY", "JOURNALIER" -> DAY;
            case "MONTH", "MOIS", "MONTHLY", "MENSUEL" -> MONTH;
            case "YEAR", "ANNEE", "ANNÉE", "YEARLY", "ANNUEL" -> YEAR;
            default -> MONTH;
        };
    }
}
