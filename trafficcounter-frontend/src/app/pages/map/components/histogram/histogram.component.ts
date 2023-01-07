/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Chart} from "chart.js/auto";
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

  /**
   * This method will create the charts for the speed data and the vehicle amount data.
   */
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
    };

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

  /**
   * This method will prepare the data to for the charts.
   *
   * @param duration holds the set time filter. Can either be "7d" or "24h".
   * @param data holds the historic data to be filtered.
   * @returns the new representation ready-to-use for the charts.
   */
  private createChartData(duration: string, data: HistoricDataCollectionDto): ChartData {
    const measurements = data.measurements.sort((m1, m2) => {
      if(m1.time instanceof Date && m2.time instanceof Date) {
        return m1.time == m2.time ? 0 : (m1.time < m2.time ? -1 : 1);
      }
      return 0;
    });

    const labels = measurements.map(measurement => {
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
    });

    const speedData = measurements.map(measurement => measurement.avgVehicleSpeed);
    const vehicleAmount = measurements.map(measurement => measurement.avgVehicleAmount);

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
