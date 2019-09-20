import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Registration from '@/components/Registration'
import Login from '@/components/Login'
import Protected from '@/components/Protected'
import ForgotPassword from '@/components/ForgotPassword'
import PasswordUpdate from '@/components/PasswordUpdate'
import RegistrationConfirm from '@/components/RegistrationConfirm'
import VueEasySession from "vue-easysession";
import VueCookie from 'vue-cookie'
import api from './components/backend-api'

Vue.use(Router);
Vue.use(VueCookie);

const router = new Router({
    mode: 'history',
    routes: [
        { path: '/', component: Hello },
        { path: '/registration', component: Registration },
        { path: '/login', component: Login, meta: { loginPage: true } },
        { path: '/forgot-password', component: ForgotPassword },
        { path: '/password-update/:code', component: PasswordUpdate },
        { path: '/protected', component: Protected, meta: { requiresAuth: true } },
        { path: '/registration-confirm/:code', component: RegistrationConfirm },
        { path: '*', redirect: '/' }
    ]
});
router.beforeEach((to, from, next) => {
    const session = VueEasySession.getInstance();
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
    const isLoginPage = to.matched.some(record => record.meta.loginPage);
    const isAuthenticated = session.exists();
    const token =  Vue.cookie.get('token');
    let tokenExist = false;
    if ((isLoginPage || requiresAuth) && !isAuthenticated && token != null) {
        api.checkTokenExist(token).then(response => {
            tokenExist = response.data;
            if(tokenExist) {
                session.start();
                session.set('token', token);
                router.push('/protected').catch(error => {console.log(error);});
            } else {
                next();
            }
        }).catch(error => {
            console.log(error);
            next();
        });
    } else if (requiresAuth && !isAuthenticated) {
        next('/login');
    } else if (isLoginPage && isAuthenticated) {
        router.push('/protected').catch(error => {console.log(error);});
    } else {
        next();
    }
});

export default router;