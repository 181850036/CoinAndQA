import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import router from './router'
import store from './store'
import $ from 'jquery'
import VueContextMenu from 'vue-contextmenu'
import '@/permission'

Vue.config.productionTip = false;
window.jQuery=$
window.$=$

Vue.config.productionTip = false
Vue.use(VueContextMenu)

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
