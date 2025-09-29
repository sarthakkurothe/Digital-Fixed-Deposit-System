import axios from 'axios';

import store from '../store';
import router from '../router';

import { useToast } from 'vue-toastification';

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.common['Content-Type'] = 'application/json';

axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

const toast = useToast();

axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      toast.error('Your session has expired. Please login again.');
      store.dispatch('logout');
      router.push('/');
    }
    return Promise.reject(error);
  }
);

export default axios;
