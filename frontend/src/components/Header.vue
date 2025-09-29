<template>
  <header
    class="sticky top-0 z-50 bg-gradient-to-r from-white/95 via-white/95 to-indigo-50/10 shadow-sm backdrop-blur-sm border-b border-white/20"
    role="banner"
  >
    <div class="container mx-auto flex items-center justify-between px-4 md:px-6 py-3">
      <!-- Logo -->
      <button
        type="button"
        class="flex items-center gap-3 group cursor-pointer"
        @click="scrollToSection('hero')"
        aria-label="Go to Home"
      >
        <div
          class="w-9 h-9 rounded-lg flex items-center justify-center text-white shadow-sm transform transition-transform duration-200 group-hover:scale-105"
          style="background: linear-gradient(135deg, #2563eb, #4f46e5)"
        >
          <Vault class="w-5 h-5" />
        </div>
        <span class="text-xl md:text-2xl font-extrabold text-slate-900">SmartFD</span>
      </button>

      <!-- Nav: only on HomeView -->
      <nav v-if="isHomeView" class="hidden md:block" aria-label="Primary">
        <div ref="navInner" class="relative flex items-center gap-x-12 h-12">
          <button
            v-for="(item, i) in navItems"
            :key="item.id"
            :ref="el => (tabRefs[i] = el)"
            type="button"
            @click="navClick(item.id)"
            class="h-full flex items-center px-2 text-sm font-medium text-slate-700 transition-colors duration-150 bg-transparent hover:text-blue-600 cursor-pointer focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-200"
          >
            {{ item.label }}
          </button>

          <!-- Single sliding underline -->
          <span
            aria-hidden="true"
            class="absolute bottom-0 h-[2px] bg-blue-600 transition-all duration-300 ease-in-out rounded-full"
            :style="`width: ${underline.width}px; left: ${underline.left}px;`"
          />
        </div>
      </nav>

      <!-- Right actions -->
      <div class="flex items-center gap-3">
        <div class="hidden md:flex items-center gap-3">
          <button
            type="button"
            class="text-sm font-medium px-4 h-10 flex items-center justify-center rounded-md bg-white border border-gray-200 text-slate-800 cursor-pointer focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-200"
            @click="goToLogin"
          >
            Login
          </button>

          <button
            type="button"
            class="text-sm font-medium px-4 h-10 flex items-center justify-center rounded-md bg-gradient-to-br from-blue-600 to-indigo-600 text-white shadow-md cursor-pointer focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-300"
            @click="goToRegister"
          >
            Register
          </button>
        </div>

        <!-- Mobile menu toggle -->
        <button
          type="button"
          @click="toggleMenu"
          class="md:hidden p-2 rounded-lg bg-white/80 hover:bg-white transition cursor-pointer focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-200"
          :aria-expanded="isOpen.toString()"
          aria-controls="mobile-menu"
          aria-label="Toggle menu"
        >
          <component :is="isOpen ? X : Menu" class="w-5 h-5 text-slate-800" />
        </button>
      </div>
    </div>

    <!-- Mobile menu -->
    <transition
      name="mobile-menu"
      enter-active-class="ease-out duration-200"
      leave-active-class="ease-in duration-150"
    >
      <div
        v-if="isOpen"
        id="mobile-menu"
        class="md:hidden border-t border-white/20 bg-white/70 backdrop-blur-sm"
      >
        <div class="px-4 pt-4 pb-6">
          <nav class="flex flex-col gap-2" aria-label="Mobile primary">
            <button
              v-for="item in navItems"
              :key="item.id + '-mobile'"
              type="button"
              @click="navClickMobile(item.id)"
              class="w-full text-left px-4 py-2 rounded-md text-sm font-medium text-slate-800 transition relative bg-transparent hover:text-blue-600 cursor-pointer focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-200"
            >
              {{ item.label }}
              <span
                aria-hidden="true"
                class="absolute left-0 bottom-0 h-[2px] bg-blue-600 transition-all duration-200"
                :class="activeSection === item.id ? 'w-full' : 'w-0'"
              />
            </button>
          </nav>

          <div class="mt-4 flex gap-3">
            <button
              type="button"
              class="flex-1 text-sm font-medium px-3 py-2 rounded-md bg-white border border-gray-200 text-slate-800 cursor-pointer focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-200"
              @click="goToLogin"
            >
              Login
            </button>
            <button
              type="button"
              class="flex-1 text-sm font-medium px-3 py-2 rounded-md bg-gradient-to-br from-blue-600 to-indigo-600 text-white cursor-pointer"
              @click="goToRegister"
            >
              Register
            </button>
          </div>
        </div>
      </div>
    </transition>
  </header>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Menu, X, Vault } from 'lucide-vue-next';

const router = useRouter();
const route = useRoute();

const navItems = [
  { id: 'features', label: 'Features' },
  { id: 'security', label: 'Security' },
];

const isOpen = ref(false);
const activeSection = ref('features');

// refs for nav & tabs
const navInner = ref(null);
const tabRefs = ref([]);

// underline state
const underline = ref({ width: 0, left: 0 });

// only show tabs on Home page
const isHomeView = computed(() => route.name === 'Home');

const updateUnderline = async () => {
  await nextTick();
  const index = navItems.findIndex(i => i.id === activeSection.value);
  const tab = tabRefs.value[index];
  const container = navInner.value;
  if (tab && container) {
    underline.value.width = tab.offsetWidth;
    underline.value.left = tab.offsetLeft;
  } else {
    underline.value.width = 0;
    underline.value.left = 0;
  }
};

const toggleMenu = () => {
  isOpen.value = !isOpen.value;
};
const goToLogin = () => router.push({ name: 'Login' });
const goToRegister = () => router.push({ name: 'Register' });

const scrollToSection = id => {
  const el = document.getElementById(id);
  if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  else {
    router.push('/');
  }
};

const navClick = id => {
  activeSection.value = id;
  scrollToSection(id);
  updateUnderline();
  isOpen.value = false;
};
const navClickMobile = id => {
  activeSection.value = id;
  scrollToSection(id);
  isOpen.value = false;
  updateUnderline();
};

let observer = null;
const onResize = () => updateUnderline();

onMounted(() => {
  updateUnderline();

  const sections = navItems.map(i => document.getElementById(i.id)).filter(Boolean);
  if (sections.length) {
    observer = new IntersectionObserver(
      entries => {
        entries.forEach(e => {
          if (e.isIntersecting) {
            activeSection.value = e.target.id;
            updateUnderline();
          }
        });
      },
      { root: null, rootMargin: '-40% 0px -40% 0px', threshold: [0, 0.25, 0.5, 0.75, 1] }
    );
    sections.forEach(s => observer.observe(s));
  }

  window.addEventListener('resize', onResize);
});

onBeforeUnmount(() => {
  if (observer) observer.disconnect();
  window.removeEventListener('resize', onResize);
});
</script>

<style scoped>
/* mobile menu transition */
.mobile-menu-enter-from {
  transform: translateY(-6px);
  opacity: 0;
}
.mobile-menu-enter-to {
  transform: translateY(0);
  opacity: 1;
}
.mobile-menu-leave-from {
  transform: translateY(0);
  opacity: 1;
}
.mobile-menu-leave-to {
  transform: translateY(-6px);
  opacity: 0;
}

/* keep focus visible */
button:focus {
  outline: none;
}

/* mobile container padding */
@media (max-width: 767px) {
  .container {
    padding-left: 1rem;
    padding-right: 1rem;
  }
}
</style>
