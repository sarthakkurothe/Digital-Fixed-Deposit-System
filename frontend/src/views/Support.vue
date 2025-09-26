<template>
  <div class="p-6 space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-start">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Support Center</h1>
        <p class="text-gray-500 mt-1">Get help with your Fixed Deposit queries</p>
      </div>

      <!-- Create Ticket Button -->
      <button
        @click="showNewTicketDialog = true"
        class="flex items-center gap-2 px-5 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700 transition cursor-pointer"
      >
        <Plus class="w-4 h-4" />
        Create Ticket
      </button>
    </div>

    <!-- Modal -->
    <div
      v-if="showNewTicketDialog"
      class="fixed inset-0 z-50 flex items-center justify-center bg-white/60 backdrop-blur-sm"
      @click.self="closeModal"
    >
      <div class="bg-white rounded-lg shadow-lg max-w-md w-full p-6">
        <!-- Header -->
        <div class="mb-4">
          <h2 class="text-xl font-semibold">Create Support Ticket</h2>
          <p class="text-sm text-gray-500">
            Describe your issue and we'll help you resolve it quickly.
          </p>
        </div>

        <!-- Form -->
        <form @submit.prevent="handleSubmitTicket" class="space-y-4">
          <!-- FD Dropdown -->
          <div class="space-y-2">
            <label class="text-sm font-medium">Related FD (Optional)</label>
            <SchemeDropdown v-model="newTicket.fd" :schemes="fixedDeposits" />
          </div>

          <!-- Subject -->
          <div class="space-y-2">
            <label for="ticket-subject" class="text-sm font-medium">Subject</label>
            <input
              id="ticket-subject"
              v-model="newTicket.subject"
              type="text"
              placeholder="Brief description of your issue"
              class="w-full border rounded-md p-2 cursor-pointer"
              required
            />
          </div>

          <!-- Description -->
          <div class="space-y-2">
            <label for="ticket-description" class="text-sm font-medium">Description</label>
            <textarea
              id="ticket-description"
              v-model="newTicket.description"
              placeholder="Provide detailed information about your query or issue"
              class="w-full border rounded-md p-2 cursor-pointer"
              rows="4"
              required
            />
          </div>

          <!-- Actions -->
          <div class="flex gap-3 pt-4">
            <button
              type="button"
              @click="closeModal"
              class="flex-1 px-4 py-2 border rounded-md text-gray-700 hover:bg-gray-100 cursor-pointer"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="flex-1 px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700 cursor-pointer"
            >
              Submit Ticket
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Tickets -->
    <div class="bg-white rounded-xl shadow p-6 space-y-4">
      <h2 class="text-xl font-semibold">Your Support Tickets</h2>

      <div v-if="loading" class="text-gray-500">Loading tickets...</div>
      <div v-else-if="tickets.length === 0" class="text-gray-500">No support tickets found</div>

      <div
        v-for="ticket in tickets"
        :key="ticket.id"
        class="bg-gray-50 p-4 rounded-lg border space-y-3"
      >
        <div class="flex items-center gap-2">
          <p class="font-semibold">#{{ ticket.id }}</p>
          <span
            class="px-2 py-1 text-xs rounded-lg flex items-center gap-1"
            :class="statusClasses(ticket.status)"
          >
            <component :is="statusIcon(ticket.status)" class="w-4 h-4" />
            {{ ticket.status }}
          </span>
        </div>

        <h3 class="font-medium">{{ ticket.subject }}</h3>
        <p class="text-sm text-gray-500">{{ ticket.description }}</p>

        <div class="text-xs text-gray-500 flex gap-4">
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
</template>

<script setup>
import {
  HelpCircle,
  Plus,
  Clock,
  CheckCircle,
  XCircle,
  FileText
} from "lucide-vue-next"
import { ref, onMounted, computed } from "vue"
import { useStore } from "vuex"
import axios from "../api"
import SchemeDropdown from "../components/SchemeDropdown.vue"

// store
const store = useStore()

// state
const tickets = ref([])
const loading = ref(true)
const showNewTicketDialog = ref(false)
const newTicket = ref({ fd: null, subject: "", description: "" })

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
    console.log("Fetched tickets:", tickets.value)
  } catch (err) {
    console.error("Error fetching tickets:", err)
  } finally {
    loading.value = false
  }
}

// submit ticket
const handleSubmitTicket = async () => {
  try {
    await axios.post("/support", {
      fdId: newTicket.value.fd?.id || null,
      subject: newTicket.value.subject,
      description: newTicket.value.description,
      userId: userId.value,
    })
    alert("Support Ticket Created Successfully 🎫")
    closeModal()
    await fetchTickets()
  } catch (err) {
    console.error("Error creating ticket:", err)
  }
}

// modal close
const closeModal = () => {
  showNewTicketDialog.value = false
  newTicket.value = { fd: null, subject: "", description: "" }
}

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
