import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import 'vue-material-design-icons/styles.css';
import vuetify from './plugins/vuetify';
import VueCookie from 'vue-cookie';
import VueEasySession from 'vue-easysession'
import i18n from './plugins/i18n';
import FlagIcon from 'vue-flag-icon';
import Vuelidate from 'vuelidate'

Vue.config.productionTip = false;
Vue.use(Vuetify);
Vue.use(VueCookie);
Vue.use(Vuelidate);
Vue.use(VueEasySession.install);
Vue.use(FlagIcon);

const message = {};

const storeMessage = new Vue({
    data: message
});

new Vue({
    VueCookie,
    router,
    store,
    vuetify,
    VueEasySession,
    i18n,
    render: h => h(App)
}).$mount('#app');

