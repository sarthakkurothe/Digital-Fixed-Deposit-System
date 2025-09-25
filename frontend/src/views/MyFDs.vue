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
                : 'text-gray-500 text-sm font-medium'"
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
          class="px-3 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700"
        >
          View
        </button>
        <button
          @click="breakFD(fd)"
          :disabled="fd.status !== 'ACTIVE'"
          class="px-3 py-2 bg-red-600 text-white rounded-md text-sm hover:bg-red-700 disabled:opacity-50"
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
    </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "MyFDs",
  data() {
    return {
      showModal: false,
      selectedFD: {},
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
      if (confirm(`Are you sure you want to break FD ID ${fd.id}?`)) {
        console.log("Breaking FD:", fd.id);
        alert(`FD ID ${fd.id} has been broken.`);
        fd.status = "BROKEN"; // update locally
      }
    }
  },
  mounted() {
    this.$store.dispatch("fetchFDs");
  }
};
</script>