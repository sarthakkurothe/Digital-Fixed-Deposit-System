<template>
  <div class="home-layout">
    <!-- Router outlet for nested routes -->
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'HomeView'
}
</script>

<style scoped>
.home-layout {
  min-height: 100%;
}
</style>
