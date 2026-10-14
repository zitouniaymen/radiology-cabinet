export const MODALITIES = [
  'XRAY',
  'CT',
  'MRI',
  'ULTRASOUND',
  'MAMMOGRAPHY',
  'FLUOROSCOPY',
] as const;

export type Modality = (typeof MODALITIES)[number];

export const EXAM_STATUSES = ['SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'] as const;

export type ExamStatus = (typeof EXAM_STATUSES)[number];

export interface Exam {
  readonly id: string;
  readonly reference: string;
  readonly patientReference: string;
  readonly modality: Modality;
  readonly bodyRegion: string;
  readonly status: ExamStatus;
  readonly scheduledAt: string;
  readonly durationMinutes: number;
  readonly room: string;
}

export interface ChronoStatisticsExam {
 readonly periodeType: 'DAY' | 'MONTH' | 'YEAR';
readonly  periode: string;
 readonly total: number;
}

/** Payload sent when creating or replacing an exam. Never carries clinical data. */
export interface ExamPayload {
  readonly patientReference: string;
  readonly modality: Modality;
  readonly bodyRegion: string;
  readonly status: ExamStatus;
  readonly scheduledAt: string;
  readonly durationMinutes: number;
  readonly room: string;
}

export interface PageResponse<T> {
  readonly content: readonly T[];
  readonly page: number;
  readonly size: number;
  readonly totalElements: number;
  readonly totalPages: number;
}

export interface ExamStatistics {
  readonly total: number;
  readonly countByStatus: Readonly<Record<ExamStatus, number>>;
}

export interface ExamQuery {
  readonly status?: ExamStatus;
  readonly modality?: Modality;
  readonly page?: number;
  readonly size?: number;
}
