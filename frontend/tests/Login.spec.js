import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { createStore } from 'vuex'
import { createRouter, createWebHistory } from 'vue-router'
import Login from '../src/views/Login.vue'

// Mock store
const createVuexStore = () => {
  return createStore({
    actions: {
      login: vi.fn(),
      setUserData: vi.fn()
    },
    getters: {
      getAccessToken: () => 'mock-token',
      getUser: () => ({ role: 'ROLE_USER' })
    }
  })
}

// Mock router
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { 
      path: '/',
      component: { template: '<div>Home</div>' }
    },
    { 
      path: '/user/dashboard',
      name: 'Dashboard',
      component: { template: '<div>Dashboard</div>' }
    },
    { 
      path: '/admin',
      name: 'Admin',
      component: { template: '<div>Admin</div>' }
    },
    { 
      path: '/register',
      name: 'Register',
      component: { template: '<div>Register</div>' }
    }
  ]
})

describe('Login.vue', () => {
  let wrapper
  let store

  beforeEach(() => {
    store = createVuexStore()
    wrapper = mount(Login, {
      global: {
        plugins: [store, router],
        stubs: ['DotLottieVue'] // Stub the lottie animation component
      }
    })
  })

  it('renders the login form correctly', () => {
    expect(wrapper.find('h2').text()).toBe('Welcome Back')
    expect(wrapper.find('input[type="email"]').exists()).toBe(true)
    expect(wrapper.find('input[id="password"]').exists()).toBe(true)
    expect(wrapper.find('button[type="submit"]').exists()).toBe(true)
  })

  it('validates email format', async () => {
    const emailInput = wrapper.find('input[type="email"]')
    
    // Test invalid email
    await emailInput.setValue('invalid-email')
    expect(wrapper.vm.emailError).toBe('Please enter a valid email address')
    
    // Test valid email
    await emailInput.setValue('test@example.com')
    expect(wrapper.vm.emailError).toBe(null)
  })


  it('shows loading state during form submission', async () => {
    const form = wrapper.find('form')
    await wrapper.setData({
      email: 'test@example.com',
      password: 'password123'
    })

    // Trigger form submission
    form.trigger('submit')
    await wrapper.vm.$nextTick()
    
    expect(wrapper.vm.loading).toBe(true)
  })

  it('handles successful login for regular user', async () => {
    const routerPushSpy = vi.spyOn(router, 'push')
    
    await wrapper.setData({
      email: 'test@example.com',
      password: 'password123'
    })

    await wrapper.vm.handleLogin()
    
    // Verify router push was called with correct path
    expect(routerPushSpy).toHaveBeenCalledWith('/user/dashboard')
  })

  it('displays error message on login failure', async () => {
    // Mock login action to throw an error
    store.dispatch = vi.fn().mockRejectedValue({ status: 404 })
    
    await wrapper.setData({
      email: 'test@example.com',
      password: 'wrongpassword'
    })

    await wrapper.vm.handleLogin()
    
    expect(wrapper.vm.error).toBe('User not found. Please check your email or password')
    expect(wrapper.vm.loading).toBe(false)
  })
})
