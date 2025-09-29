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

          <!-- Inputs area -->
          <div class="flex-grow">
            <!-- Investment Amount -->
            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700 mb-2"
                >Investment Amount (₹)</label
              >
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
              <div class="mt-2 text-sm text-red-600" v-if="amountError">
                {{ amountError }}
              </div>
            </div>

            <!-- Interest Scheme Dropdown -->
            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700 mb-2">Interest Scheme</label>
              <SchemeDropdown v-model="selectedScheme" :schemes="schemesToShow" />
              <p v-if="isSenior" class="text-green-600 text-xs mt-1">
                ✓ Senior citizen benefits applicable (+0.5% on all rates)
              </p>
              <p
                v-if="selectedScheme && selectedScheme.hasCompound"
                class="text-green-600 text-xs mt-1"
              >
                ✓ Compound Interest benefits applicable
              </p>
            </div>
          </div>

          <!-- Summary + Book Button -->
          <div class="mt-auto space-y-4">
            <div v-if="selectedScheme" class="bg-gray-50 rounded-lg p-4">
              <div class="flex items-center justify-between mb-2">
                <span class="text-sm text-gray-600">Tenure:</span>
                <span class="font-semibold">{{ selectedScheme.tenureMonths }} months</span>
              </div>
              <div class="flex items-center justify-between mb-2">
                <span class="text-sm text-gray-600">Interest Rate:</span>
                <span class="font-semibold text-blue-600"
                  >{{ Number(effectiveRate).toFixed(1) }}% p.a.</span
                >
              </div>
              <div class="flex items-center justify-between">
                <span class="text-sm text-gray-600">Maturity Date:</span>
                <span class="font-semibold">{{ formattedMaturityDate }}</span>
              </div>
            </div>

            <div class="flex">
              <button
                @click="openConfirmModal"
                :disabled="isBookDisabled"
                class="w-full bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed shadow-lg h-14 flex items-center justify-center cursor-pointer"
              >
                <div class="flex items-center justify-center">
                  <span v-if="loading" class="loader mr-2"></span>
                  <CirclePlus class="mr-2" />
                  {{ loading ? 'Booking...' : 'Book Fixed Deposit' }}
                </div>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Side - Investment Preview -->
      <div class="h-full">
        <div class="bg-white rounded-lg shadow p-6 h-full flex flex-col">
          <div class="flex items-center mb-4">
            <div class="w-8 h-8 bg-green-100 rounded mr-3 flex items-center justify-center">
              <ChartPie />
            </div>
            <h3 class="text-lg font-semibold text-gray-800">Preview your investment returns</h3>
          </div>

          <!-- Placeholder -->
          <div
            v-if="!selectedScheme"
            class="flex-1 flex items-center justify-center text-gray-500 text-sm"
          >
            <iframe
              class="w-full h-full"
              src="https://lottie.host/embed/82ba37c3-3996-4db1-926c-0686019101de/UGGASiUQnm.lottie"
            ></iframe>
          </div>

          <!-- Preview Content -->
          <div v-else class="flex-1 flex flex-col">
            <div class="grid grid-cols-2 gap-4 mb-6">
              <div class="bg-blue-50 rounded-lg p-4 text-center">
                <div class="text-2xl font-bold text-blue-600 mb-1">₹</div>
                <div class="text-sm text-gray-600 mb-1">Principal Amount</div>
                <div class="text-xl font-bold text-gray-800">₹{{ formatCurrency(amount) }}</div>
              </div>
              <div class="bg-green-50 rounded-lg p-4 text-center">
                <div class="text-2xl font-bold text-green-600 mb-1">💰</div>
                <div class="text-sm text-gray-600 mb-1">Interest Earned</div>
                <div class="text-xl font-bold text-gray-800">
                  ₹{{ formatCurrency(maturityInterest) }}
                </div>
              </div>
            </div>

            <!-- Donut Chart -->
            <div class="flex flex-col items-center mb-6">
              <ChartDonut
                :principal="amount"
                :interest="maturityInterest"
                :size="192"
                :tenure="selectedScheme.tenureMonths"
              />
              <div class="flex space-x-4 text-sm mt-3">
                <div class="flex items-center">
                  <div class="w-3 h-3 bg-blue-500 rounded-full mr-2"></div>
                  <span class="text-gray-600"
                    >Principal ({{ principalPercentage.toFixed(1) }}%)</span
                  >
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
                <span class="font-semibold text-green-600"
                  >₹{{ formatCurrency(maturityInterest) }}</span
                >
              </div>
              <div
                class="flex justify-between items-center h-14 bg-blue-600 text-white px-4 rounded-lg"
              >
                <span class="font-medium">Total Maturity Amount</span>
                <span class="font-bold text-lg">₹{{ formatCurrency(maturityAmount) }}</span>
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
        Choose from flexible tenures and competitive rates. Senior citizens automatically receive an
        extra <span class="font-semibold">0.5% interest</span>.
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
              <span class="bg-green-100 text-green-800 font-bold text-lg px-2 py-1 rounded-full">
                {{ scheme.rate }}%
              </span>
            </div>
            <p class="text-xs text-gray-500 mb-4">
              Tenure: <span class="font-medium">{{ scheme.tenureMonths }} months</span>
            </p>
            <ul class="space-y-2 text-sm text-gray-700">
              <li class="flex items-start">
                <Check class="text-green-500 w-5 h-4" />
                <span class="ml-2">Guaranteed returns with no market risk</span>
              </li>
              <li v-if="scheme.hasCompound" class="flex items-start">
                <Check class="text-green-500 w-5 h-4" />
                <span class="ml-2">Compound interest for higher growth</span>
              </li>
              <li class="flex items-start">
                <Check class="text-green-500 w-5 h-4" />
                <span class="ml-2">Senior citizens +0.5% extra interest</span>
              </li>
            </ul>
          </div>
          <button
            @click="selectedScheme = scheme"
            class="mt-5 w-full bg-blue-600 text-white font-bold py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors duration-300 cursor-pointer"
          >
            Choose This Plan
          </button>
        </div>
      </div>
    </div>

    <!-- Confirmation Modal (refactored with animation, icons, blur backdrop) -->
    <transition name="modal-scale" appear>
      <div
        v-if="showConfirmModal"
        class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm px-4"
        @click.self="closeConfirmModal"
        role="dialog"
        aria-modal="true"
        aria-label="Confirm fixed deposit"
      >
        <div
          class="w-full max-w-md bg-white rounded-2xl shadow-2xl overflow-hidden transform transition-all"
          :class="{ 'scale-100': showConfirmModal }"
        >
          <!-- header -->
          <div class="flex items-center justify-between p-6 border-b border-gray-100">
            <div class="flex items-center gap-3 min-w-0">
              <NotebookTabs class="w-6 h-6 text-blue-600 flex-shrink-0" />
              <div class="min-w-0">
                <h3 class="text-lg font-bold text-gray-900 truncate">Confirm Fixed Deposit</h3>
                <p class="text-sm text-gray-500 truncate">Review details before confirming</p>
              </div>
            </div>

            <div class="flex items-center gap-2">
              <button
                @click="closeConfirmModal"
                class="p-2 rounded hover:bg-gray-100 text-gray-600 cursor-pointer"
                aria-label="Close modal"
              >
                <X class="w-5 h-5" />
              </button>
            </div>
          </div>

          <!-- body -->
          <div class="p-6 space-y-4">
            <div class="space-y-3 text-gray-700">
              <div class="flex justify-between items-center">
                <div class="flex items-center gap-2">
                  <IndianRupee class="w-4 h-4 text-blue-600" />
                  <span class="text-sm text-gray-600">Investment Amount</span>
                </div>
                <div class="font-medium">₹{{ formatCurrency(amount) }}</div>
              </div>

              <div class="flex justify-between items-center">
                <div class="flex items-center gap-2">
                  <Clock class="w-4 h-4 text-purple-600" />
                  <span class="text-sm text-gray-600">Tenure</span>
                </div>
                <div class="font-medium">{{ selectedScheme.tenureMonths }} months</div>
              </div>

              <div class="flex justify-between items-center">
                <div class="flex items-center gap-2">
                  <Percent class="w-4 h-4 text-green-600" />
                  <span class="text-sm text-gray-600">Interest Rate</span>
                </div>
                <div class="font-medium">{{ Number(effectiveRate).toFixed(1) }}% p.a.</div>
              </div>

              <div class="flex justify-between items-center">
                <div class="flex items-center gap-2">
                  <Calendar class="w-4 h-4 text-orange-600" />
                  <span class="text-sm text-gray-600">Maturity Date</span>
                </div>
                <div class="font-medium">{{ formattedMaturityDate }}</div>
              </div>

              <div class="flex justify-between items-center">
                <div class="flex items-center gap-2">
                  <TrendingUp class="w-4 h-4 text-yellow-600" />
                  <span class="text-sm text-gray-600">Expected Interest</span>
                </div>
                <div class="font-semibold text-green-600">
                  ₹{{ formatCurrency(maturityInterest) }}
                </div>
              </div>

              <div
                class="flex items-center justify-between p-3 rounded-lg bg-blue-50 border border-blue-100"
              >
                <div class="flex items-center gap-2 text-blue-700 font-semibold">
                  <Wallet class="w-4 h-4" />
                  <span>Total Maturity Amount</span>
                </div>
                <div class="text-blue-700 font-bold text-lg">
                  ₹{{ formatCurrency(maturityAmount) }}
                </div>
              </div>
            </div>
          </div>

          <!-- footer -->
          <div class="p-4 border-t border-gray-100 flex justify-end gap-3 bg-white">
            <button
              @click="closeConfirmModal"
              class="px-4 py-2 rounded-lg border border-gray-300 hover:bg-gray-100 transition-colors cursor-pointer font-bold flex items-center gap-2"
            >
              <XCircle class="w-4 h-4 text-gray-600" />
              Cancel
            </button>
            <button
              ref="confirmBtn"
              @click="confirmBookFD"
              class="px-4 py-2 rounded-lg bg-blue-600 text-white hover:bg-blue-700 transition-colors cursor-pointer font-bold flex items-center gap-2"
            >
              <CheckCircle class="w-4 h-4" />
              Confirm
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import SchemeDropdown from '../components/SchemeDropDown.vue';
import ChartDonut from '../components/ChartDonut.vue';
import { mapGetters } from 'vuex';
import axios from 'axios';
import FDCalculator, { STANDARD_FD_SCHEMES } from '../utils/fdCalculations.js';
import { DotLottieVue } from '@lottiefiles/dotlottie-vue';
import {
  IndianRupee,
  ChartPie,
  Check,
  NotebookTabs,
  CirclePlus,
  X,
  XCircle,
  CheckCircle,
  Clock,
  Percent,
  Calendar,
  TrendingUp,
  Wallet,
} from 'lucide-vue-next';
import { useToast } from 'vue-toastification';
import { nextTick } from 'vue';

export default {
  name: 'BookFD',
  components: {
    SchemeDropdown,
    ChartDonut,
    DotLottieVue,
    IndianRupee,
    ChartPie,
    Check,
    NotebookTabs,
    CirclePlus,
    X,
    XCircle,
    CheckCircle,
    Clock,
    Percent,
    Calendar,
    TrendingUp,
    Wallet,
  },
  data() {
    return {
      amount: 1000,
      minAmount: 1000,
      maxAmount: 10000000,
      selectedScheme: null,
      loading: false,
      baseSchemes: STANDARD_FD_SCHEMES,
      amountError: '',
      showConfirmModal: false,
    };
  },
  watch: {
    amount(newVal) {
      if (newVal < this.minAmount) {
        this.amountError = `Amount must be at least ₹${this.minAmount.toLocaleString('en-IN')}`;
      } else if (newVal > this.maxAmount) {
        this.amountError = `Amount must not exceed ₹${this.maxAmount.toLocaleString('en-IN')}`;
      } else {
        this.amountError = '';
      }
    },
    showConfirmModal(newVal) {
      // focus confirm button when modal opens and prevent body scroll
      document.body.style.overflow = newVal ? 'hidden' : '';
      if (newVal) {
        nextTick(() => {
          const el = this.$refs.confirmBtn;
          if (el && el.focus) el.focus();
        });
      }
    },
  },
  computed: {
    ...mapGetters(['getUser', 'getToken']),
    isSenior() {
      return this.getUser?.age >= 60;
    },
    schemesToShow() {
      return this.baseSchemes.map(scheme => {
        const finalRate = FDCalculator.getApplicableRate(scheme.baseRate, this.getUser?.age || 30);
        return { ...scheme, rate: Number(finalRate.toFixed(1)) };
      });
    },
    effectiveRate() {
      return this.selectedScheme ? this.selectedScheme.rate : 0;
    },
    calculationResults() {
      if (!this.selectedScheme)
        return {
          simple: { interest: 0, maturityAmount: 0 },
          compound: { interest: 0, maturityAmount: 0 },
          isCompoundEligible: false,
        };
      return FDCalculator.calculateFDReturns({
        principal: this.amount,
        rate: this.selectedScheme.baseRate,
        tenureMonths: this.selectedScheme.tenureMonths,
        age: this.getUser?.age || 30,
      });
    },
    maturityInterest() {
      const res = this.calculationResults;
      return res.isCompoundEligible ? res.compound.interest : res.simple.interest;
    },
    maturityAmount() {
      const res = this.calculationResults;
      return res.isCompoundEligible ? res.compound.maturityAmount : res.simple.maturityAmount;
    },
    principalPercentage() {
      return this.maturityAmount ? (this.amount / this.maturityAmount) * 100 : 0;
    },
    interestPercentage() {
      return this.maturityAmount ? (this.maturityInterest / this.maturityAmount) * 100 : 0;
    },
    formattedMaturityDate() {
      if (!this.selectedScheme) return '';
      const today = new Date();
      today.setMonth(today.getMonth() + this.selectedScheme.tenureMonths);
      return today.toLocaleDateString();
    },
    isBookDisabled() {
      return (
        !this.selectedScheme ||
        this.loading ||
        this.amount < this.minAmount ||
        this.amount > this.maxAmount
      );
    },
  },
  setup() {
    const toast = useToast();
    return { toast };
  },
  methods: {
    openConfirmModal() {
      if (this.isBookDisabled) return;
      this.showConfirmModal = true;
    },
    closeConfirmModal() {
      this.showConfirmModal = false;
    },
    async confirmBookFD() {
      this.showConfirmModal = false;
      await this.bookFD();
    },
    formatCurrency(amount) {
      return FDCalculator.formatCurrency(amount);
    },
    async bookFD() {
      if (!this.selectedScheme) return;
      this.loading = true;
      const payload = {
        user_id: this.getUser.id,
        amount: this.amount,
        tenure_months: this.selectedScheme.tenureMonths,
        interest_rate: parseFloat(this.effectiveRate),
      };
      try {
        await axios.post('http://localhost:8080/fd/book', payload, {
          headers: { Authorization: `bearer ${this.getToken}` },
        });
        await this.$store.dispatch('fetchFDs');
        this.toast.success('Fixed Deposit booked successfully! 🎉');
        setTimeout(() => {
          this.selectedScheme = null;
          this.amount = 1000;
        }, 1200);
      } catch (error) {
        console.error(error);
        this.toast.error('Error booking FD. Please try again.');
      } finally {
        this.loading = false;
      }
    },
  },
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
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* Donut chart animation */
circle {
  transition:
    stroke-dasharray 1s ease-in-out,
    stroke-dashoffset 1s ease-in-out;
}

/* Modal scale + fade animation */
.modal-scale-enter-active,
.modal-scale-leave-active {
  transition:
    opacity 220ms ease,
    transform 220ms cubic-bezier(0.2, 0.9, 0.2, 1);
}
.modal-scale-enter-from,
.modal-scale-leave-to {
  opacity: 0;
  transform: translateY(8px) scale(0.98);
}
</style>
