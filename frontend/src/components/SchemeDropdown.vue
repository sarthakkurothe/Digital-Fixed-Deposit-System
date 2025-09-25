<template>
  <div class="relative w-full">
    <!-- Dropdown button -->
    <button
      @click="toggleDropdown"
      class="w-full p-3 border border-gray-200 rounded-lg text-left focus:outline-none focus:ring-2 focus:ring-blue-500 flex justify-between items-center cursor-pointer"
    >
      <span>{{ selectedScheme ? selectedScheme.name : 'Choose a FD Scheme' }}</span>
      <svg
        class="w-4 h-4 transform transition-transform duration-200"
        :class="{ 'rotate-180': dropdownOpen }"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
      </svg>
    </button>

    <!-- Dropdown list -->
    <ul
      v-if="dropdownOpen"
      class="absolute z-10 w-full bg-white border border-gray-200 rounded-lg mt-1 max-h-60 overflow-auto shadow"
    >
      <li
        v-for="scheme in schemes"
        :key="scheme.name"
        @click="selectScheme(scheme)"
        class="flex justify-between items-center p-3 hover:bg-gray-100 cursor-pointer"
      >
        <span>{{ scheme.name }}</span>
        <span class="bg-green-500 text-white text-xs font-semibold px-2 py-1 rounded">
          {{ scheme.rate }}%
        </span>
      </li>
    </ul>
  </div>
</template>

<script>


export default {
  name: "SchemeDropdown",
  props: {
    schemes: { type: Array, required: true },
    modelValue: { type: Object, default: null }, // for v-model binding
  },
  data() {
    return {
      dropdownOpen: false,
      selectedSchemeInternal: this.modelValue,
    };
  },
  watch: {
    modelValue(val) {
      this.selectedSchemeInternal = val;
    },
  },
  computed: {
    selectedScheme() {
      return this.selectedSchemeInternal;
    },
  },
  methods: {
    toggleDropdown() {
      this.dropdownOpen = !this.dropdownOpen;
    },
    selectScheme(scheme) {
      this.selectedSchemeInternal = scheme;
      this.$emit("update:modelValue", scheme); // for v-model
      this.dropdownOpen = false;
    },
  },
};
</script>
