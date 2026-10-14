import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';

import { environment } from '../../../environments/environment';
import type { Exam } from '../../core/models/exam.model';
import { ExamListComponent } from './exam-list.component';

const EXAM: Exam = {
  id: '3f1b0f2e-0b8f-4f4a-9a3a-0f8f4f4a9a3a',
  reference: 'EXM-ABCD2345',
  patientReference: 'PAT-001',
  modality: 'MRI',
  bodyRegion: 'Genou',
  status: 'SCHEDULED',
  scheduledAt: '2026-09-01T08:30:00Z',
  durationMinutes: 30,
  room: 'IRM-1',
};

describe('ExamListComponent', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamListComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideNoopAnimations()],
    }).compileComponents();
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('renders the exams returned by the API', () => {
    const fixture = TestBed.createComponent(ExamListComponent);
    fixture.detectChanges();

    httpMock
      .expectOne((req) => req.url === `${environment.apiBaseUrl}/exams`)
      .flush({ content: [EXAM], page: 0, size: 10, totalElements: 1, totalPages: 1 });
    fixture.detectChanges();

    const rows = (fixture.nativeElement as HTMLElement).querySelectorAll('tbody tr');
    expect(rows.length).toBe(1);
    expect(rows[0].textContent).toContain('EXM-ABCD2345');
  });

  it('shows an empty state when there is no exam', () => {
    const fixture = TestBed.createComponent(ExamListComponent);
    fixture.detectChanges();

    httpMock
      .expectOne((req) => req.url === `${environment.apiBaseUrl}/exams`)
      .flush({ content: [], page: 0, size: 10, totalElements: 0, totalPages: 0 });
    fixture.detectChanges();

    expect((fixture.nativeElement as HTMLElement).textContent).toContain('Aucun examen');
  });
});
