<template>
  <Header ></Header>
  <div class="min-h-[calc(100vh-200px)] bg-gradient-to-br from-white via-blue-50 to-blue-100 flex items-center justify-center p-6">
    <div class="w-full max-w-6xl mx-auto flex flex-col lg:flex-row items-center justify-between p-6 lg:p-12 gap-5">
      
      <!-- Left Side - Branding -->
      <DotLottieVue style="height: 500px; width: 500px" autoplay loop src="https://lottie.host/8ddc1863-565e-41eb-b9f0-24f37513683d/JfunXkHkx9.lottie" />

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
import { DotLottieVue } from '@lottiefiles/dotlottie-vue'


export default {
  components: { EyeIcon, EyeSlashIcon, Header,DotLottieVue},
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
