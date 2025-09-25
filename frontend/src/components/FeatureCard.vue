<template>
  <div
    class="p-6 bg-white/80 backdrop-blur-sm border border-gray-200 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 group"
  >
    <div class="flex items-start space-x-4">
      <div class="flex h-12 w-12 items-center justify-center rounded-lg icon-gradient">
        <component :is="icon" class="h-6 w-6 text-white" />
      </div>
      <div class="flex-1">
        <h3 class="font-semibold text-gray-900">{{ title }}</h3>
        <p class="text-sm text-gray-600 mt-1">{{ description }}</p>
        <span
          v-if="badge"
          class="mt-2 inline-block px-2 py-1 text-xs font-medium bg-gray-100 text-gray-700 rounded-full"
        >
          {{ badge }}
        </span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FeatureCard",
  props: {
    icon:{ type: [Object, Function], required: true },
    title: { type: String, required: true },
    description: { type: String, required: true },
    badge: { type: String, default: null },
  },
};
</script>

<style scoped>
.icon-gradient {
  background: linear-gradient(135deg, #2563eb, #4f46e5); /* Blue gradient */
}
</style>
