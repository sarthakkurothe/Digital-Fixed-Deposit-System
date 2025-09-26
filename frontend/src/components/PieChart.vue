<template>
  <div class="bg-white rounded-lg border border-gray-200 p-4 h-80">
    <div class="flex items-center justify-between mb-3">
      <h3 class="text-s font-bold text-gray-900">FD Allocation (by principal)</h3>
      <div class="text-s text-gray-500 font-bold">Total: ₹{{ formattedTotal }}</div>
    </div>
    <div class="h-65">
      <Pie v-if="chartData" :data="chartData" :options="options" />
      <div v-else class="h-full flex items-center justify-center text-s text-gray-500 font-bold">
        No FDs to display
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
import { mapGetters } from 'vuex'

ChartJS.register(ArcElement, Tooltip, Legend)

export default {
  name: 'PieChart',
  components: { Pie },
  computed: {
    ...mapGetters(['getFDs']),
    chartData() {
      const fds = this.getFDs || []
      if (!fds.length) return null

      const principalOf = (fd) => {
        const candidates = ['amount', 'principal']
        for (const k of candidates) {
          if (fd[k] != null && !isNaN(Number(fd[k]))) return Number(fd[k])
        }
        if (!isNaN(Number(fd.total))) return Number(fd.total)
        return 0
      }

      const labels = fds.map((fd, idx) => fd.name || fd.label || fd.bank || `FD ${idx + 1}`)
      const data = fds.map(fd => principalOf(fd))

      const total = data.reduce((a, b) => a + b, 0)
      if (total <= 0) return null

      const baseColors = ['#60A5FA', '#34D399', '#FBBF24', '#F472B6', '#A78BFA', '#FB7185', '#60FDF2', '#F97316']
      const backgroundColor = labels.map((_, i) => baseColors[i % baseColors.length])

      return {
        labels,
        datasets: [
          {
            backgroundColor,
            data
          }
        ]
      }
    },
    options() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              boxWidth: 10,
              padding: 8
            }
          },
          tooltip: {
            callbacks: {
              label: (ctx) => {
                const val = ctx.parsed || 0
                return `${ctx.label}: ₹${this.formatNumber(val)}`
              }
            }
          }
        }
      }
    },
    formattedTotal() {
      const data = this.chartData?.datasets?.[0]?.data || []
      const total = (data.length ? data.reduce((a, b) => a + b, 0) : 0)
      return this.formatNumber(total)
    }
  },
  methods: {
    formatNumber(v) {
      return Number(v).toLocaleString('en-IN', { maximumFractionDigits: 2 })
    }
  }
}
</script>
