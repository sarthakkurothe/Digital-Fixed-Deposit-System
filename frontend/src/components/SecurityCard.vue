<template>
  <div
    class="p-4 bg-white/80 backdrop-blur-sm border border-gray-200 rounded-xl shadow-lg hover:shadow-2xl hover:scale-105 transition-all duration-300 cursor-pointer"
  >
    <div class="flex items-start space-x-4">
      <div
        class="flex h-10 w-10 items-center justify-center rounded-lg bg-gradient-to-r from-blue-500 to-indigo-500 flex-shrink-0"
      >
        <component :is="icon" class="h-5 w-5 text-white" />
      </div>
      <div>
        <h4 class="font-semibold mb-1 text-gray-900">{{ title }}</h4>
        <p class="text-sm text-gray-600">{{ description }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SecurityCard',
  props: {
    icon: { type: [Object, Function], required: true },
    title: { type: String, required: true },
    description: { type: String, required: true },
  },
};
</script>
