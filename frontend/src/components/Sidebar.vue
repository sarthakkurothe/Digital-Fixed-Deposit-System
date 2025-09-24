<template>
  <!-- Overlay for mobile -->
  <div 
    v-if="showMobile" 
    class="fixed inset-0 bg-black bg-opacity-50 z-40 md:hidden"
    @click="closeMobileSidebar"
  ></div>

  <!-- Sidebar -->
  <div class="sidebar-container" :class="sidebarClasses">
    <!-- Sidebar Content -->
    <div class="flex flex-col h-full bg-white border-r border-gray-200">
        

      <!-- Navigation Menu -->
      <nav class="flex-1 p-4">
        <div class="space-y-1">

            <!-- Logo -->
          <router-link to="/user/dashboard" class="flex items-center space-x-2 text-gray-800 hover:text-blue-600 transition-colors">
            <span class="text-3xl">💰</span>
            <span v-if="!isCollapsed" class="text-xl font-bold">DigitalFD</span>
          </router-link>
                <br>
                
          <router-link 
            to="/user/dashboard" 
            class="nav-item group"
            :class="navItemClasses($route.name === 'Dashboard')"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2H5a2 2 0 00-2-2z"></path>
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 5a2 2 0 012-2h4a2 2 0 012 2v4H8V5z"></path>
                </svg>
              </div>
              <span v-if="!isCollapsed" class="font-medium">Dashboard</span>
            </div>
            <div v-if="$route.name === 'Dashboard'" class="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 rounded-r"></div>
          </router-link>
          
          <router-link 
            to="/user/book-fd" 
            class="nav-item group"
            :class="navItemClasses($route.name === 'BookFD')"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                </svg>
              </div>
              <span v-if="!isCollapsed" class="font-medium">Book FD</span>
            </div>
            <div v-if="$route.name === 'BookFD'" class="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 rounded-r"></div>
          </router-link>
          
          <router-link 
            to="/user/my-fds" 
            class="nav-item group"
            :class="navItemClasses($route.name === 'MyFDs')"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>
                </svg>
              </div>
              <span v-if="!isCollapsed" class="font-medium">My FDs</span>
            </div>
            <div v-if="$route.name === 'MyFDs'" class="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 rounded-r"></div>
          </router-link>
          
          <router-link 
            to="/user/calculator" 
            class="nav-item group"
            :class="navItemClasses($route.name === 'Calculator')"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z"></path>
                </svg>
              </div>
              <span v-if="!isCollapsed" class="font-medium">Calculator</span>
            </div>
            <div v-if="$route.name === 'Calculator'" class="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 rounded-r"></div>
          </router-link>
          
          <router-link 
            to="/user/support" 
            class="nav-item group"
            :class="navItemClasses($route.name === 'Support')"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
              </div>
              <span v-if="!isCollapsed" class="font-medium">Support</span>
            </div>
            <div v-if="$route.name === 'Support'" class="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 rounded-r"></div>
          </router-link>
        </div>
      </nav>

      <!-- User Profile Section -->
      <div class="p-4 border-t border-gray-200">
        <button
          @click="logout"
          class="flex items-center w-full px-4 py-2 text-gray-700 rounded-lg hover:bg-red-50 hover:text-red-600 transition group"
        >
          <!-- Fixed-size icon -->
          <ArrowLeftEndOnRectangleIcon
            class="w-6 h-6 text-gray-500 group-hover:text-red-600"
          />

          <!-- Only show text if sidebar is expanded -->
          <span
            v-if="!isCollapsed"
            class="ml-3 font-medium whitespace-nowrap"
          >
            Logout
          </span>
        </button>
      </div>

      <!-- Bottom Actions -->
      <div class="p-4 border-t border-gray-100">
        <button 
          @click="toggleSidebar"
          class="hidden md:flex w-full items-center justify-center px-3 py-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-md transition-colors duration-200"
          :class="{ 'px-2': isCollapsed }"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path v-if="isCollapsed" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
            <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
          </svg>
          <span v-if="!isCollapsed" class="ml-2 text-xs font-medium">Collapse</span>
        </button>
         
      </div>
    </div>
  </div>
</template>

<script>
import { ArrowLeftEndOnRectangleIcon } from '@heroicons/vue/24/solid'
import { ArrowLeft } from 'lucide-vue-next'
import { ref, computed } from 'vue'

export default {
  name: 'Sidebar',
  components: { ArrowLeftEndOnRectangleIcon},
  setup(props, { emit }) {
    const isCollapsed = ref(false)
    const showMobile = ref(false)

    const sidebarClasses = computed(() => {
      return [
        'fixed top-0 left-0 h-full shadow-lg z-50 transition-all duration-300',
        {
          'w-22': isCollapsed.value,
          'w-64': !isCollapsed.value,
          'transform -translate-x-full md:translate-x-0': !showMobile.value,
          'transform translate-x-0': showMobile.value
        }
      ]
    })

    const navItemClasses = (isActive) => {
      return [
        'flex items-center px-3 py-3 rounded-lg transition-all duration-200 relative text-gray-700 mb-1',
        {
          'bg-blue-50 text-blue-700 border-r-2 border-blue-700': isActive,
          'hover:bg-gray-100 hover:text-gray-900': !isActive,
          'justify-center': isCollapsed.value
        }
      ]
    }

    const toggleSidebar = () => {
      if (window.innerWidth < 768) {
        // On mobile, toggle visibility
        showMobile.value = !showMobile.value
      } else {
        // On desktop, toggle collapse
        isCollapsed.value = !isCollapsed.value
        emit('sidebar-toggled', isCollapsed.value)
      }
    }

    const openMobileSidebar = () => {
      showMobile.value = true
    }

    const closeMobileSidebar = () => {
      showMobile.value = false
    }

    return {
      isCollapsed,
      showMobile,
      sidebarClasses,
      navItemClasses,
      toggleSidebar,
      openMobileSidebar,
      closeMobileSidebar
    }
  }
}
</script>

<style scoped>
.nav-item {
  text-decoration: none;
}

/* Custom animations */
@media (max-width: 768px) {
  .sidebar-container {
    width: 16rem !important;
  }
}
</style>
