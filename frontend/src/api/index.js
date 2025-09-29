import axios from 'axios';

import store from '../store';
import router from '../router';

import { useToast } from 'vue-toastification';

axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.common['Content-Type'] = 'application/json';

axios.interceptors.request.use(
  config => {
    if (config.url.includes("/auth/refresh")) {
      return config;
    }
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      config.headers['Authorization'] = `bearer ${accessToken}`;
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
  async error => {
    const originalRequest = error.config;

    if (originalRequest.url.includes("/auth/refresh")) {
      return Promise.reject(error);
    }

    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true; // avoid retry loop

      
      try {
        const res = await axios.post("/auth/refresh", {
          refreshToken: store.state.refreshToken,
        });

        if (res.status === 200) {
          const { accessToken } = res.data;

          store.commit("setTokens", { accessToken, refreshToken: store.state.refreshToken });
          localStorage.setItem("accessToken", accessToken);

          originalRequest.headers["Authorization"] = `bearer ${accessToken}`;

          return axios(originalRequest);
        }
      } catch (err) {
        // refresh failed → logout user
        toast.error('Your session has expired. Please login again.');
        store.dispatch("logout");
        router.push('/');
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);


export default axios;
