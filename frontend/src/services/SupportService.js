import api from '../api';

export const SupportService = {
  // Create new support ticket
  async createTicket(ticketData) {
    try {
      const response = await api.post('/support', ticketData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get user's support tickets
  async getUserTickets(userId) {
    try {
      const response = await api.get(`/support/user/${userId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Get ticket details
  async getTicketDetails(ticketId) {
    try {
      const response = await api.get(`/support/${ticketId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Update ticket status
  async updateTicketStatus(ticketId, status) {
    try {
      const response = await api.put(`/support/${ticketId}/status`, { status });
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Add comment to ticket
  async addTicketComment(ticketId, comment) {
    try {
      const response = await api.post(`/support/${ticketId}/comment`, { comment });
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },
};

export default SupportService;
