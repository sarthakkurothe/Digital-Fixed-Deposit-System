import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import Register from '../src/views/Register.vue';
import { createStore } from 'vuex';
import { createRouter, createWebHistory } from 'vue-router';
import Header from '../src/components/Header.vue';

// Mock store
const createVuexStore = () => {
  return createStore({
    actions: {
      register: vi.fn(),
    },
  });
};

// Mock router
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: { template: '<div>Login</div>' },
    },
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
        components: {
          Header,
        },
      },
    });
  });

  it('renders the registration form', () => {
    expect(wrapper.find('form').exists()).toBe(true);
    expect(wrapper.find('input[placeholder="Full Name"]').exists()).toBe(true);
    expect(wrapper.find('input[placeholder="Email Address"]').exists()).toBe(true);
    expect(wrapper.find('input[placeholder="Age"]').exists()).toBe(true);
    expect(wrapper.find('input[placeholder="Password"]').exists()).toBe(true);
    expect(wrapper.find('input[placeholder="Confirm Password"]').exists()).toBe(true);
  });

  it('updates data properties when form inputs change', async () => {
    const nameInput = wrapper.find('input[placeholder="Full Name"]');
    const emailInput = wrapper.find('input[placeholder="Email Address"]');
    const ageInput = wrapper.find('input[placeholder="Age"]');
    const passwordInput = wrapper.find('input[placeholder="Password"]');
    const confirmPasswordInput = wrapper.find('input[placeholder="Confirm Password"]');

    await nameInput.setValue('John Doe');
    await emailInput.setValue('john@example.com');
    await ageInput.setValue(25);
    await passwordInput.setValue('password123');
    await confirmPasswordInput.setValue('password123');

    expect(wrapper.vm.name).toBe('John Doe');
    expect(wrapper.vm.email).toBe('john@example.com');
    expect(wrapper.vm.age).toBe(25);
    expect(wrapper.vm.password).toBe('password123');
    expect(wrapper.vm.confirmPassword).toBe('password123');
  });

  it('shows error message when required fields are empty', async () => {
    await wrapper.find('form').trigger('submit');
    expect(wrapper.vm.errorMessage).toBe('All fields are required.');
  });

  it('shows error message when passwords do not match', async () => {
    await wrapper.find('input[placeholder="Full Name"]').setValue('John Doe');
    await wrapper.find('input[placeholder="Email Address"]').setValue('john@example.com');
    await wrapper.find('input[placeholder="Age"]').setValue('25');
    await wrapper.find('input[placeholder="Password"]').setValue('password123');
    await wrapper.find('input[placeholder="Confirm Password"]').setValue('differentpassword');

    await wrapper.find('form').trigger('submit');
    expect(wrapper.vm.errorMessage).toBe('Passwords do not match!');
  });

  it('calls register action when form is submitted with valid data', async () => {
    const mockRegisterAction = vi.fn().mockResolvedValue({ status: 201 });
    store.dispatch = mockRegisterAction;

    await wrapper.find('input[placeholder="Full Name"]').setValue('John Doe');
    await wrapper.find('input[placeholder="Email Address"]').setValue('john@example.com');
    await wrapper.find('input[placeholder="Age"]').setValue(25);
    await wrapper.find('input[placeholder="Password"]').setValue('password123');
    await wrapper.find('input[placeholder="Confirm Password"]').setValue('password123');

    await wrapper.find('form').trigger('submit');

    expect(mockRegisterAction).toHaveBeenCalledWith('register', {
      name: 'John Doe',
      email: 'john@example.com',
      age: 25,
      password: 'password123',
    });
  });

  it('shows success message on successful registration', async () => {
    const mockRegisterAction = vi.fn().mockResolvedValue({ status: 201 });
    store.dispatch = mockRegisterAction;

    await wrapper.find('input[placeholder="Full Name"]').setValue('John Doe');
    await wrapper.find('input[placeholder="Email Address"]').setValue('john@example.com');
    await wrapper.find('input[placeholder="Age"]').setValue('25');
    await wrapper.find('input[placeholder="Password"]').setValue('password123');
    await wrapper.find('input[placeholder="Confirm Password"]').setValue('password123');

    await wrapper.find('form').trigger('submit');

    expect(wrapper.vm.successMessage).toBe('Registration successful! 🎉 You can now log in.');
  });

  it('shows error message when registration fails', async () => {
    const mockError = new Error('Registration failed');
    mockError.response = { data: 'Registration failed. Please try again.' };
    const mockRegisterAction = vi.fn().mockRejectedValue(mockError);
    store.dispatch = mockRegisterAction;

    await wrapper.find('input[placeholder="Full Name"]').setValue('John Doe');
    await wrapper.find('input[placeholder="Email Address"]').setValue('john@example.com');
    await wrapper.find('input[placeholder="Age"]').setValue('25');
    await wrapper.find('input[placeholder="Password"]').setValue('password123');
    await wrapper.find('input[placeholder="Confirm Password"]').setValue('password123');

    await wrapper.find('form').trigger('submit');

    expect(wrapper.vm.errorMessage).toBe('Registration failed. Please try again.');
  });
});
