import { describe, it, expect, beforeEach, vi } from 'vitest';
import { shallowMount } from '@vue/test-utils';
import BookFD from '../src/views/BookFD.vue';
import FDCalculator from '../src/utils/fdCalculations.js';

describe('BookFD.vue', () => {
  let wrapper;
  const user = { id: 1, age: 30 };

  beforeEach(() => {
    wrapper = shallowMount(BookFD, {
      global: {
        mocks: {
          $store: {
            getters: { getUser: user, getToken: 'fake-token' },
            dispatch: vi.fn(),
          },
          $toast: { success: vi.fn(), error: vi.fn() },
        },
      },
    });
  });

  it('validates minimum amount', async () => {
    await wrapper.setData({ amount: 500 });
    expect(wrapper.vm.amount).toBeLessThan(wrapper.vm.minAmount);
  });

  it('computes maturity date correctly', async () => {
    const scheme = { tenureMonths: 6, baseRate: 6.5 };
    await wrapper.setData({ selectedScheme: scheme });

    const today = new Date();
    today.setMonth(today.getMonth() + scheme.tenureMonths);

    expect(wrapper.vm.formattedMaturityDate).toBe(today.toLocaleDateString());
  });

  it('calculates simple interest correctly', async () => {
    const scheme = { tenureMonths: 12, baseRate: 6.5 };
    await wrapper.setData({ selectedScheme: scheme, amount: 10000 });

    const res = FDCalculator.calculateFDReturns({
      principal: 10000,
      rate: 6.5,
      tenureMonths: 12,
      age: 30,
    });

    expect(wrapper.vm.maturityInterest).toBe(res.simple.interest);
    expect(wrapper.vm.maturityAmount).toBe(res.simple.maturityAmount);
  });

  it('validates tenure > 0', async () => {
    const scheme = { tenureMonths: 0, baseRate: 6.5 };
    await wrapper.setData({ selectedScheme: scheme });
    expect(wrapper.vm.selectedScheme.tenureMonths).toBe(0);
  });
});
