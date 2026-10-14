import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { RouterLink } from '@angular/router';
import { catchError, of } from 'rxjs';

import type { ChronoStatisticsExam, ExamStatistics } from '../../core/models/exam.model';
import { ExamService } from '../../core/services/exam.service';

interface ServiceCard {
  readonly icon: string;
  readonly title: string;
  readonly description: string;
}

@Component({
  selector: 'app-home',
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressBarModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HomeComponent {
  private readonly examService = inject(ExamService);

  protected readonly statistics = signal<ExamStatistics | null>(null);
   statistics1 = signal<ChronoStatisticsExam[]>([]);
  protected readonly loading = signal(true);

  protected readonly services: readonly ServiceCard[] = [
    {
      icon: 'monitor_heart',
      title: 'Imagerie par résonance magnétique',
      description: 'Deux IRM 1.5T pour les explorations neurologiques et ostéo-articulaires.',
    },
    {
      icon: 'scanner',
      title: 'Scanner',
      description: 'Scanner multibarrettes basse dose, résultats transmis au médecin prescripteur.',
    },
    {
      icon: 'radio_button_checked',
      title: 'Radiologie et échographie',
      description: 'Radiologie numérique, échographie et mammographie sur rendez-vous.',
    },
  ];

  constructor() {
    // this.examService
    //   .statistics()
    //   .pipe(catchError(() => of(null)))
    //   .subscribe((statistics) => {
    //     this.statistics.set(statistics);
    //     this.loading.set(false);
    //   });

      this.examService.getChronostatistics().subscribe(data=>{this.statistics1.set(data); console.log(this.statistics1);    this.loading.set(false);})
  }
}
