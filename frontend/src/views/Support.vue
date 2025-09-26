<template>
  <div class="p-6 space-y-6">
    <!-- Header + Create Ticket -->
    <div class="flex justify-between items-start">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Support Center</h1>
        <p class="text-gray-500 mt-1">Get help with your Fixed Deposit queries</p>
      </div>

      <!-- Gradient Create Ticket Button -->
      <button
        @click="showNewTicketDialog = true"
        class="flex items-center gap-2 px-5 py-2 rounded-md bg-blue-600 text-white font-bold hover:bg-blue-700 shadow-md transition-all cursor-pointer"
      >
        <Plus class="w-4 h-4" />
        Raise a Ticket
      </button>
    </div>

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div
        class="bg-gradient-to-r from-yellow-200 to-yellow-100 rounded-xl p-4 flex flex-col justify-between shadow-md transform transition-all hover:-translate-y-1 hover:shadow-xl"
      >
        <div class="flex items-center justify-between mb-2">
          <p class="text-sm font-bold text-yellow-800">Open Tickets</p>
          <Clock class="w-4 h-4 text-yellow-800" />
        </div>
        <div class="text-2xl font-bold text-yellow-900">{{ tickets.filter(t => t.status === 'OPEN').length }}</div>
        <p class="text-xs text-yellow-800 mt-1">Awaiting response</p>
      </div>

      <div
        class="bg-gradient-to-r from-green-200 to-green-100 rounded-xl p-4 flex flex-col justify-between shadow-md transform transition-all hover:-translate-y-1 hover:shadow-xl"
      >
        <div class="flex items-center justify-between mb-2">
          <p class="text-sm font-bold text-green-800">Resolved Tickets</p>
          <CheckCircle class="w-4 h-4 text-green-800" />
        </div>
        <div class="text-2xl font-bold text-green-900">{{ tickets.filter(t => t.status === 'RESOLVED').length }}</div>
        <p class="text-xs text-green-800 mt-1">Issues resolved</p>
      </div>

      <div
        class="bg-gradient-to-r from-blue-200 to-blue-100 rounded-xl p-4 flex flex-col justify-between shadow-md transform transition-all hover:-translate-y-1 hover:shadow-xl"
      >
        <div class="flex items-center justify-between mb-2">
          <p class="text-sm font-bold text-blue-800">Avg Response Time</p>
          <MessageSquare class="w-4 h-4 text-blue-800" />
        </div>
        <div class="text-2xl font-bold text-blue-900">4.2h</div>
        <p class="text-xs text-blue-800 mt-1">Within 24 hours</p>
      </div>
    </div>

    <!-- Modal -->
    <div
      v-if="showNewTicketDialog"
      class="fixed inset-0 w-full h-full z-50 flex items-center justify-center bg-black/40 backdrop-blur-md"
      @click.self="closeModal"
    >
      <div class="bg-white rounded-xl shadow-xl max-w-md w-full p-6">
        <h2 class="text-xl font-bold mb-1">Create Support Ticket</h2>
        <p class="text-sm text-gray-500 mb-4">
          Describe your issue and we'll help you resolve it quickly.
        </p>

        <form @submit.prevent="handleSubmitTicket" class="space-y-4" novalidate>
          <!-- FD Dropdown -->
          <div class="space-y-1">
            <label class="text-sm font-medium">Related FD</label>
            <SchemeDropdown
              v-model="newTicket.fd"
              :schemes="fixedDeposits"
              placeholder="Select FD"
            />
            <p v-if="errors.fd" class="text-red-500 text-xs mt-1">{{ errors.fd }}</p>
          </div>

          <!-- Subject -->
          <div class="space-y-1">
            <label class="text-sm font-medium">Subject</label>
            <input
              v-model="newTicket.subject"
              type="text"
              placeholder="Brief description of your issue"
              class="w-full p-2 rounded-md bg-white border border-gray-300 focus:border-gray-500 focus:outline-none"
            />
            <p v-if="errors.subject" class="text-red-500 text-xs mt-1">{{ errors.subject }}</p>
          </div>

          <!-- Description -->
          <div class="space-y-1">
            <label class="text-sm font-medium">Description</label>
            <textarea
              v-model="newTicket.description"
              placeholder="Provide detailed information about your query or issue"
              rows="4"
              class="w-full p-2 rounded-md bg-white border border-gray-300 focus:border-gray-500 focus:outline-none"
            ></textarea>
            <p v-if="errors.description" class="text-red-500 text-xs mt-1">{{ errors.description }}</p>
          </div>

          <!-- Actions -->
          <div class="flex gap-3 pt-4">
            <button
              type="button"
              @click="closeModal"
              class="flex-1 px-4 py-2 rounded-md bg-white border border-red-500 text-red-600 font-bold hover:bg-red-50 transition-all cursor-pointer"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="flex-1 px-4 py-2 rounded-md bg-blue-600 text-white font-bold hover:bg-blue-700 shadow-md transition-all cursor-pointer"
            >
              Confirm
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Tickets List -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <div class="lg:col-span-2 space-y-4">
        <div class="flex items-center justify-between mb-2">
          <h2 class="text-xl font-bold">Your Support Tickets</h2>
          <div class="relative">
            <input
              v-model="searchTerm"
              type="text"
              placeholder="Search tickets..."
              class="border rounded-xl p-2 pl-10 w-64 border-gray-300"
            />
            <Search class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400" />
          </div>
        </div>

        <div v-if="loading" class="text-gray-500">Loading tickets...</div>
        <div v-else-if="filteredTickets.length === 0" class="text-gray-500">No support tickets found</div>

        <div v-else class="space-y-4">
          <div
            v-for="ticket in filteredTickets"
            :key="ticket.id"
            class="bg-white p-4 rounded-xl shadow-sm space-y-2 border-none hover:shadow-lg transform transition-all hover:-translate-y-1"
          >
            <div class="flex items-center justify-between">
              <div class="flex items-center gap-2">
                <p class="font-bold">#{{ ticket.id }}</p>
                <span
                  class="px-2 py-1 text-xs rounded-lg flex items-center gap-1"
                  :class="statusClasses(ticket.status)"
                >
                  <component :is="statusIcon(ticket.status)" class="w-4 h-4" />
                  {{ ticket.status }}
                </span>
              </div>
            </div>

            <h3 class="font-medium text-gray-800">{{ ticket.subject }}</h3>
            <p class="text-sm text-gray-500 line-clamp-2">{{ ticket.description }}</p>

            <div class="flex items-center gap-4 text-xs text-gray-500">
              <span v-if="ticket.fdId" class="flex items-center gap-1">
                <FileText class="w-3 h-3" /> FD #{{ ticket.fdId }}
              </span>
              <span>Created: {{ formatDate(ticket.createdDate) }}</span>
            </div>

            <div
              v-if="ticket.response"
              class="mt-2 p-3 bg-green-50 rounded border-l-4 border-green-600"
            >
              <p class="text-sm font-medium text-green-700">Support Response:</p>
              <p class="text-sm">{{ ticket.response }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Contact & FAQ -->
      <div class="space-y-4">
        <!-- Contact -->
        <div class="bg-white rounded-xl shadow p-4 space-y-3">
          <h3 class="font-bold">Contact Information</h3>
          <p class="text-sm text-gray-500">Alternative ways to reach us</p>

          <div class="flex items-center gap-3 p-3 bg-gray-50 rounded-xl">
            <div class="w-10 h-10 bg-blue-100 rounded flex items-center justify-center">
              <Phone class="w-5 h-5 text-blue-600" />
            </div>
            <div>
              <p class="font-medium">Phone Support</p>
              <p class="text-sm text-gray-500">1800-XXX-XXXX</p>
              <p class="text-xs text-gray-400">Mon-Fri: 9 AM - 6 PM</p>
            </div>
          </div>

          <div class="flex items-center gap-3 p-3 bg-gray-50 rounded-xl">
            <div class="w-10 h-10 bg-green-100 rounded flex items-center justify-center">
              <Mail class="w-5 h-5 text-green-600" />
            </div>
            <div>
              <p class="font-medium">Email Support</p>
              <p class="text-sm text-gray-500">support@smartfd.com</p>
              <p class="text-xs text-gray-400">Response within 24 hours</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  HelpCircle,
  Plus,
  Clock,
  CheckCircle,
  XCircle,
  FileText,
  Phone,
  Mail,
  MessageSquare,
  Search
} from "lucide-vue-next"
import { ref, onMounted, computed } from "vue"
import { useStore } from "vuex"
import axios from "../api"
import SchemeDropdown from "../components/SchemeDropdown.vue"
import { useToast } from "vue-toastification" // <-- import toast

const toast = useToast() // <-- initialize toast

// store
const store = useStore()

// state
const tickets = ref([])
const loading = ref(true)
const showNewTicketDialog = ref(false)
const newTicket = ref({ fd: null, subject: "", description: "" })
const searchTerm = ref("")
const errors = ref({})

// fetch tickets from backend
onMounted(async () => {
  await store.dispatch("fetchFDs") // fetch user's FDs
  await fetchTickets()
})

const userId = computed(() => store.getters.getUser?.id)

const fixedDeposits = computed(() =>
  store.getters.getFDs.map(fd => ({
    id: fd.id,
    name: `FD #${fd.id} - ₹${fd.amount}`,
    rate: fd.interestRate,
    status: fd.status,
    created_at: fd.createdDate
  }))
)

const fetchTickets = async () => {
  if (!userId.value) return
  try {
    const res = await axios.get(`/support/user/${userId.value}`)
    tickets.value = res.data
  } catch (err) {
    console.error("Error fetching tickets:", err)
  } finally {
    loading.value = false
  }
}

// submit ticket
const handleSubmitTicket = async () => {
  errors.value = {}
  if (!newTicket.value.fd) errors.value.fd = "Please select a Fixed Deposit."
  if (!newTicket.value.subject) errors.value.subject = "Subject is required."
  if (!newTicket.value.description) errors.value.description = "Description is required."

  if (Object.keys(errors.value).length) return

  try {
    await axios.post("/support", {
      fdId: newTicket.value.fd?.id || null,
      subject: newTicket.value.subject,
      description: newTicket.value.description,
      userId: userId.value,
    })
    toast.success("Support Ticket Created Successfully 🎫") // <-- replaced alert
    closeModal()
    await fetchTickets()
  } catch (err) {
    console.error("Error creating ticket:", err)
    toast.error("Failed to create support ticket ❌")
  }
}

// modal close
const closeModal = () => {
  showNewTicketDialog.value = false
  newTicket.value = { fd: null, subject: "", description: "" }
  errors.value = {}
}

// filtered tickets
const filteredTickets = computed(() =>
  tickets.value.filter(ticket =>
    ticket.subject.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
    ticket.description.toLowerCase().includes(searchTerm.value.toLowerCase())
  )
)

// utils
const formatDate = (dateStr) => new Date(dateStr).toLocaleDateString()

const statusClasses = (status) => {
  switch (status) {
    case "OPEN":
      return "bg-yellow-100 text-yellow-800"
    case "RESOLVED":
      return "bg-green-100 text-green-800"
    case "CLOSED":
      return "bg-red-100 text-red-800"
    default:
      return "bg-gray-100 text-gray-800"
  }
}
const statusIcon = (status) => {
  switch (status) {
    case "OPEN":
      return Clock
    case "RESOLVED":
      return CheckCircle
    case "CLOSED":
      return XCircle
    default:
      return HelpCircle
  }
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;  
  overflow: hidden;
}
</style>
