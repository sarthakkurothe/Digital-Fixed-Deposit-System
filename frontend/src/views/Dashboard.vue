<template>
  <div class="p-6 space-y-6">
    <!-- Stats Grid -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      <dashboard-cards />
    </div>

    <!-- Charts Grid: Pie + Bar -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <pie-chart />
      <bar-chart />
    </div>

    <!-- Quick Actions Section -->
    <div class="bg-white rounded-lg border border-gray-200 p-6">
      <h2 class="text-lg font-semibold text-gray-900 mb-4">Quick Actions</h2>
      <p class="text-sm text-gray-600 mb-4">Manage your fixed deposits</p>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <!-- Book FD -->
        <router-link
          to="book-fd"
          class="flex items-center p-4 border border-blue-200 rounded-lg hover:bg-blue-50 transition-colors group"
        >
          <div class="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-blue-200 transition-colors">
            <ActivityIcon class="w-6 h-6 text-blue-500" />
          </div>
          <div>
            <h3 class="font-medium text-gray-900">Book New FD</h3>
            <p class="text-sm text-gray-600">Start earning up to 8.5% interest</p>
          </div>
          <ArrowRightIcon class="w-5 h-5 text-gray-400 ml-auto" />
        </router-link>

        <!-- Interest Calculator -->
        <router-link
          to="calculator"
          class="flex items-center p-4 border border-green-200 rounded-lg hover:bg-green-50 transition-colors group"
        >
          <div class="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-green-200 transition-colors">
            <CalculatorIcon class="w-6 h-6 text-green-500" />
          </div>
          <div>
            <h3 class="font-medium text-gray-900">Interest Calculator</h3>
            <p class="text-sm text-gray-600">Calculate potential returns</p>
          </div>
          <ArrowRightIcon class="w-5 h-5 text-gray-400 ml-auto" />
        </router-link>

        <!-- Get Support -->
        <router-link
          to="support"
          class="flex items-center p-4 border border-orange-200 rounded-lg hover:bg-orange-50 transition-colors group"
        >
          <div class="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-orange-200 transition-colors">
            <HelpCircleIcon class="w-6 h-6 text-orange-500" />
          </div>
          <div>
            <h3 class="font-medium text-gray-900">Get Support</h3>
            <p class="text-sm text-gray-600">Need help with your FDs?</p>
          </div>
          <ArrowRightIcon class="w-5 h-5 text-gray-400 ml-auto" />
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import DashboardCards from '../components/DashboardCards.vue';
import PieChart from '../components/PieChart.vue';
import BarChart from '../components/BarChart.vue';

// Import Lucide Icons
import { ArrowRightIcon, ActivityIcon, CalculatorIcon, HelpCircleIcon } from 'lucide-vue-next'

export default {
  name: 'Dashboard',
  components: { DashboardCards, PieChart, BarChart, ArrowRightIcon, ActivityIcon, CalculatorIcon, HelpCircleIcon },
  mounted() {
    if(this.$store.getters.getFDs.length === 0)
      this.$store.dispatch('fetchFDs');
  }
}
</script>

<style scoped>
/* Optional: custom hover effects or shadows */
</style>
