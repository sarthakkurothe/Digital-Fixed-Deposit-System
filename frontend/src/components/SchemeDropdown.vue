<template>
  <div class="relative w-full">
    <!-- Dropdown button -->
    <button
      @click="toggleDropdown"
      type="button"
      class="w-full p-3 border border-gray-300 rounded-md text-left focus:outline-none focus:border-gray-400 flex justify-between items-center cursor-pointer bg-white"
    >
      <span>
        {{
          selectedScheme
            ? selectedScheme.name + (selectedScheme.rate ? ' - ' + selectedScheme.rate + '%' : '')
            : placeholder
        }}
      </span>
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
      class="absolute z-10 w-full bg-white border border-gray-300 rounded-md mt-1 max-h-60 overflow-auto shadow-md"
    >
      <li
        v-for="scheme in schemes"
        :key="scheme.id"
        @click="selectScheme(scheme)"
        class="flex justify-between items-center p-3 hover:bg-gray-100 cursor-pointer"
      >
        <span>{{ scheme.name }}</span>
        <span
          v-if="scheme.rate !== null && scheme.rate !== undefined"
          class="bg-green-500 text-white text-xs font-semibold px-2 py-1 rounded"
        >
          {{ scheme.rate }}%
        </span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'SchemeDropdown',
  props: {
    schemes: { type: Array, required: true },
    modelValue: { type: Object, default: null },
    placeholder: { type: String, default: 'Select FD' },
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
  mounted() {
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },
  methods: {
    toggleDropdown() {
      this.dropdownOpen = !this.dropdownOpen;
    },
    selectScheme(scheme) {
      this.selectedSchemeInternal = scheme;
      this.$emit('update:modelValue', scheme);
      this.dropdownOpen = false;
    },
    handleClickOutside(e) {
      if (!this.$el.contains(e.target)) {
        this.dropdownOpen = false;
      }
    },
  },
};
</script>

<style scoped>
ul::-webkit-scrollbar {
  width: 6px;
}
ul::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 3px;
}
</style>
