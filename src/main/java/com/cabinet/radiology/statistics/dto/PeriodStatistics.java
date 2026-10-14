package com.cabinet.radiology.statistics.dto;

import com.cabinet.radiology.exam.domain.Modality;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * Rapport consolidé des statistiques du cabinet sur une période donnée
 * (jour, mois ou année), pour les examens comme pour les patients.
 *
 * @param periodType       granularité appliquée ({@code day}, {@code month} ou {@code year})
 * @param from             début de l'intervalle analysé (inclus), {@code null} si non borné
 * @param toExclusive      fin de l'intervalle analysé (exclue), {@code null} si non borné
 * @param totalExams       nombre total d'examens sur l'intervalle
 * @param totalPatients    nombre de patients distincts sur l'intervalle
 * @param byPeriod         séries temporelles (examens + patients) par période
 * @param countByModality  nombre d'examens par type d'examen
 * @param chronoByModality séries temporelles détaillées par type d'examen
 */
public record PeriodStatistics(
        String periodType,
        OffsetDateTime from,
        OffsetDateTime toExclusive,
        long totalExams,
        long totalPatients,
        List<PeriodStatisticsDto> byPeriod,
        Map<Modality, Long> countByModality,
        List<ChronoStatisticsDto> chronoByModality
) {
}