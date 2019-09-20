<template>

</template>

<script>

    import api from './backend-api'
    import i18n from '../plugins/i18n'

    export default {
        name: "RegistrationConfirm",
        data() {
            return {
                code: ''
            }
        },
        created() {
            this.code = this.$route.params.code;
            api.registrationConfirm(this.code)
                .then(response => {
                    if (response.data.success) {
                        this.$root.$data.message = response.data.message;
                        this.$router.push('/login').catch(error => {
                            console.log(error);
                        });
                    } else {
                        this.$root.$data.message = response.data.message;
                        this.$router.push('/registration').catch(error => {
                            console.log(error);
                        });
                    }
                })
                .catch(() => {
                    this.$root.$data.message = i18n.t('connectionLost');
                    this.$router.push('/registration').catch(error => {
                        console.log(error);
                    });
                })
        }
    }
</script>

<style scoped>

</style>