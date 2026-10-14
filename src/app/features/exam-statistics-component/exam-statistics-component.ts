import { Component, inject, signal } from "@angular/core";
import { ExamService } from "../../core/services/exam.service";
import { ChronoStatisticsExam } from "../../core/models/exam.model";
import { MatTableDataSource, MatTableModule } from "@angular/material/table";
import { MatFormField, MatFormFieldModule, MatLabel } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { DatePipe } from "@angular/common";

;

@Component({
  selector: 'app-exam-statistics-component',
  imports: [
    MatTableModule,
    MatFormField,
    MatLabel,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    DatePipe,
  ],
  templateUrl: './exam-statistics-component.html',
  styleUrl: './exam-statistics-component.scss',
})
export class ExamStatisticsComponent {
  examenService = inject(ExamService);

  readonly statistics = signal<ChronoStatisticsExam[]>([]);
  readonly loading = signal(false);

  displayedColumns: string[] = ['periodeType', 'periode', 'total'];
  readonly dataSource = new MatTableDataSource<ChronoStatisticsExam>([]);

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  ngOnInit(): void {
    console.log('Valeur du signal :', this.statistics());
    this.loadStatistics();
  }

  private loadStatistics(): void {
    this.loading.set(true);

    this.examenService.getChronostatistics().subscribe((data) => {
      this.statistics.set(data);
      this.dataSource.data = data;
      console.log('Valeur du signal :', this.statistics());
      this.loading.set(false);
    });
  }
}
