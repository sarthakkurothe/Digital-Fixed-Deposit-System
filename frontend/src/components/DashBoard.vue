<template>
    <h1>{{ user.name }}</h1>
    <h2>{{ user.email }}</h2>
    <h2>{{ user.age }}</h2>
</template>
<script setup>
    import { computed } from 'vue';
import store from '../store';

    const user = computed(() => store.state.user);



</script>