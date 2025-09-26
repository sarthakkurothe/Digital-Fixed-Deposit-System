<template>
     <!-- Total Investment Card -->
      <div class="bg-white p-6 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-sm font-medium text-gray-600">Total Investment</h3>
          <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
           <Wallet />
          </div>
        </div>
        <div class="space-y-1">
          <p class="text-2xl font-bold text-gray-900">₹{{ totalAmount }}</p>
        </div>
      </div>

    <!-- Interest Earned Card -->
      <div class="bg-gradient-to-r from-green-50 to-green-100 p-6 rounded-lg border border-green-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-sm font-medium text-green-700">Interest Earned</h3>
          <div class="w-8 h-8 bg-green-200 rounded-lg flex items-center justify-center">
           <TrendingUp class="w-6 h-6 text-green-600" />
          </div>
        </div>
        <div class="space-y-1">
          <p class="text-2xl font-bold text-green-800">₹{{ this.totalAccuredInterest }}</p>
         
        </div>
      </div>

      <!-- Active FDs Card -->
      <div class="bg-white p-6 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-sm font-medium text-gray-600">Active FDs</h3>
          <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
             <Building class="w-6 h-6 text-blue-600" />
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
             <BarChart2 class="w-6 h-6 text-yellow-600" />
          </div>
        </div>
        <div class="space-y-1">
          <p class="text-2xl font-bold text-gray-900">{{ averageInterest }}</p>
        </div>
      </div>



</template>

<script>
import { mapGetters } from "vuex";
import { Wallet,TrendingUp,Building,BarChart2 } from "lucide-vue-next";
export default {
  name: "DashboardCards",
  components: { Wallet,TrendingUp,Building,BarChart2 },
  computed: {
    ...mapGetters(["getFDs","getFDsCount"]),

    totalAmount() {
      return this.getFDs.reduce((sum, fd) => sum + fd.amount, 0).toLocaleString('en-IN');
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
      return this.getFDs.reduce((sum, fd) => sum + parseFloat(fd.interest_rate), 0);
  
    },
    totalAccuredInterest() {
      return this.getFDs.reduce((sum, fd) => sum + parseFloat(fd.accrued_interest), 0).toFixed(2).toLocaleString('en-IN');
    },
    averageInterest() {
      if (this.getFDsCount === 0) return 0;
      return (this.totalFDInterest / this.getFDsCount).toFixed(2) + "%";
    },
  },
};
</script>

<style scoped>
</style>
