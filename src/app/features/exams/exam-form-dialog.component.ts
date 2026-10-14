import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
  type FormControl,
  type FormGroup,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import {
  EXAM_STATUSES,
  MODALITIES,
  type Exam,
  type ExamPayload,
  type ExamStatus,
  type Modality,
} from '../../core/models/exam.model';

export interface ExamFormDialogData {
  readonly mode: 'create' | 'edit';
  readonly exam?: Exam;
}

interface ExamForm {
  patientReference: FormControl<string>;
  modality: FormControl<Modality>;
  bodyRegion: FormControl<string>;
  status: FormControl<ExamStatus>;
  scheduledAt: FormControl<string>;
  durationMinutes: FormControl<number>;
  room: FormControl<string>;
}

@Component({
  selector: 'app-exam-form-dialog',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
  ],
  templateUrl: './exam-form-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ExamFormDialogComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly dialogRef = inject<MatDialogRef<ExamFormDialogComponent, ExamPayload | undefined>>(MatDialogRef);
  protected readonly data = inject<ExamFormDialogData>(MAT_DIALOG_DATA);

  protected readonly modalities = MODALITIES;
  protected readonly statuses = EXAM_STATUSES;

  protected readonly form: FormGroup<ExamForm> = this.formBuilder.nonNullable.group<ExamForm>({
    patientReference: this.formBuilder.nonNullable.control(this.data.exam?.patientReference ?? '', [
      Validators.required,
      Validators.pattern(/^[A-Z0-9-]+$/),
      Validators.maxLength(32),
    ]),
    modality: this.formBuilder.nonNullable.control<Modality>(this.data.exam?.modality ?? 'XRAY', [
      Validators.required,
    ]),
    bodyRegion: this.formBuilder.nonNullable.control(this.data.exam?.bodyRegion ?? '', [
      Validators.required,
      Validators.maxLength(64),
    ]),
    status: this.formBuilder.nonNullable.control<ExamStatus>(this.data.exam?.status ?? 'SCHEDULED', [
      Validators.required,
    ]),
    scheduledAt: this.formBuilder.nonNullable.control(toLocalInput(this.data.exam?.scheduledAt), [
      Validators.required,
    ]),
    durationMinutes: this.formBuilder.nonNullable.control(this.data.exam?.durationMinutes ?? 30, [
      Validators.required,
      Validators.min(5),
      Validators.max(480),
    ]),
    room: this.formBuilder.nonNullable.control(this.data.exam?.room ?? '', [
      Validators.required,
      Validators.maxLength(32),
    ]),
  });

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = this.form.getRawValue();
    const payload: ExamPayload = {
      ...value,
      patientReference: value.patientReference.trim().toUpperCase(),
      scheduledAt: new Date(value.scheduledAt).toISOString(),
    };
    this.dialogRef.close(payload);
  }
}

function toLocalInput(isoDate: string | undefined): string {
  const date = isoDate ? new Date(isoDate) : new Date();
  const offsetMs = date.getTimezoneOffset() * 60_000;
  return new Date(date.getTime() - offsetMs).toISOString().slice(0, 16);
}
