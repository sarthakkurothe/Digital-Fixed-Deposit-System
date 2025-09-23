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