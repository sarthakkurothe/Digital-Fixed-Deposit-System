



      <template>
  <div class="p-6 space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-start">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Support Center</h1>
        <p class="text-gray-500 mt-1">
          Get help with your Fixed Deposit queries
        </p>
      </div>

      <!-- Create Ticket Button & Dialog -->
      <Dialog v-model:open="showNewTicketDialog">
        <DialogTrigger as-child>
          <button
            class="flex items-center gap-2 px-4 py-2 rounded-xl bg-blue-600 text-white hover:bg-blue-700 transition"
          >
            <Plus class="w-4 h-4" />
            Create Ticket
          </button>
        </DialogTrigger>

        <DialogContent class="max-w-md">
          <DialogHeader>
            <DialogTitle>Create Support Ticket</DialogTitle>
            <DialogDescription>
              Describe your issue and we'll help you resolve it quickly.
            </DialogDescription>
          </DialogHeader>

          <form @submit.prevent="handleSubmitTicket" class="space-y-4">
            <!-- FD Select -->
            <div class="space-y-2">
              <label class="text-sm font-medium">Related FD (Optional)</label>
              <select
                v-model="newTicket.fdId"
                class="w-full border rounded-lg p-2"
              >
                <option value="">Select FD</option>
                <option
                  v-for="fd in fixedDeposits"
                  :key="fd.id"
                  :value="fd.id"
                >
                  FD #{{ fd.id }} - ₹{{ fd.amount.toLocaleString() }} ({{ fd.status }})
                </option>
              </select>
            </div>

            <!-- Subject -->
            <div class="space-y-2">
              <label class="text-sm font-medium" for="ticket-subject">Subject</label>
              <input
                id="ticket-subject"
                v-model="newTicket.subject"
                type="text"
                placeholder="Brief description of your issue"
                class="w-full border rounded-lg p-2"
                required
              />
            </div>

            <!-- Description -->
            <div class="space-y-2">
              <label class="text-sm font-medium" for="ticket-description">Description</label>
              <textarea
                id="ticket-description"
                v-model="newTicket.description"
                placeholder="Provide detailed information about your query or issue"
                class="w-full border rounded-lg p-2"
                rows="4"
                required
              />
            </div>

            <div class="flex gap-3 pt-4">
              <button
                type="button"
                @click="showNewTicketDialog = false"
                class="flex-1 px-4 py-2 border rounded-xl text-gray-700 hover:bg-gray-100"
              >
                Cancel
              </button>
              <button
                type="submit"
                class="flex-1 px-4 py-2 rounded-xl bg-blue-600 text-white hover:bg-blue-700"
              >
                Submit Ticket
              </button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div class="bg-white rounded-xl shadow p-4">
        <div class="flex justify-between items-center mb-2">
          <p class="text-sm text-gray-500">Open Tickets</p>
          <Clock class="h-4 w-4 text-yellow-500" />
        </div>
        <p class="text-2xl font-bold">{{ openTickets }}</p>
        <p class="text-xs text-gray-500">Awaiting response</p>
      </div>

      <div class="bg-green-50 rounded-xl shadow p-4">
        <div class="flex justify-between items-center mb-2">
          <p class="text-sm text-green-700">Resolved Tickets</p>
          <CheckCircle class="h-4 w-4 text-green-600" />
        </div>
        <p class="text-2xl font-bold text-green-700">{{ resolvedTickets }}</p>
        <p class="text-xs text-green-600">Issues resolved</p>
      </div>

      <div class="bg-white rounded-xl shadow p-4">
        <div class="flex justify-between items-center mb-2">
          <p class="text-sm text-gray-500">Avg Response Time</p>
          <MessageSquare class="h-4 w-4 text-blue-600" />
        </div>
        <p class="text-2xl font-bold">4.2h</p>
        <p class="text-xs text-gray-500">Within 24 hours</p>
      </div>
    </div>

    <!-- Tickets & Sidebar -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Support Tickets -->
      <div class="lg:col-span-2 bg-white rounded-xl shadow p-6 space-y-4">
        <div class="flex justify-between items-center mb-4">
          <div>
            <h2 class="text-xl font-semibold">Your Support Tickets</h2>
            <p class="text-gray-500">Track your queries and their status</p>
          </div>
          <div class="relative">
            <Search class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
            <input
              v-model="searchTerm"
              type="text"
              placeholder="Search tickets..."
              class="pl-10 border rounded-lg p-2 w-64"
            />
          </div>
        </div>

        <div v-if="filteredTickets.length === 0" class="text-center py-8 text-gray-500">
          <HelpCircle class="w-12 h-12 mx-auto mb-3 opacity-50" />
          <p>No support tickets found</p>
          <p class="text-sm">Create a new ticket to get help with your queries</p>
        </div>

        <div
          v-for="ticket in filteredTickets"
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
          <p class="text-sm text-gray-500 line-clamp-2">
            {{ ticket.description }}
          </p>

          <div class="text-xs text-gray-500 flex gap-4">
            <span v-if="ticket.fdId" class="flex items-center gap-1">
              <FileText class="w-3 h-3" /> FD #{{ ticket.fdId }}
            </span>
            <span>Created: {{ formatDate(ticket.created_at) }}</span>
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

      <!-- Sidebar -->
      <div class="space-y-6">
        <!-- Contact Info -->
        <div class="bg-white rounded-xl shadow p-6 space-y-3">
          <h3 class="text-lg font-semibold mb-4">Contact Information</h3>
          <div class="flex items-center gap-3">
            <Phone class="text-blue-600 w-5 h-5" />
            <div>
              <p class="font-medium">Phone Support</p>
              <p class="text-sm text-gray-500">1800-XXX-XXXX</p>
            </div>
          </div>
          <div class="flex items-center gap-3">
            <Mail class="text-green-600 w-5 h-5" />
            <div>
              <p class="font-medium">Email Support</p>
              <p class="text-sm text-gray-500">support@digitalfd.com</p>
            </div>
          </div>
        </div>

        <!-- FAQ -->
        <div class="bg-white rounded-xl shadow p-6 space-y-3">
          <h3 class="text-lg font-semibold mb-3">Frequently Asked Questions</h3>
          <div class="p-3 bg-gray-50 rounded-lg">
            <p class="font-medium text-sm">How is FD interest calculated?</p>
            <p class="text-xs text-gray-500">
              Interest = (P × R × T) / 100
            </p>
          </div>
          <div class="p-3 bg-gray-50 rounded-lg">
            <p class="font-medium text-sm">Can I break my FD early?</p>
            <p class="text-xs text-gray-500">
              Yes, but penalties may apply. Check our withdrawal policy.
            </p>
          </div>
          <div class="p-3 bg-gray-50 rounded-lg">
            <p class="font-medium text-sm">How do I get my FD certificate?</p>
            <p class="text-xs text-gray-500">
              Certificates are auto-generated and available in your account.
            </p>
          </div>
          <button class="w-full border rounded-lg px-3 py-2 text-sm hover:bg-gray-100">
            View All FAQs
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  HelpCircle,
  Plus,
  Search,
  Clock,
  CheckCircle,
  XCircle,
  MessageSquare,
  Phone,
  Mail,
  FileText
} from "lucide-vue-next"
import { ref, computed } from "vue"

// Dummy data (replace with API later)
const supportTickets = [
  {
    id: 1,
    subject: "FD premature withdrawal",
    description: "I want to withdraw my FD before maturity",
    status: "OPEN",
    fdId: 101,
    created_at: "2025-09-01",
    response: null,
  },
  {
    id: 2,
    subject: "FD certificate not visible",
    description: "My FD certificate is missing in downloads",
    status: "RESOLVED",
    fdId: 102,
    created_at: "2025-09-10",
    response: "Your FD certificate has been reissued.",
  }
]

const fixedDeposits = [
  { id: 101, amount: 50000, status: "Active" },
  { id: 102, amount: 75000, status: "Closed" },
]

const searchTerm = ref("")
const newTicket = ref({ fdId: "", subject: "", description: "" })
const showNewTicketDialog = ref(false)

const filteredTickets = computed(() =>
  supportTickets.filter(
    (t) =>
      t.subject.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
      t.description.toLowerCase().includes(searchTerm.value.toLowerCase())
  )
)

const openTickets = computed(
  () => supportTickets.filter((t) => t.status === "OPEN").length
)
const resolvedTickets = computed(
  () => supportTickets.filter((t) => t.status === "RESOLVED").length
)

const handleSubmitTicket = () => {
  alert("Support Ticket Created Successfully 🎫")
  newTicket.value = { fdId: "", subject: "", description: "" }
  showNewTicketDialog.value = false
}

const formatDate = (dateStr) => new Date(dateStr).toLocaleDateString()

// Status UI helpers
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
