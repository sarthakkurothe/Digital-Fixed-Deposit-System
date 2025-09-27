<template>
  <div class="bg-white rounded-lg border border-gray-200 p-4 h-120">
    <div class="mt-4 mb-4 text-center">
        <h1 class="text-lg font-bold text-gray-900">Support Tickets Status</h1>
    </div>
    <div class="h-95">
      <Pie v-if="chartData" :data="chartData" :options="chartOptions" />
      <div v-else class="h-full flex items-center justify-center text-s text-gray-500 font-bold">
        No Tickets to display
      </div>
    </div>
  </div>
</template>

<script>
import { Pie } from 'vue-chartjs'
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js'

ChartJS.register(ArcElement, Tooltip, Legend)

export default {
  name: 'TicketsPieChart',
  components: { Pie },
  props: {
    open: Number,
    closed: Number
  },
  computed: {
    chartData() {
      return {
        labels: ['Open Tickets', 'Closed Tickets'],
        datasets: [
          {
            data: [this.open, this.closed],
            backgroundColor: ['#3B82F6', '#10B981'], // blue and green
            borderWidth: 1
          }
        ]
      }
    },
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
                padding: 20, 
                boxWidth: 20, 
                font: {
                    size: 16
                }
            }
          }
        }
      }
    }
  }
}
</script>
