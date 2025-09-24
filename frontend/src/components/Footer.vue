<template>
  <footer class="bg-gray-900 text-gray-200">
    <div class="max-w-7xl mx-auto px-6 py-12">
      <div class="grid lg:grid-cols-4 gap-12">
        <!-- Brand -->
        <div class="space-y-4">
          <div class="flex items-center space-x-2">
            <div
              class="flex h-10 w-10 items-center justify-center rounded-lg bg-gradient-to-r from-blue-600 to-indigo-600"
            >
              <Banknote class="h-6 w-6 text-white" />
            </div>
            <span class="text-2xl font-bold">SmartFD</span>
          </div>
          <p class="text-gray-400 text-sm leading-relaxed">
            Your trusted partner for digital Fixed Deposit management. Secure,
            simple, and smart investments.
          </p>
        </div>

        <!-- Quick Links -->
        <div class="space-y-4">
          <h3 class="font-semibold text-white">Quick Links</h3>
          <div class="space-y-2">
            <button
              v-for="item in navItems"
              :key="item.id"
              @click="scrollToSection(item.id)"
              class="block text-gray-400 hover:text-white transition-colors cursor-pointer text-left w-full"
            >
              {{ item.label }}
            </button>
          </div>
        </div>

        <!-- Support -->
        <div class="space-y-4">
          <h3 class="font-semibold text-white">Support</h3>
          <div class="space-y-3 text-gray-400 text-sm">
            <div class="flex items-center space-x-2 cursor-pointer">
              <Phone class="h-4 w-4" />
              <span>1800-XXX-XXXX</span>
            </div>
            <div class="flex items-center space-x-2 cursor-pointer">
              <Mail class="h-4 w-4" />
              <a
                href="mailto:support@smartfd.com"
                class="hover:text-white transition-colors"
              >
                support@smartfd.com
              </a>
            </div>
            <div class="flex items-center space-x-2 cursor-pointer">
              <MapPin class="h-4 w-4" />
              <span>Bangalore, India</span>
            </div>
          </div>
        </div>

        <!-- CTA -->
        <div class="space-y-4">
          <h3 class="font-semibold text-white">Get Started</h3>
          <p class="text-gray-400 text-sm">
            Ready to start your FD journey? Create your account today.
          </p>
          <button
            @click="goToRegister"
            class="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors cursor-pointer"
          >
            Create Account
          </button>
        </div>
      </div>

      <!-- Bottom section -->
      <div
        class="border-t border-gray-700 mt-10 pt-6 flex flex-col md:flex-row justify-between items-center"
      >
        <p class="text-gray-500 text-sm">© 2024 SmartFD. All rights reserved.</p>
        <div class="flex space-x-6 mt-4 md:mt-0">
          <button
            @click="scrollToSection('privacy')"
            class="text-gray-400 hover:text-white text-sm transition-colors cursor-pointer"
          >
            Privacy Policy
          </button>
          <button
            @click="scrollToSection('terms')"
            class="text-gray-400 hover:text-white text-sm transition-colors cursor-pointer"
          >
            Terms of Service
          </button>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
import { Banknote, Mail, Phone, MapPin } from "lucide-vue-next";
import { useRouter } from "vue-router";

export default {
  name: "Footer",
  components: { Banknote, Mail, Phone, MapPin },
  setup() {
    const router = useRouter();

    const navItems = [
      { id: "features", label: "Features" },
      { id: "security", label: "Security" },
      { id: "support", label: "Support" },
      { id: "careers", label: "Careers" },
    ];

    const goToRegister = () => {
      router.push("/register");
    };

    const scrollToSection = (id) => {
      // If not on Home page, navigate first then scroll
      if (router.currentRoute.value.name !== "Home") {
        router.push("/").then(() => {
          nextTick(() => {
            const el = document.getElementById(id);
            if (el) el.scrollIntoView({ behavior: "smooth", block: "start" });
          });
        });
      } else {
        const el = document.getElementById(id);
        if (el) el.scrollIntoView({ behavior: "smooth", block: "start" });
      }
    };

    return { navItems, goToRegister, scrollToSection };
  },
};
</script>
