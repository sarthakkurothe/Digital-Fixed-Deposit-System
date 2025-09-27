<template>
  
        <div class="space-y-6">
          <div class="bg-white rounded-lg border border-gray-200">
            <div class="px-6 py-4 border-b border-gray-200">
              <h1 class="text-lg font-semibold text-gray-900">Fixed Deposits Management</h1>
            </div>
            
            <div class="p-6">
              <!-- Search and Filter -->
              <div class="mb-6 grid grid-cols-1 md:grid-cols-3 gap-5">
                <input
                  v-model="searchTerm"
                  type="text"
                  placeholder="Search by customer name or FD ID"
                  class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
                <select
                  v-model="statusFilter"
                  class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                >
                  <option value="">All Status</option>
                  <option value="ACTIVE">Active</option>
                  <option value="MATURED">Matured</option>
                  <option value="PENDING">Pending</option>
                  <option value="BROKEN">Broken</option>
                </select>
                <select
                  v-model="sortBy"
                  class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                >
                  <option value="amount">Sort by Amount</option>
                  <option value="date">Sort by Date</option>
                  <option value="customer">Sort by Customer</option>
                </select>
              </div>

              <!-- FD Cards Grid (Mobile) -->
              <div class="md:hidden space-y-4">
                <div v-for="fd in paginatedFDs" :key="fd.id" 
                     class="bg-white border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow">
                  <div class="flex justify-between items-start mb-3">
                    <div>
                      <h3 class="font-medium text-gray-900">{{ fd.customerName }}</h3>
                      <p class="text-sm text-gray-500">{{ fd.customerEmail }}</p>
                    </div>
                    <span class="px-2 py-1 text-xs rounded-full" :class="getStatusClass(fd.status)">
                      {{ fd.status }}
                    </span>
                  </div>
                  
                  <div class="grid grid-cols-2 gap-4 text-sm">
                    <div>
                      <p class="text-gray-500">Amount</p>
                      <p class="font-medium">₹{{ Number(fd.amount).toLocaleString() }}</p>
                    </div>
                    <div>
                      <p class="text-gray-500">Interest Rate</p>
                      <p class="font-medium">{{ fd.interestRate }}%</p>
                    </div>
                    <div>
                      <p class="text-gray-500">FD Number</p>
                      <p class="font-medium">{{ fd.fdNumber }}</p>
                    </div>
                    <div>
                      <p class="text-gray-500">Maturity</p>
                      <p class="font-medium">{{ formatDate(fd.maturityDate) }}</p>
                    </div>
                  </div>
                  
                  <div class="mt-4 flex space-x-2">
                    <button
                      @click="viewDetails(fd)"
                      class="flex-1 px-3 py-2 text-blue-600 bg-blue-50 rounded-lg hover:bg-blue-100 transition-colors text-sm"
                    >
                      View Details
                    </button>
                    <button
                      @click="updateStatus(fd)"
                      class="flex-1 px-3 py-2 text-green-600 bg-green-50 rounded-lg hover:bg-green-100 transition-colors text-sm"
                    >
                      Update Status
                    </button>
                  </div>
                </div>
              </div>

              <!-- FD Table (Desktop) -->
              <div class="hidden md:block overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200">
                  <thead class="bg-gray-50">
                    <tr>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">FD Details</th>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer</th>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Amount</th>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Interest</th>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Maturity</th>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>

                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    <tr v-for="fd in paginatedFDs" :key="fd.id" class="hover:bg-gray-50">
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div>
                          <div class="text-sm font-medium text-gray-900">{{ fd.fdId }}</div>
                        </div>
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div>
                          <div class="text-sm font-medium text-gray-900">{{ fd.name }}</div>
                          <div class="text-sm text-gray-500">{{ fd.email }}</div>
                        </div>
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        ₹{{ Number(fd.amount).toLocaleString() }}
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {{ fd.interest_rate }}%
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {{ formatDate(fd.mature_date) }}
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <span class="px-2 py-1 text-xs rounded-full" :class="getStatusClass(fd.fdStatus)">
                          {{ fd.fdStatus }}
                        </span>
                      </td>
                      
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- Pagination -->
              <div class="mt-6 flex justify-between items-center">
                <div class="text-sm text-gray-700">
                  Showing {{ ((currentPage - 1) * pageSize) + 1 }} to {{ Math.min(currentPage * pageSize, filteredFDs.length) }} of {{ filteredFDs.length }} results
                </div>
                <div class="flex space-x-2">
                  <button
                    @click="currentPage--"
                    :disabled="currentPage === 1"
                    class="px-3 py-2 border border-gray-300 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
                  >
                    Previous
                  </button>
                  <button
                    @click="currentPage++"
                    :disabled="currentPage * pageSize >= filteredFDs.length"
                    class="px-3 py-2 border border-gray-300 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
                  >
                    Next
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      
</template>

<script>
import AdminSidebar from '../components/AdminSidebar.vue'
import { MenuIcon, Download } from 'lucide-vue-next'
import { mapGetters, mapActions } from 'vuex'
import Navbar from '../components/Navbar.vue'
import axios from '../api'

export default {
  name: 'AdminFixedDeposits',
  components: {
    AdminSidebar,
    Navbar,
    MenuIcon,
    Download
  },
  data() {
    return {
      sidebarCollapsed: false,
      isMobile: false,
      searchTerm: '',
      statusFilter: '',
      sortBy: 'amount',
      currentPage: 1,
      pageSize: 10,
      // Mock data - replace with actual data from store
      mockFDs: [
        {
          id: 1,
          fdNumber: 'FD001',
          customerName: 'John Doe',
          customerEmail: 'john@example.com',
          amount: 50000,
          interestRate: 7.5,
          maturityDate: '2024-12-31',
          status: 'ACTIVE',
          scheme: 'Regular FD'
        },
        {
          id: 2,
          fdNumber: 'FD002',
          customerName: 'Jane Smith',
          customerEmail: 'jane@example.com',
          amount: 100000,
          interestRate: 8.0,
          maturityDate: '2024-11-15',
          status: 'PENDING',
          scheme: 'Senior Citizen FD'
        },
        {
          id: 3,
          fdNumber: 'FD003',
          customerName: 'Mike Johnson',
          customerEmail: 'mike@example.com',
          amount: 75000,
          interestRate: 7.8,
          maturityDate: '2025-01-20',
          status: 'ACTIVE',
          scheme: 'Tax Saver FD'
        }
      ],
      allFixedDeposits: [] // This will hold data from the store
    }
  },
  computed: {
    mainContentClasses() {
      if (this.isMobile) {
        return 'ml-0'
      }
      return this.sidebarCollapsed ? 'ml-30' : 'md:ml-65'
    },
    allFDs() {
      // Use store data if available, otherwise use mock data
      return this.allFixedDeposits
    },
    filteredFDs() {
      let filtered = this.allFDs
      
      if (this.searchTerm) {
        const term = this.searchTerm.toLowerCase()
        filtered = filtered.filter(fd => 
          fd.customerName?.toLowerCase().includes(term) ||
          fd.fdNumber?.toLowerCase().includes(term) ||
          fd.customerEmail?.toLowerCase().includes(term)
        )
      }
      
      if (this.statusFilter) {
        filtered = filtered.filter(fd => fd.fdStatus === this.statusFilter)
      }
      
      // Sort
      if (this.sortBy === 'amount') {
        filtered.sort((a, b) => b.amount - a.amount)
      } else if (this.sortBy === 'date') {
        filtered.sort((a, b) => new Date(b.maturity_date) - new Date(a.maturity_date))
      } else if (this.sortBy === 'customer') {
        filtered.sort((a, b) => a.name.localeCompare(b.name))
      }
      
      return filtered
    },
    paginatedFDs() {
      const start = (this.currentPage - 1) * this.pageSize
      return this.filteredFDs.slice(start, start + this.pageSize)
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
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString()
    },
    getStatusClass(status) {
      const classes = {
        'ACTIVE': 'bg-green-100 text-green-800',
        'MATURED': 'bg-blue-100 text-blue-800',
        'PENDING': 'bg-yellow-100 text-yellow-800',
        'CLOSED': 'bg-red-100 text-red-800'
      }
      return classes[status] || 'bg-gray-100 text-gray-800'
    },
    viewDetails(fd) {
      // Implement view details functionality
      console.log('Viewing FD details:', fd)
      // You can open a modal or navigate to a detail page
    },
    updateStatus(fd) {
      // Implement status update functionality
      console.log('Updating FD status:', fd)
      // You can open a modal or make an API call
    },
    exportData() {
      // Implement export functionality
      console.log('Exporting FD data')
      // Generate CSV or Excel file
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
  async mounted() {
    this.checkMobile()
    window.addEventListener('resize', this.checkMobile)
    
    // Fetch FDs data
    try {
      const res = await axios.get('admin/fds')
      this.allFixedDeposits = res.data
    } catch (error) {
      console.error('Failed to load fixed deposits:', error)
    }
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.checkMobile)
  }
}
</script>