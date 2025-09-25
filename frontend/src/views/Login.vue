<template>
  <Header />

  <div class="min-h-[calc(100vh-64px)] bg-gradient-to-br from-white via-blue-50 to-blue-100 flex items-center justify-center p-4">
    <div class="w-full max-w-6xl flex items-center justify-between gap-8">
      
      <!-- Left Side - Branding & Features -->
      <div class="hidden lg:flex flex-1 flex-col justify-center space-y-10 pl-6">
        <div class="flex items-center gap-3">
          <div class="w-12 h-12 bg-blue-600 rounded-xl flex items-center justify-center shadow-lg">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 1.343-3 3h6c0-1.657-1.343-3-3-3z" />
            </svg>
          </div>
          <div>
            <h1 class="text-3xl font-bold text-blue-600">DigitalFD</h1>
            <p class="text-base text-gray-500">Secure Fixed Deposit Management</p>
          </div>
        </div>

        <div class="space-y-6">
          <div class="flex items-start gap-4">
            <div class="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
              <svg class="w-5 h-5 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div>
              <h3 class="font-semibold text-lg text-gray-800">Bank-Grade Security</h3>
              <p class="text-base text-gray-500">Your deposits are protected with advanced encryption and security protocols</p>
            </div>
          </div>

          <div class="flex items-start gap-4">
            <div class="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
              <svg class="w-5 h-5 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
              </svg>
            </div>
            <div>
              <h3 class="font-semibold text-lg text-gray-800">Competitive Interest Rates</h3>
              <p class="text-base text-gray-500">Earn up to 8.5% annual interest on your fixed deposits</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Side - Login Form -->
      <div class="w-full lg:w-96">
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
                <button type="button" @click="showPassword = !showPassword" class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                  <EyeIcon v-if="!showPassword" class="w-5 h-5" />
                  <EyeSlashIcon v-else class="w-5 h-5" />
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

          <!-- Demo Credentials -->
          <div class="pt-4 text-center">
            <p class="text-sm text-gray-400">Demo Credentials: rajesh.kumar@example.com / password123</p>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>


<script>
import { EyeIcon, EyeSlashIcon } from '@heroicons/vue/24/solid';
import { mapActions } from 'vuex';

import Header from '../components/Header.vue';
import { mapGetters } from 'vuex/dist/vuex.cjs.js';

export default {
  components: { Header,EyeIcon, EyeSlashIcon },
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
