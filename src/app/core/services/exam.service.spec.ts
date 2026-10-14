import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import type { Exam, PageResponse } from '../models/exam.model';
import { ExamService } from './exam.service';

describe('ExamService', () => {
  let service: ExamService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ExamService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('sends filters and pagination as query parameters', () => {
    const page: PageResponse<Exam> = {
      content: [],
      page: 1,
      size: 5,
      totalElements: 0,
      totalPages: 0,
    };

    service.list({ status: 'SCHEDULED', modality: 'CT', page: 1, size: 5 }).subscribe((result) => {
      expect(result.size).toBe(5);
    });

    const request = httpMock.expectOne(
      (req) =>
        req.url === `${environment.apiBaseUrl}/exams` &&
        req.params.get('status') === 'SCHEDULED' &&
        req.params.get('modality') === 'CT' &&
        req.params.get('page') === '1' &&
        req.params.get('size') === '5',
    );
    expect(request.request.method).toBe('GET');
    request.flush(page);
  });

  it('patches only the status when cancelling an exam', () => {
    service.updateStatus('abc', 'CANCELLED').subscribe();

    const request = httpMock.expectOne(`${environment.apiBaseUrl}/exams/abc/status`);
    expect(request.request.method).toBe('PATCH');
    expect(request.request.body).toEqual({ status: 'CANCELLED' });
    request.flush({});
  });
});
