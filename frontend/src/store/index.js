import axios from '../api';
import { createStore } from 'vuex';

export default createStore({
  state: {
    user: JSON.parse(localStorage.getItem('user')) || null,
    accessToken: localStorage.getItem('accessToken') || null,
    refreshToken: localStorage.getItem('refreshToken') || null,
    fds: [],
    summary: {},
    dashboardInfo: {},
    loading: false,
  },

  mutations: {
    SET_SUMMARY(state, summary) {
      state.summary = summary;
    },
    // --- AUTH ---
    setUser(state, user) {
      state.user = user;
      localStorage.setItem('user', JSON.stringify(user));
    },
    setTokens(state, { accessToken, refreshToken }) {
      state.accessToken = accessToken;
      state.refreshToken = refreshToken;
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
    },
    clearAuth(state) {
      state.user = null;
      state.accessToken = null;
      state.refreshToken = null;
      state.fds = [];
      state.summary = {};
      localStorage.removeItem('user');
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
    },

    // --- FDs ---
    SET_FDS(state, fds) {
      state.fds = fds;
    },
    SET_LOADING(state, loading) {
      state.loading = loading;
    },
    SET_SUMMARY(state, summary) {
      state.summary = summary;
    },
    UPDATE_FD_STATUS(state, { fdId, status }) {
      const fd = state.fds.find(fd => fd.id === fdId);
      if (fd) fd.status = status;
    },
    SET_DASHBOARD_INFO(state, info) {
      state.dashboardInfo = info;
    },
  },

  actions: {
    /**
     * Fetch all FDs for the logged-in user
     */
    async fetchFDs({ commit, state }) {
      commit('SET_LOADING', true);
      try {
        const res = await axios.get(`/fd/user/${state.user.id}`);
        commit('SET_FDS', res.data);
      } catch (err) {
        console.error('Error fetching FDs:', err);
      } finally {
        commit('SET_LOADING', false);
      }
    },

    async fetchSummary({ commit, state }) {
      commit('SET_LOADING', true);
      try {
        const res = await axios.get(`/user/investments`);
        commit('SET_SUMMARY', res.data);
      } catch (err) {
        throw err;
      }
    },

    /**
     * Break an FD (set status to PENDING)
     */
    async fetchBreakPreview({ state }, fdId) {
      try {
        const res = await axios.get(`/fd/${fdId}/break-preview`, {
          headers: { Authorization: `bearer ${state.accessToken}` },
        });
        return res.data;
      } catch (err) {
        return null;
      }
    },

    async breakFD({ commit, state }, fdId) {
      try {
        const res = await axios.post(
          `/fd/${fdId}/break`,
          {},
          {
            headers: { Authorization: `bearer ${state.accessToken}` },
          }
        );

        if (res.status === 201) {
          commit('UPDATE_FD_STATUS', { fdId, status: 'PENDING' });
          return {
            success: true,
            message: 'Request for Break FD has been created successfully.',
          };
        }

        return {
          success: false,
          message: 'Unexpected response from server.',
        };
      } catch (err) {
        return {
          success: false,
          message: 'Failed to submit FD Break Request.',
        };
      }
    },

    async fetchDashboardInfo({ commit, state }) {
      commit('SET_LOADING', true);
      try {
        const res = await axios.get('/admin/dashboard/info', {
          headers: { Authorization: `bearer ${state.accessToken}` },
        });
        commit('SET_DASHBOARD_INFO', res.data);
      } catch (err) {
        throw err;
      } finally {
        commit('SET_LOADING', false);
      }
    },

    /**
     * Login user -> save token
     */
    async login({ commit }, credentials) {
      try {
        const res = await axios.post('/auth/login', {
          email: credentials.email,
          password: credentials.password,
        });

        const { accessToken, refreshToken } = res.data;
        commit('setTokens', { accessToken, refreshToken });

        return res;
      } catch (err) {
        console.log(err);
        throw err;
      }
    },

    /**
     * Fetch logged-in user details using stored token
     */
    async setUserData({ commit, state }) {
      if (!state.accessToken) return;

      try {
        const res = await axios.get('/auth/me', {
          headers: { Authorization: `bearer ${state.accessToken}` },
        });
        commit('setUser', res.data);
      } catch (err) {
        throw err;
      }
    },

    /**
     * Register a new user
     */
    async register({ commit }, credentials) {
      try {
        const res = await axios.post('/auth/register', {
          name: credentials.name,
          email: credentials.email,
          dateOfBirth: credentials.dateOfBirth,
          password: credentials.password,
        });

        return res;
      } catch (err) {
        return {
          status: err.response?.status,
        };
      }
    },

    /**
     * Logout user -> clear store & localStorage
     */
    logout({ commit }) {
      commit('clearAuth');
    },
  },

  getters: {
    isAuthenticated: state => !!state.accessToken,
    getFDsCount: state => state.fds.length,
    getUser: state => state.user,
    getUserName: state => state.user?.name || '',
    getUserEmail: state => state.user?.email || '',
    getAccessToken: state => state.accessToken,
    getRefreshToken: state => state.refreshToken,
    getFDs: state => state.fds,
    isLoading: state => state.loading,
    getFDById: state => id => state.fds.find(fd => fd.id === id),
    getSummary: state => state.summary,
    getDashboardInfo: state => state.dashboardInfo,
  },
});
