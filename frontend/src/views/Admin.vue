<template>
  
        <!-- Admin Dashboard Content -->
        <div class="space-y-6">
          <!-- Stats Grid -->
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            <!-- Total Users Card -->
            <div class="bg-white p-6 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
              <div class="flex items-center justify-between mb-4">
                <h3 class="text-sm font-medium text-gray-600">Total Users</h3>
                <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
                  <Users class="w-5 h-5 text-blue-600" />
                </div>
              </div>
              <div class="space-y-1">
                <p class="text-2xl font-bold text-gray-900">{{ totalUsers }}</p>
                <p class="text-sm text-green-600">+12% from last month</p>
              </div>
            </div>

            <!-- Total FDs Card -->
            <div class="bg-gradient-to-r from-green-50 to-green-100 p-6 rounded-lg border border-green-200 hover:shadow-sm transition-shadow">
              <div class="flex items-center justify-between mb-4">
                <h3 class="text-sm font-medium text-green-700">Total FDs</h3>
                <div class="w-8 h-8 bg-green-200 rounded-lg flex items-center justify-center">
                  <Building class="w-5 h-5 text-green-600" />
                </div>
              </div>
              <div class="space-y-1">
                <p class="text-2xl font-bold text-green-800">{{ totalFDs }}</p>
                <p class="text-sm text-green-600">{{ activeFDs }} active</p>
              </div>
            </div>

            <!-- Total Investment Card -->
            <div class="bg-white p-6 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
              <div class="flex items-center justify-between mb-4">
                <h3 class="text-sm font-medium text-gray-600">Total Investment</h3>
                <div class="w-8 h-8 bg-yellow-100 rounded-lg flex items-center justify-center">
                  <IndianRupee class="w-5 h-5 text-yellow-600" />
                </div>
              </div>
              <div class="space-y-1">
                <p class="text-2xl font-bold text-gray-900">₹{{ totalInvestment }}</p>
                <p class="text-sm text-gray-600">Across all FDs</p>
              </div>
            </div>

            <!-- Support Tickets Card -->
            <div class="bg-white p-6 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
              <div class="flex items-center justify-between mb-4">
                <h3 class="text-sm font-medium text-gray-600">Support Tickets</h3>
                <div class="w-8 h-8 bg-red-100 rounded-lg flex items-center justify-center">
                  <HelpCircle class="w-5 h-5 text-red-600" />
                </div>
              </div>
              <div class="space-y-1">
                <p class="text-2xl font-bold text-gray-900">{{ openTickets }}</p>
                <p class="text-sm text-red-600">{{ openTickets }} open</p>
              </div>
            </div>
          </div>

          
        </div>
</template>

<script>
import AdminSidebar from '../components/AdminSidebar.vue'
import Navbar from '../components/Navbar.vue'
import { MenuIcon, Users, Building, IndianRupee, HelpCircle, ChevronRight, BarChart2 } from 'lucide-vue-next'
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'Admin',
  components: {
    MenuIcon, 
    Users, 
    Building, 
    IndianRupee, 
    HelpCircle, 
    ChevronRight, 
    BarChart2,
  },
  data() {
    return {
      sidebarCollapsed: false,
      isMobile: false,
      // Mock data - replace with actual data from store
      totalUsers: 1248,
      totalFDs: 456,
      activeFDs: 342,
      totalInvestment: '2,45,67,890',
      openTickets: 23,
      recentFDs: [
        { id: 1, amount: '50,000', customerName: 'John Doe', status: 'Active' },
        { id: 2, amount: '1,00,000', customerName: 'Jane Smith', status: 'Pending' },
        { id: 3, amount: '75,000', customerName: 'Mike Johnson', status: 'Active' }
      ],
      recentTickets: [
        { id: 1, subject: 'FD Maturity Query', customerName: 'John Doe', priority: 'high' },
        { id: 2, subject: 'Interest Rate Question', customerName: 'Jane Smith', priority: 'medium' },
        { id: 3, subject: 'Account Update', customerName: 'Mike Johnson', priority: 'low' }
      ]
    }
  },
  computed: {
    ...mapGetters(['currentUser']),
    mainContentClasses() {
      if (this.isMobile) {
        return 'ml-0'
      }
      return this.sidebarCollapsed ? 'md:ml-30' : 'md:ml-65'
    }
  },
  methods: {
    ...mapActions(['logout']),
    handleToggleSidebar() {
      if (this.$refs.sidebar) {
        this.$refs.sidebar.toggleSidebar()
      }
    },
    handleSidebarToggle(collapsed) {
      this.sidebarCollapsed = collapsed
    },
    checkMobile() {
      this.isMobile = window.innerWidth < 768
    },
    getPriorityClass(priority) {
      const classes = {
        'high': 'bg-red-100 text-red-800',
        'medium': 'bg-yellow-100 text-yellow-800',
        'low': 'bg-green-100 text-green-800'
      }
      return classes[priority] || 'bg-gray-100 text-gray-800'
    },
    async logout() {
      try {
        await this.logout()
        this.$router.push('/login')
      } catch (error) {
        console.error('Logout error:', error)
      }
    }
  },
  mounted() {
    this.checkMobile()
    window.addEventListener('resize', this.checkMobile)
    
    // Fetch admin dashboard data
    // this.$store.dispatch('fetchAllUsers')
    // this.$store.dispatch('fetchAllFDs')
    // this.$store.dispatch('fetchAllSupportTickets')
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.checkMobile)
  }
}
</script>