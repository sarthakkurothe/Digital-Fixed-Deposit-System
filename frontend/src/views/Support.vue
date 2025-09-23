<template>
  <div class="p-4 md:p-8 min-h-screen bg-gray-50">
    <div class="max-w-6xl mx-auto">
      <div class="mb-8">
        <h1 class="text-3xl md:text-4xl font-bold text-gray-800 mb-2">Help & Support</h1>
        <p class="text-gray-600">Get assistance with your fixed deposit queries and account management</p>
      </div>
      
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Quick Actions -->
        <div class="lg:col-span-2 space-y-6">
          <!-- Support Categories -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 class="text-xl font-semibold text-gray-800 mb-4">How can we help you?</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 cursor-pointer transition-colors">
                <div class="flex items-center space-x-3">
                  <span class="text-2xl">💰</span>
                  <div>
                    <h3 class="font-medium text-gray-800">FD Management</h3>
                    <p class="text-sm text-gray-500">Account & investment queries</p>
                  </div>
                </div>
              </div>
              <div class="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 cursor-pointer transition-colors">
                <div class="flex items-center space-x-3">
                  <span class="text-2xl">🔒</span>
                  <div>
                    <h3 class="font-medium text-gray-800">Security</h3>
                    <p class="text-sm text-gray-500">Login & security issues</p>
                  </div>
                </div>
              </div>
              <div class="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 cursor-pointer transition-colors">
                <div class="flex items-center space-x-3">
                  <span class="text-2xl">📊</span>
                  <div>
                    <h3 class="font-medium text-gray-800">Reports</h3>
                    <p class="text-sm text-gray-500">Statements & certificates</p>
                  </div>
                </div>
              </div>
              <div class="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 cursor-pointer transition-colors">
                <div class="flex items-center space-x-3">
                  <span class="text-2xl">⚙️</span>
                  <div>
                    <h3 class="font-medium text-gray-800">Technical</h3>
                    <p class="text-sm text-gray-500">App & website issues</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Contact Form -->
          <div class="bg-gray-100 border-2 border-dashed border-gray-300 rounded-xl p-8">
            <h2 class="text-xl font-semibold text-gray-700 mb-4">📝 Support Ticket Form</h2>
            <p class="text-gray-500 mb-4">Implementation area for support ticket creation:</p>
            <ul class="text-left text-gray-500 space-y-1 mb-4">
              <li>• Category selection</li>
              <li>• Issue description</li>
              <li>• File attachments</li>
              <li>• Priority selection</li>
              <li>• Contact preferences</li>
            </ul>
            <p class="text-blue-600 font-semibold"><em>Team can implement support form here</em></p>
          </div>
        </div>
        
        <!-- Sidebar -->
        <div class="space-y-6">
          <!-- Contact Info -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">Contact Information</h3>
            <div class="space-y-3">
              <div class="flex items-center space-x-3">
                <span class="text-blue-500">📞</span>
                <div>
                  <p class="font-medium text-gray-800">Phone Support</p>
                  <p class="text-sm text-gray-500">1800-123-4567</p>
                </div>
              </div>
              <div class="flex items-center space-x-3">
                <span class="text-green-500">✉️</span>
                <div>
                  <p class="font-medium text-gray-800">Email Support</p>
                  <p class="text-sm text-gray-500">support@digitalfd.com</p>
                </div>
              </div>
              <div class="flex items-center space-x-3">
                <span class="text-purple-500">💬</span>
                <div>
                  <p class="font-medium text-gray-800">Live Chat</p>
                  <p class="text-sm text-gray-500">Available 24/7</p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- FAQ Section -->
          <div class="bg-gray-100 border-2 border-dashed border-gray-300 rounded-xl p-6">
            <h3 class="text-lg font-semibold text-gray-700 mb-3">❓ FAQ Section</h3>
            <p class="text-gray-500 text-sm mb-3">Common questions and answers</p>
            <p class="text-blue-600 font-semibold text-sm"><em>Team can implement FAQ component here</em></p>
          </div>
          
          <!-- Ticket Status -->
          <div class="bg-gray-100 border-2 border-dashed border-gray-300 rounded-xl p-6">
            <h3 class="text-lg font-semibold text-gray-700 mb-3">🎫 Ticket Status</h3>
            <p class="text-gray-500 text-sm mb-3">Track your support requests</p>
            <p class="text-blue-600 font-semibold text-sm"><em>Team can implement ticket tracking here</em></p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Support',
  mounted() {
    console.log('Support component mounted - ready for team development')
  }
}
</script>

<style scoped>
/* Custom styles if needed */
</style>