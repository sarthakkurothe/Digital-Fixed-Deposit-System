export const FdStatus = {
  ACTIVE: 'ACTIVE',
  MATURED: 'MATURED', 
  BROKEN: 'BROKEN'
}

// Fixed Deposit model template
export const createFixedDeposit = () => ({
  id: null,
  userId: null,
  amount: 0,
  interestRate: 0,
  tenureMonths: 0,
  startDate: new Date(),
  maturityDate: new Date(),
  status: FdStatus.ACTIVE,
  accruedInterest: 0,
  createdAt: new Date()
})

// Book FD request template
export const createBookFdRequest = () => ({
  amount: 0,
  interestRate: 0,
  tenureMonths: 0
})

// FD Interest response template
export const createFdInterestResponse = () => ({
  fdId: null,
  originalAmount: 0,
  accruedInterest: 0,
  totalAmount: 0,
  interestBreakdown: {
    monthlyInterest: 0,
    daysElapsed: 0
  }
})

// Break FD preview template
export const createBreakFdPreview = () => ({
  fdId: null,
  originalAmount: 0,
  accruedInterest: 0,
  penalty: 0,
  interestLoss: 0,
  finalPayout: 0,
  penaltyReason: ''
})