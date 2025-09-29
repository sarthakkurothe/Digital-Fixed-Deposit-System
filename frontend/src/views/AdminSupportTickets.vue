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
          <div
            v-for="ticket in paginatedTickets"
            :key="ticket.id"
            class="bg-white border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow"
          >
            <div class="flex justify-between items-start mb-3">
              <div>
                <h3 class="font-medium text-gray-900">{{ ticket.subject }}</h3>
                <p class="text-sm text-gray-500">{{ ticket.customerName }}</p>
              </div>
              <div class="flex flex-col items-end space-y-1">
                <span class="px-2 py-1 text-xs rounded-full" :class="getStatusClass(ticket.status)">
                  {{ ticket.status }}
                </span>
                <span
                  class="px-2 py-1 text-xs rounded-full"
                  :class="getPriorityClass(ticket.priority)"
                >
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
              <!-- REPLY button (uppercase) -->
              <button
                @click="replyToTicket(ticket)"
                class="flex-1 px-3 py-2 text-purple-600 bg-purple-50 rounded-lg hover:bg-purple-100 transition-colors text-sm cursor-pointer font-semibold"
              >
                REPLY
              </button>
            </div>
          </div>
        </div>

        <!-- Tickets Table (Desktop) -->
        <div class="hidden md:block overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Ticket ID
                </th>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Customer
                </th>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Subject
                </th>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Status
                </th>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Created
                </th>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Actions
                </th>
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
                  <span
                    class="px-2 py-1 text-xs rounded-full"
                    :class="getStatusClass(ticket.status)"
                  >
                    {{ ticket.status }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ formatDate(ticket.createdDate) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                  <!-- REPLY button as a styled button now -->
                  <button
                    @click="replyToTicket(ticket)"
                    :disabled="ticket.status === 'CLOSED'"
                    :class="[
                      'px-3 py-2 rounded-lg font-semibold transition-colors',
                      ticket.status !== 'CLOSED'
                        ? 'bg-purple-50 text-purple-600 hover:bg-purple-100 cursor-pointer'
                        : 'bg-purple-50 text-purple-600 cursor-not-allowed opacity-80',
                    ]"
                  >
                    REPLY
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div class="mt-6 flex justify-between items-center">
          <div class="text-sm text-gray-700">
            Showing {{ (currentPage - 1) * pageSize + 1 }} to
            {{ Math.min(currentPage * pageSize, filteredTickets.length) }} of
            {{ filteredTickets.length }} results
          </div>
          <div class="flex space-x-2">
            <button
              @click="prevPage"
              :disabled="currentPage === 1"
              class="px-3 py-2 border border-gray-300 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50 cursor-pointer"
            >
              Previous
            </button>
            <button
              @click="nextPage"
              :disabled="currentPage * pageSize >= filteredTickets.length"
              class="px-3 py-2 border border-gray-300 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50 cursor-pointer"
            >
              Next
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Ticket Details Modal (improved: header fixed, body scrollable, footer visible; prevents page scroll) -->
    <transition name="modal-fade" appear>
      <div
        v-if="showTicketModal"
        class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 px-4"
        role="dialog"
        aria-modal="true"
        @click.self="closeModal"
      >
        <!-- modal card (flex column) -->
        <div
          class="relative w-full max-w-3xl bg-white rounded-lg shadow-xl overflow-hidden flex flex-col"
          style="max-height: 90vh"
          aria-label="Ticket details"
        >
          <!-- header (fixed height) -->
          <div
            class="flex items-start justify-between gap-4 p-6 border-b border-gray-100 flex-shrink-0"
          >
            <div class="min-w-0">
              <h3 class="text-lg font-bold text-gray-900 truncate">
                Ticket Details - #{{ selectedTicket?.id ?? '—' }}
              </h3>
              <p class="text-sm text-gray-500 mt-1 truncate">{{ selectedTicket?.subject ?? '' }}</p>
            </div>
            <div class="flex items-center gap-2">
              <button
                @click="closeModal"
                class="text-gray-400 hover:text-gray-600 p-2 rounded cursor-pointer"
                aria-label="Close"
              >
                <X class="w-6 h-6" />
              </button>
            </div>
          </div>

          <!-- body (scrollable) -->
          <div class="p-6 space-y-6 overflow-y-auto" style="flex: 1 1 auto">
            <div v-if="selectedTicket" class="space-y-6">
              <!-- Ticket Information -->
              <div class="bg-gray-50 p-4 rounded-lg">
                <h4 class="font-semibold text-gray-900 mb-3">Ticket Information</h4>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                  <div>
                    <p class="text-gray-500">Customer Name:</p>
                    <p class="font-medium">{{ selectedTicket.name }}</p>
                  </div>
                  <div>
                    <p class="text-gray-500">Email:</p>
                    <p class="font-medium">{{ selectedTicket.email }}</p>
                  </div>
                  <div>
                    <p class="text-gray-500">Subject:</p>
                    <p class="font-medium">{{ selectedTicket.subject }}</p>
                  </div>
                  <div>
                    <p class="text-gray-500">Status:</p>
                    <span
                      class="px-2 py-1 text-xs rounded-full"
                      :class="getStatusClass(selectedTicket.status)"
                    >
                      {{ selectedTicket.status }}
                    </span>
                  </div>
                  <div class="md:col-span-2">
                    <p class="text-gray-500">Description:</p>
                    <p class="font-medium">{{ selectedTicket.description }}</p>
                  </div>
                </div>
              </div>

              <!-- FD Details (if applicable) -->
              <div v-if="selectedTicket.fd?.id" class="bg-blue-50 p-4 rounded-lg">
                <h4 class="font-semibold text-gray-900 mb-3">Fixed Deposit Details</h4>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                  <div>
                    <p class="text-gray-500">FD ID:</p>
                    <p class="font-medium">{{ selectedTicket.fd.id }}</p>
                  </div>
                  <div>
                    <p class="text-gray-500">Principal Amount:</p>
                    <p class="font-medium">₹{{ selectedTicket.fd.amount?.toLocaleString() }}</p>
                  </div>
                  <div>
                    <p class="text-gray-500">Interest Rate:</p>
                    <p class="font-medium">{{ selectedTicket.fd.interest_rate }}%</p>
                  </div>
                  <div>
                    <p class="text-gray-500">Status:</p>
                    <p class="font-medium">{{ selectedTicket.fd.status }}</p>
                  </div>
                </div>
                <div class="mt-4" v-if="selectedTicket.fd.status !== 'BROKEN'">
                  <button
                    @click="breakFD(selectedTicket.fd.id)"
                    :disabled="selectedTicket.fd.status !== 'PENDING'"
                    :class="[
                      'px-4 py-2 rounded-lg font-bold transition-colors',
                      selectedTicket.fd.status === 'PENDING'
                        ? 'bg-red-600 text-white hover:bg-red-700 cursor-pointer'
                        : 'bg-red-400 text-white opacity-70 cursor-not-allowed',
                    ]"
                  >
                    Break FD
                  </button>
                </div>
              </div>

              <!-- Admin Response -->
              <div class="bg-green-50 p-4 rounded-lg">
                <h4 class="font-semibold text-gray-900 mb-3">Admin Response</h4>
                <textarea
                  ref="responseBox"
                  v-model="responseText"
                  rows="6"
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 min-h-[140px]"
                  placeholder="Enter your response to the customer..."
                ></textarea>
              </div>
            </div>

            <div v-else class="text-center text-gray-500 py-12">Loading ticket details...</div>
          </div>

          <!-- footer (fixed, visible) -->
          <div
            class="p-4 border-t border-gray-100 flex items-center justify-end gap-3 flex-shrink-0 bg-white"
          >
            <button
              @click="closeModal"
              class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors cursor-pointer font-bold"
            >
              Cancel
            </button>
            <button
              @click="closeTicket"
              :disabled="!responseText.trim()"
              class="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors cursor-pointer font-bold"
            >
              Close Ticket
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import AdminSidebar from '../components/AdminSidebar.vue';
import { MenuIcon, Download, X } from 'lucide-vue-next';
import { mapGetters, mapActions } from 'vuex';
import Navbar from '../components/Navbar.vue';
import axios from '../api';
import { nextTick } from 'vue';
import { useToast } from 'vue-toastification';

export default {
  name: 'AdminSupportTickets',
  components: {
    AdminSidebar,
    MenuIcon,
    Download,
    X,
    Navbar,
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
      allSupportTickets: [],
      allFds: [],
      showTicketModal: false,
      selectedTicket: null,
      responseText: '',
    };
  },
  computed: {
    mainContentClasses() {
      if (this.isMobile) return 'ml-0';
      return this.sidebarCollapsed ? 'ml-30' : 'md:ml-65';
    },
    allTickets() {
      return this.allSupportTickets;
    },
    filteredTickets() {
      let filtered = this.allTickets;

      if (this.searchTerm) {
        const term = this.searchTerm.toLowerCase();
        filtered = filtered.filter(
          ticket =>
            ticket.name?.toLowerCase().includes(term) ||
            ticket.subject?.toLowerCase().includes(term) ||
            ticket.id?.toString().includes(term) ||
            ticket.email?.toLowerCase().includes(term)
        );
      }

      if (this.statusFilter)
        filtered = filtered.filter(ticket => ticket.status === this.statusFilter);
      if (this.priorityFilter)
        filtered = filtered.filter(ticket => ticket.priority === this.priorityFilter);

      if (this.sortBy === 'date') {
        filtered.sort((a, b) => new Date(b.createdDate) - new Date(a.createdDate));
      } else if (this.sortBy === 'status') {
        filtered.sort((a, b) => a.status.localeCompare(b.status));
      }

      return filtered;
    },
    paginatedTickets() {
      const start = (this.currentPage - 1) * this.pageSize;
      return this.filteredTickets.slice(start, start + this.pageSize);
    },
    openTicketsCount() {
      return this.allTickets.filter(ticket => ticket.status === 'OPEN').length;
    },
  },
  methods: {
    ...mapActions(['logout']),
    handleToggleSidebar() {
      if (this.$refs.sidebar) this.$refs.sidebar.toggleSidebar();
    },
    handleSidebarToggle(collapsed) {
      this.sidebarCollapsed = collapsed;
    },
    checkMobile() {
      this.isMobile = window.innerWidth < 768;
    },
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString();
    },
    getStatusClass(status) {
      const classes = {
        OPEN: 'bg-red-100 text-red-800',
        CLOSED: 'bg-gray-100 text-gray-800',
      };
      return classes[status] || 'bg-gray-100 text-gray-800';
    },
    getPriorityClass(priority) {
      const classes = {
        high: 'bg-red-100 text-red-800',
        medium: 'bg-yellow-100 text-yellow-800',
        low: 'bg-green-100 text-green-800',
      };
      return classes[priority] || 'bg-gray-100 text-gray-800';
    },
    replyToTicket(ticket) {
      this.selectedTicket = ticket;
      this.responseText = '';
      this.showTicketModal = true;
    },
    closeModal() {
      this.showTicketModal = false;
      this.selectedTicket = null;
      this.responseText = '';
    },
    prevPage() {
      if (this.currentPage > 1) this.currentPage--;
    },
    nextPage() {
      if (this.currentPage * this.pageSize < this.filteredTickets.length) this.currentPage++;
    },
    async breakFD(fdId) {
      try {
        const res = await axios.put(`/admin/fd/${fdId}`, { status: 'BROKEN' });
        if (this.selectedTicket) {
          this.selectedTicket.fd.status = 'BROKEN';
        }
        this.toast.success('FD has been broken successfully');
      } catch (error) {
        this.toast.error('Failed to break FD: ' + (error.response?.data?.message || error.message));
      }
    },
    async closeTicket() {
      if (!this.responseText.trim()) {
        this.toast.error('Please enter a response before closing the ticket');
        return;
      }

      try {
        const response = await axios.post(
          `/admin/tickets/${this.selectedTicket.id}`,
          this.responseText,
          {
            headers: {
              'Content-Type': 'text/plain',
            },
          }
        );

        // Update the ticket status locally
        const ticketIndex = this.allSupportTickets.findIndex(t => t.id === this.selectedTicket.id);
        if (ticketIndex !== -1) {
          this.allSupportTickets[ticketIndex].status = 'CLOSED';
        }

        this.closeModal();
        this.toast.success('Ticket has been closed successfully');
      } catch (error) {
        this.toast.error(
          'Failed to close ticket: ' + (error.response?.data?.message || error.message)
        );
      }
    },
    async logout() {
      try {
        await this.logout();
        this.$router.push('/login');
      } catch (error) {
        this.toast.error('Logout error');
      }
    },
    /* keyboard handler for ESC to close modal */
    onKeydown(e) {
      if (e.key === 'Escape' && this.showTicketModal) this.closeModal();
    },
  },
  async mounted() {
    this.checkMobile();
    window.addEventListener('resize', this.checkMobile);
    window.addEventListener('keydown', this.onKeydown);

    try {
      const res = await axios.get('/fd/fds');
      this.allFds = res.data;
    } catch (error) {
      // no console logs by request
    }

    // Fetch support tickets data
    try {
      const res = await axios.get('/admin/tickets');
      this.allSupportTickets = res.data;
    } catch (error) {
      // no console logs by request
    }

    // initialize toast
    this.toast = useToast();
  },
  watch: {
    showTicketModal(newVal) {
      // prevent background scrolling while modal is open
      document.body.style.overflow = newVal ? 'hidden' : '';
      if (newVal) {
        // focus textarea next tick
        nextTick(() => {
          const el = this.$refs.responseBox;
          if (el && el.focus) el.focus();
        });
      }
    },
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.checkMobile);
    window.removeEventListener('keydown', this.onKeydown);
    // ensure body scroll is restored
    document.body.style.overflow = '';
  },
};
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* Modal animation */
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition:
    opacity 200ms ease,
    transform 200ms ease;
}
.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
  transform: translateY(8px) scale(0.995);
}
</style>
