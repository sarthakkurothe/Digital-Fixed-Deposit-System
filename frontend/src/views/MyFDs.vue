<template>
  <div class="p-4 md:p-8 min-h-screen bg-gray-50">
    <!-- Header Section -->
    <div class="text-center space-y-2 mb-8">
      <h1 class="text-3xl md:text-4xl font-bold text-gray-800">My Fixed Deposits</h1>
      <p class="text-gray-600">Manage and track all your fixed deposit investments</p>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="text-center text-gray-500">Loading FDs...</div>

    <!-- Summary Cards -->
    <div v-else>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
        <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center transition-transform transform hover:scale-105 hover:shadow-lg">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Total FDs</h3>
          <span class="text-2xl md:text-3xl font-bold text-gray-900">{{ fds.length }}</span>
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center transition-transform transform hover:scale-105 hover:shadow-lg">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Active</h3>
          <span class="text-2xl md:text-3xl font-bold text-green-600">{{ activeCount }}</span>
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center transition-transform transform hover:scale-105 hover:shadow-lg">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Matured</h3>
          <span class="text-2xl md:text-3xl font-bold text-blue-600">{{ maturedCount }}</span>
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 text-center transition-transform transform hover:scale-105 hover:shadow-lg">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Total Value</h3>
          <span class="text-lg md:text-xl font-bold text-green-600 hover:text-green-700 transition-colors">
            ₹{{ totalValue.toLocaleString() }}
          </span>
        </div>
      </div>

      <!-- FD Cards Heading -->
      <h2 class="text-xl md:text-3xl font-bold text-gray-800 mb-6 text-center">Current Investments</h2>

      <!-- FD Cards Grid -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <FDCard
          v-for="(fd, index) in fds"
          :key="fd.id"
          :fd="fd"
          :index="index"
          @view="openViewModal"
          @break="breakFD"
        />
      </div>
    </div>

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
