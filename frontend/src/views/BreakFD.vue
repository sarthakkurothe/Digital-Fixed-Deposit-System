<template>
  <transition name="fd-modal" appear>
    <div
      v-if="visible"
      class="fixed inset-0 z-50 flex items-center justify-center px-4 py-8"
      role="dialog"
      aria-modal="true"
    >
      <!-- overlay: click outside to close -->
      <div class="absolute inset-0 bg-black/50 backdrop-blur-sm" @click.self="close"></div>

      <!-- modal card -->
      <div
        class="relative bg-white rounded-2xl shadow-2xl w-full max-w-2xl mx-auto z-10 overflow-hidden"
      >
        <!-- header -->
        <div class="flex items-center justify-between gap-3 p-5 border-b border-gray-100">
          <div class="flex items-center gap-3">
            <div
              class="flex items-center justify-center w-12 h-12 rounded-xl bg-red-50 text-red-600"
            >
              <AlertTriangle class="w-6 h-6" />
            </div>
            <div>
              <h3 class="text-lg font-semibold text-slate-900">Break Fixed Deposit</h3>
              <p class="text-sm text-slate-500">Preview penalty & payout before confirming</p>
            </div>
          </div>

          <button
            @click="close"
            class="p-2 rounded-md text-slate-500 hover:bg-gray-100 hover:text-slate-700 transition cursor-pointer"
            aria-label="Close break preview"
          >
            <X class="w-5 h-5" />
          </button>
        </div>

        <!-- body -->
        <div class="p-6">
          <!-- loading -->
          <div v-if="loading" class="flex items-center justify-center py-8">
            <div class="text-gray-500">Loading preview...</div>
          </div>

          <!-- error -->
          <div v-else-if="error" class="text-center py-8">
            <div class="text-red-600 font-semibold mb-2">Failed to load preview</div>
            <div class="text-sm text-gray-500 mb-4">{{ error }}</div>
            <div class="flex justify-center gap-2">
              <button
                @click="fetchPreview"
                class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
              >
                Retry
              </button>
              <button @click="close" class="px-4 py-2 bg-gray-100 rounded hover:bg-gray-200">
                Close
              </button>
            </div>
          </div>

          <!-- preview -->
          <div v-else-if="preview">
            <!-- summary row -->
            <div class="grid grid-cols-2 gap-4 mb-4">
              <div class="p-4 rounded-lg bg-gray-50 border border-gray-100">
                <div class="text-xs text-slate-400 mb-1">FD</div>
                <div class="flex items-center gap-2">
                  <div class="font-semibold text-slate-900">#{{ preview.fdId ?? '—' }}</div>
                  <button
                    v-if="preview.fdId != null"
                    @click="copy(preview.fdId, 'FD ID copied')"
                    class="text-slate-400 hover:text-slate-700 p-1 rounded"
                    title="Copy FD ID"
                  >
                    <Clipboard class="w-4 h-4" />
                  </button>
                </div>
              </div>

              <div class="p-4 rounded-lg bg-gray-50 border border-gray-100">
                <div class="text-xs text-slate-400 mb-1">Principal</div>
                <div class="font-semibold text-slate-900">
                  ₹{{ formatNumber(preview.principalAmount) }}
                </div>
              </div>
            </div>

            <!-- FD Info -->
            <div class="bg-white p-4 rounded-lg space-y-2 text-sm border border-gray-100 mb-4">
              <div class="flex justify-between px-4">
                <span>Amount:</span
                ><span class="font-bold">₹{{ formatNumber(preview.principalAmount) }}</span>
              </div>
              <div class="flex justify-between px-4">
                <span>Interest Rate:</span
                ><span class="font-bold">{{ preview.interestRate }}%</span>
              </div>
              <div class="flex justify-between px-4">
                <span>Tenure:</span><span class="font-bold">{{ preview.tenure }} months</span>
              </div>
              <div class="flex justify-between px-4">
                <span>Start Date:</span
                ><span class="font-bold">{{ formatDate(preview.startDate) }}</span>
              </div>
              <div class="flex justify-between px-4">
                <span>Maturity Date:</span
                ><span class="font-bold">{{ formatDate(preview.maturityDate) }}</span>
              </div>
              <div class="flex justify-between px-4">
                <span>Time Elapsed:</span
                ><span class="font-bold">{{ preview.timeElapsed }} months</span>
              </div>
            </div>

            <!-- Premature Withdrawal Calculation -->
            <div class="bg-red-50 p-4 rounded-lg border border-red-200 text-sm mb-4">
              <div class="flex items-center justify-between mb-3">
                <h4 class="font-medium text-red-600">Premature Withdrawal Calculation</h4>
                <div class="flex items-center gap-2">
                  <button
                    @click="copy(preview.payout, 'Payout copied')"
                    class="text-slate-500 hover:text-slate-700 p-1 rounded"
                    title="Copy payout"
                  >
                    <Clipboard class="w-4 h-4" />
                  </button>
                </div>
              </div>

              <div class="space-y-2">
                <div class="flex justify-between px-4">
                  <span>Principal:</span
                  ><span class="font-bold">₹{{ formatNumber(preview.principalAmount) }}</span>
                </div>
                <div class="flex justify-between px-4">
                  <span>Accrued Interest:</span
                  ><span class="text-green-600 font-semibold"
                    >₹{{ formatNumber(preview.accruedInterest) }}</span
                  >
                </div>
                <div class="flex justify-between px-4">
                  <span>Penalty:</span
                  ><span class="text-red-600 font-semibold"
                    >₹{{ formatNumber(preview.penalty) }}</span
                  >
                </div>

                <div
                  class="w-full flex justify-between py-3 bg-white text-black px-4 rounded-lg mt-2"
                >
                  <span class="font-medium">Total Payout</span>
                  <span class="font-bold">₹{{ formatNumber(preview.payout) }}</span>
                </div>
              </div>
            </div>

            <!-- actions -->
            <div class="flex gap-3 justify-end">
              <button
                @click="close"
                class="px-4 py-2 rounded-lg bg-gray-100 hover:bg-gray-200 text-slate-700 transition font-medium cursor-pointer"
              >
                Cancel
              </button>

              <button
                :disabled="confirming"
                @click="showConfirmDialog = true"
                class="px-4 py-2 rounded-lg bg-red-600 hover:bg-red-700 text-white font-semibold transition disabled:opacity-60 disabled:cursor-not-allowed cursor-pointer"
              >
                <span v-if="!confirming">Confirm Break</span>
                <span v-else>Submitting...</span>
              </button>
            </div>
          </div>

          <!-- fallback if no preview and not loading  -->
          <div v-else class="text-center py-6 text-gray-500">No preview available.</div>
        </div>
      </div>

      <div
        v-if="showConfirmDialog"
        class="absolute inset-0 flex items-center justify-center z-50 bg-black/10 backdrop-blur-sm"
      >
        <div class="bg-white rounded-lg p-6 w-full max-w-md shadow-lg">
          <div class="flex items-center gap-3 mb-4">
            <div
              class="flex items-center justify-center w-10 h-10 rounded-full bg-red-100 text-red-600"
            >
              <CircleAlert class="w-5 h-5" />
            </div>
            <h2 class="text-lg font-semibold text-slate-900">Confirm Break Fixed Deposit</h2>
          </div>
          <p class="mb-6 text-sm text-gray-600">Are you sure you want to break this FD?</p>
          <div class="flex justify-end space-x-3">
            <button
              @click="showConfirmDialog = false"
              class="px-4 py-2 bg-gray-300 hover:bg-gray-400 text-gray-800 rounded-lg cursor-pointer"
            >
              Cancel
            </button>
            <button
              @click="
                () => {
                  confirmBreakFD();
                  showConfirmDialog = false;
                }
              "
              class="px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-lg cursor-pointer"
            >
              Confirm
            </button>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { ref, onMounted, watch, onUnmounted } from 'vue';
import { useStore } from 'vuex';
import { useToast } from 'vue-toastification';
import { AlertTriangle, X, Clipboard, CircleAlert } from 'lucide-vue-next';

// props & emits
const props = defineProps({
  fdId: { type: [Number, String], required: true },
});
const emit = defineEmits(['close', 'fdBroken']);

// store & toast
const store = useStore();
const toast = useToast();

// state
const visible = ref(true);
const preview = ref(null);
const loading = ref(false);
const error = ref(null);
const confirming = ref(false);
const showConfirmDialog = ref(false);

const formatNumber = v => {
  if (v == null || isNaN(Number(v))) return '0';
  return Number(v).toLocaleString('en-IN');
};
const formatDate = dateStr => {
  if (!dateStr) return '—';
  const d = new Date(dateStr);
  return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
};

// fetch preview
const fetchPreview = async () => {
  loading.value = true;
  error.value = null;
  preview.value = null;
  try {
    const res = await store.dispatch('fetchBreakPreview', props.fdId);
    preview.value = res;
    if (!preview.value) {
      error.value = 'No preview returned from server.';
    }
  } catch (err) {
    console.error('Error fetching break preview', err);
    error.value = err?.response?.data?.message || err?.message || 'Failed to fetch preview.';
    toast.error(error.value);
  } finally {
    loading.value = false;
  }
};

// confirm break (emit to parent)
const confirmBreakFD = async () => {
  if (!preview.value || confirming.value) return;

  confirming.value = true;
  try {
    const payload = {
      id: preview.value.fdId,
      status: 'PENDING',
      payout: preview.value.payout,
    };

    console.log('BreakFD: emitting fdBroken payload', payload);
    emit('fdBroken', payload);
    close();
  } catch (e) {
    console.error('Error confirming break', e);
    toast.error('Failed to submit break request');
  } finally {
    confirming.value = false;
  }
};

// copy helper
const copy = async (val, message = 'Copied') => {
  try {
    await navigator.clipboard.writeText(String(val));
    toast.success(message);
  } catch (e) {
    toast.error('Copy failed');
  }
};

// close helper
const close = () => {
  visible.value = false;
  emit('close');
};

// handle esc key to close
const onKey = e => {
  if (e.key === 'Escape') close();
};

onMounted(() => {
  fetchPreview();
  window.addEventListener('keydown', onKey);
});

onUnmounted(() => {
  window.removeEventListener('keydown', onKey);
});

watch(
  () => props.fdId,
  n => {
    if (n) {
      visible.value = true;
      fetchPreview();
    }
  }
);
</script>

<style scoped>
.fd-modal-enter-active,
.fd-modal-leave-active {
  transition:
    opacity 180ms ease,
    transform 180ms ease;
}
.fd-modal-enter-from,
.fd-modal-leave-to {
  opacity: 0;
  transform: translateY(6px) scale(0.995);
}
</style>
