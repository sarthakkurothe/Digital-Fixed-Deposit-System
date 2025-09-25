<template>
  <div
    class="bg-white p-6 md:p-6 rounded-xl shadow-lg border border-gray-200 hover:shadow-xl transform transition-all hover:-translate-y-1 flex flex-col justify-between"
  >
    <!-- FD Header: Serial and Status Badge -->
    <div class="flex justify-between items-center mb-4">
      <h3 class="text-lg md:text-xl font-semibold text-gray-800">FD #{{ index + 1 }}</h3>
      <span
        :class="statusBadgeClass"
        class="text-xs md:text-sm font-medium px-3 py-1 rounded-full"
      >
        {{ fd.status }}
      </span>
    </div>

    <!-- FD Details Grid -->
    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 text-gray-700 mb-4">
      <!-- Amount -->
      <div class="flex items-center gap-2 text-green-600 text-base md:text-lg font-semibold">
        <Currency class="w-4 h-4 md:w-5 md:h-5" />
        ₹{{ fd.amount.toLocaleString() }}
      </div>

      <!-- Interest Rate -->
      <div class="flex items-center gap-2 text-gray-700">
        <Percent class="w-4 h-4 md:w-5 md:h-5 text-blue-500" />
        <span><strong>Interest Rate:</strong> {{ fd.interest_rate }}%</span>
      </div>

      <!-- Tenure -->
      <div class="flex items-center gap-2 text-gray-700">
        <Clock class="w-4 h-4 md:w-5 md:h-5 text-yellow-500" />
        <span><strong>Tenure:</strong> {{ fd.tenure_months }} months</span>
      </div>

      <!-- Start Date -->
      <div class="flex items-center gap-2 text-gray-700">
        <Calendar class="w-4 h-4 md:w-5 md:h-5 text-purple-500" />
        <span><strong>Start:</strong> {{ formatDate(fd.start_date) }}</span>
      </div>

      <!-- Maturity Date -->
      <div class="flex items-center gap-2 text-gray-700">
        <CalendarClock class="w-4 h-4 md:w-5 md:h-5 text-red-500" />
        <span><strong>Maturity:</strong> {{ formatDate(fd.maturity_date) }}</span>
      </div>

      <!-- Accrued Interest -->
      <div class="flex items-center gap-2 text-green-600 font-medium">
        <TrendingUp class="w-4 h-4 md:w-5 md:h-5" />
        <span><strong>Accrued Interest:</strong> ₹{{ fd.accrued_interest.toLocaleString() }}</span>
      </div>
    </div>

    <!-- Action Buttons -->
    <div class="flex gap-3 mt-3">
      <button
        @click="$emit('view', fd)"
        class="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg text-sm md:text-sm font-medium hover:bg-blue-700 transition cursor-pointer"
      >
        View
      </button>
      <button
        @click="$emit('delete', fd)"
        class="flex-1 px-4 py-2 bg-red-600 text-white rounded-lg text-sm md:text-sm font-medium hover:bg-red-700 transition cursor-pointer"
      >
        Delete
      </button>
    </div>
  </div>
</template>

<script>
import { Currency, Percent, Clock, Calendar, CalendarClock, TrendingUp } from "lucide-vue-next";

export default {
  name: "FDCard",
  components: { Currency, Percent, Clock, Calendar, CalendarClock, TrendingUp },
  props: {
    fd: { type: Object, required: true },
    index: { type: Number, required: true },
  },
  computed: {
    statusBadgeClass() {
      switch (this.fd.status) {
        case "ACTIVE":
          return "bg-green-100 text-green-800";
        case "MATURED":
          return "bg-blue-100 text-blue-800";
        case "PENDING":
          return "bg-yellow-100 text-yellow-800";
        default:
          return "bg-red-100 text-red-800";
      }
    }
  },
  methods: {
    formatDate(date) {
      if (!date) return "-";
      return new Date(date).toLocaleDateString();
    },
  },
};
</script>

<style scoped>
/* Optional: subtle hover lift */
</style>
