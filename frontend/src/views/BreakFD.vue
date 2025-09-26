<template>
  <div class="fixed inset-0 flex items-center justify-center bg-black/40 z-50">
    <div v-if="preview" class="bg-white p-6 rounded-lg w-130 space-y-4">
      <!-- Header -->
      <div>
        <div class="flex items-center gap-2 mb-2">
          <AlertTriangle class="w-5 h-5 text-red-600" />
          <h3 class="font-semibold text-lg">Break Fixed Deposit</h3>
        </div>
        <p class="text-sm text-gray-600">
          Are you sure you want to break FD #{{ preview.fdId }}? This action cannot be undone.
        </p>
        <p class="text-sm text-red-600 font-medium mt-1">
          Warning: Breaking this FD before maturity will incur a penalty.
        </p>
      </div>

      <!-- FD Info -->
      <div class="bg-gray-100 p-4 rounded-lg space-y-2 text-sm">
        <div class="flex justify-between px-4"><span>Amount:</span><span class="font-bold">₹{{ preview.principalAmount.toLocaleString('en-IN') }}</span></div>
        <div class="flex justify-between px-4"><span>Interest Rate:</span><span class="font-bold">{{ preview.interestRate }}%</span></div>
        <div class="flex justify-between px-4"><span>Tenure:</span><span class="font-bold">{{ preview.tenure }} months</span></div>
        <div class="flex justify-between px-4"><span>Start Date:</span><span class="font-bold">{{ formatDate(preview.startDate) }}</span></div>
        <div class="flex justify-between px-4"><span>Maturity Date:</span><span class="font-bold">{{ formatDate(preview.maturityDate) }}</span></div>
        <div class="flex justify-between px-4"><span>Time Elapsed:</span><span class="font-bold">{{ preview.timeElapsed }} months</span></div>
      </div>

      <!-- Premature Withdrawal Calculation -->
      <div class="bg-red-50 p-4 rounded-lg border border-red-200 text-sm">
        <h4 class="font-medium text-red-600 mb-3">Premature Withdrawal Calculation</h4>
        <div class="space-y-2">
          <div class="flex justify-between px-4"><span>Principal:</span><span class="font-bold">₹{{ preview.principalAmount.toLocaleString('en-IN') }}</span></div>
          <div class="flex justify-between px-4"><span>Accrued Interest:</span><span class="text-green-600">₹{{ preview.accruedInterest.toLocaleString('en-IN') }}</span></div>
          <div class="flex justify-between px-4"><span>Penalty:</span><span class="text-red-600">₹{{ preview.penalty.toLocaleString('en-IN') }}</span></div>
          <div class="w-full flex justify-between py-3 bg-white text-black px-4 rounded-lg">
            <span class="font-medium">Total Payout</span>
            <span class="font-bold">₹{{ preview.payout.toLocaleString('en-IN') }}</span>
          </div>
        </div>
      </div>

      <!-- Actions -->
      <div class="flex gap-3 pt-4">
        <button @click="$emit('close')" class="flex-1 border border-gray-300 rounded px-3 py-1 hover:bg-gray-100  cursor-pointer font-bold">Cancel</button>
        <button @click="confirmBreakFD" class="flex-1 bg-red-600 text-white hover:bg-red-700 rounded px-3 py-1 cursor-pointer font-bold">Confirm</button>
      </div>
    </div>

    <!-- Optional loading fallback -->
    <div v-else class="text-white text-center p-6">
      Loading FD details...
    </div>
  </div>
</template>



<script setup>
import { AlertTriangle } from 'lucide-vue-next';
import { ref, onMounted } from 'vue';
import { useStore } from 'vuex';

const props = defineProps({ fdId: Number });
const emit = defineEmits(['close', 'fdBroken']);

const store = useStore();
const preview = ref(null);

onMounted(async () => {
  preview.value = await store.dispatch('fetchBreakPreview', props.fdId);
});

const formatDate = (dateStr) => {
  const date = new Date(dateStr);
  return date.toLocaleDateString('en-IN', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  });
};

const confirmBreakFD = () => {
  if (!preview.value) return;

  const updatedFd = {
    id: preview.value.fdId,
    status: 'PENDING',
    payout: preview.value.payout,
  };
  console.log('Emitting fdBroken with:', updatedFd);
  emit('fdBroken', updatedFd);
  emit('close');
};
</script>
