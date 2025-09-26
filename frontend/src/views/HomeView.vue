<template>
  <div class="home-layout">
    <Header ></Header>

    <HeroSection />
    <FeaturesSection :features="features" />
    <SecuritySection :securityFeatures="securityFeatures" :compliance="compliance" />
    <Footer />
    
    <router-view />
  </div>
</template>

<script>
import Header from '../components/Header.vue';
import HeroSection from '../components/HeroSection.vue';
import FeaturesSection from '../components/FeaturesSection.vue';
import SecuritySection from '../components/SecuritySection.vue';
import Footer from "../components/Footer.vue"; 

import { 
  Smartphone, PieChart, Calendar, RefreshCw, Headphones, Lock,
  Shield, Eye, CheckCircle, Award, Globe
} from 'lucide-vue-next';

export default {
  name: 'HomeView',
  components: { Header, HeroSection, FeaturesSection, SecuritySection, Footer },

  beforeRouteEnter(to, from, next) {
    const token = localStorage.getItem("token");
    if (token) {
      // If token exists → redirect to dashboard
      next("/user/dashboard");
    } else {
      next(); // Otherwise stay on Home
    }
  },

  
  data() {
    return {
      features: [
        { icon: Smartphone, title: "Digital FD Booking", description: "Book Fixed Deposits instantly with just a few clicks. Choose your amount, tenure, and interest scheme.", badge: "Core Feature" },
        { icon: PieChart, title: "Interest Computation", description: "Real-time calculation of accrued interest with detailed breakdown and maturity projections.", badge: "Smart Analytics" },
        { icon: Calendar, title: "Maturity Tracking", description: "Never miss a maturity date with detailed timeline views.", badge: "Time Management" },
        { icon: RefreshCw, title: "Premature Withdrawal", description: "Break FDs early with transparent penalty calculations and instant payout estimates.", badge: "Flexibility" },
        { icon: Headphones, title: "24/7 Support System", description: "Dedicated support for all FD-related queries with ticket tracking and quick resolution.", badge: "Customer Care" },
        { icon: Lock, title: "Bank-Grade Security", description: "Multi-layered security with encryption and secure authentication protocols.", badge: "Security First" }
      ],

      securityFeatures: [
        { icon: Shield, title: "256-bit SSL Encryption", description: "All data transmission is protected using bank-grade encryption protocols." },
        { icon: Lock, title: "Credential-based Authentication", description: "Secure login with username and password enforcing complexity rules." },
        { icon: Eye, title: "Real-time System Monitoring", description: "Continuous system monitoring to ensure performance, reliability, and uptime." },
        { icon: Lock, title: "Secure Data Storage", description: "Secure storage of sensitive data with encryption and access control." }
      ],

      compliance: [
        { icon: Award, title: "RBI Compliant", description: "Fully compliant with Reserve Bank of India regulations and guidelines." },
        { icon: CheckCircle, title: "ISO 27001 Certified", description: "International standard for information security management." },
        { icon: Globe, title: "GDPR Compliant", description: "Your data privacy rights are protected under global standards." },
        { icon: Smartphone, title: "PCI DSS Level 1", description: "Highest level of payment card industry security standards." }
      ]
    };
  }
};
</script>

<style scoped>
.home-layout { min-height: 100vh; }
.gradient-text {
  display: inline-block;
  background: linear-gradient(to right, #2563eb, #4f46e5);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
</style>
