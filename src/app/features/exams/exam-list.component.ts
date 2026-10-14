import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, type PageEvent } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { DatePipe } from '@angular/common';
import { filter, switchMap } from 'rxjs';

import {
  EXAM_STATUSES,
  MODALITIES,
  type Exam,
  type ExamPayload,
  type ExamStatus,
  type Modality,
} from '../../core/models/exam.model';
import { ExamService } from '../../core/services/exam.service';
import { NotificationService } from '../../core/services/notification.service';
import { ExamFormDialogComponent, type ExamFormDialogData } from './exam-form-dialog.component';

@Component({
  selector: 'app-exam-list',
  imports: [
    DatePipe,
    FormsModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatSelectModule,
    MatTableModule,
  ],
  templateUrl: './exam-list.component.html',
  styleUrl: './exam-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ExamListComponent {
  private readonly examService = inject(ExamService);
  private readonly notifications = inject(NotificationService);
  private readonly dialog = inject(MatDialog);

  protected readonly modalities = MODALITIES;
  protected readonly statuses = EXAM_STATUSES;
  protected readonly displayedColumns = [
    'reference',
    'patientReference',
    'modality',
    'bodyRegion',
    'scheduledAt',
    'room',
    'status',
    'actions',
  ] as const;

  protected readonly exams = signal<readonly Exam[]>([]);
  protected readonly totalElements = signal(0);
  protected readonly pageIndex = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly loading = signal(false);
  protected statusFilter: ExamStatus | '' = '';
  protected modalityFilter: Modality | '' = '';

  constructor() {
    this.reload();
  }

  protected reload(): void {
    this.loading.set(true);
    this.examService
      .list({
        status: this.statusFilter || undefined,
        modality: this.modalityFilter || undefined,
        page: this.pageIndex(),
        size: this.pageSize(),
      })
      .subscribe({
        next: (page) => {
          this.exams.set(page.content);
          this.totalElements.set(page.totalElements);
          this.loading.set(false);
        },
        error: () => this.loading.set(false),
      });
  }

  protected onFilterChange(): void {
    this.pageIndex.set(0);
    this.reload();
  }

  protected onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.reload();
  }

  protected openCreateDialog(): void {
    this.openDialog({ mode: 'create' })
      .pipe(switchMap((payload) => this.examService.create(payload)))
      .subscribe((exam) => {
        this.notifications.success(`Examen ${exam.reference} créé.`);
        this.reload();
      });
  }

  protected openEditDialog(exam: Exam): void {
    this.openDialog({ mode: 'edit', exam })
      .pipe(switchMap((payload) => this.examService.update(exam.id, payload)))
      .subscribe(() => {
        this.notifications.success(`Examen ${exam.reference} mis à jour.`);
        this.reload();
      });
  }

  protected cancelExam(exam: Exam): void {
    this.examService.updateStatus(exam.id, 'CANCELLED').subscribe(() => {
      this.notifications.success(`Examen ${exam.reference} annulé.`);
      this.reload();
    });
  }

  protected deleteExam(exam: Exam): void {
    this.examService.delete(exam.id).subscribe(() => {
      this.notifications.success(`Examen ${exam.reference} supprimé.`);
      this.reload();
    });
  }

  private openDialog(data: ExamFormDialogData) {
    return this.dialog
      .open<ExamFormDialogComponent, ExamFormDialogData, ExamPayload | undefined>(
        ExamFormDialogComponent,
        { data, width: '520px' },
      )
      .afterClosed()
      .pipe(filter((payload): payload is ExamPayload => payload !== undefined));
  }
}
