import { LowerCasePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { catchError, finalize, forkJoin, of } from 'rxjs';

import {
  MODALITY_LABELS,
  PERIOD_LABELS,
  STATUS_LABELS,
  type PeriodStatistics,
  type PeriodStatisticsDto,
  type PeriodType,
  type StatisticsOverviewDto,
} from '../../core/models/statistics.model';
import { EXAM_STATUSES, MODALITIES } from '../../core/models/exam.model';
import { NotificationService } from '../../core/services/notification.service';
import { StatisticsService } from '../../core/services/statistics.service';

interface SeriesRow {
  readonly period: string;
  readonly totalExams: number;
  readonly totalPatients: number;
  readonly examsHeight: number;
  readonly patientsHeight: number;
}

interface ModalityRow {
  readonly modality: string;
  readonly label: string;
  readonly total: number;
  readonly share: number;
  readonly barWidth: number;
}

interface StatusRow {
  readonly status: string;
  readonly label: string;
  readonly total: number;
  readonly share: number;
  readonly barWidth: number;
}

interface DonutSlice {
  readonly status: string;
  readonly label: string;
  readonly total: number;
  readonly share: number;
  readonly color: string;
}

const MODALITY_COLORS: Readonly<Record<string, string>> = {
  XRAY: '#1565c0',
  CT: '#00838f',
  MRI: '#6a1b9a',
  ULTRASOUND: '#2e7d32',
  MAMMOGRAPHY: '#ad1457',
  FLUOROSCOPY: '#ef6c00',
};

const STATUS_COLORS: Readonly<Record<string, string>> = {
  COMPLETED: '#2e7d32',
  IN_PROGRESS: '#1565c0',
  SCHEDULED: '#ef6c00',
  CANCELLED: '#c62828',
};

/**
 * Page de statistiques du cabinet : aux granularités jour / mois / année,
 * elle présente les volumes d'examens (tous types confondus et par type) et
 * les patients pris en charge, avec graphiques, filtres de dates et export CSV.
 */
@Component({
  selector: 'app-statistics',
  providers: [provideNativeDateAdapter()],
  imports: [
    LowerCasePipe,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatIconModule,
    MatProgressBarModule,
    MatTableModule,
    MatTooltipModule,
  ],
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StatisticsComponent {
  private readonly statisticsService = inject(StatisticsService);
  private readonly notifications = inject(NotificationService);

  protected readonly periodTypes: readonly PeriodType[] = ['DAY', 'MONTH', 'YEAR'];
  protected readonly periodLabels = PERIOD_LABELS;
  protected readonly statusLabels = STATUS_LABELS;

  protected readonly period = signal<PeriodType>('MONTH');
  protected readonly from = signal<Date | null>(null);
  protected readonly to = signal<Date | null>(null);
  protected readonly report = signal<PeriodStatistics | null>(null);
  protected readonly overview = signal<StatisticsOverviewDto | null>(null);
  protected readonly loading = signal(false);
  protected readonly loadError = signal(false);

  protected readonly periodColumns = ['period', 'totalExams', 'totalPatients'] as const;
  protected readonly modalityColumns = ['modality', 'total', 'share'] as const;
  protected readonly statusColumns = ['status', 'total', 'share'] as const;

  /** Granularité active en toutes lettres (ex. « Mois »). */
  protected readonly currentPeriodLabel = computed(() => this.periodLabels[this.period()]);

  /** Granularité active en minuscules (ex. « mois »). */
  protected readonly currentPeriodLabelLower = computed(() => this.currentPeriodLabel().toLowerCase());

  /** Séries temporelles brutes (examens / patients) issues du rapport. */
  protected readonly byPeriod = computed<readonly PeriodStatisticsDto[]>(
    () => this.report()?.byPeriod ?? [],
  );

  /** Nombre de périodes renvoyées par le backend. */
  protected readonly periodCount = computed(() => this.byPeriod().length);

  /** Séries temporelles enrichies d'une hauteur de barre normalisée. */
  protected readonly series = computed<readonly SeriesRow[]>(() => {
    const rows = this.byPeriod();
    const max = Math.max(1, ...rows.map((row) => Math.max(row.totalExams, row.totalPatients)));
    return rows.map((row) => ({
      period: row.period,
      totalExams: row.totalExams,
      totalPatients: row.totalPatients,
      examsHeight: this.ratio(row.totalExams, max),
      patientsHeight: this.ratio(row.totalPatients, max),
    }));
  });

  /** Répartition des examens par type d'examen, triée décroissante. */
  protected readonly modalityRows = computed<readonly ModalityRow[]>(() => {
    const report = this.report();
    if (!report) {
      return [];
    }
    const total = report.totalExams;
    const max = Math.max(1, ...MODALITIES.map((modality) => report.countByModality[modality] ?? 0));
    return MODALITIES.map((modality) => {
      const value = report.countByModality[modality] ?? 0;
      return {
        modality,
        label: MODALITY_LABELS[modality] ?? modality,
        total: value,
        share: this.ratio(value, total),
        barWidth: this.ratio(value, max),
      };
    }).sort((a, b) => b.total - a.total);
  });

  /** Répartition des examens par statut (vue globale), triée décroissante. */
  protected readonly statusRows = computed<readonly StatusRow[]>(() => {
    const overview = this.overview();
    if (!overview) {
      return [];
    }
    const total = overview.totalExams;
    const max = Math.max(1, ...EXAM_STATUSES.map((status) => overview.countByStatus[status] ?? 0));
    return EXAM_STATUSES.map((status) => {
      const value = overview.countByStatus[status] ?? 0;
      return {
        status,
        label: STATUS_LABELS[status] ?? status,
        total: value,
        share: this.ratio(value, total),
        barWidth: this.ratio(value, max),
      };
    }).sort((a, b) => b.total - a.total);
  });

  /** Segments de l'anneau d'aperçu des statuts. */
  protected readonly donut = computed<readonly DonutSlice[]>(() => {
    const overview = this.overview();
    if (!overview || overview.totalExams === 0) {
      return [];
    }
    const total = overview.totalExams;
    return EXAM_STATUSES.map((status) => {
      const value = overview.countByStatus[status] ?? 0;
      return {
        status,
        label: STATUS_LABELS[status] ?? status,
        total: value,
        share: this.ratio(value, total),
        color: STATUS_COLORS[status] ?? '#9e9e9e',
      };
    }).filter((slice) => slice.total > 0);
  });

  /** Dégradé conique alimentant l'anneau des statuts. */
  protected readonly donutGradient = computed(() => {
    const slices = this.donut();
    if (slices.length === 0) {
      return 'conic-gradient(#e0e0e0 0% 100%)';
    }
    let cursor = 0;
    const segments = slices.map((slice) => {
      const start = cursor;
      cursor += slice.share;
      return `${slice.color} ${start}% ${cursor}%`;
    });
    return `conic-gradient(${segments.join(', ')})`;
  });

  constructor() {
    this.load();
  }

  protected onPeriodChange(value: PeriodType): void {
    this.period.set(value);
    this.load();
  }

  protected onDateChange(): void {
    this.load();
  }

  protected resetFilters(): void {
    this.from.set(null);
    this.to.set(null);
    this.period.set('MONTH');
    this.load();
  }

  protected refresh(): void {
    this.load();
  }

  protected modalityColor(modality: string): string {
    return MODALITY_COLORS[modality] ?? '#607d8b';
  }

  /** Exporte les séries temporelles et le résumé au format CSV. */
  protected exportCsv(): void {
    const report = this.report();
    if (!report || report.byPeriod.length === 0) {
      this.notifications.error('Aucune donnée à exporter pour la période sélectionnée.');
      return;
    }

    const lines: string[] = [];
    lines.push('Rapport statistique du cabinet');
    lines.push(`Granularité;${this.currentPeriodLabel()}`);
    lines.push(`Total examens;${report.totalExams}`);
    lines.push(`Total patients;${report.totalPatients}`);
    lines.push('');
    lines.push('Série temporelle');
    lines.push('Periode;Examens;Patients');
    report.byPeriod.forEach((row) =>
      lines.push(`${row.period};${row.totalExams};${row.totalPatients}`),
    );
    lines.push('');
    lines.push("Repartition par type d'examen");
    lines.push('Type;Examens');
    this.modalityRows().forEach((row) => lines.push(`${row.label};${row.total}`));

    const blob = new Blob(['\uFEFF' + lines.join('\n')], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `statistiques-${report.periodType.toLowerCase()}-${new Date().toISOString().slice(0, 10)}.csv`;
    link.click();
    URL.revokeObjectURL(url);
    this.notifications.success('Export CSV généré.');
  }

  private ratio(value: number, base: number): number {
    return base > 0 ? Math.round((value / base) * 100) : 0;
  }

  private load(): void {
    this.loading.set(true);
    const period = this.period();
    const from = this.toIsoStart(this.from());
    const to = this.toIsoEndExclusive(this.to());

    forkJoin({
      report: this.statisticsService.report(period, from, to).pipe(catchError(() => of(null))),
      overview: this.statisticsService.overview().pipe(catchError(() => of(null))),
    })
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe(({ report, overview }) => {
        this.loadError.set(report === null);
        this.report.set(report);
        this.overview.set(overview);
      });
  }

  private toIsoStart(date: Date | null): string | undefined {
    return date
      ? new Date(date.getFullYear(), date.getMonth(), date.getDate()).toISOString()
      : undefined;
  }

  private toIsoEndExclusive(date: Date | null): string | undefined {
    // La borne haute de l'API est exclusive : on inclut la journée entière sélectionnée.
    return date
      ? new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1).toISOString()
      : undefined;
  }
}
