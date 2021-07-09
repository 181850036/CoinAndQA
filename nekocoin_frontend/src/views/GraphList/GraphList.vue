<template>
    <v-app>
        <v-main class="lighten-3">
            <v-container fluid style="max-width: 90%">
                <v-row>
                    <v-col
                            v-for="graph in this.graphList"
                            v-bind:key="graph.graphId"
                            class="d-flex child-flex"
                            cols="3"
                    >
                        <v-card
                                class="mx-auto"
                                max-width="300"
                        >
                            <v-img
                                    :src="`https://picsum.photos/500/300?image=${12}`"
                                    aspect-ratio="1"
                                    class="grey lighten-2"
                                    v-on:click="loadGraph(graph)"
                            ></v-img>

                            <v-card-title>
                                {{graph.name}}
                            </v-card-title>
                            <v-divider></v-divider>
                            <v-card-actions>
                                <v-btn
                                        color="error"
                                        @click="showDeleteModal(graph)"
                                        style="margin:auto"
                                >
                                    删除
                                </v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-col>

                </v-row>
            </v-container>
        </v-main>
        <AddGraphModal></AddGraphModal>
        <DeleteGraphModal
                :deleteModalVisible="deleteModalVisible"
                :deleteGraphId="deleteGraphId"
                @chChangeDeleteModalVisible="faChangeDeleteModalVisible"
        ></DeleteGraphModal>
    </v-app>
</template>

<script>
    import AddGraphModal from './addGraphModal'
    import DeleteGraphModal from './deleteGraphModal'
    import {mapGetters, mapMutations, mapActions} from 'vuex'
    import {setToken} from '@/utils/auth'

    export default {
        data: () => ({
            graphListCnt: 0,
            List: [],
            deleteModalVisible: false,
            deleteGraphId:''
        }),
        components: {
            AddGraphModal,
            DeleteGraphModal
        },
        computed: {
            ...mapGetters([
                'graphList',
            ]),
        },
        methods: {
            ...mapMutations([
                'set_graphList'
            ]),
            ...mapActions([
                'getGraphList',
                'deleteGraph'
            ]),
            faChangeDeleteModalVisible(visible){
                this.deleteModalVisible=visible
            },
            loadGraph(graph) {
                setToken(graph.graphId + ',' + graph.name)
                this.$router.push({
                    name: 'Helloworld',
                    params: {
                        currentGraphId: graph.graphId,
                        currentGraphName: graph.name
                    }
                })

            },

            showDeleteModal(graph) {
                this.deleteModalVisible = true
                this.deleteGraphId = graph.graphId
            }
        },
        async mounted() {
            window.vue = this
            await this.getGraphList()
            this.graphListCnt = this.graphList.length
            this.List = this.graphList
        }
    }
</script>