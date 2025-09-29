import { describe, it, expect, vi, beforeEach } from 'vitest';
import { mount } from '@vue/test-utils';
import BreakFD from '../src/views/BreakFD.vue';

// Mock store and toast
const mockDispatch = vi.fn();
const mockStore = { dispatch: mockDispatch };
vi.mock('vuex', () => ({
  useStore: () => mockStore,
}));

const mockToast = {
  success: vi.fn(),
  error: vi.fn(),
};
vi.mock('vue-toastification', () => ({
  useToast: () => mockToast,
}));

describe('BreakFD.vue', () => {
  const fdId = 1;
  const previewMock = {
    fdId: 1,
    principalAmount: 10000,
    interestRate: 5.5,
    tenure: 12,
    startDate: '2025-01-01',
    maturityDate: '2026-01-01',
    timeElapsed: 6,
    accruedInterest: 300,
    penalty: 100,
    payout: 10200,
  };

  beforeEach(() => {
    mockDispatch.mockReset();
    mockToast.success.mockReset();
    mockToast.error.mockReset();
    vi.spyOn(console, 'error').mockImplementation(() => {});
  });

  it('renders loading state initially', async () => {
    mockDispatch.mockImplementation(() => {
      return new Promise(resolve => setTimeout(() => resolve(null), 100)); // delay response
    });

    const wrapper = mount(BreakFD, { props: { fdId: 1 } });

    // Wait for the next tick so the DOM updates after mount
    await wrapper.vm.$nextTick();

    // Assert loading state before the promise resolves
    expect(wrapper.text()).toContain('Loading preview...');
  });

  it('renders preview after fetch', async () => {
    mockDispatch.mockResolvedValueOnce(previewMock);
    const wrapper = mount(BreakFD, {
      props: { fdId },
    });
    await new Promise(resolve => setTimeout(resolve)); // wait for fetchPreview
    expect(wrapper.text()).toContain('Break Fixed Deposit');
    expect(wrapper.text()).toContain('₹10,000');
    expect(wrapper.text()).toContain('Total Payout');
  });

  it('shows error message on fetch failure', async () => {
    mockDispatch.mockRejectedValueOnce(new Error('Server error'));
    const wrapper = mount(BreakFD, {
      props: { fdId },
    });
    await new Promise(resolve => setTimeout(resolve));
    expect(wrapper.text()).toContain('Failed to load preview');
    expect(mockToast.error).toHaveBeenCalledWith('Server error');
  });

  it('opens confirmation dialog on Confirm Break click', async () => {
    mockDispatch.mockResolvedValueOnce(previewMock);
    const wrapper = mount(BreakFD, { props: { fdId: 1 } });
    await new Promise(resolve => setTimeout(resolve)); // wait for preview to load
    const confirmBtn = wrapper.findAll('button').find(btn => btn.text() === 'Confirm Break');
    expect(confirmBtn).toBeDefined();
    await confirmBtn.trigger('click');
    await wrapper.vm.$nextTick(); // wait for dialog to render
    expect(wrapper.text()).toContain('Confirm Break Fixed Deposit');
  });

  it('emits fdBroken when confirm button is clicked', async () => {
    mockDispatch.mockResolvedValueOnce(previewMock);
    const wrapper = mount(BreakFD, { props: { fdId: 1 } });
    await new Promise(resolve => setTimeout(resolve)); // wait for preview to load
    const confirmBtn = wrapper.findAll('button').find(btn => btn.text() === 'Confirm Break');
    expect(confirmBtn).toBeDefined();
    await confirmBtn.trigger('click');
    await wrapper.vm.$nextTick(); // wait for dialog to appear
    const yesBtn = wrapper.findAll('button').find(btn => btn.text() === 'Yes, Break FD');
    expect(yesBtn).toBeDefined();
    await yesBtn.trigger('click');
    expect(wrapper.emitted()).toHaveProperty('fdBroken');
  });

  it('emits close on cancel', async () => {
    mockDispatch.mockResolvedValueOnce(previewMock);
    const wrapper = mount(BreakFD, { props: { fdId: 1 } });
    await new Promise(resolve => setTimeout(resolve));
    const cancelBtn = wrapper.findAll('button').find(btn => btn.text() === 'Cancel');
    expect(cancelBtn).toBeDefined();
    await cancelBtn.trigger('click');
    expect(wrapper.emitted()).toHaveProperty('close');
  });
});

