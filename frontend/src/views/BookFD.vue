<template>
  <div class="p-6 space-y-6 bg-gray-50 min-h-screen">
    <!-- Header -->
    <div class="text-center space-y-2">
      <h1 class="text-3xl font-bold text-gray-800">Book Fixed Deposit</h1>
      <p class="text-gray-600">Secure your savings with competitive interest rates</p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 items-stretch">
      <!-- Left Side - FD Details Form -->
      <div class="space-y-6 h-full">
        <div class="bg-white rounded-lg shadow p-6 h-full flex flex-col">
          <div class="flex items-center mb-4">
            <div class="w-8 h-8 bg-blue-100 rounded mr-3 flex items-center justify-center">
             <IndianRupee />
            </div>
            <h2 class="text-lg font-semibold text-gray-800">FD Details</h2>
          </div>
          <p class="text-gray-600 text-sm mb-6">
            Enter your investment details to book a new Fixed Deposit
          </p>

           <!-- Inputs area should grow -->
          <div class="flex-grow">
          <!-- Investment Amount -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Investment Amount (₹)</label>
            <input
              type="number"
              class="w-full p-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              v-model.number="amount"
              :min="minAmount"
              :max="maxAmount"
              step="1000"
              placeholder="1000"
            />
            <p class="text-gray-500 text-xs mt-1">
              Minimum investment: ₹{{ minAmount.toLocaleString() }}
            </p>
          </div>

          <!-- Interest Scheme Selection -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Interest Scheme</label>
             <SchemeDropdown v-model="selectedScheme" :schemes="schemesToShow" />
            <p v-if="isSenior" class="text-green-600 text-xs mt-1">
              ✓ Senior citizen benefits applicable (+0.5% on all rates)
            </p>
            <p v-if="selectedScheme && selectedScheme.compound" class="text-green-600 text-xs mt-1">
              ✓ Compound Interest benefits applicable
            </p>
          </div>
          </div>

          <!-- Summary + Button pinned at bottom -->
          <div class="mt-auto space-y-4">
          <!-- Summary Details -->
          <div v-if="selectedScheme" class="bg-gray-50 rounded-lg p-4">
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm text-gray-600">Tenure:</span>
              <span class="font-semibold">{{ selectedScheme.tenure }} months</span>
            </div>
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm text-gray-600">Interest Rate:</span>
              <span class="font-semibold text-blue-600">{{ effectiveRate }}% p.a.</span>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-sm text-gray-600">Maturity Date:</span>
              <span class="font-semibold">{{ formattedMaturityDate }}</span>
            </div>
          </div>
        </div>

        <!-- Book FD Button -->
        <button
          @click="bookFD"
          :disabled="!selectedScheme || loading"
          class="w-full bg-blue-600 text-white font-semibold py-4 px-6 rounded-lg hover:bg-blue-700 transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed shadow-lg mt-5"
        >
          <div class="flex items-center justify-center">
            <span v-if="loading" class="loader mr-2"></span>
            <CirclePlus class="mr-2" />
            {{ loading ? "Booking..." : "Book Fixed Deposit" }}
          </div>
        </button>
        </div>
      </div>

      <!-- Right Side - Interest Calculation -->
      <div class="h-full">
        <div class="bg-white rounded-lg shadow p-6 h-full flex flex-col">
          <div class="flex items-center mb-4">
            <div class="w-8 h-8 bg-green-100 rounded mr-3 flex items-center justify-center">
              <ChartPie />
            </div>
            <h3 class="text-lg font-semibold text-gray-800">Preview your investment returns</h3>
          </div>

          <!-- Show Placeholder -->
          <div
            v-if="!selectedScheme"
            class="flex-1 flex items-center justify-center text-gray-500 text-sm"
          >
            <iframe class="w-full h-full" src="https://lottie.host/embed/82ba37c3-3996-4db1-926c-0686019101de/UGGASiUQnm.lottie"></iframe>
          </div>

          <!-- Preview Content -->
          <div v-else class="flex-1 flex flex-col">
            <!-- Investment Overview -->
            <div class="grid grid-cols-2 gap-4 mb-6">
              <div class="bg-blue-50 rounded-lg p-4 text-center">
                <div class="text-2xl font-bold text-blue-600 mb-1">₹</div>
                <div class="text-sm text-gray-600 mb-1">Principal Amount</div>
                <div class="text-xl font-bold text-gray-800">₹{{ formatCurrency(amount) }}</div>
              </div>
              <div class="bg-green-50 rounded-lg p-4 text-center">
                <div class="text-2xl font-bold text-green-600 mb-1">💰</div>
                <div class="text-sm text-gray-600 mb-1">Interest Earned</div>
                <div class="text-xl font-bold text-gray-800">₹{{ formatCurrency(maturityInterest) }}</div>
              </div>
            </div>

            <!-- Donut Chart -->
            <div class="flex flex-col items-center mb-6">
              <div class="relative w-48 h-48 mb-4">
                <svg class="w-full h-full transform -rotate-90" viewBox="0 0 100 100">
                  <circle cx="50" cy="50" r="35" stroke="#e5e7eb" stroke-width="10" fill="none"/>
                  <circle
                    cx="50"
                    cy="50"
                    r="35"
                    stroke="#3b82f6"
                    stroke-width="10"
                    fill="none"
                    :stroke-dasharray="`${principalPercentage * 2.2} 220`"
                    stroke-linecap="round"
                  />
                  <circle
                    cx="50"
                    cy="50"
                    r="35"
                    stroke="#10b981"
                    stroke-width="10"
                    fill="none"
                    :stroke-dasharray="`${interestPercentage * 2.2} 220`"
                    :stroke-dashoffset="`${-(principalPercentage * 2.2)}`"
                    stroke-linecap="round"
                  />
                </svg>
                <div class="absolute inset-0 flex flex-col items-center justify-center">
                  <div class="text-center">
                    <div class="text-sm text-gray-600">Tenure</div>
                    <div class="text-2xl font-bold text-gray-800">{{ selectedScheme.tenure }}</div>
                    <div class="text-xs text-gray-600">months</div>
                  </div>
                </div>
              </div>
              <div class="flex space-x-4 text-sm">
                <div class="flex items-center">
                  <div class="w-3 h-3 bg-blue-500 rounded-full mr-2"></div>
                  <span class="text-gray-600">Principal ({{ principalPercentage.toFixed(1) }}%)</span>
                </div>
                <div class="flex items-center">
                  <div class="w-3 h-3 bg-green-500 rounded-full mr-2"></div>
                  <span class="text-gray-600">Interest ({{ interestPercentage.toFixed(1) }}%)</span>
                </div>
              </div>
            </div>

            <!-- Summary Table -->
            <div class="space-y-3">
              <div class="flex justify-between py-2 border-b border-gray-100">
                <span class="text-gray-600">Principal Amount</span>
                <span class="font-semibold">₹{{ formatCurrency(amount) }}</span>
              </div>
              <div class="flex justify-between py-2 border-b border-gray-100">
                <span class="text-gray-600">Interest Earned</span>
                <span class="font-semibold text-green-600">₹{{ formatCurrency(maturityInterest) }}</span>
              </div>
              <div class="flex justify-between py-3 bg-blue-600 text-white px-4 rounded-lg">
                <span class="font-medium">Total Maturity Amount</span>
                <span class="font-bold">₹{{ formatCurrency(maturityAmount) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Schemes Section -->
    <div class="bg-white rounded-lg shadow p-6">
      <div class="flex items-center mb-4">
        <NotebookTabs />
        <h2 class="text-lg font-semibold text-gray-800">Our Fixed Deposit Schemes</h2>
      </div>
      <p class="text-gray-600 text-sm mb-6">
        Choose from flexible tenures and competitive rates. Senior citizens automatically receive
        an extra <span class="font-semibold">0.5% interest</span>.
      </p>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="scheme in schemesToShow"
          :key="scheme.name"
          class="bg-gray-50 hover:bg-white border border-gray-200 rounded-xl p-5 shadow-sm hover:shadow-lg transition-all duration-300 flex flex-col justify-between"
        >
          <div>
            <div class="flex items-center justify-between mb-2">
              <h3 class="text-base font-semibold text-gray-800">{{ scheme.name }}</h3>
             <span class="bg-green-100 text-green-800 font-bold text-lg px-2 py-1 rounded-full">  {{ scheme.rate }}% </span>

            </div>
            <p class="text-xs text-gray-500 mb-4">Tenure: <span class="font-medium">{{ scheme.tenure }} months</span></p>
            <ul class="space-y-2 text-sm text-gray-700">
              <li class="flex items-start">
               <Check :class="'text-green-500 w-5 h-4'" />
                Guaranteed returns with no market risk
              </li>
              <li v-if="scheme.compound" class="flex items-start">
                <Check :class="'text-green-500 w-5 h-4'" />
                Compound interest for higher growth
              </li>
              <li class="flex items-start">
               <Check :class="'text-green-500 w-5 h-4'" />
                Senior citizens +0.5% extra interest
              </li>
            </ul>
          </div>
          <button
            @click="selectedScheme = scheme"
            class="mt-5 w-full bg-blue-600 text-white font-bold py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors duration-300"
          >
            Choose This Plan
          </button>
        </div>
      </div>
    </div>

    <!-- Toast -->
    <div
      v-if="toast.show"
      :class="{
        'fixed top-18 right-6 z-50 px-6 py-4 rounded-lg shadow-lg text-white transition-all duration-300': true,
        'bg-green-500': toast.type === 'success',
        'bg-red-500': toast.type !== 'success'
      }"
      role="status"
      aria-live="polite"
    >
      <div class="flex items-center"> 
        <Check v-if="toast.type === 'success'"/>
        <X v-else /> 
        {{ toast.message }}
      </div>
    </div>
  </div>
</template>

<script>
import SchemeDropdown from "../components/SchemeDropDown.vue";
import { mapGetters } from "vuex";
import axios from "axios";
import FDCalculator, { STANDARD_FD_SCHEMES } from '../utils/fdCalculations.js';
import { DotLottieVue } from '@lottiefiles/dotlottie-vue'
import { IndianRupee,ChartPie,Check,NotebookTabs,X,CirclePlus } from "lucide-vue-next";

export default {
  name: "BookFD",
  components: { SchemeDropdown, DotLottieVue, IndianRupee ,ChartPie,Check,NotebookTabs,X,CirclePlus },
  data() {
    return {
      amount: 1000,
      minAmount: 1000,
      maxAmount: 1000000,
      startDate: new Date().toISOString().substr(0, 10),
      selectedScheme: "",
      loading: false,
      toast: { show: false, message: "", type: "success" },
      // Using standardized schemes from utility
      baseSchemes: STANDARD_FD_SCHEMES,
    };
  },
  computed: {
    ...mapGetters(["getUser", "getToken"]),
    isSenior() {
      return this.getUser?.age >= 60;
    },
    schemesToShow() {
      return this.baseSchemes.map(scheme => {
        const finalRate = FDCalculator.getApplicableRate(scheme.baseRate, this.getUser?.age || 30);
        return {
          ...scheme,
          rate: finalRate.toFixed(1),
          name: scheme.name,
          tenure: scheme.tenureMonths,
          compound: scheme.hasCompound
        };
      });
    },
    effectiveRate() {
      return this.selectedScheme ? this.selectedScheme.rate : 0;
    },
    calculationResults() {
      if (!this.selectedScheme) {
        return { simple: { interest: 0, maturityAmount: 0 }, compound: { interest: 0, maturityAmount: 0 } };
      }
      
      return FDCalculator.calculateFDReturns({
        principal: this.amount,
        rate: this.selectedScheme.baseRate,
        tenureMonths: this.selectedScheme.tenure,
        age: this.getUser?.age || 30
      });
    },
    maturityInterest() {
      // Use compound interest if eligible, otherwise simple (consistent with Calculator)
      const results = this.calculationResults;
      return results.isCompoundEligible 
        ? results.compound.interest 
        : results.simple.interest;
    },
    maturityAmount() {
      const results = this.calculationResults;
      return results.isCompoundEligible 
        ? results.compound.maturityAmount 
        : results.simple.maturityAmount;
    },
    maturityDate() {
      if (!this.selectedScheme) return "";
      return FDCalculator.getMaturityDate(this.startDate, this.selectedScheme.tenure);
    },
    formattedMaturityDate() {
      if (!this.selectedScheme) return "";
      return this.maturityDate.formattedDate;
    },
    // Chart calculations
    principalPercentage() {
      if (!this.selectedScheme || this.maturityAmount === 0) return 0;
      return (this.amount / this.maturityAmount) * 100;
    },
    interestPercentage() {
      if (!this.selectedScheme || this.maturityAmount === 0) return 0;
      return (this.maturityInterest / this.maturityAmount) * 100;
    },
  },
  methods: {
    formatCurrency(amount) {
      return FDCalculator.formatCurrency(amount);
    },
    async bookFD() {
      if (!this.selectedScheme) return;

      this.loading = true;
      const payload = {
        user_id: this.getUser.id,
        amount: this.amount,  
        tenure_months: this.selectedScheme.tenureMonths || this.selectedScheme.tenure,
        interest_rate: parseFloat(this.effectiveRate)
      };

      try {
        await axios.post("http://localhost:8080/fd/book", payload, {
          headers: { Authorization: `bearer ${this.getToken}` },
        });
        this.showToast("Fixed Deposit booked successfully! 🎉", "success");
        
        // Reset form after successful booking
        setTimeout(() => {
          this.selectedScheme = "";
          this.amount = 1000;
        }, 2000);
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
      }, 4000);
    },
  },
  mounted() {
    this.startDate = new Date().toISOString().substr(0, 10);
  }
};
</script>

<style scoped>
/* Loading spinner */
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

/* Smooth transitions */
.transition-all {
  transition: all 0.3s ease;
}

/* Custom focus styles */
input:focus, select:focus, button:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
}

/* Chart animation */
circle {
  transition: stroke-dasharray 1s ease-in-out, stroke-dashoffset 1s ease-in-out;
}
</style>
