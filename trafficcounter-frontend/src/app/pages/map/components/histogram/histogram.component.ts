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

  @ViewChild('speedDataHistogram', {static: true})
  speedDataChartElement!: ElementRef;

  @ViewChild('vehicleAmountHistogram', {static: true})
  vehicleAmountChartElement!: ElementRef;

  @ViewChild('primary', {static: true})
  primaryElement!: ElementRef;

  @ViewChild('accent', {static: true})
  accentElement!: ElementRef;

  private primaryColor!: string;

  private accentColor!: string;

  @Input('duration')
  duration$!: Observable<string | null>

  @Input('data')
  data$!: Observable<HistoricDataCollectionDto | null>

  private speedDataChart!: Chart;
  private vehicleAmountChart!: Chart;

  constructor(private readonly translateService: TranslateService) {
  }

  private createCharts(): void {
    const defaultConfig = () => {
      return {
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
            }
          }
        }
      };
    }
    this.speedDataChart = new Chart(this.speedDataChartElement.nativeElement, Object.assign(defaultConfig(), {
      options: {
        plugins: {
          title: {
            display: true,
            text: this.translateService.instant('map.histogram.title.speedData')
          }
        }
      }
    }));
    this.vehicleAmountChart = new Chart(this.vehicleAmountChartElement.nativeElement, Object.assign(defaultConfig(), {
      options: {
        plugins: {
          title: {
            display: true,
            text: this.translateService.instant('map.histogram.title.vehicleAmount')
          }
        }
      }
    }));
  }

  private createChartData(duration: string, data: HistoricDataCollectionDto): ChartData {
    const labels = data.measurements.map(measurement => {
      if (measurement.time instanceof Date) {
        measurement.time.setMinutes(0);
        measurement.time.setSeconds(0);
        measurement.time.setMilliseconds(0);
        if (duration === '7d') {
          return measurement.time.toLocaleDateString();
        }
        return measurement.time.toLocaleTimeString();
      }
      return '';
    }).reverse();
    const speedData = data.measurements.map(measurement => measurement.avgVehicleSpeed).reverse();
    const vehicleAmount = data.measurements.map(measurement => measurement.avgVehicleAmount).reverse();

    return {
      labels: labels,
      datasets: [
        {
          label: this.translateService.instant('map.histogram.label.speedData'),
          backgroundColor: this.primaryColor,
          data: speedData
        },
        {
          label: this.translateService.instant('map.histogram.label.vehicleAmount'),
          backgroundColor: this.accentColor,
          data: vehicleAmount
        }
      ]
    }
  }

  ngOnInit(): void {

    this.primaryColor = getComputedStyle(this.primaryElement.nativeElement).color;
    this.accentColor = getComputedStyle(this.accentElement.nativeElement).color;

    this.createCharts();

    this.duration$.subscribe(duration => {
      this.data$.subscribe((data: HistoricDataCollectionDto | null) => {
        if (data === null || duration === null) {
          return;
        }
        const chartData = this.createChartData(duration, data);
        this.speedDataChart.data.labels = chartData.labels;
        this.speedDataChart.data.datasets = [chartData.datasets[0]];
        this.speedDataChart.update('show');
        this.vehicleAmountChart.data.labels = chartData.labels;
        this.vehicleAmountChart.data.datasets = [chartData.datasets[1]];
        this.vehicleAmountChart.update('show');
      });
    });

  }

}
