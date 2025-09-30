import axios from 'axios';
import store from '../store';
import router from '../router';
import { useToast } from 'vue-toastification';

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.common['Content-Type'] = 'application/json';
axios.defaults.withCredentials = true;

axios.interceptors.request.use(
  config => {
    if (config.url.includes('/auth/refresh') || config.url.includes('/auth/logout') || config.url.includes('/auth/register')) {
      return config;
    }
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      config.headers['Authorization'] = `bearer ${accessToken}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

const toast = useToast();

axios.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    if (originalRequest.url.includes('/auth/refresh')) {
      return Promise.reject(error);
    }

    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const res = await axios.post('/auth/refresh');

        if (res.status === 200) {
          const { accessToken, refreshToken } = res.data;

          store.commit('setTokens', { accessToken, refreshToken });

          originalRequest.headers['Authorization'] = `bearer ${accessToken}`;

          return axios(originalRequest);
        }
      } catch (err) {
        toast.error('Your session has expired. Please login again.');
        store.dispatch('logout');
        router.push('/');
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);

export default axios;
