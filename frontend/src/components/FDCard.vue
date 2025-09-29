<template>
  <div
    class="bg-white rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition-all duration-200 overflow-hidden"
  >
    <!-- Header -->
    <div class="bg-gradient-to-r from-slate-50 to-gray-50 px-5 py-4 border-b border-gray-100">
      <div class="flex justify-between items-center">
        <h3 class="text-lg font-semibold text-slate-800">FD #{{ fd.id }}</h3>
        <span :class="statusBadgeClass" class="text-xs font-medium px-3 py-1.5 rounded-full border">
          {{ fd.status }}
        </span>
      </div>
    </div>

    <!-- Content -->
    <div class="p-5">
      <!-- Principal Amount -->
      <div class="mb-4">
        <div class="text-2xl font-bold text-slate-900 mb-1">
          ₹{{ fd.amount.toLocaleString('en-IN') }}
        </div>
        <div class="flex items-center gap-3 text-sm">
          <span class="flex items-center gap-1 text-emerald-600 font-medium">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"
              />
            </svg>
            {{ fd.interest_rate }}% p.a.
          </span>
          <span class="text-slate-600">{{ fd.tenure_months }} months</span>
        </div>
      </div>

      <!-- Maturity Details -->
      <div class="bg-slate-50 rounded-lg p-3 mb-4">
        <div class="flex justify-between items-center">
          <span class="text-sm font-medium text-slate-600">Maturity Date</span>
          <span class="text-sm font-semibold text-slate-800">{{
            formatDate(fd.maturity_date)
          }}</span>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="flex gap-3">
        <button
          @click="$emit('view', fd)"
          class="flex-1 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium text-sm py-2.5 px-4 rounded-lg transition-colors duration-150 flex items-center cursor-pointer justify-center gap-2"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
            />
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
            />
          </svg>
          View Details
        </button>
        <button
          @click="$emit('break', fd)"
          :disabled="fd.status !== 'ACTIVE'"
          :class="[
            'flex-1 font-medium text-sm py-2.5 px-4 rounded-lg transition-all duration-150 flex items-center justify-center gap-2',
            fd.status === 'ACTIVE'
              ? 'bg-red-400 hover:bg-red-600 cursor-pointer text-white shadow-sm hover:shadow'
              : 'bg-gray-100 text-gray-400 cursor-not-allowed',
          ]"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          {{ fd.status === 'ACTIVE' ? 'Break FD' : 'Unavailable' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FDCard',
  props: {
    fd: { type: Object, required: true },
    index: { type: Number, required: true },
  },
  computed: {
    statusBadgeClass() {
      switch (this.fd.status) {
        case 'ACTIVE':
          return 'bg-emerald-50 text-emerald-700 border-emerald-200';
        case 'MATURED':
          return 'bg-blue-50 text-blue-700 border-blue-200';
        case 'PENDING':
          return 'bg-amber-50 text-amber-700 border-amber-200';
        default:
          return 'bg-red-50 text-red-700 border-red-200';
      }
    },
  },
  methods: {
    formatDate(date) {
      return date
        ? new Date(date).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
          })
        : '-';
    },
  },
};
</script>
