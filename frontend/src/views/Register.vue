<template>
  <Header></Header>
  <div
    class="min-h-[calc(100vh-150px)] bg-gradient-to-br from-white via-blue-50 to-blue-100 flex items-center justify-center p-6"
  >
    <div
      class="w-full max-w-6xl mx-auto flex flex-col lg:flex-row items-center justify-between p-6 lg:p-12 gap-5"
    >
      <!-- Left Side - Branding -->
      <DotLottieVue
        class="hidden md:block"
        style="height: 500px; width: 500px"
        autoplay
        loop
        src="https://lottie.host/3dfb02e8-bf8e-4872-9de1-bc51ae29925b/dEg2EQLi5K.lottie"
      />

      <!-- Right Side - Registration Form -->
      <div class="flex-1 max-w-md w-full">
        <div class="bg-white shadow-md rounded-xl p-8">
          <h2 class="text-2xl font-bold text-center text-gray-900">Create Account</h2>
          <p class="text-sm text-gray-500 text-center mb-6">
            Start your investment journey with DigitalFD
          </p>

          <form @submit.prevent="handleRegister" class="space-y-4">
            <!-- Name -->
            <div>
              <input
                v-model="name"
                type="text"
                placeholder="Full Name"
                required
                class="w-full h-11 px-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <p v-if="errors.name" class="text-xs text-red-600 mt-1">{{ errors.name }}</p>
            </div>

            <!-- Email -->
            <div>
              <input
                v-model="email"
                type="email"
                placeholder="Email Address"
                required
                class="w-full h-11 px-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <p v-if="errors.email" class="text-xs text-red-600 mt-1">{{ errors.email }}</p>
            </div>

            <!-- Date of Birth -->
            <div>
              <input
                v-model="dateOfBirth"
                type="date"
                placeholder="Date of Birth"
                required
                class="w-full h-11 px-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                :max="maxDate"
                :min="minDate"
              />
              <p v-if="errors.dateOfBirth" class="text-xs text-red-600 mt-1">
                {{ errors.dateOfBirth }}
              </p>
            </div>

            <!-- Password -->
            <div class="relative">
              <input
                :type="showPassword ? 'text' : 'password'"
                v-model="password"
                placeholder="Password"
                required
                class="w-full h-11 px-4 pr-12 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button
                type="button"
                @click="showPassword = !showPassword"
                class="absolute right-3 top-3 text-gray-500 cursor-pointer"
              >
                <Eye v-if="!showPassword" class="w-5 h-5" />
                <EyeOff v-else class="w-5 h-5" />
              </button>
              <p v-if="errors.password" class="text-xs text-red-600 mt-1">{{ errors.password }}</p>
            </div>

            <!-- Confirm Password -->
            <div class="relative">
              <input
                :type="showConfirmPassword ? 'text' : 'password'"
                v-model="confirmPassword"
                placeholder="Confirm Password"
                required
                class="w-full h-11 px-4 pr-12 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button
                type="button"
                @click="showConfirmPassword = !showConfirmPassword"
                class="absolute right-3 top-3 text-gray-500 cursor-pointer"
              >
                <Eye v-if="!showConfirmPassword" class="w-5 h-5" />
                <EyeOff v-else class="w-5 h-5" />
              </button>
              <p v-if="errors.confirmPassword" class="text-xs text-red-600 mt-1">
                {{ errors.confirmPassword }}
              </p>
            </div>

            <!-- Success message -->
            <p v-if="successMessage" class="text-sm text-center text-green-600">
              {{ successMessage }}
            </p>

            <button
              type="submit"
              class="w-full h-11 rounded-xl bg-blue-600 text-white text-base font-semibold shadow hover:bg-blue-700 transition flex items-center justify-center disabled:opacity-60 disabled:cursor-not-allowed cursor-pointer"
              :disabled="loading"
            >
              <LoadingSpinner v-if="loading" customClass="h-5 w-5 text-white" />
              <span v-else>Create Account</span>
            </button>
          </form>

          <div class="text-center text-sm text-gray-600 mt-4">
            Already have an account?
            <router-link to="/login" class="text-blue-600 hover:underline font-medium"
              >Sign In</router-link
            >
          </div>

          <div class="pt-4 text-center">
            <p class="text-xs text-gray-400">
              By creating an account, you agree to our Terms and Privacy Policy
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '../components/Header.vue';
import { mapActions } from 'vuex/dist/vuex.cjs.js';
import { DotLottieVue } from '@lottiefiles/dotlottie-vue';
import { Eye, EyeOff } from 'lucide-vue-next';
import LoadingSpinner from '../components/LoadingSpinner.vue';

export default {
  components: { Eye, EyeOff, LoadingSpinner, Header, DotLottieVue },
  data() {
    const today = new Date();
    const maxDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate())
      .toISOString()
      .split('T')[0];
    const minDate = new Date(today.getFullYear() - 100, today.getMonth(), today.getDate())
      .toISOString()
      .split('T')[0];

    return {
      name: '',
      email: '',
      dateOfBirth: '',
      password: '',
      confirmPassword: '',
      loading: false,
      successMessage: '',
      showPassword: false,
      showConfirmPassword: false,
      suppressValidation: false,
      errors: {
        name: '',
        email: '',
        dob: '',
        password: '',
        confirmPassword: '',
      },
      maxDate,
      minDate,
    };
  },
  watch: {
    name(newName) {
      if (this.suppressValidation) return;
      this.errors.name = newName ? '' : 'Name cannot be empty';
    },
    email(newEmail) {
      if (this.suppressValidation) return;
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      this.errors.email = !newEmail
        ? 'Email cannot be empty'
        : !emailRegex.test(newEmail)
          ? 'Invalid email address'
          : '';
    },
    password(newPassword) {
      if (this.suppressValidation) return;
      const passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
      if (!newPassword) {
        this.errors.password = 'Password cannot be empty';
      } else if (!passwordRegex.test(newPassword)) {
        this.errors.password =
          'Password must be at least 8 characters long and include 1 uppercase, 1 number, and 1 special character';
      } else {
        this.errors.password = '';
      }

      if (this.confirmPassword && this.confirmPassword !== newPassword) {
        this.errors.confirmPassword = 'Passwords do not match';
      } else {
        this.errors.confirmPassword = '';
      }
    },
    confirmPassword(newConfirm) {
      if (this.suppressValidation) return;
      this.errors.confirmPassword =
        newConfirm && newConfirm !== this.password ? 'Passwords do not match' : '';
    },
  },
  methods: {
    ...mapActions(['register']),
    resetForm() {
      this.suppressValidation = true;
      this.name = '';
      this.email = '';
      this.dateOfBirth = '';
      this.password = '';
      this.confirmPassword = '';
      this.errors = { name: '', email: '', dateOfBirth: '', password: '', confirmPassword: '' };
      this.$nextTick(() => {
        this.suppressValidation = false;
      });
    },
    async handleRegister() {
      const hasErrors = Object.values(this.errors).some(err => err);
      if (hasErrors) return;

      this.loading = true;
      this.successMessage = '';
      try {
        console.log('Registering user with:', {
          name: this.name,
          email: this.email,
          dateOfBirth: this.dateOfBirth,
          password: this.password,
        });
        const res = await this.register({
          name: this.name,
          email: this.email,
          dateOfBirth: this.dateOfBirth,
          password: this.password,
        });

        if (res.status === 201) {
          this.successMessage = 'Registration successful! 🎉 Redirecting to login in 3 seconds...';

          this.resetForm();

          let countdown = 3;
          const timer = setInterval(() => {
            countdown--;
            if (countdown > 0) {
              this.successMessage = `Registration successful! 🎉 Redirecting to login in ${countdown} seconds...`;
            } else {
              clearInterval(timer);
              this.$router.push('/login');
            }
          }, 1000);
        } else if (res.data === 'User already exists!') {
          this.errors.email = res.data;
        } else {
          this.errors.email = res.error || 'Registration failed. Please try again.';
        }
      } catch (error) {
        console.error('Registration error:', error);
        this.errors.email = error.response?.data || 'Registration failed. Please try again.';
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>
