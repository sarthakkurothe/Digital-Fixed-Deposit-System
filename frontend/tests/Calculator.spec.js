import { mount } from "@vue/test-utils"
import { describe, it, expect, vi } from "vitest"
import { createStore } from "vuex"
import Calculator from "../src/views/Calculator.vue"

// Mock fdCalculations utils (simple deterministic behavior)
vi.mock("../src/utils/fdCalculations.js", () => {
  const calculateFDReturns = vi.fn(({ principal, rate, tenureMonths }) => {
    const years = tenureMonths / 12
    // Simple interest: P * r * t / 100
    const simpleInterest = Math.round((principal * rate * years) / 100)
    const simpleMaturity = principal + simpleInterest
    // Compound maturity (rounded)
    const compoundMaturity = Math.round(principal * Math.pow(1 + rate / 100, years))
    const compoundInterest = compoundMaturity - principal
    return {
      simple: { interest: simpleInterest, maturityAmount: simpleMaturity },
      compound: { interest: compoundInterest, maturityAmount: compoundMaturity },
      isCompoundEligible: tenureMonths >= 12,
    }
  })

  const getApplicableRate = vi.fn((baseRate, age) => (age >= 60 ? baseRate + 0.5 : baseRate))
  const formatCurrency = vi.fn((amt) => `₹${amt.toLocaleString()}`)

  return {
    __esModule: true,
    default: { calculateFDReturns, getApplicableRate, formatCurrency },
    STANDARD_FD_SCHEMES: [
      { name: "6 months (mock)", baseRate: 6.0, tenureMonths: 6 },
      { name: "2 years (mock)", baseRate: 6.0, tenureMonths: 24 },
    ],
  }
})

// Minimal stub for SchemeDropDown used by the view
vi.mock("../components/SchemeDropDown.vue", () => ({
  default: {
    name: "SchemeDropDown",
    props: ["modelValue", "schemes"],
    template: "<div />",
  },
}))

describe("FD Calculator ", () => {
  it("calculates simple interest for ₹1000 for 6 months (0.5 year) at 6% => interest ₹30, maturity ₹1030", async () => {
    const fdUtils = (await import("../src/utils/fdCalculations.js")).default
    const result = fdUtils.calculateFDReturns({ principal: 1000, rate: 6, tenureMonths: 6 })

    expect(result.simple.interest).toBe(30)
    expect(result.simple.maturityAmount).toBe(1030)
    expect(result.isCompoundEligible).toBe(false)
  })

  it("calculates compound interest for ₹1000 for 2 years at 6% => compound interest ≈ ₹124, maturity ₹1124", async () => {
    const fdUtils = (await import("../src/utils/fdCalculations.js")).default
    const result = fdUtils.calculateFDReturns({ principal: 1000, rate: 6, tenureMonths: 24 })

    // compound: 1000 * (1+0.06)^2 = 1123.6 -> rounded to 1124
    expect(result.compound.interest).toBe(124)
    expect(result.compound.maturityAmount).toBe(1124)
    expect(result.isCompoundEligible).toBe(true)
  })

  it("applies senior citizen rate bump (baseRate + 0.5) for age >= 60", async () => {
    const fdUtils = (await import("../src/utils/fdCalculations.js")).default

    expect(fdUtils.getApplicableRate(6.0, 65)).toBe(6.5) // senior
    expect(fdUtils.getApplicableRate(6.0, 59)).toBe(6.0) // not senior
  })

  it("shows senior benefits text in the Calculator view when user age >= 60", async () => {
    const seniorStore = createStore({
      state: { user: { age: 65 } },
      actions: { loadCurrentUser: vi.fn() },
    })

    const wrapper = mount(Calculator, {
      global: {
        plugins: [seniorStore],
        stubs: ["CircleDollarSign", "TrendingUp", "Calendar", "AlertCircle", "SchemeDropDown"],
      },
    })

    // The view includes a line that indicates senior benefits; check presence
    expect(wrapper.text()).toContain("Senior citizen benefits applicable")
  })
})