import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import Login from '../src/views/Login.vue'

const push = vi.fn()

function factory(overrides = {}) {
  const mockStore = {
    getters: {
      getToken: () => overrides.token || 'mock-token',
      getUser: () => overrides.getUser || { role: 'ROLE_USER' }
    },
    dispatch: vi.fn().mockImplementation((action, payload) => {
      if (action === 'login') {
        return overrides.login ? overrides.login(payload) : Promise.resolve(true)
      }
      if (action === 'setUserData') {
        return overrides.setUserData ? overrides.setUserData() : Promise.resolve(true)
      }
      return Promise.resolve()
    })
  }

  return mount(Login, {
    global: {
      mocks: {
        $router: { push },
        $store: mockStore
      },
      stubs: {
        Header: true,
        'router-link': true,
        'DotLottieVue': true,
        'Eye': true,
        'EyeOff': true,
        'LoadingSpinner': true
      }
    }
  })
}

describe('Login.vue', () => {
  beforeEach(() => {
    push.mockClear()
  })

  it('renders form', () => {
    const wrapper = factory()
    expect(wrapper.find('h2').text()).toBe('Welcome Back')
    expect(wrapper.find('#email').exists()).toBe(true)
    expect(wrapper.find('#password').exists()).toBe(true)
  })

  it('binds email and password', async () => {
    const wrapper = factory()
    await wrapper.find('#email').setValue('test@example.com')
    await wrapper.find('#password').setValue('secret')
    expect(wrapper.vm.email).toBe('test@example.com')
    expect(wrapper.vm.password).toBe('secret')
  })

  it('toggles password visibility', async () => {
    const wrapper = factory()
    const btn = wrapper.find('button[type="button"]')
    const input = wrapper.find('#password')
    expect(input.attributes('type')).toBe('password')
    await btn.trigger('click')
    expect(input.attributes('type')).toBe('text')
  })

  it('redirects user to dashboard', async () => {
    const wrapper = factory()
    await wrapper.setData({ email: 'u@e.com', password: '123' })
    await wrapper.find('form').trigger('submit.prevent')
    await wrapper.vm.$nextTick()
    expect(push).toHaveBeenCalledWith('/user/dashboard')
  })

  it('shows error on 404', async () => {
    const wrapper = factory({
      login: () => Promise.reject({ response: { status: 404 } })
    })
    await wrapper.setData({ email: 'x@e.com', password: 'bad' })
    await wrapper.find('form').trigger('submit.prevent')
    await wrapper.vm.$nextTick()
    expect(wrapper.text()).toContain('User not found')
  })

  it('shows generic error', async () => {
    const wrapper = factory({
      login: vi.fn().mockRejectedValue({ response: { status: 500 } }),
    })
    wrapper.setData({ email: 'y@e.com', password: 'oops' })
    await wrapper.find('form').trigger('submit.prevent')
    expect(wrapper.text()).toContain('Login failed')
  })
})
