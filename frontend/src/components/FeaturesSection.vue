<template>
  <section id="features" class="py-20 bg-gray-50">
    <div class="container mx-auto px-4">
      <div class="text-center mb-16">
        <span
          class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800 mb-4"
        >
          <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"
            />
          </svg>
          Platform Features
        </span>
        <h2 class="text-3xl md:text-4xl font-bold mb-4 text-gray-900">
          Everything You Need for <span class="gradient-text">FD Management</span>
        </h2>
        <p class="text-xl text-gray-600 max-w-2xl mx-auto">
          Comprehensive tools and features designed to make Fixed Deposit management simple, secure,
          and profitable.
        </p>
      </div>

      <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
        <FeatureCard
          v-for="(feature, index) in features"
          :key="index"
          :icon="feature.icon"
          :title="feature.title"
          :description="feature.description"
          :badge="feature.badge"
        />
      </div>
    </div>
  </section>
</template>

<script>
import FeatureCard from './FeatureCard.vue';

export default {
  name: 'FeaturesSection',
  components: { FeatureCard },
  props: {
    features: { type: Array, required: true },
  },
};
</script>
