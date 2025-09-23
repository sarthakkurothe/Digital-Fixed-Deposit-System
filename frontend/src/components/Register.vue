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
      <h2 class="text-4xl font-bold mb-8 text-black text-center">Register</h2>
      <form @submit.prevent="handleRegister" class="w-full max-w-md space-y-6">

        <div>
          <label class="block text-sm font-medium text-black mb-2">Name</label>
          <input 
            v-model="name" 
            type="text" 
            required 
            class="w-full px-4 py-3 rounded-lg border focus:outline-none focus:ring-2 focus:ring-blue-500 text-black"
            placeholder="Enter your full name"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-black mb-2">Email</label>
          <input 
            v-model="email" 
            type="email" 
            required 
            class="w-full px-4 py-3 rounded-lg border focus:outline-none focus:ring-2 focus:ring-blue-500 text-black"
            placeholder="Enter your email"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-black mb-2">Age</label>
          <input 
            v-model="age" 
            type="number" 
            required 
            min="0"
            class="w-full px-4 py-3 rounded-lg border focus:outline-none focus:ring-2 focus:ring-blue-500 text-black"
            placeholder="Enter your age"
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

        <div>
          <label class="block text-sm font-medium text-black mb-2">Confirm Password</label>
          <input 
            v-model="confirmPassword" 
            type="password" 
            required 
            class="w-full px-4 py-3 rounded-lg border focus:outline-none focus:ring-2 focus:ring-blue-500 text-black"
            placeholder="Confirm your password"
          />
        </div>


        <button type="submit" :disabled="loading" class="w-full py-3 px-4 bg-blue-900 text-white rounded-lg hover:bg-blue-950 transition text-lg font-semibold">
            {{ loading ? "Registering..." : "Register" }}
        </button>
    
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      name: "",
      email: "",
      password: "",
      confirmPassword: "",
      age: "",
      loading: false
    };
  },
  methods: {
    async handleRegister() {
        if (this.password !== this.confirmPassword) {
            alert("Passwords do not match!");
            return;
        }

        this.loading = true;
        
        try {
            const res = await axios.post("http://localhost:8080/auth/register", {
                name: this.name,
                email: this.email,
                password: this.password,
                age: this.age
            });
    
            if(res.status === 201) {
                this.$router.push("/login");
            } else {
                alert("Registration failed. Please try again.");
            }
            
        } catch (error) {
            console.log("Registration error:", error);
        }
        finally{
            this.loading = false;
        }
    }
  }
};
</script>
