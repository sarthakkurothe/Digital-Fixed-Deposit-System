<template>
  <div
    class="bg-white p-6 md:p-6 rounded-xl shadow-lg border border-gray-200 hover:shadow-xl transform transition-all hover:-translate-y-1 flex flex-col justify-between w-full"
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
      <div class="text-gray-800">
        <strong>Amount: </strong> ₹{{ fd.amount.toLocaleString() }}
      </div>

      <!-- Interest Rate -->
      <div class="text-gray-800">
        <strong>Interest Rate: </strong>
        <span class="text-green-600">{{ fd.interest_rate }}%</span>
      </div>

      <!-- Tenure -->
      <div class="text-gray-800">
        <strong>Tenure: </strong> {{ fd.tenure_months }} months
      </div>

      <!-- Start Date -->
      <div class="text-gray-800">
        <strong>Start: </strong> {{ formatDate(fd.start_date) }}
      </div>

      <!-- Maturity Date -->
      <div class="text-gray-800">
        <strong>Maturity: </strong> {{ formatDate(fd.maturity_date) }}
      </div>

      <!-- Accrued Interest -->
      <div class="text-gray-800">
        <strong>Accrued Interest: </strong> ₹{{ fd.accrued_interest.toLocaleString() }}
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
        @click="$emit('break', fd)"
        :disabled="fd.status !== 'ACTIVE'"
        :class="[
          'flex-1 px-4 py-2 bg-red-600 text-white rounded-lg text-sm md:text-sm font-medium transition disabled:opacity-70',
          fd.status === 'ACTIVE' ? 'hover:bg-red-700 cursor-pointer' : 'cursor-not-allowed'
        ]"
      >
        Break
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "FDCard",
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

</style>
