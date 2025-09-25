<template>
  <div class="p-4 md:p-8 min-h-screen bg-gray-50">
    <div class="mb-8">
      <h1 class="text-3xl md:text-4xl font-bold text-gray-800 mb-2">My Fixed Deposits</h1>
      <p class="text-gray-600">Manage and track all your fixed deposit investments</p>
    </div>

    <div v-if="loading" class="text-center text-gray-500">Loading FDs...</div>

    <div v-else>
      <div class="space-y-8">
        <!-- Summary Cards -->
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center">
            <h3 class="text-sm font-medium text-gray-500 mb-2">Total FDs</h3>
            <span class="text-2xl font-bold text-gray-900">{{ fds.length }}</span>
          </div>
          <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center">
            <h3 class="text-sm font-medium text-gray-500 mb-2">Active</h3>
            <span class="text-2xl font-bold text-green-600">{{ activeCount }}</span>
          </div>
          <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center">
            <h3 class="text-sm font-medium text-gray-500 mb-2">Matured</h3>
            <span class="text-2xl font-bold text-blue-600">{{ maturedCount }}</span>
          </div>
          <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center">
            <h3 class="text-sm font-medium text-gray-500 mb-2">Total Value</h3>
            <span class="text-lg font-bold text-purple-600">
              ₹{{ totalValue.toLocaleString() }}
            </span>
          </div>
        </div>

   <div class="bg-white shadow-sm border border-gray-200 rounded-lg p-6 mt-6">
  <h2 class="text-xl font-semibold text-gray-700 mb-4">FD List</h2>

  <div class="space-y-4">
    <div
      v-for="(fd, index) in fds"
      :key="fd.id"
      class="flex flex-col md:flex-row justify-between items-start md:items-center bg-gray-50 p-4 border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition"
    >
      <!-- Left Side: FD details -->
      <div class="flex flex-col md:flex-row md:items-center gap-4 w-full">
        <div>
          <!-- Serial Number instead of FD ID -->
          <h3 class="text-lg font-semibold text-gray-800">FD #{{ index + 1 }}</h3>
          <p
            :class="fd.status === 'ACTIVE' 
              ? 'text-green-600 text-sm font-medium' 
              : fd.status === 'MATURED' 
                ? 'text-blue-600 text-sm font-medium' 
                : fd.status === 'PENDING' 
                  ? 'text-yellow-500 text-sm font-medium' 
                  : 'text-red-600 text-sm font-medium'"
          >
            {{ fd.status }}
          </p>
        </div>

        <div class="flex flex-wrap gap-6 text-gray-600 text-sm">
          <p><strong>Amount:</strong> ₹{{ fd.amount }}</p>
          <p><strong>Rate:</strong> {{ fd.interest_rate }}%</p>
          <p><strong>Tenure:</strong> {{ fd.tenure_months }} months</p>
          <p><strong>Start:</strong> {{ formatDate(fd.start_date) }}</p>
          <p><strong>Maturity:</strong> {{ formatDate(fd.maturity_date) }}</p>
        </div>
      </div>

      <!-- Right Side: Actions -->
      <div class="flex gap-2 mt-3 md:mt-0">
        <button
          @click="openViewModal(fd)"
          class="px-3 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700 cursor-pointer"
        >
          View
        </button>
        <button
          @click="breakFD(fd)"
          :disabled="fd.status !== 'ACTIVE'"
          :class="[
            'px-3 py-2 bg-red-600 text-white rounded-md text-sm disabled:opacity-50',
            fd.status === 'ACTIVE' ? 'hover:bg-red-700 cursor-pointer' : 'cursor-not-allowed'
          ]"
        >
          Break
        </button>
      </div>
    </div>
  </div>
</div>


    <!-- View FD Modal -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg shadow-lg w-11/12 md:w-1/2 p-6 relative">
        <button
          @click="closeModal"
          class="absolute top-2 right-2 text-gray-500 hover:text-gray-800 text-lg font-bold"
        >
          ✕
        </button>
        <h2 class="text-xl font-semibold text-gray-700 mb-4">FD Details (ID: {{ selectedFD.id }})</h2>
        <ul class="space-y-2 text-gray-600">
          <li><strong>Amount:</strong> ₹{{ selectedFD.amount }}</li>
          <li><strong>Interest Rate:</strong> {{ selectedFD.interest_rate }}%</li>
          <li><strong>Tenure:</strong> {{ selectedFD.tenure_months }} months</li>
          <li><strong>Start Date:</strong> {{ formatDate(selectedFD.start_date) }}</li>
          <li><strong>Maturity Date:</strong> {{ formatDate(selectedFD.maturity_date) }}</li>
          <li><strong>Accrued Interest:</strong> ₹{{ selectedFD.accrued_interest }}</li>
          <li><strong>Status:</strong> {{ selectedFD.status }}</li>
        </ul>
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
        toast.type === 'success' ? 'bg-blue-400' : 'bg-red-500'
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
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import BreakFD from "./BreakFD.vue";

export default {
  name: "MyFDs",
  components: { BreakFD },
  data() {
    return {
      showModal: false,
      showBreakModal: false,
      selectedFD: {},
      toast: { show: false, message: "", type: "success" }
    };
  },
  computed: {
    ...mapState(["fds", "loading"]),
    activeCount() {
      return this.fds.filter(fd => fd.status === "ACTIVE").length;
    },
    maturedCount() {
      return this.fds.filter(fd => fd.status === "MATURED").length;
    },
    totalValue() {
      return this.fds.reduce((sum, fd) => sum + fd.amount, 0);
    }
  },
  methods: {
    formatDate(date) {
      if (!date) return "-";
      return new Date(date).toLocaleDateString();
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

      setTimeout(() => {
        this.toast.show = false;
      }, 4000);
    },
    async handleFdBroken(fd) {
      console.log('Received fd:', fd);

      try {
        const response = await this.$store.dispatch('breakFD', fd.id);
        this.showToast(response.message, response.success ? 'success' : 'error');

        if (response.success) {
          await this.$store.dispatch('fetchFDs');
        }
      } catch (err) {
        console.error('Break FD error:', err);
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