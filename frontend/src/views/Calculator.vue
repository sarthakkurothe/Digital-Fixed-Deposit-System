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
        <!-- FD Calculator Card -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center mb-4">
            <div class="w-6 h-6 bg-blue-100 rounded mr-3 flex items-center justify-center">
              <svg class="w-4 h-4 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <h2 class="text-lg font-semibold text-gray-800">FD Calculator</h2>
          </div>
          <p class="text-gray-600 text-sm mb-6">Enter your investment details to calculate returns</p>

          <!-- Age Input -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Your Age</label>
            <p class="w-full p-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" 
              >
               {{ age }}
            </p>
            <!-- <input 
              type="number" 
              class="w-full p-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" 
              v-model.number="age" 
              min="18" 
              max="100"
              placeholder="Enter your age"
            /> -->
            <p v-if="isSeniorCitizen" class="text-green-600 text-xs mt-1">
              ✓ Senior citizen benefits applicable (+0.5% on all rates)
            </p>
          </div>

          <!-- Quick Scheme Selection -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Quick Select Scheme (Optional)</label>
            <select 
              class="w-full p-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" 
              v-model="selectedScheme" 
              @change="handleSchemeChange"
            >
              <option value="">Choose a predefined scheme</option>
              <option v-for="scheme in availableSchemes" :key="scheme.id" :value="scheme.id">
                {{ scheme.description }} - {{ scheme.displayRate }}% p.a.
              </option>
            </select>
             <p v-if="hasCompound" class="text-green-600 text-xs mt-1">
              ✓ Compound Interest benefits applicable 
            </p>
          </div> 
            
          <!-- Investment Form -->
          <div class="space-y-4">
             <p v-if="selectedScheme && hasCompoundInterest" class="text-green-600 text-xs mt-1">
              ✓ Compound Interest benefits applicable 
            </p>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Investment Amount (₹)</label>
              <input 
                type="number" 
                class="w-full p-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" 
                v-model.number="amount" 
                min="1000" 
                step="1000"
                placeholder="100000"
              />
            </div>
           
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Interest Rate (% per annum)</label>
              <input 
                type="number" 
                class="w-full p-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" 
                v-model.number="interestRate" 
                min="1" 
                max="15" 
                step="0.1"
                placeholder="7.0"
              />
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Tenure (Months)</label>
              <select 
                class="w-full p-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" 
                v-model.number="tenure"
              >
                <option value="6">6 months</option>
                <option value="12">12 months</option>
                <option value="24">24 months</option>
                <option value="36">36 months</option>
                <option value="60">60 months</option>
              </select>
            </div>
            

          </div>

          <!-- Investment Summary -->
          <div class="bg-gray-50 rounded-lg p-4 mt-6">
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm text-gray-600">Principal:</span>
              <span class="font-semibold">₹{{ formatCurrency(amount) }}</span>
            </div>
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm text-gray-600">Rate:</span>
              <span class="font-semibold">{{ interestRate }}% p.a.</span>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-sm text-gray-600">Tenure:</span>
              <span class="font-semibold">{{ tenure }} months</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Side - Results -->
      <div class="space-y-6">
        <!-- Simple Interest Calculation -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center mb-4">
            <div class="w-6 h-6 bg-green-100 rounded mr-3 flex items-center justify-center">
              <svg class="w-4 h-4 text-green-600" fill="currentColor" viewBox="0 0 20 20">
                <path d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3z"></path>
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-800">Simple Interest Calculation</h3>
          </div>
          <p class="text-gray-600 text-sm mb-6">Standard FD interest calculation</p>

          <!-- Interest Cards -->
          <div class="grid grid-cols-2 gap-4 mb-6">
            <div class="bg-green-50 rounded-lg p-4 text-center">
              <div class="text-2xl font-bold text-green-600 mb-1">₹</div>
              <div class="text-sm text-gray-600 mb-1">Interest Earned</div>
              <div class="text-xl font-bold text-gray-800">₹{{ formatCurrency(simpleInterest.interest) }}</div>
            </div>
            <div class="bg-blue-50 rounded-lg p-4 text-center">
              <div class="text-2xl font-bold text-blue-600 mb-1">📅</div>
              <div class="text-sm text-gray-600 mb-1">Maturity Amount</div>
              <div class="text-xl font-bold text-gray-800">₹{{ formatCurrency(simpleInterest.maturityAmount) }}</div>
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
              <span class="font-semibold text-green-600">₹{{ formatCurrency(simpleInterest.interest) }}</span>
            </div>
            <div class="flex justify-between py-3 bg-blue-600 text-white px-4 rounded-lg">
              <span class="font-medium">Total Maturity Amount</span>
              <span class="font-bold">₹{{ formatCurrency(simpleInterest.maturityAmount) }}</span>
            </div>
          </div>
        </div>

        <!-- Compound Interest Reference (only for eligible tenures) -->
        <div v-if="hasCompoundInterest" class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center mb-4">
            <div class="w-6 h-6 bg-orange-100 rounded mr-3 flex items-center justify-center">
              <svg class="w-4 h-4 text-orange-600" fill="currentColor" viewBox="0 0 20 20">
                <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <h3 class="text-lg font-semibold text-gray-800">Compound Interest (Reference)</h3>
          </div>
          <p class="text-gray-600 text-sm mb-6">For comparison with compound schemes</p>

          <!-- Compound Interest Cards -->
          <div class="grid grid-cols-2 gap-4 mb-6">
            <div class="bg-orange-50 rounded-lg p-4 text-center">
              <div class="text-2xl font-bold text-orange-600 mb-1">💰</div>
              <div class="text-sm text-gray-600 mb-1">Compound Interest</div>
              <div class="text-xl font-bold text-gray-800">₹{{ formatCurrency(compoundInterest.interest) }}</div>
            </div>
            <div class="bg-blue-50 rounded-lg p-4 text-center">
              <div class="text-2xl font-bold text-blue-600 mb-1">💵</div>
              <div class="text-sm text-gray-600 mb-1">Compound Maturity</div>
              <div class="text-xl font-bold text-gray-800">₹{{ formatCurrency(compoundInterest.maturityAmount) }}</div>
            </div>
          </div>

          <div class="bg-green-50 rounded-lg p-4 text-center">
            <div class="text-sm text-gray-600 mb-1">Additional Earnings vs Simple Interest</div>
            <div class="text-xl font-bold text-green-600">₹{{ formatCurrency(compoundInterest.interest - simpleInterest.interest) }}</div>
          </div>
        </div>
      </div>
    </div>
 
    <!-- About FD calculator -->
    
    <div class="bg-white rounded-lg shadow p-6 text-gray-700 leading-relaxed">
        <h2 class="text-xl font-semibold text-gray-800 mb-4">About FD Calculator</h2>

        <p>
            A Fixed Deposit (FD) is a type of term investment offered by banks and NBFCs. These deposits typically offer a higher rate of interest, subject to certain terms and conditions. The amount you deposit is locked for a predetermined period, which can range from 7 days to 10 years.
        </p>

        <p class="mt-4">
            An FD calculator helps you determine the interest and maturity amount your deposit will earn. It is a simple-to-use tool available on the Groww website.
        </p>

        <h3 class="text-lg font-semibold text-gray-800 mt-6 mb-2">How can an FD calculator help you?</h3>
        <ul class="list-disc list-inside space-y-2">
            <li>Eliminates the complexity of manual maturity amount calculations.</li>
            <li>Helps save time on interest computations.</li>
            <li>Allows you to compare maturity amounts and interest rates across banks or NBFCs.</li>
            <li>Enables informed investment decisions with accurate and quick calculations.</li>
        </ul>

        <h3 class="text-lg font-semibold text-gray-800 mt-6 mb-2">FD Maturity Formulas</h3>
        <p class="mt-2">
            There are two types of fixed deposits — <strong>Simple Interest FD</strong> and <strong>Compound Interest FD</strong>. The calculator uses the appropriate formula based on your input.
        </p>

        <h4 class="font-medium mt-4">1. Simple Interest Formula</h4>
        <p class="bg-gray-100 p-3 rounded mt-2 text-sm">
            <code>M = P + (P × r × t / 100)</code>
        </p>
        <p class="text-sm mt-2">
            Where:<br />
            P = Principal amount<br />
            r = Annual interest rate (%)<br />
            t = Tenure (in years)
        </p>
        <p class="text-sm mt-2">
            <strong>Example:</strong> For ₹1,00,000 at 10% for 5 years:<br />
            M = 1,00,000 + (1,00,000 × 10 × 5 / 100) = ₹1,50,000
        </p>

        <h4 class="font-medium mt-4">2. Compound Interest Formula</h4>
        <p class="bg-gray-100 p-3 rounded mt-2 text-sm overflow-x-auto">
            <code>M = P × (1 + r/n)<sup>n × t</sup></code>
        </p>
        <p class="text-sm mt-2">
            Where:<br />
            P = Principal amount<br />
            r = Annual interest rate (as a decimal)<br />
            n = Number of times interest is compounded per year<br />
            t = Time in years
        </p>
        <p class="text-sm mt-2">
            <strong>Example:</strong> For ₹1,00,000 at 10% compounded annually for 5 years:<br />
            M = 1,00,000 × (1 + 0.10/1)<sup>1 × 5</sup> = ₹1,61,051
        </p>
    </div>



  </div>
</template>

<script> 
export default { 
  name: 'InterestCalculator',
  data() {
    return {
      amount: 100000,
      interestRate: 7.0,
      tenure: 12,
      age: this.$store.state.user ? this.$store.state.user.age : 30,
      selectedScheme: '',
      hasCompoundInterest: false,
       baseSchemes: [
        { 
          id: 1, 
          description: '6 months', 
          baseRate: 6.00, 
          tenureMonths: 6, 
          hasCompound: false 
        },
        { 
          id: 2, 
          description: '1 year', 
          baseRate: 6.50, 
          tenureMonths: 12, 
          hasCompound: false 
        },
        { 
          id: 3, 
          description: '2 years', 
          baseRate: 7.00, 
          tenureMonths: 24, 
          hasCompound: true 
        },
        { 
          id: 4, 
          description: '3 years', 
          baseRate: 7.50, 
          tenureMonths: 36, 
          hasCompound: true 
        },
        { 
          id: 5, 
          description: '5 years', 
          baseRate: 8.00, 
          tenureMonths: 60, 
          hasCompound: true 
        }
      ]
    }
  },
  computed: { 
    fetchUserAge(){
        const user = fetchUser();
        console.log(user);
        return user ? user.age : this.age;
    },
    isSeniorCitizen() {
      return this.age >= 60
    },
    availableSchemes() {
      return this.baseSchemes.map(scheme => {
        const seniorBonus = this.isSeniorCitizen ? 0.5 : 0
        const finalRate = scheme.baseRate + seniorBonus
        return {
          ...scheme,
          displayRate: finalRate.toFixed(2),
          finalRate: finalRate
        }
      })
    },
    currentScheme() {
      return this.availableSchemes.find(s => s.tenureMonths === this.tenure)
    },
    hasCompoundInterest() {
      return this.currentScheme ? this.currentScheme.hasCompound : this.tenure >= 24
    },
    simpleInterest() {
      const p = Number(this.amount), r = Number(this.interestRate), t = Number(this.tenure)
      if (!p || !r || !t) return { interest: 0, maturityAmount: 0 }
      const interest = (p * r * t) / (12 * 100)
      return { 
        interest: Math.round(interest), 
        maturityAmount: Math.round(p + interest) 
      }
    },
    compoundInterest() {
      const p = Number(this.amount), r = Number(this.interestRate), t = Number(this.tenure)
      if (!p || !r || !t || !this.hasCompoundInterest) return { interest: 0, maturityAmount: 0 }
      
      // Compound quarterly for FDs
      const quarterlyRate = r / (4 * 100)
      const quarters = Math.floor(t / 3)
      const maturityAmount = p * Math.pow(1 + quarterlyRate, quarters)
      
      return { 
        interest: Math.round(maturityAmount - p), 
        maturityAmount: Math.round(maturityAmount) 
      }
    }
  },
  async mounted() {
     try {
       await this.$store.dispatch('loadCurrentUser') 
    } catch (err) {
     }
  },
  methods: { 
    formatCurrency(amount) {
      if (!amount) return '0'
      return new Intl.NumberFormat('en-IN').format(Math.round(amount))
    },
    handleSchemeChange() {
      if (!this.selectedScheme) return
      
      const scheme = this.availableSchemes.find(s => s.id === Number(this.selectedScheme))
      if (scheme) {
        this.interestRate = scheme.finalRate
        this.tenure = scheme.tenureMonths
      }
    }
  },
  watch: {
    // if the store's user changes (login/logout), auto update calculations
    '$store.state.user'(val) {
      if (this.selectedScheme) {
        this.handleSchemeChange()
      }
    }
  }
}
</script>

<style scoped>
/* Optional styling here */
</style>

