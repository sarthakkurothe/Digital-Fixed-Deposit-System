<template>
  <div class="text-center">
    <div class="text-3xl font-bold">{{ number }}</div>
    <div class="text-sm text-blue-200">{{ label }}</div>
  </div>
</template>

<script>
export default {
  name: 'TrustIndicator',
  props: {
    number: { type: String, required: true },
    label: { type: String, required: true }
  }
};
</script>
