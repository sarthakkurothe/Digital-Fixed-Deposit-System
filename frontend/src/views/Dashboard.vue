 <template>
  <div class="p-6 space-y-6">
    <!-- Stats Grid -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      <!-- Total Investment Card -->
      <div class="bg-white p-6 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-sm font-medium text-gray-600">Total Investment</h3>
          <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
            <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
            </svg>
          </div>
        </div>
        <div class="space-y-1">
          <p class="text-2xl font-bold text-gray-900">₹4,75,000</p>
          <p class="text-xs text-gray-500">Across 4 FDs</p>
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
          <p class="text-2xl font-bold text-gray-900">2</p>
          <p class="text-xs text-gray-500">1 matured this year</p>
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
          <p class="text-2xl font-bold text-gray-900">7.2%</p>
          <p class="text-xs text-gray-500">Annual percentage yield</p>
        </div>
      </div>
    </div>

      <!-- Quick Actions Section -->
    <div class="bg-white rounded-lg border border-gray-200 p-6">
      <h2 class="text-lg font-semibold text-gray-900 mb-4">Quick Actions</h2>
      <p class="text-sm text-gray-600 mb-4">Manage your fixed deposits</p>
      
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <router-link 
          to="book-fd"
          class="flex items-center p-4 border border-blue-200 rounded-lg hover:bg-blue-50 transition-colors group"
        >
          <div class="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-blue-200 transition-colors">
            <svg class="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
            </svg>
          </div>
          <div>
            <h3 class="font-medium text-gray-900">Book New FD</h3>
            <p class="text-sm text-gray-600">Start earning up to 8.5% interest</p>
          </div>
          <svg class="w-5 h-5 text-gray-400 ml-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
          </svg>
        </router-link>

        <router-link 
          to="calculator"
          class="flex items-center p-4 border border-green-200 rounded-lg hover:bg-green-50 transition-colors group"
        >
          <div class="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-green-200 transition-colors">
            <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 002 2v14a2 2 0 002 2z"></path>
            </svg>
          </div>
          <div>
            <h3 class="font-medium text-gray-900">Interest Calculator</h3>
            <p class="text-sm text-gray-600">Calculate potential returns</p>
          </div>
          <svg class="w-5 h-5 text-gray-400 ml-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
          </svg>
        </router-link>

        <router-link 
          to="support"
          class="flex items-center p-4 border border-orange-200 rounded-lg hover:bg-orange-50 transition-colors group"
        >
          <div class="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-orange-200 transition-colors">
            <svg class="w-5 h-5 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
          </div>
          <div>
            <h3 class="font-medium text-gray-900">Get Support</h3>
            <p class="text-sm text-gray-600">Need help with your FDs?</p>
          </div>
          <svg class="w-5 h-5 text-gray-400 ml-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
          </svg>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  mounted() {
    console.log('Dashboard component mounted - ready for team development')
  }
}
</script>

<style scoped>
/* Custom styles if needed */
</style>