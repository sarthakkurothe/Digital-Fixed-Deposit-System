<template>
  <div class="p-4 md:p-8 min-h-screen bg-gray-50">
    <div class="max-w-4xl mx-auto">
      <div class="mb-8">
        <h1 class="text-3xl md:text-4xl font-bold text-gray-800 mb-2">Book New Fixed Deposit</h1>
        <p class="text-gray-600">Create a new fixed deposit investment with competitive interest rates</p>
      </div>
      
      <!-- Form Preview -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 md:p-8 mb-6">
        <h2 class="text-xl font-semibold text-gray-800 mb-6">FD Booking Form</h2>
        
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Investment Amount</label>
              <div class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center text-gray-500">
                Input field for amount
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Investment Duration</label>
              <div class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center text-gray-500">
                Dropdown for duration
              </div>
            </div>
          </div>
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Interest Rate</label>
              <div class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center text-gray-500">
                Display calculated rate
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Maturity Amount</label>
              <div class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center text-gray-500">
                Display projected amount
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Placeholder Content -->
      <div class="bg-gray-100 border-2 border-dashed border-gray-300 rounded-xl p-8 text-center">
        <h2 class="text-xl font-semibold text-gray-700 mb-4">📝 FD Booking Implementation</h2>
        <p class="text-gray-500 mb-2">This section will include:</p>
        <ul class="text-left text-gray-500 space-y-1 mb-4 max-w-md mx-auto">
          <li>• Form validation</li>
          <li>• Real-time interest calculation</li>
          <li>• Terms and conditions</li>
          <li>• Payment integration</li>
          <li>• Confirmation workflow</li>
        </ul>
        <p class="text-blue-600 font-semibold"><em>Team can implement FD booking form here</em></p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BookFD',
  mounted() {
    console.log('BookFD component mounted - ready for team development')
  }
}
</script>

<style scoped>
/* Custom styles if needed */
</style>