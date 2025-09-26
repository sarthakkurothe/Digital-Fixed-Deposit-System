<template>
  <nav class="bg-white border-b border-gray-200 shadow-sm sticky top-0 z-40 h-22">
    <div class="px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <!-- Left Section - Mobile menu + Logo -->
        <div class="flex items-center space-x-4">
          <!-- Mobile menu button -->
          <button 
            @click="toggleSidebar"
            class="md:hidden inline-flex items-center justify-center p-2 rounded-md text-gray-600 hover:text-gray-900 hover:bg-gray-100 focus:outline-none"
          >
            <span class="sr-only">Open sidebar</span>
            <svg class="h-5 w-5" stroke="currentColor" fill="none" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7" />
            </svg>
          </button>
           
          <!-- Welcome Message -->
          <div class="hidden sm:block">
            <h1 class="text-lg font-semibold text-gray-900 mt-4">
              Welcome back, {{ getUserName }}! 👋 
            </h1>
            <p class="text-sm text-gray-600">Here's your fixed deposit portfolio overview</p>
          </div> 
        </div>
 
        <!-- Right Section - User Avatar -->
        <div class="flex items-center space-x-4">
          <div class="flex items-center space-x-2">
            <div class="hidden md:block text-right mt-4">
              <div class="text-m font-medium text-gray-900">{{ getUserName }}</div>
              <div class="text-sm text-gray-600">{{ getUserEmail }}</div>
            </div>
            <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center mt-4">
              <span class="text-blue-600 text-sm font-medium">
                {{ getUserName ? getUserName.charAt(0).toUpperCase() : "U" }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "Navbar",
  computed: {
    ...mapGetters(["getUserName", "getUserEmail"])
  },
  methods: {
    toggleSidebar() {
      this.$emit("toggle-sidebar");
    }
  }
};
</script>
