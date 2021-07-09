<template>
    <v-dialog v-model="graphDetailModalVisible" @click:outside="closeDialog" max-width="60%">
        <v-card>
            <v-tabs
                    v-model="tab"
                    fixed-tabs
                    background-color="indigo"
                    dark
            >
                <v-tab>节点</v-tab>
                <v-tab>关系</v-tab>
            </v-tabs>
            <v-container>
                <v-tabs-items v-model="tab">
                    <v-tab-item>
                        <v-text-field
                                v-model="nodeSearch"
                                append-icon="mdi-magnify"
                                label="搜索"
                                single-line
                                hide-details
                        ></v-text-field>
                        <v-data-table
                                :headers="nodeHeaders"
                                :items="graph.nodes.map(e=>{return{name:e.name,value:e.value,category:graph.categories[e.category].name}})"
                                :search="nodeSearch"
                        >
                        </v-data-table>
                    </v-tab-item>
                    <v-tab-item>
                        <v-text-field
                                v-model="linkSearch"
                                append-icon="mdi-magnify"
                                label="搜索"
                                single-line
                                hide-details
                        ></v-text-field>
                        <v-data-table
                                :headers="linkHeaders"
                                :items="graph.links"
                                :search="linkSearch"
                        >
                        </v-data-table>
                    </v-tab-item>
                </v-tabs-items>
            </v-container>

            <v-divider></v-divider>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                        color="error"
                        @click="closeDialog"
                >
                    关闭
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {mapGetters, mapMutations} from 'vuex'
    // import graph from '../../assets/testData'

    export default {
        data: () => ({
            /*      nodes: '',
                  links: '',*/
            tab: '',
            nodeSearch: '',
            nodeHeaders: [
                {
                    text: '节点名',
                    align: 'start',
                    value: 'name',
                },
                {text: '值', value: 'value'},
                {text: '类目', value: 'category'},
            ],
            linkSearch: '',
            linkHeaders: [
                {
                    text: '源节点',
                    align: 'start',
                    value: 'source',
                },
                {text: '目标节点', value: 'target'},
                {text: '关系名', value: 'value'}
            ],
        }),
        props: {
            graphDetailModalVisible: Boolean,
        },
        computed: {
            ...mapGetters([
                'graph'
            ]),
        },
        methods: {
            ...mapMutations([]),
            closeDialog() {
                this.$emit("chChangeGraphDetailModalVisible", false)
            }
        }
    }
</script>
