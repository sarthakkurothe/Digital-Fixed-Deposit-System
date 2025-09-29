<template>
  <div class="bg-white rounded-lg border border-gray-200 p-4 h-80">
    <!-- Header -->
    <div class="flex items-center justify-between mb-3">
      <h3 class="text-s font-bold text-gray-900">FD Interest Rates</h3>
      <div class="text-s text-gray-500 font-bold">Avg: {{ formattedAvg }}</div>
    </div>

    <!-- Chart -->
    <div class="h-65">
      <Bar v-if="chartData" :data="chartData" :options="options" />
      <div v-else class="h-full flex items-center justify-center text-sm text-gray-500">
        No FD data available
      </div>
    </div>
  </div>
</template>

<script>
import { Bar } from 'vue-chartjs';
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from 'chart.js';
import { mapGetters } from 'vuex';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

export default {
  name: 'BarChart',
  components: { Bar },
  computed: {
    ...mapGetters(['getFDs']),

    chartData() {
      const fds = this.getFDs || [];
      if (!fds.length) return null;

      // extract interest rates
      const rateOf = fd => {
        const keys = ['interest_rate', 'interest'];
        for (const k of keys) {
          if (fd?.[k] != null && !isNaN(Number(fd[k]))) return Number(fd[k]);
        }
        return 0;
      };

      const labels = fds.map((fd, i) => fd.name || fd.label || fd.bank || `FD ${i + 1}`);
      const rates = fds.map(fd => rateOf(fd));

      const total = rates.reduce((a, b) => a + b, 0);
      if (total === 0) return null;

      return {
        labels,
        datasets: [
          {
            label: 'Interest Rate (%)',
            backgroundColor: '#3b82f6',
            data: rates,
          },
        ],
      };
    },

    options() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: ctx => {
                const val = ctx.parsed.y ?? 0;
                return `${val}%`;
              },
            },
          },
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: v => `${v}%`,
            },
          },
        },
      };
    },

    formattedAvg() {
      const arr = this.chartData?.datasets?.[0]?.data || [];
      if (!arr.length) return '0%';
      const avg = arr.reduce((a, b) => a + b, 0) / arr.length;
      return `${Number(avg).toFixed(2)}%`;
    },
  },
};
</script>
