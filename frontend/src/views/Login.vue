<template>
  <Header />

  <div class="min-h-[calc(100vh-64px)] bg-gradient-to-br from-white via-blue-50 to-blue-100 flex items-center justify-center p-3">
    <div class="w-full max-w-6xl mx-auto flex flex-col lg:flex-row items-center justify-between p-6 lg:p-12 gap-12">
      
      <!-- Left Side - Branding & Features -->
      <DotLottieVue
        style="height: 500px; width: 500px"
        autoplay
        loop
        src="https://lottie.host/61b67c2f-ce7e-42bd-9f35-74450e0453a5/YbUAdRE2Jl.lottie"
      />

      <!-- Right Side - Login Form -->
      <div class="w-full max-w-md mx-auto">
        <div class="bg-white shadow-xl rounded-2xl p-8">
          <h2 class="text-2xl font-bold text-center mb-2">Welcome Back</h2>
          <p class="text-base text-center text-gray-500 mb-6">
            Sign in to access your Fixed Deposit account
          </p>

          <form @submit.prevent="handleLogin" class="space-y-5">
            <!-- Email -->
            <div class="space-y-2">
              <label for="email" class="block text-base font-medium text-gray-700">Email Address</label>
              <input
                v-model="email"
                type="email"
                id="email"
                placeholder="rajesh.kumar@example.com"
                required
                class="w-full h-11 px-4 rounded-xl border border-gray-300 bg-white shadow-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
              />
            </div>

            <!-- Password -->
            <div class="space-y-2">
              <label for="password" class="block text-base font-medium text-gray-700">Password</label>
              <div class="relative">
                <input
                  v-model="password"
                  :type="showPassword ? 'text' : 'password'"
                  id="password"
                  placeholder="Enter your password"
                  required
                  class="w-full h-11 px-4 pr-10 rounded-xl border border-gray-300 bg-white shadow-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <button
                  type="button"
                  @click="showPassword = !showPassword"
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 cursor-pointer"
                >
                  <Eye v-if="!showPassword" class="w-5 h-5" />
                  <EyeOff v-else class="w-5 h-5" />
                </button>
              </div>
            </div>

            <!-- Submit -->
            <button
              type="submit"
              class="w-full h-11 rounded-xl bg-blue-600 text-white text-base font-semibold shadow hover:bg-blue-700 transition flex items-center justify-center disabled:opacity-60 disabled:cursor-not-allowed cursor-pointer"
              :disabled="loading"
            >
              <LoadingSpinner v-if="loading" customClass="h-5 w-5 text-white" />
              <span v-else>Sign In</span>
            </button>

            <!-- Error -->
            <p v-if="error" class="text-red-500 text-center text-sm mt-2">{{ error }}</p>
          </form>

          <!-- Register -->
          <div class="text-center text-base text-gray-500 mt-6">
            Don’t have an account?
            <router-link to="/register" class="text-blue-600 hover:underline font-medium">
              Create Account
            </router-link>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>



<script>
import { Eye, EyeOff } from 'lucide-vue-next'
import { mapActions, mapGetters } from 'vuex'
import { DotLottieVue } from '@lottiefiles/dotlottie-vue'
import LoadingSpinner from '../components/LoadingSpinner.vue'

import Header from '../components/Header.vue'

export default {
  name: 'LoginView',
  components: {
    Header,
    Eye,
    EyeOff,
    DotLottieVue,
    LoadingSpinner,
  },

  data() {
    return {
      email: '',
      password: '',
      loading: false,
      error: null,
      showPassword: false,
    }
  },

  // mapGetters must be used in computed
  computed: {
    ...mapGetters(['getToken']),
  },

  methods: {
    // mapActions goes in methods
    ...mapActions(['getToken', 'setUserData', 'login']),

    async handleLogin() {
      // start UI loading
      this.loading = true
      this.error = null

      try {
        // call the login action; your Vuex action should commit the token
        await this.login({
          email: this.email,
          password: this.password,
        })

        // read token via mapped getter
        const token = this.getToken

        if (token) {
          // fetch & set user data (optional)
          await this.setUserData()
          const user = this.$store.getters.getUser
           if (user.role === 'ROLE_ADMIN') {
            this.$router.push('/admin')
          } else {
            this.$router.push('/user/dashboard')
          }
        } else {
          this.error = 'Invalid credentials'
        }
      } catch (err) {
        // prefer server message when available, fallback to generic message
        this.error = err?.response?.data?.message || err.message || 'Server'
        console.error('Login error:', err)
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

