import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import './style.css';

import Toast, { POSITION } from 'vue-toastification';
import 'vue-toastification/dist/index.css';

const app = createApp(App);

app.use(store);
app.use(router);

app.use(Toast, {
  position: POSITION.TOP_RIGHT,
  timeout: 3000,
  closeOnClick: true,
  pauseOnHover: true,
  draggable: true,
  hideProgressBar: false,
  offset: '80px',
});

app.mount('#app');
