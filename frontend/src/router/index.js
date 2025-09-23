import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import DashBoard from '../views/DashBoard.vue'
import MyFDs from '../views/MyFDs.vue'
import BookFD from '../views/BookFD.vue'
import Calculator from '../views/Calculator.vue'
import Support from '../views/Support.vue'
import Login from '../components/Login.vue'
import Register from '../components/Register.vue'
import store from '../store'
import Interface from '../components/Interface.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView,  // public homepage content
    children:[
      {
        path: 'login',
        component: Login,
        meta: { requiresAuth: false }
      },
      {
        path: 'register',
        component: Register,
        meta: { requiresAuth: false }
      },
    ]
  },
  {
    path: '/user',
    component: Interface,
    children:
    [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: DashBoard,
        meta: { requiresAuth: true }
      },
      {
        path: 'my-fds',
        name: 'MyFDs',
        component: MyFDs,
        meta: { requiresAuth: true }
      },
      {
        path: 'book-fd',
        component: BookFD,
        name: 'BookFD',
        meta: { requiresAuth: true }
      },
      {
        path: 'calculator',
        name: 'Calculator',
        component: Calculator,
        meta: { requiresAuth: true }
      },
      {
        path: 'support',
        name: 'Support',
        component: Support,
        meta: { requiresAuth: true }
      },
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guard
router.beforeEach((to, from, next) => {
  const isAuthenticated = !!store.state.token;

  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login');
  } else if ((to.path === '/login' || to.path === '/register') && isAuthenticated) {
    next('/user/dashboard');
  } else {
    next();
  }
});

export default router
