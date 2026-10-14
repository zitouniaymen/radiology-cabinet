import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import type {
  ModalityStatisticsDto,
  PeriodStatistics,
  PeriodStatisticsDto,
  PeriodType,
  StatisticsOverviewDto,
} from '../models/statistics.model';

/**
 * Accès à l'API de statistiques du cabinet (`/api/v1/statistics`).
 */
@Injectable({ providedIn: 'root' })
export class StatisticsService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/statistics`;

  /** Rapport consolidé (totaux, séries temporelles, détail par type d'examen). */
  report(period: PeriodType, from?: string, to?: string): Observable<PeriodStatistics> {
    return this.http.get<PeriodStatistics>(`${this.baseUrl}/report`, {
      params: this.buildParams(period, from, to),
    });
  }

  /** Vue globale : total examens, patients distincts, répartition par type et statut. */
  overview(): Observable<StatisticsOverviewDto> {
    return this.http.get<StatisticsOverviewDto>(`${this.baseUrl}/overview`);
  }

  /** Séries temporelles : examens et patients distincts par période. */
  examsByPeriod(period: PeriodType, from?: string, to?: string): Observable<PeriodStatisticsDto[]> {
    return this.http.get<PeriodStatisticsDto[]>(`${this.baseUrl}/exams`, {
      params: this.buildParams(period, from, to),
    });
  }

  /** Répartition du nombre d'examens par type d'examen sur un intervalle. */
  modalityStatistics(from?: string, to?: string): Observable<ModalityStatisticsDto> {
    let params = new HttpParams();
    if (from) {
      params = params.set('from', from);
    }
    if (to) {
      params = params.set('to', to);
    }
    return this.http.get<ModalityStatisticsDto>(`${this.baseUrl}/modalities`, { params });
  }

  private buildParams(period: PeriodType, from?: string, to?: string): HttpParams {
    let params = new HttpParams().set('period', period);
    if (from) {
      params = params.set('from', from);
    }
    if (to) {
      params = params.set('to', to);
    }
    return params;
  }
}
