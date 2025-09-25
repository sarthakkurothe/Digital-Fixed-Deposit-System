import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.headers.common['Content-Type'] = 'application/json'

axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers['Authorization'] = `bearer ${token}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

export default axios

