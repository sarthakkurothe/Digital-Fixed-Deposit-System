 <template>
  <div class="p-6 space-y-6">
    <!-- Stats Grid -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
     <dashboard-cards/>
      
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
import DashboardCards from '../components/DashboardCards.vue';
export default {
  name: 'Dashboard',
  components: { DashboardCards },
  mounted() {
    console.log('Dashboard component mounted - ready for team development')
  }
}
</script>

<style scoped>
/* Custom styles if needed */
</style>