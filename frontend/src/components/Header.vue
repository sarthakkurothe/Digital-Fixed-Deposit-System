<template>
  <header class="flex justify-between items-center px-6 py-4 bg-gray-800 text-white">
    <h1 class="text-2xl font-bold">Fixed Deposit System</h1>
    <nav class="space-x-4">
      <button 
        class="px-4 py-2 rounded bg-blue-500 hover:bg-blue-600" 
        @click="goToLogin">
        Login
      </button>
      <button 
        class="px-4 py-2 rounded bg-green-500 hover:bg-green-600" 
        @click="goToRegister">
        Register
      </button>
    </nav>
  </header>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const goToLogin = () => {
  router.push("/login");
};

const goToRegister = () => {
  router.push("/register");
};
</script>
