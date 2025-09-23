<template>
  <div class="p-4 md:p-8 min-h-screen bg-gray-50">
    <div class="mb-8">
      <h1 class="text-3xl md:text-4xl font-bold text-gray-800 mb-2">My Fixed Deposits</h1>
      <p class="text-gray-600">Manage and track all your fixed deposit investments</p>
    </div>
    
    <div class="space-y-8">
      <!-- Summary Cards -->
      <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div class="bg-white p-4 md:p-6 rounded-lg shadow-sm border border-gray-200 text-center">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Total FDs</h3>
          <span class="text-2xl md:text-3xl font-bold text-gray-900">4</span>
        </div>
        <div class="bg-white p-4 md:p-6 rounded-lg shadow-sm border border-gray-200 text-center">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Active</h3>
          <span class="text-2xl md:text-3xl font-bold text-green-600">2</span>
        </div>
        <div class="bg-white p-4 md:p-6 rounded-lg shadow-sm border border-gray-200 text-center">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Matured</h3>
          <span class="text-2xl md:text-3xl font-bold text-blue-600">1</span>
        </div>
        <div class="bg-white p-4 md:p-6 rounded-lg shadow-sm border border-gray-200 text-center">
          <h3 class="text-sm font-medium text-gray-500 mb-2">Total Value</h3>
          <span class="text-lg md:text-xl font-bold text-purple-600">₹4,75,000</span>
        </div>
      </div>
      
      <!-- Content Grid -->
      <div class="grid grid-cols-1 xl:grid-cols-3 gap-6">
        <div class="xl:col-span-2 bg-gray-100 border-2 border-dashed border-gray-300 rounded-xl p-6 md:p-8">
          <h2 class="text-xl font-semibold text-gray-700 mb-4">📋 FD List Table</h2>
          <p class="text-gray-500 mb-4">This section will display a detailed table of all fixed deposits with:</p>
          <ul class="text-left text-gray-500 space-y-1 mb-4">
            <li>• FD Number & Status</li>
            <li>• Principal Amount & Interest Rate</li>
            <li>• Start Date & Maturity Date</li>
            <li>• Current Value & Interest Earned</li>
            <li>• Action buttons (View Details, Renew, etc.)</li>
          </ul>
          <p class="text-blue-600 font-semibold"><em>Team can implement FD table component here</em></p>
        </div>
        
        <div class="bg-gray-100 border-2 border-dashed border-gray-300 rounded-xl p-6 md:p-8 text-center">
          <h2 class="text-xl font-semibold text-gray-700 mb-4">📊 FD Performance Chart</h2>
          <p class="text-gray-500 mb-4">Visual representation of FD performance over time...</p>
          <p class="text-blue-600 font-semibold"><em>Team can implement chart component here</em></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MyFDs',
  mounted() {
    console.log('MyFDs component mounted - ready for team development')
  }
}
</script>

<style scoped>
/* Custom styles if needed */
</style>