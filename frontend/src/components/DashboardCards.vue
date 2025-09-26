<template>
     <!-- Total Investment Card -->
      <div class="bg-white p-6 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-sm font-medium text-gray-600">Total Investment</h3>
          <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
           <IndianRupee />
          </div>
        </div>
        <div class="space-y-1">
          <p class="text-2xl font-bold text-gray-900">{{ totalAmount }}</p>
        </div>
      </div>

    <!-- Interest Earned Card -->
      <div class="bg-gradient-to-r from-green-50 to-green-100 p-6 rounded-lg border border-green-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-sm font-medium text-green-700">Interest Earned</h3>
          <div class="w-8 h-8 bg-green-200 rounded-lg flex items-center justify-center">
            <svg class="w-4 h-4 text-green-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path>
            </svg>
          </div>
        </div>
        <div class="space-y-1">
          <p class="text-2xl font-bold text-green-800">₹47,146</p>
          <p class="text-xs text-green-600">+18.5% from last month</p>
        </div>
      </div>

      <!-- Active FDs Card -->
      <div class="bg-white p-6 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-sm font-medium text-gray-600">Active FDs</h3>
          <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
            <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>
            </svg>
          </div>
        </div>
        <div class="space-y-1">
          <p class="text-2xl font-bold text-gray-900">{{ activeFDs }}</p>
        </div>
      </div>

        <!-- Average Interest Card -->
      <div class="bg-white p-6 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-sm font-medium text-gray-600">Average Interest</h3>
          <div class="w-8 h-8 bg-yellow-100 rounded-lg flex items-center justify-center">
            <svg class="w-4 h-4 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
            </svg>
          </div>
        </div>
        <div class="space-y-1">
          <p class="text-2xl font-bold text-gray-900">{{ averageInterest }}</p>
          <p class="text-xs text-gray-500">Annual percentage yield</p>
        </div>
      </div>



</template>

<script>
import { mapGetters } from "vuex";
import { IndianRupee } from "lucide-vue-next";
export default {
  name: "DashboardCards",
  components: { IndianRupee },
  computed: {
    ...mapGetters(["getFDs","getFDsCount"]),

    totalAmount() {
      return this.getFDs.reduce((sum, fd) => sum + fd.amount, 0);
    },
    activeFDs() {
      return this.getFDs.filter((fd) => fd.status === "ACTIVE").length;
    },
    pendingFDs() {
      return this.getFDs.filter((fd) => fd.status === "PENDING").length;
    },
    maturedFDs() {
      return this.getFDs.filter((fd) => fd.status === "MATURED").length;
    },
     totalFDInterest() {
       return this.getFDs.reduce((sum, fd) => sum + fd.intereast_rate, 0);
    },
    averageInterest() {
      if (this.getFDsCount === 0) return 0;
      return this.totalFDInterest;
    },
  },
};
</script>

<style scoped>
</style>
