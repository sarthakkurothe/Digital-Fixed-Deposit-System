<template>
  <section
    id="hero"
    class="relative overflow-hidden bg-gradient-to-br from-gray-50 via-blue-50 to-indigo-100"
  >
    <div class="container mx-auto px-4 py-20 md:py-32">
      <div class="grid lg:grid-cols-2 gap-12 items-center">
        <!-- Left Content -->
        <div class="space-y-8">
          <div class="space-y-4">
            <!-- Badge -->
            <span
              class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800"
            >
              <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                <path
                  fill-rule="evenodd"
                  d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                  clip-rule="evenodd"
                />
              </svg>
              Bank-Grade Security
            </span>

            <!-- Heading -->
            <h1 class="text-4xl md:text-6xl font-bold tracking-tight text-gray-900">
              Manage Your
              <span class="gradient-text-static">Fixed Deposits</span>
              <br />
              <transition name="fade-slide" mode="out-in">
                <span :key="currentWord" class="animated-word">
                  {{ currentWord }}
                </span>
              </transition>
            </h1>

            <!-- Subtext -->
            <p class="text-xl text-gray-600 max-w-[600px]">
              Book, track, and manage your Fixed Deposits with ease. Calculate returns, view
              maturity dates, and handle premature withdrawals - all from one secure platform.
            </p>
          </div>

          <!-- Quick Stats -->
          <div class="grid grid-cols-3 gap-6 pt-8">
            <div v-for="(stat, index) in stats" :key="index" class="text-center">
              <div :class="['text-2xl font-bold', stat.color]">
                {{ stat.prefix || '' }}{{ stat.displayValue }}{{ stat.suffix || '' }}
              </div>
              <div class="text-sm text-gray-600">{{ stat.label }}</div>
            </div>
          </div>
        </div>

        <!-- Right Content -->
        <div class="relative">
          <div
            class="absolute inset-0 bg-gradient-to-r from-blue-400 to-indigo-400 opacity-10 rounded-3xl blur-3xl"
          ></div>
          <div class="relative grid gap-6">
            <FeatureCard
              v-for="(feature, index) in heroFeatures"
              :key="index"
              :icon="feature.icon"
              :title="feature.title"
              :description="feature.description"
              :class="['feature-card', { 'glow-active': activeFeature === index }]"
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
      words: ['Digitally', 'Securely', 'Seamlessly'],
      currentWordIndex: 0,
      activeFeature: 0,
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
      stats: [
        { value: 8, suffix: '%', color: 'text-blue-600', label: 'Max Interest', displayValue: 0 },
        {
          value: 1000,
          prefix: '₹',
          suffix: '+',
          color: 'text-green-600',
          label: 'Min Amount',
          displayValue: 0,
        },
        { value: 24, suffix: '/7', color: 'text-blue-600', label: 'Support', displayValue: 0 },
      ],
    };
  },
  computed: {
    currentWord() {
      return this.words[this.currentWordIndex];
    },
  },
  mounted() {
    setInterval(() => {
      this.currentWordIndex = (this.currentWordIndex + 1) % this.words.length;
    }, 3000);

    setInterval(() => {
      this.activeFeature = (this.activeFeature + 1) % this.heroFeatures.length;
    }, 2500);

    this.stats.forEach((stat, i) => {
      this.animateValue(i, stat.value, 1500);
    });
  },
  methods: {
    animateValue(index, endValue, duration) {
      const startTime = performance.now();
      const step = currentTime => {
        const progress = Math.min((currentTime - startTime) / duration, 1);
        const currentValue = Math.floor(progress * endValue);
        this.stats[index].displayValue = currentValue;
        if (progress < 1) requestAnimationFrame(step);
      };
      requestAnimationFrame(step);
    },
  },
};
</script>

<style scoped>
/* ===== Gradient for Fixed Deposits ===== */
.gradient-text-static {
  display: inline-block;
  background: linear-gradient(to right, #2563eb, #4f46e5);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* ===== Animated words transition ===== */
.animated-word {
  color: #111827;
  display: inline-block;
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.6s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* ===== Feature card glow ===== */
.feature-card {
  transition: all 0.2s ease;
  border-radius: 1rem;
}

.feature-card.glow-active {
  box-shadow: 0 0 25px rgba(37, 99, 235, 0.4);
  transform: translateY(-4px);
}
</style>
