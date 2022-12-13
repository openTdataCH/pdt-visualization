import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Chart, UpdateMode} from "chart.js/auto";
import {HistoricDataCollectionDto} from "../../../../api/models/historic-data-collection-dto";
import {TranslateService} from "@ngx-translate/core";
import {Observable} from "rxjs";
import {ChartData} from "chart.js";

@Component({
  selector: 'app-histogram',
  templateUrl: './histogram.component.html',
  styleUrls: ['./histogram.component.scss']
})
export class HistogramComponent implements OnInit {

  @ViewChild('histogram', {static: true})
  chartElement!: ElementRef;

  @Input('duration')
  duration$!: Observable<string | null>

  @Input('data')
  data$!: Observable<HistoricDataCollectionDto | null>

  private chart!: Chart;

  constructor(private readonly translateService: TranslateService) {
  }

  private createChart(): void {
    this.chart = new Chart(this.chartElement.nativeElement,
      {
        type: 'bar',
        data: {
          labels: [],
          datasets: []
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'top',
            },
            title: {
              display: true,
              text: this.translateService.instant('map.histogram.title')
            }
          }
        }
      });
  }

  private createChartData(data: HistoricDataCollectionDto): ChartData {
    const labels = data.measurements.map(measurement => measurement.ordinal);
    const speedData = data.measurements.map(measurement => measurement.avgVehicleSpeed);
    const vehicleAmount = data.measurements.map(measurement => measurement.avgVehicleAmount);

    return {
      labels: labels,
      datasets: [
        {
          label: this.translateService.instant('map.histogram.label.speedData'),
          backgroundColor: 'red',
          data: speedData
        },
        {
          label: this.translateService.instant('map.histogram.label.vehicleAmount'),
          backgroundColor: 'blue',
          data: vehicleAmount
        }
      ]
    }
  }

  ngOnInit(): void {

    this.createChart();

    this.duration$.subscribe(duration => {
      this.data$.subscribe((data: HistoricDataCollectionDto | null) => {
        if(data === null) {
          return;
        }
        const chartData = this.createChartData(data);
        this.chart.data.labels = chartData.labels;
        this.chart.data.datasets = chartData.datasets;
        this.chart.update('show');
      });
    });

  }

}
