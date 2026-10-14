import { HttpErrorResponse, type HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

import type { ApiError } from '../models/api-error.model';
import { NotificationService } from '../services/notification.service';

/** Turns any backend failure into a single, user-facing message. */
export const apiErrorInterceptor: HttpInterceptorFn = (request, next) => {
  const notifications = inject(NotificationService);

  return next(request).pipe(
    catchError((error: unknown) => {
      notifications.error(toMessage(error));
      return throwError(() => error);
    }),
  );
};

function toMessage(error: unknown): string {
  if (!(error instanceof HttpErrorResponse)) {
    return 'Une erreur inattendue est survenue.';
  }
  if (error.status === 0) {
    return 'Serveur injoignable. Vérifiez que radiology-api est démarré.';
  }
  const body = error.error as ApiError | null;
  const violations = body?.violations ?? [];
  if (violations.length > 0) {
    return violations.map((violation) => `${violation.field} : ${violation.message}`).join(' — ');
  }
  return body?.message ?? `Erreur ${error.status}.`;
}
