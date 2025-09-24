<template>
  <section id="hero" class="relative overflow-hidden bg-gradient-to-br from-gray-50 via-blue-50 to-indigo-100">
    <div class="container mx-auto px-4 py-20 md:py-32">
      <div class="grid lg:grid-cols-2 gap-12 items-center">
        <!-- Left Content -->
        <div class="space-y-8">
          <div class="space-y-4">
            <span
              class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800"
            >
              <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd"
                  d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                  clip-rule="evenodd"/>
              </svg>
              Bank-Grade Security
            </span>
            <h1 class="text-4xl md:text-6xl font-bold tracking-tight text-gray-900">
              Manage Your <span class="gradient-text">Fixed Deposits</span> Digitally
            </h1>
            <p class="text-xl text-gray-600 max-w-[600px]">
              Book, track, and manage your Fixed Deposits with ease. Calculate returns, view maturity dates, and handle premature withdrawals - all from one secure platform.
            </p>
          </div>

          <!-- Quick Stats -->
          <div class="grid grid-cols-3 gap-6 pt-8">
            <div class="text-center">
              <div class="text-2xl font-bold text-blue-600">8%</div>
              <div class="text-sm text-gray-600">Max Interest</div>
            </div>
            <div class="text-center">
              <div class="text-2xl font-bold text-green-600">₹1K+</div>
              <div class="text-sm text-gray-600">Min Amount</div>
            </div>
            <div class="text-center">
              <div class="text-2xl font-bold text-blue-600">24/7</div>
              <div class="text-sm text-gray-600">Support</div>
            </div>
          </div>
        </div>

        <!-- Right Content -->
        <div class="relative">
          <div class="absolute inset-0 bg-gradient-to-r from-blue-400 to-indigo-400 opacity-10 rounded-3xl blur-3xl"></div>
          <div class="relative grid gap-6">
            <FeatureCard
              v-for="(feature, index) in heroFeatures"
              :key="index"
              :icon="feature.icon"
              :title="feature.title"
              :description="feature.description"
            />
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import FeatureCard from './FeatureCard.vue';
import { ChartLine, Clock4, Calculator } from 'lucide-vue-next';

export default {
  name: 'HeroSection',
  components: { FeatureCard },
  data() {
    return {
      heroFeatures: [
        {
          icon: ChartLine,
          title: 'Smart Interest Calculation',
          description: 'Real-time interest tracking with compound calculations',
        },
        {
          icon: Clock4,
          title: 'Flexible Tenure Options',
          description: 'Choose from 6 months to 10 years with competitive rates',
        },
        {
          icon: Calculator,
          title: 'Premature Withdrawal',
          description: 'Break FDs early with transparent penalty calculations',
        },
      ],
    };
  },
};
</script>

<style scoped>
.gradient-text {
  display: inline-block; /* Important for Tailwind gradient */
  background: linear-gradient(to right, #2563eb, #4f46e5);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
</style>
