<template>
  <span role="status" aria-live="polite" class="inline-flex items-center">
    <svg
      :class="computedClass"
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 24 24"
      fill="none"
      aria-hidden="true"
    >
      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z" />
    </svg>
    <span v-if="label" class="sr-only">{{ label }}</span>
  </span>
</template>

<script>
export default {
  name: 'LoadingSpinner',
  props: {
    customClass: { type: String, default: 'h-5 w-5' },
    label: { type: String, default: 'Loading' },
  },
  computed: {
    computedClass() {
      return `animate-spin ${this.customClass}`.trim();
    },
  },
};
</script>

<style scoped></style>
