<template>
  <div class="p-4 md:p-8 min-h-screen bg-gray-50">
    <div class="max-w-3xl mx-auto bg-white rounded-xl shadow-md p-6">
      <h1 class="text-2xl font-bold text-gray-800 mb-6">Book Fixed Deposit</h1>

      <!-- Deposit Amount -->
      <div class="mb-6">
        <label class="block mb-2 font-medium text-gray-700">Deposit Amount (₹)</label>
        <input
          type="range"
          v-model.number="amount"
          :min="minAmount"
          :max="maxAmount"
          step="1000"
          class="w-full h-3 bg-gray-300 rounded-lg appearance-none cursor-pointer mb-2"
        />
        <div class="flex justify-between text-sm text-gray-600">
          <span>₹{{ minAmount.toLocaleString() }}</span>
          <span>₹{{ maxAmount.toLocaleString() }}</span>
        </div>
        <input
          type="number"
          v-model.number="amount"
          :min="minAmount"
          :max="maxAmount"
          class="w-40 p-2 border rounded-lg mt-3"
        />
      </div>

      <!-- Start Date -->
      <div class="mb-6">
        <label class="block mb-2 font-medium text-gray-700">Start Date</label>
        <input
          type="date"
          v-model="startDate"
          class="w-full p-2 border rounded-lg"
          readonly
        />
      </div>

            <!-- Scheme Selection -->
      <div class="mb-6">
        <label class="block mb-2 font-medium text-gray-700">Select Scheme</label>
        <!-- Senior Citizen Info under dropdown -->
        <p
          v-if="isSenior"
          class="mt-1 text-sm text-blue-600"
        >
          Interest rates for senior citizens are +0.5% higher than normal rates.
        </p>
        <select
          v-model="selectedScheme"
          class="w-full p-2 border rounded-lg"
        >
          <option disabled value="">-- Choose a Scheme --</option>
          <option
            v-for="scheme in schemesToShow"
            :key="scheme.name"
            :value="scheme"
          >
            {{ scheme.name }} ({{ scheme.rate }}%)
          </option>
        </select>
      </div>


      <!-- Calculated Card -->
      <div v-if="selectedScheme" class="mt-8">
        <div class="p-6 border rounded-xl shadow bg-gray-50">
          <h2 class="text-xl font-semibold mb-4 text-gray-800">Maturity Details</h2>
          <p class="mb-2"><span class="font-medium">Interest Rate:</span> {{ effectiveRate }}%</p>
          <p class="mb-2"><span class="font-medium">Tenure:</span> {{ selectedScheme.tenure }} months</p>
          <p class="mb-2"><span class="font-medium">Maturity Date:</span> {{ maturityDate }}</p>
          <p class="mb-2"><span class="font-medium">Maturity Interest:</span> ₹{{ maturityInterest.toLocaleString() }}</p>
          <p class="mb-2"><span class="font-medium">Total Maturity Amount:</span> ₹{{ maturityAmount.toLocaleString() }}</p>

          <!-- Book FD Button -->
          <button
            @click="bookFD"
            :disabled="!selectedScheme || loading"
            class="mt-4 w-full flex justify-center items-center bg-blue-600 text-white font-semibold py-2 rounded-lg hover:bg-blue-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span v-if="loading" class="loader mr-2"></span>
            {{ loading ? "Booking..." : "Book FD" }}
          </button>
        </div>
      </div>
    </div>

    <!-- Toast Notification -->
    <div
      v-if="toast.show"
      :class="['fixed bottom-4 right-4 px-4 py-3 rounded-lg shadow-lg text-white transition-all', toast.type === 'success' ? 'bg-green-600' : 'bg-red-600']"
    >
      {{ toast.message }}
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import axios from "axios";

export default {
  name: "BookFD",
  data() {
    return {
      amount: 50000,
      minAmount: 1000,
      maxAmount: 1000000,
      startDate: new Date().toISOString().substr(0, 10),
      selectedScheme: "",
      loading: false,
      toast: { show: false, message: "", type: "success" },
      schemesNonSenior: [
        { name: "6 Months FD", tenure: 6, rate: 6.0, compound: false },
        { name: "1 Year FD", tenure: 12, rate: 6.5, compound: false },
        { name: "2 Year FD", tenure: 24, rate: 7.0, compound: true },
        { name: "3 Year FD", tenure: 36, rate: 7.5, compound: true },
        { name: "5 Year FD", tenure: 60, rate: 8.0, compound: true },
      ],
      schemesSenior: [
        { name: "6 Months FD", tenure: 6, rate: 6.5, compound: false },
        { name: "1 Year FD", tenure: 12, rate: 7.0, compound: false },
        { name: "2 Year FD", tenure: 24, rate: 7.5, compound: true },
        { name: "3 Year FD", tenure: 36, rate: 8.0, compound: true },
        { name: "5 Year FD", tenure: 60, rate: 8.5, compound: true },
      ],
    };
  },
  computed: {
    ...mapGetters(["getUser", "getToken"]),
    isSenior() {
      return this.getUser?.age >= 60;
    },
    schemesToShow() {
      return this.isSenior ? this.schemesSenior : this.schemesNonSenior;
    },
    effectiveRate() {
      return this.selectedScheme ? this.selectedScheme.rate : 0;
    },
    maturityInterest() {
      if (!this.selectedScheme) return 0;
      const r = this.effectiveRate / 100;
      const years = this.selectedScheme.tenure / 12;
      if (this.selectedScheme.compound) {
        const maturity = this.amount * Math.pow(1 + r, years);
        return Math.round(maturity - this.amount);
      } else {
        return Math.round(this.amount * r * years);
      }
    },
    maturityAmount() {
      return this.amount + this.maturityInterest;
    },
    maturityDate() {
      if (!this.selectedScheme) return "";
      const start = new Date(this.startDate);
      const maturity = new Date(start.setMonth(start.getMonth() + this.selectedScheme.tenure));
      return maturity.toISOString().substr(0, 10);
    },
  },
  methods: {
    async bookFD() {
      if (!this.selectedScheme) return;

      this.loading = true;
      const payload = {
        amount: this.amount,
        start_date: this.startDate,
        scheme_name: this.selectedScheme.name,
        tenure_months: this.selectedScheme.tenure,
        interest_rate: this.effectiveRate,
        maturity_date: this.maturityDate,
        total_amount: this.maturityAmount,
      };

      try {
        await axios.post("https://68d2a364cc7017eec544b1a9.mockapi.io/api/dfd/FixedDeposits", payload, {
          headers: { Authorization: `Bearer ${this.getToken}` },
        });
        this.showToast("FD booked successfully!", "success");
      } catch (error) {
        console.error(error);
        this.showToast("Error booking FD. Please try again.", "error");
      } finally {
        this.loading = false;
      }
    },
    showToast(message, type = "success") {
      this.toast.message = message;
      this.toast.type = type;
      this.toast.show = true;

      setTimeout(() => {
        this.toast.show = false;
      }, 3000); // Hide after 3 seconds
    },
  },
};
</script>

<style scoped>
.loader {
  border: 2px solid #f3f3f3;
  border-top: 2px solid white;
  border-radius: 50%;
  width: 16px;
  height: 16px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
