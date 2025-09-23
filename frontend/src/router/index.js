<<<<<<< HEAD

import { createRouter, createWebHistory } from "vue-router";
import store from "../store";
import Login from "../components/Login.vue";
import Register from "../components/Register.vue";
import DashBoard from "../components/DashBoard.vue";


const routes = [
  {
    path: "/login",
    name: "Login",
    component: Login,
    meta: { requiresAuth: false },
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
    meta: { requiresAuth: false },
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: DashBoard,
    meta: { requiresAuth: true },
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const isAuthenticated = !!store.state.token;

  if (to.meta.requiresAuth && !isAuthenticated) {
    // Not authenticated, redirect to login
    next("/login");
  } else if ((to.path === "/login" || to.path === "/register") && isAuthenticated) {
    // Authenticated user trying to access login/register → redirect to dashboard
    next("/dashboard");
  } else {
    next(); // Proceed normally
  }
});
export default router;
=======
import { createRouter, createWebHistory } from 'vue-router'

// Import components
import HomeView from '../views/HomeView.vue'
import Dashboard from '../views/Dashboard.vue'
import MyFDs from '../views/MyFDs.vue'
import BookFD from '../views/BookFD.vue'
import Calculator from '../views/Calculator.vue'
import Support from '../views/Support.vue'

const routes = [
  // Main application routes (with layout)
  {
    path: '/',
    component: HomeView,
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: Dashboard
      },
      {
        path: 'my-fds',
        name: 'MyFDs', 
        component: MyFDs
      },
      {
        path: 'book-fd',
        name: 'BookFD',
        component: BookFD
      },
      {
        path: 'calculator',
        name: 'Calculator',
        component: Calculator
      },
      {
        path: 'support',
        name: 'Support',
        component: Support
      }
    ]
  },
  // Catch all route - redirect to dashboard
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})
 

export default router
>>>>>>> main
