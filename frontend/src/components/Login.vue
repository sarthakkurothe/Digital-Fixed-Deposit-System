<template>
  <div class="grid grid-cols-1 lg:grid-cols-2 h-screen w-screen bg-white">

    <div class="flex items-center justify-center text-gray-800 bg-gray-200 p-8">
      <div class="max-w-md">
        <h2 class="text-4xl font-bold mb-4">Fixed Deposit System</h2>
        <p class="text-lg leading-relaxed">
          Empowering businesses with secure and scalable solutions.  
          Together, let’s build the future of finance and management.
        </p>
      </div>
    </div>

    <div class="flex flex-col justify-center items-center bg-gray-100 p-8">
      <h2 class="text-4xl font-bold mb-8 text-black text-center">Welcome</h2>
      <form @submit.prevent="handleLogin" class="w-full max-w-md space-y-6">
        <div>
          <label class="block text-sm font-medium text-black mb-2">Email</label>
          <input 
            v-model="email" 
            type="text" 
            required 
            class="w-full px-4 py-3 rounded-lg border focus:outline-none focus:ring-2 focus:ring-blue-500 text-black"
            placeholder="Enter your username"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-black mb-2">Password</label>
          <input 
            v-model="password" 
            type="password" 
            required 
            class="w-full px-4 py-3 rounded-lg border focus:outline-none focus:ring-2 focus:ring-blue-500 text-black"
            placeholder="Enter your password"
          />
        </div>
        <button type="submit" :disabled="loading" class="w-full py-3 px-4 bg-blue-900 text-white rounded-lg hover:bg-blue-950 transition text-lg font-semibold">
            {{ loading ? "Logging in..." : "Login" }}
        </button>
    
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { mapActions } from 'vuex';

export default {
  data() {
    return {
      email: "",
      password: "",
      loading: false,
      error: null,
    };
  },
  methods: {
    ...mapActions(["login", "setUserData"]),

    async handleLogin() {
      this.loading = true;
      this.error = null;

      
      try {
          console.log(this.email, this.password);
        
        const res = await axios.post("http://localhost:8080/auth/login", {
          email: this.email,
          password: this.password,
        });

        const token = res.data.token;

        console.log("Received token:", token);

        
        this.login(token);

        const meRes = await axios.get("http://localhost:8080/auth/me", {
          headers: { Authorization: `bearer ${token}` },
        });

        this.setUserData(meRes.data);

        console.log("User data:", meRes.data);

        this.$router.push("/dashboard");
      } catch (err) {
        this.error = err.response?.data?.message || "Invalid credentials";
        console.error("Login error:", err);
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>
