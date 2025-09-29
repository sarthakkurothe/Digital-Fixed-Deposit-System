<template>
  <div class="p-6 space-y-6 bg-gray-50 min-h-screen">
    <!-- Header -->
    <div class="text-center space-y-2">
      <h1 class="text-3xl font-bold text-gray-800">Interest Calculator</h1>
      <p class="text-gray-600">Calculate returns on your fixed deposit investments</p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <!-- Left Side - Calculator Form -->
      <div class="space-y-6">
        <div class="bg-white rounded-xl shadow-xl p-8 flex flex-col">
          <div class="flex items-center mb-4">
            <CircleDollarSign class="w-6 h-6 text-blue-600 mr-3" />
            <h2 class="text-lg font-semibold text-gray-800">FD Calculator</h2>
          </div>
          <p class="text-gray-600 text-sm mb-6">
            Enter your investment details to calculate returns
          </p>

          <!-- Age Input -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Your Age</label>
            <p class="w-full p-3 border border-gray-200 rounded-lg">
              {{ age }}
            </p>
            <p v-if="isSeniorCitizen" class="text-green-600 text-xs mt-1">
              ✓ Senior citizen benefits applicable (+0.5% on all rates)
            </p>
          </div>

          <!-- Scheme Selection -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Interest Scheme</label>
            <SchemeDropdown
              v-model="selectedScheme"
              :schemes="schemesToShow"
              placeholder="Select Scheme"
            />
            <p v-if="hasCompoundInterest" class="text-green-600 text-xs mt-1">
              ✓ Compound Interest benefits applicable (24+ months tenure)
            </p>
          </div>

          <!-- Investment Amount -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2"
              >Investment Amount (₹)</label
            >
            <input
              type="number"
              class="w-full p-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              v-model.number="amount"
              min="1000"
              step="1000"
              placeholder="100000"
            />
          </div>

          <!-- Investment Summary -->
          <div v-if="selectedScheme" class="bg-gray-50 rounded-lg p-4 mt-4">
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm text-gray-600">Principal:</span>
              <span class="font-semibold">₹{{ formatCurrency(amount) }}</span>
            </div>
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm text-gray-600">Rate:</span>
              <span class="font-semibold">{{ effectiveRate }}% p.a.</span>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-sm text-gray-600">Tenure:</span>
              <span class="font-semibold">{{ selectedScheme.tenure }} months</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Side - Results -->
      <div class="space-y-6">
        <!-- Interest Breakdown -->
        <div class="bg-white rounded-xl shadow-xl p-6 flex flex-col">
          <div class="flex items-center mb-4">
            <TrendingUp class="w-6 h-6 text-green-600 mr-3" />
            <h3 class="text-lg font-semibold text-gray-800">Interest Calculation Breakdown</h3>
          </div>
          <p class="text-gray-600 text-sm mb-6">Detailed view of your FD returns</p>

          <!-- Interest Cards -->
          <div class="grid grid-cols-2 gap-4 mb-6">
            <div class="bg-green-50 rounded-lg p-4 text-center">
              <div class="text-2xl font-bold text-green-600 mb-1">💰</div>
              <div class="text-sm text-gray-600 mb-1">Interest Earned</div>
              <div class="text-xl font-bold text-gray-800">
                ₹{{ formatCurrency(bestMaturityInterest) }}
              </div>
              <div v-if="hasCompoundInterest" class="text-xs text-green-600 mt-1">
                With Compounding
              </div>
            </div>
            <div class="bg-blue-50 rounded-lg p-4 text-center">
              <Calendar class="w-5 h-5 text-blue-600 mx-auto mb-1" />
              <div class="text-sm text-gray-600 mb-1">Best Maturity Amount</div>
              <div class="text-xl font-bold text-gray-800">
                ₹{{ formatCurrency(bestMaturityAmount) }}
              </div>
              <div v-if="hasCompoundInterest" class="text-xs text-green-600 mt-1">
                Compound Interest Applied
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
                >₹{{ formatCurrency(bestMaturityInterest) }}</span
              >
            </div>
            <div class="flex justify-between py-3 bg-blue-600 text-white px-4 rounded-lg">
              <span class="font-medium">Total Maturity Amount</span>
              <span class="font-bold">₹{{ formatCurrency(bestMaturityAmount) }}</span>
            </div>
          </div>
        </div>

        <!-- Compound Interest Comparison -->
        <div
          v-if="hasCompoundInterest"
          class="bg-white rounded-xl shadow-xl p-6 flex flex-col max-h-[400px] overflow-y-auto"
        >
          <div class="flex items-center mb-4">
            <AlertCircle class="w-6 h-6 text-orange-600 mr-3" />
            <h3 class="text-lg font-semibold text-gray-800">Compound vs Simple Interest</h3>
          </div>
          <p class="text-gray-600 text-sm mb-6">Comparison showing compound interest benefits</p>

          <div class="grid grid-cols-2 gap-4 mb-6">
            <div class="bg-orange-50 rounded-lg p-4 text-center">
              <div class="text-2xl font-bold text-orange-600 mb-1">💰</div>
              <div class="text-sm text-gray-600 mb-1">Compound Interest</div>
              <div class="text-xl font-bold text-gray-800">
                ₹{{ formatCurrency(compoundInterest.interest) }}
              </div>
            </div>
            <div class="bg-blue-50 rounded-lg p-4 text-center">
              <div class="text-2xl font-bold text-blue-600 mb-1">💵</div>
              <div class="text-sm text-gray-600 mb-1">Compound Maturity</div>
              <div class="text-xl font-bold text-gray-800">
                ₹{{ formatCurrency(compoundInterest.maturityAmount) }}
              </div>
            </div>
          </div>

          <div class="bg-green-50 rounded-lg p-4 text-center">
            <div class="text-sm text-gray-600 mb-1">Additional Earnings vs Simple Interest</div>
            <div class="text-xl font-bold text-green-600">
              ₹{{ formatCurrency(compoundInterest.interest - simpleInterest.interest) }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- About FD Calculator Section -->
    <div class="bg-white rounded-xl shadow-xl p-6 text-gray-700 leading-relaxed">
      <h2 class="text-xl font-semibold text-gray-800 mb-4">About FD Calculator</h2>
      <p>A Fixed Deposit (FD) is a type of term investment offered by banks and NBFCs...</p>
      <p class="mt-4">An FD calculator helps you determine the interest and maturity amount...</p>

      <h3 class="text-lg font-semibold text-gray-800 mt-6 mb-2">
        How can an FD calculator help you?
      </h3>
      <ul class="list-disc list-inside space-y-2">
        <li>Eliminates the complexity of manual maturity amount calculations.</li>
        <li>Helps save time on interest computations.</li>
        <li>Allows you to compare maturity amounts and interest rates across banks or NBFCs.</li>
        <li>Enables informed investment decisions with accurate and quick calculations.</li>
      </ul>

      <h3 class="text-lg font-semibold text-gray-800 mt-6 mb-2">FD Maturity Formulas</h3>
      <p class="mt-2">
        There are two types of fixed deposits — <strong>Simple Interest FD</strong> and
        <strong>Compound Interest FD</strong>...
      </p>

      <h4 class="font-medium mt-4">1. Simple Interest Formula</h4>
      <p class="bg-gray-100 p-3 rounded mt-2 text-sm">
        <code>M = P + (P × r × t / 100)</code>
      </p>
      <p class="text-sm mt-2">Where: P = Principal, r = Rate, t = Years</p>

      <h4 class="font-medium mt-4">2. Compound Interest Formula</h4>
      <p class="bg-gray-100 p-3 rounded mt-2 text-sm overflow-x-auto">
        <code>M = P × (1 + r/n)<sup>n × t</sup></code>
      </p>
      <p class="text-sm mt-2">
        Where: P = Principal, r = Rate, n = compounding per year, t = Years
      </p>
    </div>
  </div>
</template>

<script>
import FDCalculator, { STANDARD_FD_SCHEMES } from '../utils/fdCalculations.js';
import SchemeDropdown from '../components/SchemeDropDown.vue';
import { CircleDollarSign, TrendingUp, Calendar, AlertCircle } from 'lucide-vue-next';

export default {
  name: 'InterestCalculator',
  components: { SchemeDropdown, CircleDollarSign, TrendingUp, Calendar, AlertCircle },
  data() {
    return {
      amount: 100000,
      age: this.$store.state.user?.age || 30,
      selectedScheme: null,
      baseSchemes: STANDARD_FD_SCHEMES,
    };
  },
  computed: {
    isSeniorCitizen() {
      return this.age >= 60;
    },
    schemesToShow() {
      return this.baseSchemes.map(scheme => {
        const finalRate = FDCalculator.getApplicableRate(scheme.baseRate, this.age);
        return {
          ...scheme,
          rate: finalRate.toFixed(1),
          name: scheme.name,
          tenure: scheme.tenureMonths,
          compound: scheme.hasCompound,
        };
      });
    },
    effectiveRate() {
      return this.selectedScheme ? parseFloat(this.selectedScheme.rate) : 0;
    },
    calculationResults() {
      if (!this.selectedScheme) {
        return {
          simple: { interest: 0, maturityAmount: 0 },
          compound: { interest: 0, maturityAmount: 0 },
          isCompoundEligible: false,
        };
      }
      return FDCalculator.calculateFDReturns({
        principal: this.amount,
        rate: this.selectedScheme.baseRate,
        tenureMonths: this.selectedScheme.tenure,
        age: this.age,
      });
    },
    hasCompoundInterest() {
      return this.calculationResults.isCompoundEligible;
    },
    bestMaturityAmount() {
      return this.hasCompoundInterest
        ? this.calculationResults.compound.maturityAmount
        : this.calculationResults.simple.maturityAmount;
    },
    bestMaturityInterest() {
      return this.hasCompoundInterest
        ? this.calculationResults.compound.interest
        : this.calculationResults.simple.interest;
    },
    simpleInterest() {
      return this.calculationResults.simple;
    },
    compoundInterest() {
      return this.calculationResults.compound;
    },
  },
  // async mounted() {
  //   try {
  //     await this.$store.dispatch('loadCurrentUser');
  //   } catch {}
  // },
  methods: {
    formatCurrency(amount) {
      return FDCalculator.formatCurrency(amount);
    },
  },
};
</script>

<style scoped>
/* Tailwind handles most styling; additional tweaks if needed */
</style>
