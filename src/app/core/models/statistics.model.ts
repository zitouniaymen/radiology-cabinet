/**
 * Modèles des statistiques exposées par l'API `/api/v1/statistics`.
 *
 * Les volumes d'examens sont désagrégés par type d'examen (modalité) et les patients
 * sont comptés à partir de leur code pseudonymisé.
 */

/** Granularité temporelle supportée par les statistiques chronologiques. */
export type PeriodType = 'DAY' | 'MONTH' | 'YEAR';

/** Examens et patients distincts agrégés sur une période. */
export interface PeriodStatisticsDto {
  /** Libellé de la période : `YYYY-MM-DD`, `YYYY-MM` ou `YYYY`. */
  readonly period: string;
  readonly totalExams: number;
  readonly totalPatients: number;
}

/** Détail d'une période : total d'examens et répartition par type d'examen. */
export interface ChronoStatisticsDto {
  readonly period: string;
  readonly totalExams: number;
  readonly countByModality: Readonly<Record<string, number>>;
}

/** Rapport consolidé des statistiques sur un intervalle donné. */
export interface PeriodStatistics {
  readonly periodType: string;
  readonly from: string | null;
  readonly toExclusive: string | null;
  readonly totalExams: number;
  readonly totalPatients: number;
  readonly byPeriod: readonly PeriodStatisticsDto[];
  readonly countByModality: Readonly<Record<string, number>>;
  readonly chronoByModality: readonly ChronoStatisticsDto[];
}

/** Répartition du nombre d'examens par type d'examen. */
export interface ModalityStatisticsDto {
  readonly totalExams: number;
  readonly countByModality: Readonly<Record<string, number>>;
}

/** Vue synthétique globale de l'activité du cabinet. */
export interface StatisticsOverviewDto {
  readonly totalExams: number;
  readonly totalPatients: number;
  readonly countByModality: Readonly<Record<string, number>>;
  readonly countByStatus: Readonly<Record<string, number>>;
}

/** Libellés lisibles des types d'examen (modalités). */
export const MODALITY_LABELS: Readonly<Record<string, string>> = {
  XRAY: 'Radiographie',
  CT: 'Scanner (TDM)',
  MRI: 'IRM',
  ULTRASOUND: 'Échographie',
  MAMMOGRAPHY: 'Mammographie',
  FLUOROSCOPY: 'Fluoroscopie',
};

/** Libellés lisibles des statuts d'examen. */
export const STATUS_LABELS: Readonly<Record<string, string>> = {
  SCHEDULED: 'Programmé',
  IN_PROGRESS: 'En cours',
  COMPLETED: 'Terminé',
  CANCELLED: 'Annulé',
};

/** Libellés lisibles des granularités temporelles. */
export const PERIOD_LABELS: Readonly<Record<PeriodType, string>> = {
  DAY: 'Jour',
  MONTH: 'Mois',
  YEAR: 'Année',
};
