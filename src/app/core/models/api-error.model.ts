export interface FieldViolation {
  readonly field: string;
  readonly message: string;
}

export interface ApiError {
  readonly timestamp: string;
  readonly status: number;
  readonly error: string;
  readonly message: string;
  readonly path: string;
  readonly violations?: readonly FieldViolation[];
}
