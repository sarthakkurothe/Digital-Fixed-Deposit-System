<template>
  <div class="home-layout">
    <Header></Header>
    <router-view />
  </div>
</template>

<script>
import Header from '../components/Header.vue';

export default {
  name: 'HomeView',
  components: {
    Header
  },
}
</script>

<style scoped>
.home-layout {
  min-height: 100%;
}
</style>
