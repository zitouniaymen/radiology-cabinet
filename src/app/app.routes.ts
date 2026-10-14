import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/home/home.component').then((m) => m.HomeComponent),
    title: 'Cabinet de radiologie',
  },
  {
    path: 'exams',
    loadComponent: () =>
      import('./features/exams/exam-list.component').then((m) => m.ExamListComponent),
    title: 'Examens',
  },
  {
    path: 'statistics',
    loadComponent: () =>
      import('./features/statistics/statistics.component').then((m) => m.StatisticsComponent),
    title: 'Statistiques',
  },
  {
    path: 'statistic',
    redirectTo: 'statistics',
  },
  { path: '**', redirectTo: '' },
];
