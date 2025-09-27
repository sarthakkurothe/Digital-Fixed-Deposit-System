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
              <Filter class="w-5 h-5 text-gray-600" /> Filter & Search
            </h3>
            <button
              @click="resetFilters"
              class="text-sm text-gray-600 hover:text-gray-800 px-3 py-1 rounded border border-gray-300 hover:border-gray-400 cursor-pointer transition-colors"
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
                <Search class="absolute left-3 top-3 w-4 h-4 text-gray-400" />
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
        <FolderX class="w-16 h-16 mx-auto text-gray-300 mb-4" />
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

    <!-- Improved View FD Modal (replaced the original) -->
    <transition name="modal-fade" appear>
      <div
        v-if="showModal"
        class="fixed inset-0 z-50 flex items-center justify-center px-4 py-8"
        aria-modal="true"
        role="dialog"
        @keydown.esc="closeModal"
      >
        <!-- overlay: clicking background closes modal -->
        <div
          class="absolute inset-0 bg-black/50 backdrop-blur-sm"
          @click.self="closeModal"
        ></div>

        <!-- modal card -->
        <div class="relative bg-white rounded-2xl shadow-2xl w-full max-w-3xl mx-auto z-10 overflow-hidden">
          <!-- header -->
          <div class="flex items-center gap-3 p-5 border-b border-gray-100">
            <div class="flex items-center justify-center w-12 h-12 rounded-xl bg-blue-50 text-blue-600">
              <Info class="w-6 h-6" />
            </div>

            <div class="flex-1">
              <h3 class="text-lg font-semibold text-slate-900">Fixed Deposit Details</h3>
              <p class="text-sm text-slate-500">Quick overview and actions for this FD</p>
            </div>

            <button
              class="p-2 rounded-md text-slate-500 hover:bg-gray-100 hover:text-slate-700 transition cursor-pointer"
              @click="closeModal"
              aria-label="Close details dialog"
            >
              <X class="w-5 h-5" />
            </button>
          </div>

          <!-- content -->
          <div class="p-6 grid grid-cols-1 md:grid-cols-3 gap-6">
            <!-- left column: big numbers -->
            <div class="md:col-span-1 bg-gradient-to-br from-white to-blue-50 p-4 rounded-xl flex flex-col gap-3">
              <div>
                <div class="text-xs text-slate-400 mb-1">FD ID</div>
                <div class="flex items-center gap-2">
                  <div class="font-semibold text-slate-900">#{{ selectedFD?.id ?? '—' }}</div>
                  <button
                    v-if="selectedFD?.id != null"
                    @click="copyToClipboard(String(selectedFD.id), 'FD ID copied')"
                    class="text-slate-400 hover:text-slate-700 p-1 rounded"
                    title="Copy FD ID"
                  >
                    <Clipboard class="w-4 h-4" />
                  </button>
                </div>
              </div>

              <div>
                <div class="text-xs text-slate-400 mb-1">Amount</div>
                <div class="text-2xl font-bold text-slate-900">₹{{ formattedAmount }}</div>
              </div>

              <div>
                <div class="text-xs text-slate-400 mb-1">Estimated Accrued</div>
                <div class="text-lg font-semibold text-green-600">₹{{ formattedAccrued }}</div>
              </div>

              <div>
                <div class="text-xs text-slate-400 mb-1">Status</div>
                <div>
                  <span :class="statusBadgeClass" class="inline-flex items-center gap-2 px-3 py-1 rounded-full text-sm font-medium">
                    <span v-if="selectedFD?.status === 'ACTIVE'">●</span>
                    <span v-else-if="selectedFD?.status === 'PENDING'">●</span>
                    <span v-else-if="selectedFD?.status === 'MATURED'">●</span>
                    {{ selectedFD?.status ?? '—' }}
                  </span>
                </div>
              </div>
            </div>

            <!-- right column: details -->
            <div class="md:col-span-2 grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div class="p-4 rounded-xl bg-white border border-gray-100">
                <div class="text-xs text-slate-400 mb-2 flex items-center gap-2">
                  <Calendar class="w-4 h-4" /> Start Date
                </div>
                <div class="text-sm font-medium text-slate-900">{{ formatDate(selectedFD?.start_date) }}</div>
              </div>

              <div class="p-4 rounded-xl bg-white border border-gray-100">
                <div class="text-xs text-slate-400 mb-2 flex items-center gap-2">
                  <Calendar class="w-4 h-4" /> Maturity Date
                </div>
                <div class="text-sm font-medium text-slate-900">{{ formatDate(selectedFD?.maturity_date) }}</div>
              </div>

              <div class="p-4 rounded-xl bg-white border border-gray-100">
                <div class="text-xs text-slate-400 mb-2 flex items-center gap-2">
                  <Percent class="w-4 h-4" /> Interest Rate
                </div>
                <div class="text-sm font-medium text-slate-900">{{ selectedFD?.interest_rate ?? '—' }}%</div>
              </div>

              <div class="p-4 rounded-xl bg-white border border-gray-100">
                <div class="text-xs text-slate-400 mb-2 flex items-center gap-2">
                  <Clock class="w-4 h-4" /> Tenure
                </div>
                <div class="text-sm font-medium text-slate-900">{{ selectedFD?.tenure_months ?? '—' }} months</div>
              </div>

              <div class="p-4 rounded-xl bg-white border border-gray-100 sm:col-span-2">
                <div class="text-xs text-slate-400 mb-2 flex items-center gap-2">
                  <Tag class="w-4 h-4" /> Remarks
                </div>
                <div class="text-sm text-slate-700">
                  {{ selectedFD?.notes ?? 'No additional remarks' }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <!-- Break FD Modal (unchanged) -->
    <BreakFD
      v-if="showBreakModal"
      :fd-id="selectedFD.id"
      @close="closeBreakModal"
      @fdBroken="handleFdBroken"
    />
  </div>
</template>

<script>
import { mapState } from "vuex";
import BreakFD from "./BreakFD.vue";
import FDCard from "../components/FDCard.vue";

// lucide icons (added ones used by modal)
import { Info, Filter, Search, FolderX, Clipboard, X, Calendar, Percent, Clock, Tag } from "lucide-vue-next";
import { useToast } from "vue-toastification";

export default {
  name: "MyFDs",
  components: { BreakFD, FDCard, Info, Filter, Search, FolderX, Clipboard, X, Calendar, Percent, Clock, Tag },
  data() {
    return {
      showModal: false,
      showBreakModal: false,
      selectedFD: {},
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

      if (this.filters.status) filtered = filtered.filter(fd => fd.status === this.filters.status);
      if (this.filters.amountMin != null) filtered = filtered.filter(fd => fd.amount >= this.filters.amountMin);
      if (this.filters.amountMax != null) filtered = filtered.filter(fd => fd.amount <= this.filters.amountMax);
      if (this.filters.interestMin != null) filtered = filtered.filter(fd => fd.interest_rate >= this.filters.interestMin);
      if (this.filters.interestMax != null) filtered = filtered.filter(fd => fd.interest_rate <= this.filters.interestMax);
      if (this.filters.tenureMin != null) filtered = filtered.filter(fd => fd.tenure_months >= this.filters.tenureMin);
      if (this.filters.tenureMax != null) filtered = filtered.filter(fd => fd.tenure_months <= this.filters.tenureMax);

      filtered.sort((a, b) => {
        switch (this.filters.sortBy) {
          case 'amount_asc': return a.amount - b.amount;
          case 'amount_desc': return b.amount - a.amount;
          case 'interest_asc': return a.interest_rate - b.interest_rate;
          case 'interest_desc': return b.interest_rate - a.interest_rate;
          case 'tenure_asc': return a.tenure_months - b.tenure_months;
          case 'tenure_desc': return b.tenure_months - a.tenure_months;
          case 'maturity_date_asc': return new Date(a.maturity_date) - new Date(b.maturity_date);
          case 'maturity_date_desc': return new Date(b.maturity_date) - new Date(a.maturity_date);
          default: return b.amount - a.amount;
        }
      });

      return filtered;
    },
    filteredActiveCount() { return this.filteredFds.filter(fd => fd.status === "ACTIVE").length; },
    filteredMaturedCount() { return this.filteredFds.filter(fd => fd.status === "MATURED").length; },
    filteredTotalValue() { return this.filteredFds.reduce((sum, fd) => sum + fd.amount, 0); },

    // Modal-related computed properties
    formattedAmount() {
      return this.selectedFD?.amount != null ? Number(this.selectedFD.amount).toLocaleString('en-IN') : '—'
    },
    formattedAccrued() {
      return this.selectedFD?.accrued_interest != null ? Number(this.selectedFD.accrued_interest).toLocaleString('en-IN') : '0'
    },
    statusBadgeClass() {
      const s = this.selectedFD?.status
      if (s === 'ACTIVE') return 'bg-green-50 text-green-700'
      if (s === 'MATURED') return 'bg-blue-50 text-blue-700'
      if (s === 'PENDING') return 'bg-amber-50 text-amber-700'
      if (s === 'BROKEN') return 'bg-red-50 text-red-700'
      return 'bg-gray-100 text-slate-700'
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
            const avgAmount = this.fds.reduce((a, b) => a + b.amount, 0) / this.fds.length;
            this.filters.amountMin = Math.round(avgAmount);
          }
          break;
        case 'maturing_soon':
          if (this.quickFilter === type) {
            this.filters.status = 'ACTIVE';
            this.filters.sortBy = 'maturity_date_asc';
          }
          break;
        case 'high_interest':
          if (this.quickFilter === type) {
            const avgRate = this.fds.reduce((a, b) => a + b.interest_rate, 0) / this.fds.length;
            this.filters.interestMin = Math.round(avgRate * 10) / 10;
            this.filters.sortBy = 'interest_desc';
          }
          break;
      }
      if (this.quickFilter !== type) this.quickFilter = '';
    },

    // Modal open/close
    openViewModal(fd) { this.selectedFD = fd; this.showModal = true; },
    closeModal() { this.showModal = false; this.selectedFD = {}; },

    // Break FD flow (parent already uses this)
    breakFD(fd) { this.selectedFD = fd; this.showBreakModal = true; },
    closeBreakModal() { this.showBreakModal = false; this.selectedFD = {}; },

    async handleFdBroken(fd) {
      const toast = useToast();
      try {
        const response = await this.$store.dispatch('breakFD', fd.id);
        toast[response.success ? 'success' : 'error'](response.message);
        if(response.success){
          this.$store.dispatch("fetchFDs");
        }
        if (response.success) await this.$store.dispatch('fetchFDs');
      } catch (err) {
        toast.error("Something went wrong while breaking the FD.");
      }
      this.closeBreakModal();
    },

    // copy to clipboard used in modal
    async copyToClipboard(text, message = 'Copied') {
      const toast = useToast();
      try {
        await navigator.clipboard.writeText(text);
        toast.success(message);
      } catch (e) {
        toast.error('Copy failed');
      }
    }
  },
  mounted() {
    if(this.$store.getters.getFDs.length === 0) 
      this.$store.dispatch("fetchFDs"); 
  }
};
</script>

<style scoped>
/* Modal enter/leave */
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 220ms ease, transform 220ms ease;
}
.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
  transform: translateY(6px) scale(0.995);
}

</style>
