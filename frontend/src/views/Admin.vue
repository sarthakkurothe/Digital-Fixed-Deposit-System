<template>
  <div class="p-6 space-y-10">
    <!-- Stats Grid -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      <!-- Total Users Card -->
      <div class="bg-white p-4 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-gray-600">Total Users</h3>
          <div class="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
            <Users class="w-5 h-5 text-blue-600" />
          </div>
        </div>
        <p class="mt-2 text-2xl font-bold text-gray-900">{{ totalUsers }}</p>
      </div>

      <!-- Total FDs Card -->
      <div
        class="bg-gradient-to-r from-green-50 to-green-100 p-4 rounded-lg border border-green-200 hover:shadow-sm transition-shadow"
      >
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-green-700">Total FDs</h3>
          <div class="w-8 h-8 bg-green-200 rounded-lg flex items-center justify-center">
            <Building class="w-5 h-5 text-green-600" />
          </div>
        </div>
        <p class="mt-2 text-2xl font-bold text-green-800">{{ totalFDs }}</p>
      </div>

      <!-- Total Tickets Card -->
      <div class="bg-white p-4 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-gray-600">Total Tickets</h3>
          <div class="w-8 h-8 bg-yellow-100 rounded-lg flex items-center justify-center">
            <Inbox class="w-5 h-5 text-yellow-600" />
          </div>
        </div>
        <p class="mt-2 text-2xl font-bold text-gray-900">{{ totalTickets }}</p>
      </div>

      <!-- Open Tickets Card -->
      <div class="bg-white p-4 rounded-lg border border-gray-200 hover:shadow-sm transition-shadow">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-gray-600">Open Tickets</h3>
          <div class="w-8 h-8 bg-red-100 rounded-lg flex items-center justify-center">
            <HelpCircle class="w-5 h-5 text-red-600" />
          </div>
        </div>
        <p class="mt-2 text-2xl font-bold text-gray-900">{{ openTickets }}</p>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <TicketsPieChart :open="openTickets" :closed="totalTickets - openTickets" />

      <!--Quick Actions -->
      <div class="bg-white p-6 rounded-lg border border-gray-200 shadow-sm space-y-6">
        <h2 class="text-lg font-semibold text-gray-900 mb-4 text-center">Quick Actions</h2>
        <!-- Manage FDs -->
        <router-link
          to="/admin/fds"
          class="flex items-center h-[120px] p-4 border border-blue-200 rounded-lg hover:bg-blue-50 transition-colors group"
        >
          <div
            class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-blue-200 transition-colors"
          >
            <Building class="w-6 h-6 text-blue-500" />
          </div>
          <div>
            <h3 class="font-medium text-gray-900">Manage FDs</h3>
            <p class="text-sm text-gray-600">View and manage all deposits</p>
          </div>
          <ChevronRight class="w-5 h-5 text-gray-400 ml-auto" />
        </router-link>

        <!-- Support Tickets -->
        <router-link
          to="/admin/tickets"
          class="flex items-center h-[120px] p-4 border border-green-200 rounded-lg hover:bg-green-50 transition-colors group"
        >
          <div
            class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center mr-4 group-hover:bg-green-200 transition-colors"
          >
            <HelpCircle class="w-6 h-6 text-green-500" />
          </div>
          <div>
            <h3 class="font-medium text-gray-900">Support Tickets</h3>
            <p class="text-sm text-gray-600">Handle customer queries</p>
          </div>
          <ChevronRight class="w-5 h-5 text-gray-400 ml-auto" />
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import {
  MenuIcon,
  Users,
  Building,
  Inbox,
  HelpCircle,
  ChevronRight,
  BarChart2,
} from 'lucide-vue-next';
import { mapGetters, mapActions } from 'vuex';
import TicketsPieChart from '../components/TicketsPieChart.vue';

export default {
  name: 'Admin',
  components: {
    MenuIcon,
    Users,
    Building,
    Inbox,
    HelpCircle,
    ChevronRight,
    TicketsPieChart,
  },
  data() {
    return {
      sidebarCollapsed: false,
      isMobile: false,
    };
  },
  computed: {
    ...mapGetters(['getDashboardInfo']),
    totalUsers() {
      return this.getDashboardInfo.totalUsers || 0;
    },
    totalFDs() {
      return this.getDashboardInfo.totalFDs || 0;
    },
    totalTickets() {
      return this.getDashboardInfo.totalTickets || 0;
    },
    openTickets() {
      return this.getDashboardInfo.totalOpenTickets || 0;
    },
    mainContentClasses() {
      if (this.isMobile) {
        return 'ml-0';
      }
      return this.sidebarCollapsed ? 'md:ml-30' : 'md:ml-65';
    },
  },
  methods: {
    ...mapActions(['logout']),
    handleToggleSidebar() {
      if (this.$refs.sidebar) {
        this.$refs.sidebar.toggleSidebar();
      }
    },
    handleSidebarToggle(collapsed) {
      this.sidebarCollapsed = collapsed;
    },
    checkMobile() {
      this.isMobile = window.innerWidth < 768;
    },
    async logout() {
      try {
        await this.logout();
        this.$router.push('/login');
      } catch (error) {
        console.error('Logout error:', error);
      }
    },
  },
  mounted() {
    this.checkMobile();
    window.addEventListener('resize', this.checkMobile);
    this.$store.dispatch('fetchDashboardInfo');
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.checkMobile);
  },
};
</script>
