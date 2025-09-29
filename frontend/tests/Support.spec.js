import { mount, flushPromises } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { createStore } from 'vuex';
import Support from '../src/views/Support.vue';

vi.mock('../src/api', () => ({
  default: {
    get: vi.fn(() => Promise.resolve({ data: [] })),
    post: vi.fn(() => Promise.resolve({ data: { id: 1 } })),
  },
}));

let store;
beforeEach(() => {
  store = createStore({
    state: () => ({}),
    getters: {
      getUser: () => ({ id: 123 }),
      getFDs: () => [
        {
          id: 1,
          amount: 10000,
          interestRate: 7.5,
          status: 'ACTIVE',
          createdDate: '2025-09-01',
        },
      ],
    },
    actions: {
      fetchFDs: vi.fn(() => Promise.resolve()),
    },
  });
});

describe('Support.vue', () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(Support, {
      global: {
        plugins: [store],
      },
    });
  });

  it('renders Support Center header', () => {
    expect(wrapper.text()).toContain('Support Center');
    expect(wrapper.text()).toContain('Get help with your Fixed Deposit queries');
  });

  it('opens modal when Raise a Ticket button is clicked', async () => {
    expect(wrapper.find('form').exists()).toBe(false);
    await wrapper.find('button').trigger('click');
    expect(wrapper.find('form').exists()).toBe(true);
  });

  it('shows validation errors when submitting empty form', async () => {
    await wrapper.find('button').trigger('click');
    await wrapper.find('form').trigger('submit.prevent');
    await flushPromises();

    expect(wrapper.text()).toContain('Please select a Fixed Deposit.');
    expect(wrapper.text()).toContain('Subject is required.');
    expect(wrapper.text()).toContain('Description is required.');
  });

  it('submits a new ticket successfully', async () => {
    const axios = (await import('../src/api')).default;

    await wrapper.find('button').trigger('click');

    const subject = wrapper.find("input[type='text']");
    const description = wrapper.find('textarea');

    await subject.setValue('Interest rate issue');
    await description.setValue('Details about issue...');

    wrapper.vm.newTicket.fd = { id: 1 };

    await wrapper.find('form').trigger('submit.prevent');
    await flushPromises();

    expect(axios.post).toHaveBeenCalledWith('/support', {
      fdId: 1,
      subject: 'Interest rate issue',
      description: 'Details about issue...',
      userId: 123,
    });
  });
});
