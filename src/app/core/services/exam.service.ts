import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import type { ChronoStatisticsExam } from '../../core/models/exam.model';


import { environment } from '../../../environments/environment';
import type {
  Exam,
  ExamPayload,
  ExamQuery,
  ExamStatistics,
  ExamStatus,
  PageResponse,
} from '../models/exam.model';

@Injectable({ providedIn: 'root' })
export class ExamService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/exams`;

  private readonly apiUrl = 'http://localhost:8080/api/v1/exams/calculChronostatisticsExamMonth';

  list(query: ExamQuery = {}): Observable<PageResponse<Exam>> {
    let params = new HttpParams();
    if (query.status) {
      params = params.set('status', query.status);
    }
    if (query.modality) {
      params = params.set('modality', query.modality);
    }
    params = params.set('page', String(query.page ?? 0)).set('size', String(query.size ?? 20));
    return this.http.get<PageResponse<Exam>>(this.baseUrl, { params });
  }

  // statistics(): Observable<ExamStatistics> {
  //   return this.http.get<ExamStatistics>(`${this.baseUrl}/statistics`);
  // }
  statistics(): Observable<ExamStatistics> {
    return this.http.get<ExamStatistics>(`${this.baseUrl}/calculChronostatisticsExamMonth`);
  }

  getById(id: string): Observable<Exam> {
    return this.http.get<Exam>(`${this.baseUrl}/${id}`);
  }

  create(payload: ExamPayload): Observable<Exam> {
    return this.http.post<Exam>(this.baseUrl, payload);
  }

  update(id: string, payload: ExamPayload): Observable<Exam> {
    return this.http.put<Exam>(`${this.baseUrl}/${id}`, payload);
  }

  updateStatus(id: string, status: ExamStatus): Observable<Exam> {
    return this.http.patch<Exam>(`${this.baseUrl}/${id}/status`, { status });
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getChronostatistics(): Observable<ChronoStatisticsExam[]> {
    return this.http.get<ChronoStatisticsExam[]>(
      `http://localhost:8080/api/v1/exams/calculChronostatisticsExamMonth`,
    );
  }
}
