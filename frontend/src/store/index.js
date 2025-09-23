
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
      // API call will be implemented by team
      commit('SET_LOADING', false)
    },
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
