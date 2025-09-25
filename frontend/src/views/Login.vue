<template>
  <Header />

  <div class="min-h-[calc(100vh-64px)] bg-gradient-to-br from-white via-blue-50 to-blue-100 flex items-center justify-center p-3">
    <div class="w-full max-w-6xl mx-auto flex flex-col lg:flex-row items-center justify-between p-6  lg:p-12 gap-12">
      
      <!-- Left Side - Branding & Features -->
      <DotLottieVue style="height: 500px; width: 500px" autoplay loop src="https://lottie.host/61b67c2f-ce7e-42bd-9f35-74450e0453a5/YbUAdRE2Jl.lottie" />

      <!-- Right Side - Login Form -->
      <div class="w-full lg:w-110">
        <div class="bg-white shadow-xl rounded-2xl p-8">
          <h2 class="text-2xl font-bold text-center mb-2">Welcome Back</h2>
          <p class="text-base text-center text-gray-500 mb-6">Sign in to access your Fixed Deposit account</p>

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
                <button type="button" @click="showPassword = !showPassword" class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 cursor-pointer">
                  <Eye v-if="!showPassword" class="w-5 h-5" />
                  <EyeOff v-else class="w-5 h-5" />
                </button>
              </div>
            </div>

            <!-- Remember & Forgot -->
            <div class="flex items-center justify-between text-sm text-gray-600">
              <label class="flex items-center space-x-2">
                <input type="checkbox" class="rounded border-gray-300" />
                <span>Remember me</span>
              </label>
              <router-link to="/forgot-password" class="text-blue-600 hover:underline font-medium">Forgot password?</router-link>
            </div>

            <!-- Submit -->
            <button
              type="submit"
              class="w-full h-11 rounded-xl bg-blue-600 text-white text-base font-semibold shadow hover:bg-blue-700 transition"
              :disabled="loading"
            >
              {{ loading ? "Signing In..." : "Sign In" }}
            </button>

            <!-- Error -->
            <p v-if="error" class="text-red-500 text-center text-sm mt-2">{{ error }}</p>
          </form>

          <!-- Register -->
          <div class="text-center text-base text-gray-500 mt-6">
            Don’t have an account?
            <router-link to="/register" class="text-blue-600 hover:underline font-medium">Create Account</router-link>
          </div>

          
        </div>
      </div>

    </div>
  </div>
</template>


<script>
import { Eye,EyeOff } from 'lucide-vue-next';
import { mapActions } from 'vuex';
import { DotLottieVue } from '@lottiefiles/dotlottie-vue'

import Header from '../components/Header.vue';
import { mapGetters } from 'vuex/dist/vuex.cjs.js';

export default {
  components: { Header,Eye, EyeOff, DotLottieVue },
  data() {
    return {
    
      email: "",
      password: "",
      loading: false,
      error: null,
      showPassword: false,
    };
  },
  methods: {
    ...mapActions(["setToken", "setUserData","login"]),
    ...mapGetters(["getToken"]),

    async handleLogin() {
      this.loading = true;
      this.error = null;

      try{

        await this.login({
          email: this.email,
          password: this.password
        });
        const token = this.getToken();

        console.log("Received token:", token);

        if (token) {
          await this.setUserData();
          this.$router.push({ name: "Dashboard" });
        } else {
          this.error = "Login failed. Please try again.";
        }

      } catch (err) {
        this.error = err.response?.data?.message || "Invalid credentials";
        console.error("Login error:", err);
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>
