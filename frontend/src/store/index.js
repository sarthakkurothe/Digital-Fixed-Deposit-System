
import axios from '../api';
import { createStore } from "vuex";

export default createStore({
  state: {
    user: JSON.parse(localStorage.getItem("user")) || null,
    token: localStorage.getItem("token") || null,
    fds: [],
    loading: false
  },

  mutations: {
    setUser(state, user) {
      state.user = user;
      localStorage.setItem("user", JSON.stringify(user));
    },
    setToken(state, token) {
      state.token = token;
      localStorage.setItem("token", token);
    },
    clearAuth(state) {
      state.user = null;
      state.token = null;
      localStorage.removeItem("user");
      localStorage.removeItem("token");
    },
  },

  actions: {
    async fetchFDs({ commit }) {
      commit('SET_LOADING', true)
      
      commit('SET_LOADING', false)
    },
    async login({ commit }, credentials) {
      const res = await axios.post('/auth/login',{
        email: credentials.email,
        password: credentials.password
      })
      const token = res.data.token;
      commit("setToken", token);
      
    },
    async setUserData({ commit }, user) {
      const token = localStorage.getItem("token");
      if (!token) return;
      const res = await axios.get('/auth/me')

      commit("setUser", res.data);
    },
    async register({ commit }, credentials) {
      try {
        console.log("Registering user with credentials:", credentials);
        const res = await axios.post('/auth/register', {
          name: credentials.name,
          email: credentials.email,
          age: credentials.age,
          password: credentials.password,
        });

        console.log(res)
        if (res.status === 201) {
          return { success: true };
        }
        
      } catch (err) {
        
        return { success: false, status: err.response?.status, error: err.response?.data};
      }
    },
    logout({ commit }) {
      commit("clearAuth");
    },
  },

  getters: {
    isAuthenticated: (state) => !!state.token,
    getUser: (state) => state.user,
    getToken: (state) => state.token,
  },
});
