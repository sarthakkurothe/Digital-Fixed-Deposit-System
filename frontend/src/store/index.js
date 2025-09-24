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
      state.fds = [];
      localStorage.removeItem("user");
      localStorage.removeItem("token");
    },
    SET_FDS(state, fds) {
      state.fds = fds;
    },
    SET_LOADING(state, loading) {
      state.loading = loading;
    },
    UPDATE_FD_STATUS(state, { fdId, status }) {
      const fd = state.fds.find(fd => fd.id === fdId);
      if (fd) fd.status = status;
    }
  },

  actions: {
    async fetchFDs({ commit, state }) {
      commit("SET_LOADING", true);
      try {
        const res = await fetch(
          `http://localhost:8080/fd/user/${state.user.id}`,
          {
            headers: {
              Authorization: `bearer ${state.token}`
            }
          }
        );
        if (!res.ok) throw new Error("Failed to fetch FDs");
        const data = await res.json();
        commit("SET_FDS", data);
      } catch (err) {
        console.error("Error fetching FDs", err);
      } finally {
        commit("SET_LOADING", false);
      }
    },

    async breakFD({ commit, state }, fdId) {
      try {
        // Call backend API (PUT or POST depending on your design)
        const res = await fetch(
          `http://localhost:8080/fds/${fdId}/break`,
          {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
              Authorization: `bearer ${state.token}`
            }
          }
        );
        if (!res.ok) throw new Error("Failed to break FD");

        // Update store state locally
        commit("UPDATE_FD_STATUS", { fdId, status: "BROKEN" });
      } catch (err) {
        console.error("Error breaking FD", err);
      }
    },

    login({ commit }, token) {
      commit("setToken", token);
    },
    setUserData({ commit }, user) {
      commit("setUser", user);
    },
    logout({ commit }) {
      commit("clearAuth");
    }
  },

  getters: {
    isAuthenticated: (state) => !!state.token,
    getUser: (state) => state.user,
    getToken: (state) => state.token,
    getFDs: (state) => state.fds
  }
});
