<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Admin Sidebar -->
    <AdminSidebar ref="sidebar" @toggle="handleSidebarToggle" />
    
    <!-- Main Content -->
    <div class="transition-all duration-300 ease-in-out"
         :class="mainContentClasses">


         <Navbar />

      <!-- Page Content -->
      <main class="flex-1 overflow-auto p-6">
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

          <!-- Quick Actions Section -->
          <div class="bg-white rounded-lg border border-gray-200 p-6">
            <h2 class="text-lg font-semibold text-gray-900 mb-4">Quick Actions</h2>
            <p class="text-sm text-gray-600 mb-4">Manage the system efficiently</p>
            
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <router-link 
                to="/admin/fixed-deposits"
                class="flex items-center p-4 border border-green-200 rounded-lg hover:bg-green-50 transition-colors group"
              >
                <div class="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-green-200 transition-colors">
                  <Building class="w-5 h-5 text-green-600" />
                </div>
                <div>
                  <h3 class="font-medium text-gray-900">View All FDs</h3>
                  <p class="text-sm text-gray-600">Manage fixed deposits</p>
                </div>
                <ChevronRight class="w-5 h-5 text-gray-400 ml-auto" />
              </router-link>

              <router-link 
                to="/admin/support-tickets"
                class="flex items-center p-4 border border-orange-200 rounded-lg hover:bg-orange-50 transition-colors group"
              >
                <div class="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-orange-200 transition-colors">
                  <HelpCircle class="w-5 h-5 text-orange-600" />
                </div>
                <div>
                  <h3 class="font-medium text-gray-900">Support Tickets</h3>
                  <p class="text-sm text-gray-600">Help customers</p>
                </div>
                <ChevronRight class="w-5 h-5 text-gray-400 ml-auto" />
              </router-link>

              <div class="flex items-center p-4 border border-blue-200 rounded-lg hover:bg-blue-50 transition-colors group">
                <div class="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-blue-200 transition-colors">
                  <BarChart2 class="w-5 h-5 text-blue-600" />
                </div>
                <div>
                  <h3 class="font-medium text-gray-900">Analytics</h3>
                  <p class="text-sm text-gray-600">View system reports</p>
                </div>
                <ChevronRight class="w-5 h-5 text-gray-400 ml-auto" />
              </div>
            </div>
          </div>

          <!-- Recent Activity -->
          <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div class="bg-white rounded-lg border border-gray-200 p-6">
              <h3 class="text-lg font-semibold text-gray-900 mb-4">Recent FD Applications</h3>
              <div class="space-y-4">
                <div v-for="fd in recentFDs" :key="fd.id" class="flex items-center justify-between py-2 border-b border-gray-100 last:border-0">
                  <div class="flex items-center">
                    <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center mr-3">
                      <Building class="w-4 h-4 text-blue-600" />
                    </div>
                    <div>
                      <p class="font-medium text-gray-900">₹{{ fd.amount }}</p>
                      <p class="text-sm text-gray-500">{{ fd.customerName }}</p>
                    </div>
                  </div>
                  <span class="px-2 py-1 text-xs rounded-full bg-green-100 text-green-800">
                    {{ fd.status }}
                  </span>
                </div>
              </div>
            </div>

            <div class="bg-white rounded-lg border border-gray-200 p-6">
              <h3 class="text-lg font-semibold text-gray-900 mb-4">Recent Support Tickets</h3>
              <div class="space-y-4">
                <div v-for="ticket in recentTickets" :key="ticket.id" class="flex items-center justify-between py-2 border-b border-gray-100 last:border-0">
                  <div class="flex items-center">
                    <div class="w-8 h-8 bg-orange-100 rounded-lg flex items-center justify-center mr-3">
                      <HelpCircle class="w-4 h-4 text-orange-600" />
                    </div>
                    <div>
                      <p class="font-medium text-gray-900">{{ ticket.subject }}</p>
                      <p class="text-sm text-gray-500">{{ ticket.customerName }}</p>
                    </div>
                  </div>
                  <span class="px-2 py-1 text-xs rounded-full" :class="getPriorityClass(ticket.priority)">
                    {{ ticket.priority }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
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
    AdminSidebar,
    MenuIcon, 
    Users, 
    Building, 
    IndianRupee, 
    HelpCircle, 
    ChevronRight, 
    BarChart2,
    Navbar
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
      return this.sidebarCollapsed ? 'md:ml-0' : 'md:ml-65'
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