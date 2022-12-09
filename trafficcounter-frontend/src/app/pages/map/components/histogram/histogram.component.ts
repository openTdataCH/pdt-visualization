import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Chart} from "chart.js/auto";
import {HistoricDataCollectionDto} from "../../../../api/models/historic-data-collection-dto";
import {ChartData} from "chart.js";
import {HistogramType} from './histogram-type';

const DEFAUT_BACKGROUND_COLOR = '#3e95cd';

@Component({
  selector: 'app-histogram',
  templateUrl: './histogram.component.html',
  styleUrls: ['./histogram.component.scss']
})
export class HistogramComponent implements OnInit {

  @ViewChild('histogram', {static: true})
  chartElement!: ElementRef;

  @Input('type')
  type!: HistogramType

  @Input('duration')
  duration!: string

  @Input('data')
  data!: HistoricDataCollectionDto

  private chart!: Chart;

  private get chartData(): ChartData {
    const labels = this.data.measurements.map(measurement => measurement.ordinal);
    const speedData = this.data.measurements.map(measurement => measurement.avgVehicleSpeed);
    const vehicleAmount = this.data.measurements.map(measurement => measurement.avgVehicleAmount);

    return {
      labels: labels,
      datasets: [
        {
          label: 'speed-data',
          backgroundColor: DEFAUT_BACKGROUND_COLOR,
          data: speedData
        },
        {
          label: 'vehicle-amount',
          backgroundColor: DEFAUT_BACKGROUND_COLOR,
          data: vehicleAmount
        }
      ]
    }
  }

  constructor() {
  }

  ngOnInit(): void {

    this.chart = new Chart(this.chartElement.nativeElement,
      {
        type: 'bar',
        data: this.chartData,
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'top',
            },
            title: {
              display: true,
              text: 'Chart.js Bar Chart'
            }
          }
        }
      }
    );
  }

}
