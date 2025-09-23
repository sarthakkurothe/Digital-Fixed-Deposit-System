<<<<<<< HEAD
// store/index.js
import { createStore } from "vuex";

export default createStore({
  state: {
    user: JSON.parse(localStorage.getItem("user")) || null,
    token: localStorage.getItem("token") || null,
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
    login({ commit }, token) {
      commit("setToken", token);
    },
    setUserData({ commit }, user) {
      commit("setUser", user);
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
=======
import { createStore } from 'vuex'

// Define initial state
const state = {
  user: {
    name: 'Rajesh Kumar',
    email: 'rajesh.kumar@example.com',
    isAuthenticated: true
  },
  fds: [],
  loading: false
}

// Define mutations
const mutations = {
  SET_USER(state, user) {
    state.user = user
  },
  SET_FDS(state, fds) {
    state.fds = fds
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  }
}

// Define actions
const actions = {
  // TODO: Implement actions for API calls
  async fetchFDs({ commit }) {
    commit('SET_LOADING', true)
    // API call will be implemented by team
    commit('SET_LOADING', false)
  }
}

// Define getters
const getters = {
  user: state => state.user,
  fds: state => state.fds,
  isLoading: state => state.loading
}

// Create and export store
export default createStore({
  state,
  mutations,
  actions,
  getters
})
>>>>>>> main
