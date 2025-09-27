<template>
        <div class="space-y-6">
          <div class="bg-white rounded-lg border border-gray-200">
            <div class="px-6 py-4 border-b border-gray-200">
              <h1 class="text-lg font-semibold text-gray-900">Support Tickets Management</h1>
            </div>
            
            <div class="p-6">
              <!-- Search and Filter -->
              <div class="mb-6 grid grid-cols-1 md:grid-cols-4 gap-4">
                <input
                  v-model="searchTerm"
                  type="text"
                  placeholder="Search by customer or subject"
                  class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
                <select
                  v-model="statusFilter"
                  class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                >
                  <option value="">All Status</option>
                  <option value="OPEN">Open</option>
                  <option value="CLOSED">CLOSED</option>
                </select>
                <select
                  v-model="sortBy"
                  class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                >
                  <option value="date">Sort by Date</option>
                  <option value="status">Sort by Status</option>
                </select>
              </div>

              <!-- Ticket Cards Grid (Mobile) -->
              <div class="md:hidden space-y-4">
                <div v-for="ticket in paginatedTickets" :key="ticket.id" 
                     class="bg-white border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow">
                  <div class="flex justify-between items-start mb-3">
                    <div>
                      <h3 class="font-medium text-gray-900">{{ ticket.subject }}</h3>
                      <p class="text-sm text-gray-500">{{ ticket.customerName }}</p>
                    </div>
                    <div class="flex flex-col items-end space-y-1">
                      <span class="px-2 py-1 text-xs rounded-full" :class="getStatusClass(ticket.status)">
                        {{ ticket.status }}
                      </span>
                      <span class="px-2 py-1 text-xs rounded-full" :class="getPriorityClass(ticket.priority)">
                        {{ ticket.priority }}
                      </span>
                    </div>
                  </div>
                  
                  <div class="text-sm text-gray-600 mb-3 line-clamp-2">
                    {{ ticket.description }}
                  </div>
                  
                  <div class="grid grid-cols-2 gap-4 text-sm mb-4">
                    <div>
                      <p class="text-gray-500">Ticket ID</p>
                      <p class="font-medium">#{{ ticket.ticketNumber }}</p>
                    </div>
                    <div>
                      <p class="text-gray-500">Created</p>
                      <p class="font-medium">{{ formatDate(ticket.createdAt) }}</p>
                    </div>
                  </div>
                  
                  <div class="flex space-x-2">
                    <button
                      @click="viewTicket(ticket)"
                      class="flex-1 px-3 py-2 text-blue-600 bg-blue-50 rounded-lg hover:bg-blue-100 transition-colors text-sm"
                    >
                      View
                    </button>
                    <button
                      @click="updateTicketStatus(ticket)"
                      class="flex-1 px-3 py-2 text-green-600 bg-green-50 rounded-lg hover:bg-green-100 transition-colors text-sm"
                    >
                      Update
                    </button>
                    <button
                      @click="replyToTicket(ticket)"
                      class="flex-1 px-3 py-2 text-purple-600 bg-purple-50 rounded-lg hover:bg-purple-100 transition-colors text-sm"
                    >
                      Reply
                    </button>
                  </div>
                </div>
              </div>

              <!-- Tickets Table (Desktop) -->
              <div class="hidden md:block overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200">
                  <thead class="bg-gray-50">
                    <tr>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ticket ID</th>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer</th>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Subject</th>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created</th>
                      <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    <tr v-for="ticket in paginatedTickets" :key="ticket.id" class="hover:bg-gray-50">
                      <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        #{{ ticket.id }}
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div>
                          <div class="text-sm font-medium text-gray-900">{{ ticket.name }}</div>
                          <div class="text-sm text-gray-500">{{ ticket.email }}</div>
                        </div>
                      </td>
                      <td class="px-6 py-4 max-w-xs">
                        <div class="text-sm text-gray-900">{{ ticket.subject }}</div>
                        <div class="text-sm text-gray-500 truncate">{{ ticket.description }}</div>
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <span class="px-2 py-1 text-xs rounded-full" :class="getStatusClass(ticket.status)">
                          {{ ticket.status }}
                        </span>
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {{ formatDate(ticket.createdDate) }}
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                        <button
                          @click="viewTicket(ticket)"
                          class="text-blue-600 hover:text-blue-900"
                        >
                          View
                        </button>
                        <button
                          @click="replyToTicket(ticket)"
                          class="text-purple-600 hover:text-purple-900"
                        >
                          Reply
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- Pagination -->
              <div class="mt-6 flex justify-between items-center">
                <div class="text-sm text-gray-700">
                  Showing {{ ((currentPage - 1) * pageSize) + 1 }} to {{ Math.min(currentPage * pageSize, filteredTickets.length) }} of {{ filteredTickets.length }} results
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
                    :disabled="currentPage * pageSize >= filteredTickets.length"
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
  name: 'AdminSupportTickets',
  components: {
    AdminSidebar,
    MenuIcon,
    Download,
    Navbar
  },
  data() {
    return {
      sidebarCollapsed: false,
      isMobile: false,
      searchTerm: '',
      statusFilter: '',
      priorityFilter: '',
      sortBy: 'date',
      currentPage: 1,
      pageSize: 10,
      // Mock data - replace with actual data from store
      mockTickets: [
        {
          id: 1,
          ticketNumber: 'T001',
          customerName: 'John Doe',
          customerEmail: 'john@example.com',
          subject: 'FD Maturity Query',
          description: 'I would like to know when my FD will mature and what will be the final amount.',
          priority: 'high',
          status: 'open',
          createdAt: '2024-09-20T10:00:00Z'
        },
        {
          id: 2,
          ticketNumber: 'T002',
          customerName: 'Jane Smith',
          customerEmail: 'jane@example.com',
          subject: 'Interest Rate Question',
          description: 'What is the current interest rate for senior citizen FDs?',
          priority: 'medium',
          status: 'in-progress',
          createdAt: '2024-09-19T14:30:00Z'
        },
        {
          id: 3,
          ticketNumber: 'T003',
          customerName: 'Mike Johnson',
          customerEmail: 'mike@example.com',
          subject: 'Account Update Request',
          description: 'I need to update my contact information linked to my FD account.',
          priority: 'low',
          status: 'resolved',
          createdAt: '2024-09-18T09:15:00Z'
        },
        {
          id: 4,
          ticketNumber: 'T004',
          customerName: 'Sarah Wilson',
          customerEmail: 'sarah@example.com',
          subject: 'Premature Closure',
          description: 'I want to close my FD before maturity. What are the penalty charges?',
          priority: 'high',
          status: 'open',
          createdAt: '2024-09-25T16:45:00Z'
        }
      ],
      allSupportTickets: []
    }
  },
  computed: {
    mainContentClasses() {
      if (this.isMobile) {
        return 'ml-0'
      }
      return this.sidebarCollapsed ? 'ml-30' : 'md:ml-65'
    },
    allTickets() {
      // Use store data if available, otherwise use mock data
      return this.allSupportTickets
    },
    filteredTickets() {
      let filtered = this.allTickets
      
      if (this.searchTerm) {
        const term = this.searchTerm.toLowerCase()
        filtered = filtered.filter(ticket => 
          ticket.customerName?.toLowerCase().includes(term) ||
          ticket.subject?.toLowerCase().includes(term) ||
          ticket.ticketNumber?.toLowerCase().includes(term) ||
          ticket.customerEmail?.toLowerCase().includes(term)
        )
      }
      
      if (this.statusFilter) {
        filtered = filtered.filter(ticket => ticket.status === this.statusFilter)
      }
      
      if (this.priorityFilter) {
        filtered = filtered.filter(ticket => ticket.priority === this.priorityFilter)
      }
      
      // Sort
      if (this.sortBy === 'date') {
        filtered.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
      } else if (this.sortBy === 'status') {
        filtered.sort((a, b) => a.status.localeCompare(b.status))
      }
      
      return filtered
    },
    paginatedTickets() {
      const start = (this.currentPage - 1) * this.pageSize
      return this.filteredTickets.slice(start, start + this.pageSize)
    },
    openTicketsCount() {
      return this.allTickets.filter(ticket => ticket.status === 'OPEN').length
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
        'OPEN': 'bg-red-100 text-red-800',
        'CLOSED': 'bg-gray-100 text-gray-800'
      }
      return classes[status] || 'bg-gray-100 text-gray-800'
    },
    getPriorityClass(priority) {
      const classes = {
        'high': 'bg-red-100 text-red-800',
        'medium': 'bg-yellow-100 text-yellow-800',
        'low': 'bg-green-100 text-green-800'
      }
      return classes[priority] || 'bg-gray-100 text-gray-800'
    },
    viewTicket(ticket) {
      // Implement view ticket functionality
      console.log('Viewing ticket:', ticket)
      // You can open a modal or navigate to a detail page
    },
    updateTicketStatus(ticket) {
      // Implement status update functionality
      console.log('Updating ticket status:', ticket)
      // You can open a modal or make an API call
    },
    replyToTicket(ticket) {
      // Implement reply functionality
      console.log('Replying to ticket:', ticket)
      // You can open a modal with reply form
    },
    exportTickets() {
      // Implement export functionality
      console.log('Exporting tickets')
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
    
    // Fetch support tickets data
    try {
      const res = await axios.get('/admin/tickets')
      this.allSupportTickets = res.data
    } catch (error) {
      console.error('Failed to load support tickets:', error)
    }
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.checkMobile)
  }
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>