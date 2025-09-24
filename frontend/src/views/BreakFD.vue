<template>
  <div class="fixed inset-0 flex items-center justify-center bg-black/40 z-50">
    <div class="bg-white p-6 rounded-lg w-130 space-y-4">

      <!-- Dialog Header -->
      <div>
        <div class="flex items-center gap-2 mb-2">
          <AlertTriangle class="w-5 h-5 text-red-600" />
          <h3 class="font-semibold text-lg">Break Fixed Deposit</h3>
        </div>
        <p class="text-sm text-gray-600">
          Are you sure you want to break FD #101? This action cannot be undone.
        </p>
        <p class="text-sm text-red-600 font-medium mt-1">
          Warning: Breaking this FD before maturity may reduce your interest.
        </p>
      </div>

      <!-- Original FD Info -->
      <div class="bg-gray-100 p-4 rounded-lg space-y-2">
        <div class="flex justify-between">
          <span class="text-sm text-gray-500">FD ID:</span>
          <span class="font-medium">101</span>
        </div>
        <div class="flex justify-between">
          <span class="text-sm text-gray-500">Original Amount:</span>
          <span class="font-medium">₹50,000</span>
        </div>
        <div class="flex justify-between">
          <span class="text-sm text-gray-500">Interest Rate:</span>
          <span class="font-medium">6.5%</span>
        </div>
        <div class="flex justify-between">
          <span class="text-sm text-gray-500">Tenure:</span>
          <span class="font-medium">12 months</span>
        </div>
        <div class="flex justify-between">
          <span class="text-sm text-gray-500">Start Date:</span>
          <span class="font-medium">01-Jan-2025</span>
        </div>
        <div class="flex justify-between">
          <span class="text-sm text-gray-500">Maturity Date:</span>
          <span class="font-medium">31-Dec-2025</span>
        </div>
        <div class="flex justify-between">
          <span class="text-sm text-gray-500">Time Elapsed:</span>
          <span class="font-medium">6 months</span>
        </div>
      </div>

      <!-- Premature Break Calculation -->
      <div class="bg-red-50 p-4 rounded-lg border border-red-200">
        <h4 class="font-medium text-red-600 mb-3">Premature Break Calculation</h4>
        <div class="space-y-2 text-sm">
          <div class="flex justify-between">
            <span>Principal Amount:</span>
            <span class="font-medium">₹50,000</span>
          </div>
          <div class="flex justify-between">
            <span>Interest (Reduced Rate 5.5%):</span>
            <span class="font-medium text-green-600">+₹2,500</span>
          </div>
          <div class="flex justify-between">
            <span>Interest Loss:</span>
            <span class="font-medium text-red-600">₹500</span>
          </div>
          <div class="flex justify-between border-t pt-2 font-semibold">
            <span>Total Payout:</span>
            <span>₹52,000</span>
          </div>
        </div>
      </div>

      <!-- Actions -->
      <div class="flex gap-3 pt-4">
        <button class="flex-1 border rounded px-3 py-1">
          Cancel
        </button>
        <button class="flex-1 bg-red-600 text-white rounded px-3 py-1">
          Confirm Break FD
        </button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { AlertTriangle } from 'lucide-vue-next';
</script>
