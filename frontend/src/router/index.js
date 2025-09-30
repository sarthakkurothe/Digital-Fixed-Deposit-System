import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import DashBoard from '../views/DashBoard.vue';
import MyFDs from '../views/MyFDs.vue';
import BookFD from '../views/BookFD.vue';
import Calculator from '../views/Calculator.vue';
import Support from '../views/Support.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import Interface from '../components/Interface.vue';
import store from '../store';
import Admin from '../views/Admin.vue';
import AdminFixedDeposits from '../views/AdminFixedDeposits.vue';
import AdminSupportTickets from '../views/AdminSupportTickets.vue';
import AdminInterface from '../components/AdminInterface.vue';

const routes = [
  { path: '/', name: 'Home', component: HomeView },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },

  {
    path: '/admin',
    name: 'AdminInterface',
    component: AdminInterface,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        name: 'Admin',
        component: Admin,
      },
      {
        path: 'fds',
        name: 'AdminFDs',
        component: AdminFixedDeposits,
      },
      {
        path: 'tickets',
        name: 'AdminTickets',
        component: AdminSupportTickets,
      },
    ],
  },
  {
    path: '/user',
    component: Interface,
    children: [
      { path: 'dashboard', name: 'Dashboard', component: DashBoard, meta: { requiresAuth: true } },
      { path: 'my-fds', name: 'MyFDs', component: MyFDs, meta: { requiresAuth: true } },
      { path: 'book-fd', name: 'BookFD', component: BookFD, meta: { requiresAuth: true } },
      {
        path: 'calculator',
        name: 'Calculator',
        component: Calculator,
        meta: { requiresAuth: true },
      },
      { path: 'support', name: 'Support', component: Support, meta: { requiresAuth: true } },
    ],
  },
  { path: '/:pathMatch(.*)*', redirect: '/' },
];

const router = createRouter({ history: createWebHistory(), routes });

router.beforeEach((to, from, next) => {
  const isAuthenticated = !!store.state.accessToken;
  const user = store.state.user;
  const isAdmin = user?.role === 'ROLE_ADMIN';

  if (!isAuthenticated && to.meta.requiresAuth) {
    return next('/login');
  }
  if (to.meta.requiresAdmin && !isAdmin) {
    return next('/user/dashboard');
  }
  if (isAuthenticated && (to.name === 'Login' || to.name === 'Register' || to.name === 'Home')) {
    return isAdmin ? next('/admin') : next('/user/dashboard');
  }

  next();
});

export default router;
