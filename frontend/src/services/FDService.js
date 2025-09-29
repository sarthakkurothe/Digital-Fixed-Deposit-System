import api from '../api';

export const FDService = {
  // Book new FD
  async bookFD(fdData) {
    try {
      const response = await api.post('/fd/book', fdData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get user's FDs
  async getUserFDs(userId) {
    try {
      const response = await api.get(`/fd/user/${userId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get FD details
  async getFDDetails(fdId) {
    try {
      const response = await api.get(`/fd/${fdId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get current interest for an FD
  async getFDInterest(fdId) {
    try {
      const response = await api.get(`/fd/${fdId}/interest`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Update FD status
  async updateFDStatus(fdId, status) {
    try {
      const response = await api.put(`/fd/${fdId}/status`, { status });
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get break FD preview
  async getBreakFDPreview(fdId) {
    try {
      const response = await api.get(`/fd/${fdId}/break-preview`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Break FD prematurely
  async breakFD(fdId) {
    try {
      const response = await api.post(`/fd/${fdId}/break`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get FD schemes/rates
  async getFDSchemes() {
    try {
      const response = await api.get('/fd/schemes');
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },
};

export default FDService;
