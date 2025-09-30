import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import Register from '../src/views/Register.vue';
import { createStore } from 'vuex';
import { createRouter, createWebHistory } from 'vue-router';

// Mock store
const createVuexStore = () => {
  const actions = {
    register: vi.fn(),
  };
  return createStore({
    actions,
    state: {},
    mutations: {},
  });
};

// Mock router
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'Home', component: { template: '<div>Home</div>' } },
    { path: '/login', name: 'Login', component: { template: '<div>Login</div>' } },
  ],
});

describe('Register.vue', () => {
  let wrapper;
  let store;

  beforeEach(() => {
    store = createVuexStore();
    wrapper = mount(Register, {
      global: {
        plugins: [store, router],
        stubs: {
          DotLottieVue: true,
          Header: true,
        },
      },
    });
  });

  it('validates email format', async () => {
    await wrapper.find('input[type="email"]').setValue('invalid-email');

    expect(wrapper.vm.errors.email).toBe('Invalid email address');
  });

  it('validates password requirements', async () => {
    await wrapper.find('input[placeholder="Password"]').setValue('weakpass');

    expect(wrapper.vm.errors.password).toBe(
      'Password must be at least 8 characters long and include 1 uppercase, 1 number, and 1 special character'
    );
  });

  it('validates password matching', async () => {
    await wrapper.find('input[placeholder="Password"]').setValue('StrongPass1!');
    await wrapper.find('input[placeholder="Confirm Password"]').setValue('DifferentPass1!');

    expect(wrapper.vm.errors.confirmPassword).toBe('Passwords do not match');
  });

  it('successfully submits the form with valid data', async () => {
    // Setup mock before mounting
    const mockRegisterAction = vi.fn().mockResolvedValue({ status: 201 });
    store = createVuexStore();
    store.dispatch = mockRegisterAction;

    wrapper = mount(Register, {
      global: {
        plugins: [store, router],
        stubs: {
          DotLottieVue: true,
          Header: true,
        },
      },
    });

    // Fill in valid form data
    await wrapper.find('input[placeholder="Full Name"]').setValue('John Doe');
    await wrapper.find('input[type="email"]').setValue('john@example.com');
    await wrapper.find('input[type="date"]').setValue('1990-01-01');
    await wrapper.find('input[placeholder="Password"]').setValue('StrongPass1!');
    await wrapper.find('input[placeholder="Confirm Password"]').setValue('StrongPass1!');

    // Submit the form
    await wrapper.find('form').trigger('submit');

    // Wait for Vue to update
    await wrapper.vm.$nextTick();

    // Verify that the register action was called with correct data
    expect(mockRegisterAction).toHaveBeenCalledWith('register', {
      name: 'John Doe',
      email: 'john@example.com',
      dateOfBirth: '1990-01-01',
      password: 'StrongPass1!',
    });

    // Verify success message
    expect(wrapper.vm.successMessage).toContain('Registration successful!');
  });

  it('handles registration of existing user', async () => {
    // Setup mock before mounting
    const mockRegisterAction = vi.fn().mockResolvedValue({
      status: 400,
      data: 'User already exists!',
    });
    store = createVuexStore();
    store.dispatch = mockRegisterAction;

    wrapper = mount(Register, {
      global: {
        plugins: [store, router],
        stubs: {
          DotLottieVue: true,
          Header: true,
        },
      },
    });

    // Fill in form data
    await wrapper.find('input[placeholder="Full Name"]').setValue('John Doe');
    await wrapper.find('input[type="email"]').setValue('existing@example.com');
    await wrapper.find('input[type="date"]').setValue('1990-01-01');
    await wrapper.find('input[placeholder="Password"]').setValue('StrongPass1!');
    await wrapper.find('input[placeholder="Confirm Password"]').setValue('StrongPass1!');

    // Submit the form
    await wrapper.find('form').trigger('submit');

    // Wait for Vue to update
    await wrapper.vm.$nextTick();

    // Verify error message
    expect(wrapper.vm.errors.email).toBe('User already exists!');
  });
});
