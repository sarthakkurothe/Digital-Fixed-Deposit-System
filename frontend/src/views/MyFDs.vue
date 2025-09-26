<template>
  <div class="p-4 md:p-8 min-h-screen bg-gray-50">
    <!-- Header Section -->
    <div class="text-center space-y-2 mb-8">
      <h1 class="text-3xl md:text-4xl font-bold text-gray-800">My Fixed Deposits</h1>
      <p class="text-gray-600">Manage and track all your fixed deposit investments</p>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="text-center text-gray-500">Loading FDs...</div>

    <!-- Main Content -->
    <div v-else>
      <!-- Summary Cards -->
      <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
        <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center transition-transform transform hover:scale-105 hover:shadow-lg">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Total FDs</h3>
          <span class="text-2xl md:text-3xl font-bold text-gray-900">{{ filteredFds.length }}</span>
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center transition-transform transform hover:scale-105 hover:shadow-lg">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Active</h3>
          <span class="text-2xl md:text-3xl font-bold text-green-600">{{ filteredActiveCount }}</span>
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center transition-transform transform hover:scale-105 hover:shadow-lg">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Matured</h3>
          <span class="text-2xl md:text-3xl font-bold text-blue-600">{{ filteredMaturedCount }}</span>
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center transition-transform transform hover:scale-105 hover:shadow-lg">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Total Value</h3>
          <span class="text-lg md:text-xl font-bold text-green-600 hover:text-green-700 transition-colors">
            ₹{{ filteredTotalValue.toLocaleString() }}
          </span>
        </div>
      </div>

      <!-- Filter Section -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-200 mb-8">
        <div class="p-6">
          <!-- Filter Header -->
          <div class="flex items-center justify-between mb-6">
            <h3 class="text-lg font-semibold text-gray-800 flex items-center gap-2">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.207A1 1 0 013 6.5V4z"/>
              </svg>
              Filter & Search
            </h3>
            <button
              @click="resetFilters"
              class="text-sm text-gray-600 hover:text-gray-800 px-3 py-1 rounded border border-gray-300 hover:border-gray-400 cursor-pointer
              transition-colors"
            >
              Reset All
            </button>
          </div>

          <!-- Filter Controls -->
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
            <!-- Search -->
            <div class="lg:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-2">Search FDs</label>
              <div class="relative">
                <input
                  v-model="filters.search"
                  type="text"
                  placeholder="Search by ID, amount, or any detail..."
                  class="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors"
                />
                <svg class="absolute left-3 top-3 w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                </svg>
              </div>
            </div>

            <!-- Status Filter -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Status</label>
              <select
                v-model="filters.status"
                class="w-full px-3 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors"
              >
                <option value="">All Status</option>
                <option value="ACTIVE">Active</option>
                <option value="MATURED">Matured</option>
                <option value="PENDING">Pending</option>
                <option value="BROKEN">Broken</option>
              </select>
            </div>

            <!-- Sort By -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Sort By</label>
              <select
                v-model="filters.sortBy"
                class="w-full px-3 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors"
              >
                <option value="amount_desc">Amount (High to Low)</option>
                <option value="amount_asc">Amount (Low to High)</option>
                <option value="maturity_date_asc">Maturity Date (Earliest)</option>
                <option value="maturity_date_desc">Maturity Date (Latest)</option>
                <option value="interest_desc">Interest Rate (High to Low)</option>
                <option value="interest_asc">Interest Rate (Low to High)</option>
                <option value="tenure_desc">Tenure (Long to Short)</option>
                <option value="tenure_asc">Tenure (Short to Long)</option>
              </select>
            </div>
          </div>
 
        </div>
      </div>

      <!-- Results Header -->
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-xl md:text-2xl font-bold text-gray-800">
          Fixed Deposits
          <span class="text-lg font-normal text-gray-600">({{ filteredFds.length }} found)</span>
        </h2>
        
        <!-- Quick Filters -->
        <div class="flex gap-2">
          <button
            @click="applyQuickFilter('high_value')"
            :class="[
              'px-3 py-1.5 text-sm rounded-lg border cursor-pointer transition-colors',
              quickFilter === 'high_value' 
                ? 'bg-blue-100 text-blue-700 border-blue-300' 
                : 'bg-white text-gray-600 border-gray-300 hover:bg-gray-50'
            ]"
          >
            High Value
          </button>
          <button
            @click="applyQuickFilter('maturing_soon')"
            :class="[
              'px-3 py-1.5 text-sm rounded-lg border cursor-pointer transition-colors',
              quickFilter === 'maturing_soon' 
                ? 'bg-amber-100 text-amber-700 border-amber-300' 
                : 'bg-white text-gray-600 border-gray-300 hover:bg-gray-50'
            ]"
          >
            Maturing Soon
          </button>
          <button
            @click="applyQuickFilter('high_interest')"
            :class="[
              'px-3 py-1.5 text-sm rounded-lg border cursor-pointer transition-colors',
              quickFilter === 'high_interest' 
                ? 'bg-green-100 text-green-700 border-green-300' 
                : 'bg-white text-gray-600 border-gray-300 hover:bg-gray-50'
            ]"
          >
            High Interest
          </button>
        </div>
      </div>

      <!-- No Results Message -->
      <div v-if="filteredFds.length === 0" class="text-center py-12 bg-white rounded-xl border border-gray-200">
        <svg class="w-16 h-16 mx-auto text-gray-300 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
        </svg>
        <h3 class="text-lg font-medium text-gray-700 mb-2">No FDs Found</h3>
        <p class="text-gray-500 mb-4">Try adjusting your filters to see more results</p>
        <button
          @click="resetFilters"
          class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 cursor-pointer transition-colors"
        >
          Reset Filters
        </button>
      </div>

      <!-- FD Cards Grid -->
      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <FDCard
          v-for="(fd, index) in filteredFds"
          :key="fd.id"
          :fd="fd"
          :index="index"
          @view="openViewModal"
          @break="breakFD"
        />
      </div>
    </div>

    <!-- Modals (unchanged) -->
    <!-- View FD Modal -->
    <div
      v-if="showModal"
      class="fixed inset-0 flex items-center justify-center bg-black/40 z-50"
    >
      <div class="bg-white p-6 rounded-lg w-100 space-y-4">
        <h2 class="text-xl md:text-2xl font-semibold text-gray-700 mb-4 text-center">
          <InfoIcon class="w-5 h-5 text-blue-600" />Fixed Deposit Details</h2>
        <div class="bg-gray-100 p-4 rounded-lg space-y-2 text-base">
          <div class="flex justify-between px-4"><span>ID:</span><span class="font-bold">{{ selectedFD.id }}</span></div>
          <div class="flex justify-between px-4"><span>Amount:</span><span class="font-bold">₹{{ selectedFD.amount.toLocaleString('en-IN') }}</span></div>
          <div class="flex justify-between px-4"><span>Interest Rate:</span><span class="font-bold">{{ selectedFD.interest_rate }}%</span></div>
          <div class="flex justify-between px-4"><span>Tenure:</span><span class="font-bold">{{ selectedFD.tenure_months }} months</span></div>
          <div class="flex justify-between px-4"><span>Start Date:</span><span class="font-bold">{{ formatDate(selectedFD.start_date) }}</span></div>
          <div class="flex justify-between px-4"><span>Maturity Date:</span><span class="font-bold">{{ formatDate(selectedFD.maturity_date) }}</span></div>
          <div class="flex justify-between px-4"><span>Accrued Interest:</span><span class="font-bold">₹{{ selectedFD.accrued_interest.toLocaleString('en-IN') }}</span></div>
          <div class="flex justify-between px-4"><span>Status:</span><span class="font-bold">{{ selectedFD.status }}</span></div>
        </div>
        <div class="flex justify-center">
          <button
            @click="closeModal"
            class="w-1/2 bg-blue-600 hover:bg-blue-700 text-white font-bold px-4 py-2 rounded cursor-pointer"
          >
          Cancel
          </button>
        </div>
      </div>
    </div>

    <!-- Break FD Modal -->
    <BreakFD
      v-if="showBreakModal"
      :fd-id="selectedFD.id"
      @close="closeBreakModal"
      @fdBroken="handleFdBroken"
    />

    <!-- Toast Notification -->
    <div
      v-if="toast.show"
      :class="[
        'fixed top-16 right-4 px-6 py-4 rounded-lg shadow-lg text-white transition-all duration-300 z-50 font-bold',
        toast.type === 'success' ? 'bg-blue-500' : 'bg-red-500'
      ]"
    >
      <div class="flex items-center">
        <svg v-if="toast.type === 'success'" class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
        </svg>
        <svg v-else class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
        </svg>
        {{ toast.message }}
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import BreakFD from "./BreakFD.vue";
import FDCard from "../components/FDCard.vue";
import { InfoIcon } from 'lucide-vue-next'

export default {
  name: "MyFDs",
  components: { BreakFD, FDCard },
  data() {
    return {
      showModal: false,
      showBreakModal: false,
      selectedFD: {},
      toast: { show: false, message: "", type: "success" },
      quickFilter: '',
      filters: {
        search: '',
        status: '',
        sortBy: 'amount_desc',
        amountMin: null,
        amountMax: null,
        interestMin: null,
        interestMax: null,
        tenureMin: null,
        tenureMax: null
      }
    };
  },
  computed: {
    ...mapState(["fds", "loading"]),
    
    filteredFds() {
      let filtered = [...this.fds];

      // Search filter
      if (this.filters.search) {
        const searchLower = this.filters.search.toLowerCase();
        filtered = filtered.filter(fd => 
          fd.id.toString().includes(searchLower) ||
          fd.amount.toString().includes(searchLower) ||
          fd.interest_rate.toString().includes(searchLower) ||
          fd.status.toLowerCase().includes(searchLower) ||
          fd.tenure_months.toString().includes(searchLower)
        );
      }

      // Status filter
      if (this.filters.status) {
        filtered = filtered.filter(fd => fd.status === this.filters.status);
      }

      // Amount range filter
      if (this.filters.amountMin !== null && this.filters.amountMin !== '') {
        filtered = filtered.filter(fd => fd.amount >= this.filters.amountMin);
      }
      if (this.filters.amountMax !== null && this.filters.amountMax !== '') {
        filtered = filtered.filter(fd => fd.amount <= this.filters.amountMax);
      }

      // Interest rate filter
      if (this.filters.interestMin !== null && this.filters.interestMin !== '') {
        filtered = filtered.filter(fd => fd.interest_rate >= this.filters.interestMin);
      }
      if (this.filters.interestMax !== null && this.filters.interestMax !== '') {
        filtered = filtered.filter(fd => fd.interest_rate <= this.filters.interestMax);
      }

      // Tenure filter
      if (this.filters.tenureMin !== null && this.filters.tenureMin !== '') {
        filtered = filtered.filter(fd => fd.tenure_months >= this.filters.tenureMin);
      }
      if (this.filters.tenureMax !== null && this.filters.tenureMax !== '') {
        filtered = filtered.filter(fd => fd.tenure_months <= this.filters.tenureMax);
      }

      // Sorting
      filtered.sort((a, b) => {
        switch (this.filters.sortBy) {
          case 'amount_asc':
            return a.amount - b.amount;
          case 'amount_desc':
            return b.amount - a.amount;
          case 'interest_asc':
            return a.interest_rate - b.interest_rate;
          case 'interest_desc':
            return b.interest_rate - a.interest_rate;
          case 'tenure_asc':
            return a.tenure_months - b.tenure_months;
          case 'tenure_desc':
            return b.tenure_months - a.tenure_months;
          case 'maturity_date_asc':
            return new Date(a.maturity_date) - new Date(b.maturity_date);
          case 'maturity_date_desc':
            return new Date(b.maturity_date) - new Date(a.maturity_date);
          default:
            return b.amount - a.amount;
        }
      });

      return filtered;
    },

    filteredActiveCount() {
      return this.filteredFds.filter(fd => fd.status === "ACTIVE").length;
    },
    
    filteredMaturedCount() {
      return this.filteredFds.filter(fd => fd.status === "MATURED").length;
    },
    
    filteredTotalValue() {
      return this.filteredFds.reduce((sum, fd) => sum + fd.amount, 0);
    }
  },
  
  methods: {
    formatDate(date) {
      if (!date) return "-";
      return new Date(date).toLocaleDateString();
    },
    
    resetFilters() {
      this.filters = {
        search: '',
        status: '',
        sortBy: 'amount_desc',
        amountMin: null,
        amountMax: null,
        interestMin: null,
        interestMax: null,
        tenureMin: null,
        tenureMax: null
      };
      this.quickFilter = '';
    },

    applyQuickFilter(type) {
      this.resetFilters();
      this.quickFilter = this.quickFilter === type ? '' : type;
      
      switch (type) {
        case 'high_value':
          if (this.quickFilter === type) {
            const amounts = this.fds.map(fd => fd.amount);
            const avgAmount = amounts.reduce((a, b) => a + b, 0) / amounts.length;
            this.filters.amountMin = Math.round(avgAmount);
          }
          break;
        case 'maturing_soon':
          if (this.quickFilter === type) {
            const threeMonthsFromNow = new Date();
            threeMonthsFromNow.setMonth(threeMonthsFromNow.getMonth() + 3);
            this.filters.status = 'ACTIVE';
            this.filters.sortBy = 'maturity_date_asc';
          }
          break;
        case 'high_interest':
          if (this.quickFilter === type) {
            const rates = this.fds.map(fd => fd.interest_rate);
            const avgRate = rates.reduce((a, b) => a + b, 0) / rates.length;
            this.filters.interestMin = Math.round(avgRate * 10) / 10;
            this.filters.sortBy = 'interest_desc';
          }
          break;
      }
      
      if (this.quickFilter !== type) {
        this.quickFilter = '';
      }
    },

    openViewModal(fd) {
      this.selectedFD = fd;
      this.showModal = true;
    },
    
    closeModal() {
      this.showModal = false;
      this.selectedFD = {};
    },
    
    breakFD(fd) {
      this.selectedFD = fd;
      this.showBreakModal = true;
    },
    
    closeBreakModal() {
      this.showBreakModal = false;
      this.selectedFD = {};
    },
    
    showToast(message, type = "success") {
      this.toast.message = message;
      this.toast.type = type;
      this.toast.show = true;
      setTimeout(() => this.toast.show = false, 4000);
    },
    
    async handleFdBroken(fd) {
      try {
        const response = await this.$store.dispatch('breakFD', fd.id);
        this.showToast(response.message, response.success ? 'success' : 'error');
        if (response.success) await this.$store.dispatch('fetchFDs');
      } catch (err) {
        this.showToast("Something went wrong while breaking the FD.", "error");
      }
      this.closeBreakModal();
    }
  },
  
  mounted() {
    this.$store.dispatch("fetchFDs");
  }
};
</script>