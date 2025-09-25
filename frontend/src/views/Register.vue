<template>
  <Header ></Header>
  <div class="min-h-[calc(100vh-200px)] bg-gradient-to-br from-white via-blue-50 to-blue-100 flex items-center justify-center p-6">
    <div class="w-full max-w-6xl mx-auto flex flex-col lg:flex-row items-center justify-between p-6 lg:p-12 gap-12">
      
      <!-- Left Side - Branding -->
      <div class="flex-1 space-y-10">
        <div class="flex items-center gap-3">
          <div class="w-12 h-12 bg-blue-600 rounded-xl flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M9 21h6m-6-7h6m-6-7h6" />
            </svg>
          </div>
          <div>
            <h1 class="text-2xl font-bold text-blue-700">DigitalFD</h1>
            <p class="text-sm text-gray-500">Join thousands of smart investors</p>
          </div>
        </div>

        <div class="space-y-6">
          <div class="flex items-start gap-3">
            <div class="w-9 h-9 bg-blue-100 rounded-lg flex items-center justify-center">
              <svg class="w-5 h-5 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
              </svg>
            </div>
            <div>
              <h3 class="font-semibold text-gray-900">Secure & Regulated</h3>
              <p class="text-sm text-gray-500">FDIC insured deposits with bank-grade security protocols</p>
            </div>
          </div>

          <div class="flex items-start gap-3">
            <div class="w-9 h-9 bg-green-100 rounded-lg flex items-center justify-center">
              <svg class="w-5 h-5 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8l3 6 7 1-5 5 2 7-6-3-6 3 2-7-5-5 7-1z"/>
              </svg>
            </div>
            <div>
              <h3 class="font-semibold text-gray-900">Best-in-Class Returns</h3>
              <p class="text-sm text-gray-500">Competitive interest rates up to 8.5% annually</p>
            </div>
          </div>

          <div class="flex items-start gap-3">
            <div class="w-9 h-9 bg-yellow-100 rounded-lg flex items-center justify-center">
              <svg class="w-5 h-5 text-yellow-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a4 4 0 00-5-3.87M9 20H4v-2a4 4 0 015-3.87M12 12a5 5 0 100-10 5 5 0 000 10z"/>
              </svg>
            </div>
            <div>
              <h3 class="font-semibold text-gray-900">Trusted by 10,000+ Users</h3>
              <p class="text-sm text-gray-500">Join our growing community of successful investors</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Side - Registration Form -->
      <div class="flex-1 max-w-md w-full">
        <div class="bg-white shadow-md rounded-xl p-8">
          <h2 class="text-2xl font-bold text-center text-gray-900">Create Account</h2>
          <p class="text-sm text-gray-500 text-center mb-6">Start your investment journey with DigitalFD</p>

          <form @submit.prevent="handleRegister" class="space-y-4">
            <input v-model="name" type="text" placeholder="Full Name" required class="w-full h-11 px-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
            
            <input v-model="email" type="email" placeholder="Email Address" required class="w-full h-11 px-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
            

            <input v-model="age" type="number" placeholder="Age" required class="w-full h-11 px-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>

            <!-- Password -->
            <div class="relative">
              <input :type="showPassword ? 'text' : 'password'" v-model="password" placeholder="Password" required class="w-full h-11 px-4 pr-10 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
              <button type="button" @click="showPassword = !showPassword" class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500">
                <EyeIcon v-if="!showPassword" class="w-5 h-5"/>
                <EyeSlashIcon v-else class="w-5 h-5"/>
              </button>
            </div>

            <!-- Confirm Password -->
            <div class="relative">
              <input :type="showConfirmPassword ? 'text' : 'password'" v-model="confirmPassword" placeholder="Confirm Password" required class="w-full h-11 px-4 pr-10 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"/>
              <button type="button" @click="showConfirmPassword = !showConfirmPassword" class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500">
                <EyeIcon v-if="!showConfirmPassword" class="w-5 h-5"/>
                <EyeSlashIcon v-else class="w-5 h-5"/>
              </button>
            </div>

            <!-- Terms & Conditions -->
            <div class="flex items-start gap-2">
              <input type="checkbox" v-model="agreeTerms" class="mt-1 w-4 h-4 border-gray-300 rounded"/>
              <label class="text-sm text-gray-600">
                I agree to the 
                <router-link to="/terms" class="text-blue-600 hover:underline">Terms of Service</router-link> 
                and 
                <router-link to="/privacy" class="text-blue-600 hover:underline">Privacy Policy</router-link>
              </label>
            </div>

            <!-- Error / Success messages -->
            <p v-if="errorMessage" class="text-sm text-center text-red-600">{{ errorMessage }}</p>
            <p v-if="successMessage" class="text-sm text-center text-green-600">{{ successMessage }}</p>

            <button type="submit" class="w-full h-11 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition" :disabled="loading">
              {{ loading ? "Creating Account..." : "Create Account" }}
            </button>
          </form>

          <div class="text-center text-sm text-gray-600 mt-4">
            Already have an account? 
            <router-link to="/login" class="text-blue-600 hover:underline font-medium">Sign In</router-link>
          </div>

          <div class="pt-4 text-center">
            <p class="text-xs text-gray-400 text-">
              By creating an account, you agree to our Terms and Privacy Policy
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { EyeIcon, EyeSlashIcon } from "@heroicons/vue/24/solid";
import Header from "../components/Header.vue";
import { mapActions } from "vuex/dist/vuex.cjs.js";

export default {
  components: { EyeIcon, EyeSlashIcon, Header },
  data() {
    return {
      name: "",
      email: "",
      age: "",
      password: "",
      confirmPassword: "",
      agreeTerms: false,
      loading: false,
      errorMessage: "",
      successMessage: "",
      showPassword: false,
      showConfirmPassword: false,
    };
  },
  methods: {
    ...mapActions(["register"]),
    async handleRegister() {
      this.errorMessage = "";
      this.successMessage = "";
      
      if (!this.name || !this.email|| !this.age || !this.password || !this.confirmPassword) {
        this.errorMessage = "All fields are required.";
        return;
      }
      if (!this.agreeTerms) {
        this.errorMessage = "You must agree to the Terms and Privacy Policy.";
        return;
      }
      if (this.password !== this.confirmPassword) {
        this.errorMessage = "Passwords do not match!";
        return;
      }

      

      this.loading = true;
      try {
        this.errorMessage = "";
        const res = await this.register( {
          name: this.name,
          email: this.email,
          age: this.age,
          password: this.password,
        });
        console.log("Registration response:", res);
        if (res.status === 201) {
          this.successMessage = "Registration successful! 🎉 You can now log in.";
          this.name = this.email = this.phone = this.age = this.password = this.confirmPassword = "";
          this.agreeTerms = false;
        } else {
          this.errorMessage = res.error || "Registration failed. Please try again.";
        }
      } catch (error) {
        console.error("Registration error:", error);
        this.errorMessage = error.response?.data || "Registration failed. Please try again.";
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>
