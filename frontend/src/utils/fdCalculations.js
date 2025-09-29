export class FDCalculator {
  static calculateSimpleInterest(principal, rate, tenureMonths) {
    if (!principal || !rate || !tenureMonths) {
      return { interest: 0, maturityAmount: principal || 0 };
    }

    const tenureYears = tenureMonths / 12;
    const interest = (principal * rate * tenureYears) / 100;
    const maturityAmount = principal + interest;

    return {
      interest: this.roundToTwoDecimalPlaces(interest),
      maturityAmount: this.roundToTwoDecimalPlaces(maturityAmount),
    };
  }

  static calculateCompoundInterest(principal, rate, tenureMonths, compoundingFrequency = 4) {
    if (!principal || !rate || !tenureMonths) {
      return { interest: 0, maturityAmount: principal || 0 };
    }

    const rateDecimal = rate / 100;
    const tenureYears = tenureMonths / 12;

    // Use formula for Compound Interest
    const maturityAmount =
      principal *
      Math.pow(1 + rateDecimal / compoundingFrequency, compoundingFrequency * tenureYears);

    const interest = maturityAmount - principal;

    return {
      interest: this.roundToTwoDecimalPlaces(interest),
      maturityAmount: this.roundToTwoDecimalPlaces(maturityAmount),
    };
  }

  static roundToTwoDecimalPlaces(amount) {
    return parseFloat(amount.toFixed(2));
  }

  static isCompoundEligible(tenureMonths) {
    return tenureMonths >= 24;
  }

  static getApplicableRate(baseRate, age) {
    const seniorCitizenBonus = age >= 60 ? 0.5 : 0;
    return baseRate + seniorCitizenBonus;
  }

  static calculateFDReturns({ principal, rate, tenureMonths, age = 30, forceCompound = false }) {
    const finalRate = this.getApplicableRate(rate, age);

    const simpleResult = this.calculateSimpleInterest(principal, finalRate, tenureMonths);

    const isCompoundAvailable = this.isCompoundEligible(tenureMonths) || forceCompound;
    const compoundResult = isCompoundAvailable
      ? this.calculateCompoundInterest(principal, finalRate, tenureMonths, 4) // Assuming quarterly compounding
      : { interest: 0, maturityAmount: 0 };

    return {
      principal,
      finalRate,
      tenureMonths,
      tenureYears: tenureMonths / 12,
      isSeniorCitizen: age >= 60,
      seniorBonus: age >= 60 ? 0.5 : 0,
      simple: simpleResult,
      compound: compoundResult,
      isCompoundEligible: isCompoundAvailable,
      additionalEarnings: isCompoundAvailable ? compoundResult.interest - simpleResult.interest : 0,
    };
  }

  static formatCurrency(amount) {
    if (!amount) return '0';
    return new Intl.NumberFormat('en-IN').format(this.roundToTwoDecimalPlaces(amount));
  }

  static getMaturityDate(startDate, tenureMonths) {
    const start = new Date(startDate);
    const maturity = new Date(start);
    maturity.setMonth(maturity.getMonth() + tenureMonths);

    return {
      dateString: maturity.toISOString().substr(0, 10),
      formattedDate: maturity.toLocaleDateString('en-GB', {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
      }),
    };
  }
}

// Standard FD Schemes (as per Indian banking norms)
export const STANDARD_FD_SCHEMES = [
  {
    id: 1,
    name: '6 Months FD',
    tenureMonths: 6,
    baseRate: 6.0,
    description: '6 months',
    minAmount: 1000,
    hasCompound: false,
  },
  {
    id: 2,
    name: '1 Year FD',
    tenureMonths: 12,
    baseRate: 6.5,
    description: '1 year',
    minAmount: 1000,
    hasCompound: false,
  },
  {
    id: 3,
    name: '2 Years FD',
    tenureMonths: 24,
    baseRate: 7.0,
    description: '2 years',
    minAmount: 1000,
    hasCompound: true,
  },
  {
    id: 4,
    name: '3 Years FD',
    tenureMonths: 36,
    baseRate: 7.5,
    description: '3 years',
    minAmount: 1000,
    hasCompound: true,
  },
  {
    id: 5,
    name: '5 Years FD',
    tenureMonths: 60,
    baseRate: 8.0,
    description: '5 years',
    minAmount: 1000,
    hasCompound: true,
  },
];

export default FDCalculator;
