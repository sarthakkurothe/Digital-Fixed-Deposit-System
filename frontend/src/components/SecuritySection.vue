<template>
  <section id="security" class="py-20 bg-gray-50">
    <div class="container mx-auto px-4">
      <div class="text-center mb-16">
        <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800 mb-4">
          Security & Trust
        </span>
        <h2 class="text-3xl md:text-4xl font-bold mb-4 text-gray-900">
          Your Money is <span class="gradient-text">100% Secure</span>
        </h2>
        <p class="text-xl text-gray-600 max-w-2xl mx-auto">
          Built with bank-grade security and compliance standards to protect your investments.
        </p>
      </div>

      <div class="grid lg:grid-cols-2 gap-12 mb-16">
        <!-- Security Features -->
        <div class="space-y-6">
          <h3 class="text-2xl font-semibold flex items-center text-gray-900">
            Advanced Security
          </h3>
          <SecurityCard v-for="(feature,i) in securityFeatures" :key="i" :icon="feature.icon" :title="feature.title" :description="feature.description" />
        </div>

        <!-- Compliance & Certifications -->
        <div class="space-y-6">
          <h3 class="text-2xl font-semibold flex items-center text-gray-900">
            Compliance & Certifications
          </h3>
          <SecurityCard v-for="(item,i) in compliance" :key="i" :icon="item.icon" :title="item.title" :description="item.description" />
        </div>
      </div>

      <!-- Trust Indicators -->
      <div class="p-8 bg-gradient-to-r from-blue-600 to-indigo-600 text-white rounded-2xl shadow-xl">
        <div class="text-center space-y-4">
          <h3 class="text-2xl font-bold">Your Trust is Our Priority</h3>
          <p class="text-blue-100 max-w-2xl mx-auto">
            Multiple layers of protection and industry-standard compliance to safeguard your investments.
          </p>
          <div class="grid md:grid-cols-3 gap-6 mt-8">
            <TrustIndicator number="256-bit" label="SSL Encryption"/>
            <TrustIndicator number="24/7" label="System Monitoring"/>
            <TrustIndicator number="100%" label="Compliance Rate"/>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import SecurityCard from './SecurityCard.vue';
import TrustIndicator from './TrustIndicator.vue';

export default {
  name: 'SecuritySection',
  components: { SecurityCard, TrustIndicator },
  props: {
    securityFeatures: { type: Array, required: true },
    compliance: { type: Array, required: true }
  }
};
</script>
