<template>
  <div class="min-h-screen flex flex-col">
    <!-- Navbar / Header (only for authenticated routes or /user pages) -->

    <div class="flex flex-1">

      <!-- Main content -->
      <main :class="mainContentClasses + ' flex-1'" >
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>


export default {
  name: 'App',
  data() {
    return {
      sidebarCollapsed: false,
      isMobile: false
    }
  },
  computed: {
    mainContentClasses() {
      if (!this.showLayout) return 'w-full' // full width for pages without sidebar/navbar
      if (this.isMobile) return 'ml-0 w-full'
      return this.sidebarCollapsed ? 'md:ml-16 w-full' : 'md:ml-64 w-full'
    },
    showLayout() {
      // Only show Navbar + Sidebar for /user/* routes
      return this.$route.path.startsWith('/user')
    }
  },
  methods: {
    handleToggleSidebar() {
      if (this.$refs.sidebar) this.$refs.sidebar.toggleSidebar()
    },
    handleSidebarToggle(collapsed) {
      this.sidebarCollapsed = collapsed
    },
    checkMobile() {
      this.isMobile = window.innerWidth < 768
    }
  },
  mounted() {
    this.checkMobile()
    window.addEventListener('resize', this.checkMobile)
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.checkMobile)
  }
}
</script>

<style>
/* Global styles */
body {
  margin: 0;
  padding: 0;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

html {
  scroll-behavior: smooth;
}

/* Remove default margin/padding of main content */
main {
  margin: 0;
  padding: 0;
}

/* Custom scrollbar */
::-webkit-scrollbar {
  width: 6px;
}
::-webkit-scrollbar-track { background: #f1f1f1; }
::-webkit-scrollbar-thumb { background: #c1c1c1; border-radius: 3px; }
::-webkit-scrollbar-thumb:hover { background: #a8a8a8; }

/* Fade-in animation */
@keyframes fadeIn { from { opacity: 0 } to { opacity: 1 } }
.fade-in { animation: fadeIn 0.3s ease-in }

/* Responsive adjustments */
@media (max-width: 768px) {
  .ml-64 { margin-left: 0 !important; }
}
</style>
