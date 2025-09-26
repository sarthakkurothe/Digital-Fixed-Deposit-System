<template>
  <!-- Overlay for mobile -->
  <div 
    v-if="showMobile" 
    class="fixed inset-0 bg-black bg-opacity-50 z-40 md:hidden"
    @click="closeMobileSidebar"
  ></div>

  <!-- Sidebar -->
  <div class="sidebar-container" :class="sidebarClasses">
    <div class="flex flex-col h-full bg-white border-r border-gray-200 ">

      <!-- Logo + Collapse -->
      <div class="flex items-center justify-between p-4 border-b border-gray-200 mt-4">
  <router-link 
    to="/user/dashboard" 
    class="flex items-center space-x-4 mb-1 group"
  >
    <div v-if="!isCollapsed"
      class="w-9 h-9 rounded-lg flex items-center justify-center text-white shadow-sm transform transition-transform duration-200 group-hover:scale-105"
      style="background: linear-gradient(135deg,#2563eb,#4f46e5)"
    >
      <Vault class="w-5 h-5" />
    </div>
    <div v-if="isCollapsed"
      class="w-9 h-9 rounded-lg flex items-center justify-center text-white shadow-sm transform transition-transform duration-200 group-hover:scale-105 ml-5"
      style="background: linear-gradient(135deg,#2563eb,#4f46e5)"
    >
      <Vault class="w-5 h-5" />
    </div>
    <span v-if="!isCollapsed" class="text-xl md:text-2xl font-extrabold text-slate-900">SmartFD</span>
  </router-link>

        <!-- Collapse Arrow -->
        <button 
          @click="toggleSidebar"
          class="flex items-center justify-center w-8 h-8 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-md transition-colors duration-200"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path v-if="isCollapsed" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
            <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
          </svg>
        </button>
      </div>

      <!-- Navigation Menu -->
      <nav class="flex-1 p-4">
        <div class="space-y-1">

          <!-- Dashboard -->
          <router-link 
            to="/user/dashboard" 
            class="nav-item group"
            :class="[navItemClasses($route.name === 'Dashboard'), { 'nav-item-active': $route.name === 'Dashboard' }]"
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
            <div v-if="$route.name === 'Dashboard'" class="active-indicator"></div>
          </router-link>

          <!-- Book FD -->
          <router-link 
            to="/user/book-fd" 
            class="nav-item group"
            :class="[navItemClasses($route.name === 'BookFD'), { 'nav-item-active': $route.name === 'BookFD' }]"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                </svg>
              </div>
              <span v-if="!isCollapsed" class="font-medium">Book FD</span>
            </div>
            <div v-if="$route.name === 'BookFD'" class="active-indicator"></div>
          </router-link>

          <!-- My FDs -->
          <router-link 
            to="/user/my-fds" 
            class="nav-item group"
            :class="[navItemClasses($route.name === 'MyFDs'), { 'nav-item-active': $route.name === 'MyFDs' }]"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>
                </svg>
              </div>
              <span v-if="!isCollapsed" class="font-medium">My FDs</span>
            </div>
            <div v-if="$route.name === 'MyFDs'" class="active-indicator"></div>
          </router-link>

          <!-- Calculator -->
          <router-link 
            to="/user/calculator" 
            class="nav-item group"
            :class="[navItemClasses($route.name === 'Calculator'), { 'nav-item-active': $route.name === 'Calculator' }]"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z"></path>
                </svg>
              </div>
              <span v-if="!isCollapsed" class="font-medium">Calculator</span>
            </div>
            <div v-if="$route.name === 'Calculator'" class="active-indicator"></div>
          </router-link>

          <!-- Support -->
          <router-link 
            to="/user/support" 
            class="nav-item group"
            :class="[navItemClasses($route.name === 'Support'), { 'nav-item-active': $route.name === 'Support' }]"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
              </div>
              <span v-if="!isCollapsed" class="font-medium">Support</span>
            </div>
            <div v-if="$route.name === 'Support'" class="active-indicator"></div>
          </router-link>

        </div>
      </nav>

      <!-- Logout Button -->
      <div class="p-4 border-t border-gray-200">
        <button
          @click="signout"
          class="flex w-full px-4 py-2 rounded-lg bg-red-100 text-red-700 hover:bg-red-200 hover:text-red-800 transition group cursor-pointer"
          :class="isCollapsed ? 'justify-center' : 'justify-start'"
        >
          <LogOutIcon  class="w-6 h-6 " :class="isCollapsed? 'scale-x-[-1]':''"/>
          <span
            v-if="!isCollapsed"
            class="ml-3 font-medium whitespace-nowrap"
          >
            Sign out
          </span>
        </button>
      </div>


    </div>
  </div>
</template>

<script>
import { Vault, LogOutIcon } from 'lucide-vue-next'
import { mapActions } from 'vuex/dist/vuex.cjs.js'

export default {
  name: 'Sidebar',
  components: { Vault, LogOutIcon },

  data() {
    return {
      isCollapsed: false,
      showMobile: false
    }
  },

  computed: {
    sidebarClasses() {
      return [
        'fixed top-0 left-0 h-full shadow-lg z-50 transition-all duration-300',
        {
          'w-30': this.isCollapsed, 
          'w-65': !this.isCollapsed, 
          'transform -translate-x-full md:translate-x-0': !this.showMobile,
          'transform translate-x-0': this.showMobile
        }
      ]
    }
  },

  methods: {
    ...mapActions(['logout']),
    navItemClasses(isActive) {
      return [
        'flex items-center px-3 py-3 rounded-lg transition-all duration-300 relative text-gray-700 mb-1 transform',
        {
          'bg-blue-600/90 text-white shadow-md backdrop-blur-md scale-105': isActive,
          'hover:bg-gray-100 hover:shadow-lg hover:scale-[1.02] hover:text-gray-900': !isActive,
          'justify-center': this.isCollapsed
        }
      ]
    },
    toggleSidebar() {
      if (window.innerWidth < 768) {
        this.showMobile = !this.showMobile
      } else {
        this.isCollapsed = !this.isCollapsed
        this.$emit('sidebar-toggled', this.isCollapsed)
      }
    },
    openMobileSidebar() {
      this.showMobile = true
    },
    closeMobileSidebar() {
      this.showMobile = false
    },
    signout() {
      this.logout()
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.nav-item {
  text-decoration: none;
  transition: all 0.3s ease-in-out;
}

/* Smooth pop-out */
.nav-item:hover {
  transform: scale(1.02);
}

/* Extra glowing effect for active tab */
.nav-item-active {
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.4);
}
</style>
