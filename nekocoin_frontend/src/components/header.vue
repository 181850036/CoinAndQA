<template>
        <v-app-bar
                app
                color="blue darken-3"
                flat>
            <v-container fluid class="py-0 fill-height">
                <v-avatar
                        class="mr-10"
                        color="grey lighten-3"
                        size="32"></v-avatar>
                <v-btn
                        v-for="link in links"
                        :key="link"
                        text
                        @click="tabGo(link)"
                        color="grey lighten-3"
                >
                    {{ link }}
                </v-btn>
                <v-spacer/>
<!--                <v-responsive max-width="260">-->
<!--                    <v-text-field-->
<!--                            label="搜索"-->
<!--                            dense-->
<!--                            flat-->
<!--                            hide-details-->
<!--                            rounded-->
<!--                            solo-inverted></v-text-field>-->
<!--                </v-responsive>-->
              <Search class = "margin-left" :itemlist=[] v-show="this.isSearchVisible"></Search>
            </v-container>
        </v-app-bar>

</template>

<script>
    import {mapMutations} from 'vuex'
    import Search from "@/components/search"

    export default {
        components: {
            Search
        },
        data: () => ({
            // links: [
            //     '个人中心',
            //     '新建图谱',
            //     '图谱仓库',
            //     '更多',
            // ],
        }),
        computed:{
            links: function () {
                let currentlinks = [
                    '个人中心',
                    '图谱仓库',
                    '更多']
                if(this.currentRoute !== "test"){
                    currentlinks.push('新建图谱')
                }
                return currentlinks
            },
            currentRoute: function () {
                if(this.$route.fullPath === "/list"){
                    return "list"
                }
                else if(this.$route.fullPath === "/test"){
                    return "test"
                }
                else return "others"
            },
            isSearchVisible: function () {
                if(this.currentRoute === "test"){
                    return true
                }
                else return false
            }
        },
        methods: {
            ...mapMutations([
                'set_addGraphModalVisible'
            ]),
            tabGo(option) {
                if (option === '新建图谱')
                    this.set_addGraphModalVisible(true)
                if (option === '图谱仓库')
                    this.$router.push({name: 'GraphList'})
/*                if (option === '更多') {
                    setToken('ft,testName')
                    this.$router.push({
                        name: 'Helloworld',
                        params: {currentGraphId: 'ft', currentGraphName: 'testName'}
                    })
                }*/
            },
        },
    }
</script>