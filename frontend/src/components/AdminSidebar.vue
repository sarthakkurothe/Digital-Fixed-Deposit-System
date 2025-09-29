<template>
  <!-- Overlay for mobile -->
  <div
    v-if="showMobile"
    class="fixed inset-0 bg-black bg-opacity-50 z-40 md:hidden"
    @click="closeMobileSidebar"
  ></div>

  <!-- Admin Sidebar -->
  <div class="sidebar-container" :class="sidebarClasses">
    <div class="flex flex-col h-full bg-white border-r border-gray-200">
      <!-- Logo + Collapse -->
      <div class="flex items-center justify-between p-4 border-b border-gray-200 mt-4">
        <div class="flex items-center space-x-4 mb-1 group">
          <div
            v-if="!isCollapsed"
            class="w-9 h-9 rounded-lg flex items-center justify-center text-white shadow-sm transform transition-transform duration-200 group-hover:scale-105"
            style="background: linear-gradient(135deg, #2563eb, #4f46e5)"
          >
            <Vault class="w-5 h-5" />
          </div>
          <div
            v-if="isCollapsed"
            class="w-9 h-9 rounded-lg flex items-center justify-center text-white shadow-sm transform transition-transform duration-200 group-hover:scale-105 ml-5"
            style="background: linear-gradient(135deg, #2563eb, #4f46e5)"
          >
            <Vault class="w-5 h-5" />
          </div>
          <span v-if="!isCollapsed" class="text-xl md:text-2xl font-extrabold text-slate-900"
            >SmartFD</span
          >
        </div>

        <!-- Collapse Arrow -->
        <button
          @click="toggleSidebar"
          class="flex items-center justify-center w-8 h-8 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-md transition-colors duration-200"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              v-if="isCollapsed"
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M9 5l7 7-7 7"
            ></path>
            <path
              v-else
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M15 19l-7-7 7-7"
            ></path>
          </svg>
        </button>
      </div>

      <!-- Navigation Menu -->
      <nav class="flex-1 p-4">
        <div class="space-y-1">
          <!-- Admin Dashboard -->
          <router-link
            to="/admin"
            class="nav-item group"
            :class="[
              navItemClasses($route.path === '/admin'),
              { 'nav-item-active': $route.path === '/admin' },
            ]"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <LayoutDashboard class="w-5 h-5" />
              </div>
              <span v-if="!isCollapsed" class="font-medium">Dashboard</span>
            </div>
            <div v-if="$route.path === '/admin'" class="active-indicator"></div>
          </router-link>

          <!-- View All FDs -->
          <router-link
            to="/admin/fds"
            class="nav-item group"
            :class="[
              navItemClasses($route.path === '/admin/fds'),
              { 'nav-item-active': $route.path === '/admin/fds' },
            ]"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <Building class="w-5 h-5" />
              </div>
              <span v-if="!isCollapsed" class="font-medium">View All FD's</span>
            </div>
            <div v-if="$route.path === '/admin/fds'" class="active-indicator"></div>
          </router-link>

          <!-- Support Tickets -->
          <router-link
            to="/admin/tickets"
            class="nav-item group"
            :class="[
              navItemClasses($route.path === '/admin/tickets'),
              { 'nav-item-active': $route.path === '/admin/tickets' },
            ]"
          >
            <div class="flex items-center">
              <div class="w-5 h-5 flex items-center justify-center mr-3">
                <HelpCircle class="w-5 h-5" />
              </div>
              <span v-if="!isCollapsed" class="font-medium">Support Tickets</span>
            </div>
            <div v-if="$route.path === '/admin/tickets'" class="active-indicator"></div>
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
          <LogOutIcon class="w-6 h-6" :class="isCollapsed ? 'scale-x-[-1]' : ''" />
          <span v-if="!isCollapsed" class="ml-3 font-medium whitespace-nowrap"> Sign out </span>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { Shield, LayoutDashboard, Building, HelpCircle, LogOutIcon, Vault } from 'lucide-vue-next';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'AdminSidebar',
  components: { Shield, LayoutDashboard, Building, HelpCircle, LogOutIcon, Vault },

  data() {
    return {
      isCollapsed: false,
      showMobile: false,
    };
  },

  computed: {
    ...mapGetters(['currentUser']),
    sidebarClasses() {
      return [
        'fixed top-0 left-0 h-full shadow-lg z-50 transition-all duration-300',
        {
          'w-30': this.isCollapsed,
          'w-65': !this.isCollapsed,
          'transform -translate-x-full md:translate-x-0': !this.showMobile,
          'transform translate-x-0': this.showMobile,
        },
      ];
    },
    adminEmail() {
      return this.currentUser?.email || 'admin@smartfd.com';
    },
  },

  methods: {
    ...mapActions(['logout']),
    navItemClasses(isActive) {
      return [
        'flex items-center px-3 py-3 rounded-lg transition-all duration-300 relative text-gray-700 mb-1 transform',
        {
          'bg-blue-600/90 text-white shadow-md backdrop-blur-md scale-105': isActive,
          'hover:bg-gray-100 hover:shadow-lg hover:scale-[1.02] hover:text-gray-900': !isActive,
          'justify-center': this.isCollapsed,
        },
      ];
    },
    toggleSidebar() {
      if (window.innerWidth < 768) {
        this.showMobile = !this.showMobile;
      } else {
        this.isCollapsed = !this.isCollapsed;
        this.$emit('toggle', this.isCollapsed);
      }
    },
    openMobileSidebar() {
      this.showMobile = true;
    },
    closeMobileSidebar() {
      this.showMobile = false;
    },
    signout() {
      this.logout();
      this.$router.push('/');
    },
  },
};
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
