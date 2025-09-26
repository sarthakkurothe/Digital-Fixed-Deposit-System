import axios from "../api";
import { createStore } from "vuex";

export default createStore({
  state: {
    user: JSON.parse(localStorage.getItem("user")) || null,
    token: localStorage.getItem("token") || null,
    fds: [],
    loading: false,
  },

  mutations: {
    // --- AUTH ---
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

    // --- FDs ---
    SET_FDS(state, fds) {
      state.fds = fds;
    },
    SET_LOADING(state, loading) {
      state.loading = loading;
    },
    UPDATE_FD_STATUS(state, { fdId, status }) {
      const fd = state.fds.find((fd) => fd.id === fdId);
      if (fd) fd.status = status;
    },
  },

  actions: {
    /**
     * Fetch all FDs for the logged-in user
     */
    async fetchFDs({ commit, state }) {
      commit("SET_LOADING", true);
      try {
        const res = await axios.get(`/fd/user/${state.user.id}`);
        commit("SET_FDS", res.data);
      } catch (err) {
        console.error("Error fetching FDs", err);
      } finally {
        commit("SET_LOADING", false);
      }
    },

    async fetchSummary({ state }) {
      try {
        const res = await axios.get(`/user/investments/${state.user.id}`);
        return res.data;
      } catch (err) {
        console.error("Error fetching summary", err);
        return null;
      }
    },

    /**
     * Break an FD (set status to BROKEN_PENDING)
     */
    async fetchBreakPreview({ state }, fdId) {
      try {
        const res = await axios.get(`/fd/${fdId}/break-preview`, {
          headers: { Authorization: `bearer ${state.token}` },
        });
        return res.data;
      } catch (err) {
        console.error("Error fetching break preview", err);
        return null;
      }
    },

    async breakFD({ commit, state }, fdId) {
      try {
        const res = await axios.post(`/fd/${fdId}/break`, {}, {
          headers: { Authorization: `bearer ${state.token}` },
        });

        if (res.status === 201) {
          commit("UPDATE_FD_STATUS", { fdId, status: "BROKEN_PENDING" });
          return {
            success: true,
            message: "Request for Break FD has been created successfully.",
          };
        }

        return {
          success: false,
          message: "Unexpected response from server.",
        };
      } catch (err) {
        return {
          success: false,
          message: "Failed to submit FD Break Request.",
        };
      }
    },

    /**
     * Login user -> save token
     */
    async login({ commit }, credentials) {
      try {
        const res = await axios.post("/auth/login", {
          email: credentials.email,
          password: credentials.password,
        });

        const token = res.data.token;
        commit("setToken", token);

        return { success: true, token };
      } catch (err) {
        return {
          success: false,
          status: err.response?.status,
          error: err.response?.data,
        };
      }
    },

    /**
     * Fetch logged-in user details using stored token
     */
    async setUserData({ commit, state }) {
      if (!state.token) return;

      try {
        const res = await axios.get("/auth/me", {
          headers: { Authorization: `Bearer ${state.token}` },
        });
        commit("setUser", res.data);
      } catch (err) {
        console.error("Error fetching user data", err);
      }
    },

    /**
     * Register a new user
     */
    async register({ commit }, credentials) {
      try { 
        const res = await axios.post("/auth/register", {
          name: credentials.name,
          email: credentials.email,
          age: credentials.age,
          password: credentials.password,
        });

       return res;
      } catch (err) {
        return {
          status: err.response?.status
        }
      }
    },

    /**
     * Logout user -> clear store & localStorage
     */
    logout({ commit }) {
      commit("clearAuth");
    },
  },

  getters: {
    isAuthenticated: (state) => !!state.token,
    getFDsCount: (state) => state.fds.length,
    getUser: (state) => state.user,
    getUserName: (state) => state.user?.name || '',
    getUserEmail: (state) => state.user?.email || '', 
    getToken: (state) => state.token,
    getFDs: (state) => state.fds,
    isLoading: (state) => state.loading,
    getFDById: (state) => (id) => state.fds.find(fd => fd.id === id),
  },
});