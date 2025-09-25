
<template>
  <div id="app" class="min-h-screen bg-gray-50">
        
        <!-- Page Content -->
      <main class="flex-1 overflow-auto bg-gray-50">
        <router-view />
      </main>
    </div>
    
</template>

<script>
import Navbar from './components/Navbar.vue'
import Sidebar from './components/Sidebar.vue'

export default {
  name: 'App',
  components: {
    Navbar,
    Sidebar
  },
  data() {
    return {
      sidebarCollapsed: false,
      isMobile: false
    }
  },
  computed: {
    mainContentClasses() {
      if (this.isMobile) {
        return 'ml-0'
      }
      return this.sidebarCollapsed ? 'md:ml-16' : 'md:ml-64'
    }
  },
  methods: {
    handleToggleSidebar() {
      if (this.$refs.sidebar) {
        this.$refs.sidebar.toggleSidebar()
      }
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

/* Smooth scrolling */
html {
  scroll-behavior: smooth;
}

/* Custom scrollbar */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* Loading animations */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.fade-in {
  animation: fadeIn 0.3s ease-in;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .ml-64 {
    margin-left: 0 !important;
  }
}
</style>
