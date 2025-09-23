
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
