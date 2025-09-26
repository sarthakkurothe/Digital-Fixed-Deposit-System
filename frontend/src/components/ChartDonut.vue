<template>
  <div :style="{ width: size + 'px', height: size + 'px' }" class="relative">
    <Doughnut :data="chartData" :options="options" :width="size" :height="size" />
    <!-- center label area (optional) -->
    <div class="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
      <div class="text-sm text-gray-600">Tenure</div>
      <div class="text-2xl font-bold text-gray-800">{{ tenure }}</div>
      <div class="text-xs text-gray-600">months</div>
    </div>
  </div>
</template>

<script>
import { defineComponent, reactive, watch } from "vue";
import { Doughnut } from "vue-chartjs";
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  Filler,
} from "chart.js";

ChartJS.register(ArcElement, Tooltip, Legend, Filler);

export default defineComponent({
  name: "ChartDonut",
  components: { Doughnut },
  props: {
    principal: { type: Number, required: true }, // absolute principal amount
    interest: { type: Number, required: true }, // absolute interest amount
    size: { type: Number, default: 192 }, // px
    colors: {
      type: Array,
      default: () => ["#3b82f6", "#10b981"], // [principalColor, interestColor]
    },
    tenure: { type: [Number, String], default: "" },
  },
  setup(props) {
    const chartData = reactive({
      labels: ["Principal", "Interest"],
      datasets: [
        {
          data: [props.principal || 0, props.interest || 0],
          backgroundColor: props.colors,
          borderWidth: 0,
          hoverOffset: 6,
        },
      ],
    });

    const options = reactive({
      maintainAspectRatio: false,
      cutout: "70%",
      plugins: {
        legend: {
          display: false,
        },
        tooltip: {
          callbacks: {
            label: function (ctx) {
              const label = ctx.label || "";
              const value = ctx.parsed || 0;
              return `${label}: ₹${value.toLocaleString()}`;
            },
          },
        },
      },
    });

    watch(
      () => [props.principal, props.interest, props.colors],
      () => {
        chartData.datasets[0].data = [props.principal || 0, props.interest || 0];
        chartData.datasets[0].backgroundColor = props.colors;
      },
      { deep: true }
    );

    return { chartData, options };
  },
});
</script>

<style scoped>
/* nothing special — chart will fit the given size */
</style>
