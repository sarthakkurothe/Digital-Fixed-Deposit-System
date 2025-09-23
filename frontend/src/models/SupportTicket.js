export const SupportTicketStatus = {
  OPEN: 'OPEN',
  RESOLVED: 'RESOLVED', 
  CLOSED: 'CLOSED'
}

// SupportTicket model template
export const createSupportTicket = () => ({
  id: null,
  userId: null,
  fdId: null,
  subject: '',
  description: '',
  status: SupportTicketStatus.OPEN,
  response: null,
  createdAt: new Date()
})

// Support ticket request template
export const createSupportTicketRequest = () => ({
  fdId: null,
  subject: '',
  description: ''
})