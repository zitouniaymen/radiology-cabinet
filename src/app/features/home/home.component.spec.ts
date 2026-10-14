import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';

import { environment } from '../../../environments/environment';
import { HomeComponent } from './home.component';

describe('HomeComponent', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        provideNoopAnimations(),
      ],
    }).compileComponents();
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('displays the statistics returned by the API', () => {
    const fixture = TestBed.createComponent(HomeComponent);
    fixture.detectChanges();

    httpMock.expectOne(`${environment.apiBaseUrl}/exams/statistics`).flush({
      total: 12,
      countByStatus: { SCHEDULED: 5, IN_PROGRESS: 2, COMPLETED: 4, CANCELLED: 1 },
    });
    fixture.detectChanges();

    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('12');
    expect(text).toContain('examens enregistrés');
  });

  it('degrades gracefully when the API fails', () => {
    const fixture = TestBed.createComponent(HomeComponent);
    fixture.detectChanges();

    httpMock
      .expectOne(`${environment.apiBaseUrl}/exams/statistics`)
      .flush({ message: 'boom' }, { status: 500, statusText: 'Server Error' });
    fixture.detectChanges();

    expect((fixture.nativeElement as HTMLElement).textContent).toContain(
      'Statistiques indisponibles',
    );
  });
});
