<template>
    <v-dialog max-width="500" v-model="addNodeModalVisible" persistent>
        <v-snackbar
                v-model="snackbarVisible"
                :timeout="2000"
        >{{text}}
        </v-snackbar>
        <v-card>
            <v-toolbar dense dark color="primary">
                <v-toolbar-title>填写数据</v-toolbar-title>
                <v-btn color="error" @click="cancel" small absolute right>关闭</v-btn>
            </v-toolbar>
            <v-container style="padding-bottom: 0">
                <v-form ref="form">
                    <v-row align="center" no-gutters>
                        <v-col cols="3">
                            <v-subheader class="label">
                                名称
                            </v-subheader>
                        </v-col>
                        <v-col cols="5">
                            <v-text-field v-model="name" :rules="rule">
                            </v-text-field>
                        </v-col>
                    </v-row>
                    <v-row align="center" no-gutters v-show="addNewCategoryVisible">
                      <v-col cols="3">
                        <v-subheader class="label">
                          类目
                        </v-subheader>
                      </v-col>
                      <v-col cols="5">
                        <v-text-field v-model="category" :rules="rule">
                        </v-text-field>
                      </v-col>
                      <v-col cols="3">
                        <v-checkbox
                            v-model="addNewCategoryVisible"
                            :label="'新增类目'">
                        </v-checkbox>
                      </v-col>
                    </v-row>
                    <v-row align="center" no-gutters v-show="!addNewCategoryVisible">
                      <v-col cols="3">
                        <v-subheader class="label" >
                            类目
                        </v-subheader>
                      </v-col>
                      <v-col cols="5">
                        <v-select
                            :items="categoryList"
                            v-model="category"
                        ></v-select>
                      </v-col>
                      <v-col cols="3">
                        <v-checkbox
                            v-model="addNewCategoryVisible"
                            :label="'新增类目'">
                        </v-checkbox>
                      </v-col>
                    </v-row>
                    <v-row align="center" no-gutters>
                        <v-col cols="3">
                            <v-subheader class="label">
                                形状
                            </v-subheader>
                        </v-col>
                        <v-col cols="5">
                            <v-select
                                    :items="symbolSelectList"
                                    v-model="symbol"
                                    append-outer
                            >
                              <template v-slot:append-outer>
                                <v-icon v-text="symbolIcon"></v-icon>
                              </template>
                            </v-select>
                        </v-col>
                    </v-row>
                </v-form>
            </v-container>
            <v-card-actions>
                <v-img
                    max-height="70"
                    max-width="120"
                    :src=imgSrc
                    absolute bottom left
                ></v-img>
                <v-btn color="info" @click="submit" absolute bottom right>提交</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {mapGetters, mapMutations, mapActions} from 'vuex'
    import catImg from '../../../assets/cat.png'

    export default {
        data: () => {
            return {
                imgSrc:catImg,
                name: '',
                category:'',
                symbol: 'circle',
                symbolIcon: 'mdi-circle',
                rule: [v => !!v || '请输入'],
                snackbarVisible: false,
                text: '名称已存在',
                addNewCategoryVisible:false
            }
        },
        watch: {
            symbol: function (val) {
                this.symbolIcon=this.symbolSelectList.find(item=>{
                    return item.value===val
                }).icon
            }
        },
        props: {
            message: Array,
            addNodeModalVisible: Boolean
        },
        computed: {
            categoryList:function(){
              let items=[]
              for(let i=0;i<this.graph.categories.length;i++){
                items.push(this.graph.categories[i].name)
              }
              return items
            },
            ...mapGetters([
                'addNodeParams',
                'graph',
                'currentGraphId',
                'symbolSelectList'
            ]),
        },
        methods: {
            ...mapMutations([
                'set_addNodeParams',
                'addNodeToGraph',
                'addEdgeToGraph',
                'addCategoryToGraph',
            ]),
            ...mapActions([
                'addNode',
                'insertCategory'
            ]),
            cancel() {
                this.$emit("chChangeAddNodeModalVisible", false)
            },
            async submit() {
                if (this.$refs.form.validate()) {
                    if (this.graph.nodes.find(item => item.name === this.name) === undefined) {
                        let findCategory = this.graph.categories.find(item => item.name === this.category)
                        if (findCategory === undefined) {
                            await this.insertCategory({
                                graphId:this.currentGraphId,
                                name:this.category,
                                categoryProperties:{}
                            })
                            this.addCategoryToGraph({name: this.category})
                            this.category = this.graph.categories.length - 1
                        } else
                            this.category = this.graph.categories.indexOf(findCategory)
                        const readOnlyData = {
                            symbolSize: 1 / 2 + 10,
                            x: this.message[0],
                            y: this.message[1],
                            category: this.category,
                            value: 1,
                            symbol:this.symbol
                        }
                        const data={
                          name: this.name,
                          ...readOnlyData
                        }
                        this.addNodeToGraph(data)
                        let dataNewFormat = {
                            graphId: this.currentGraphId,
                            contextProperties: {},
                            entityProperties: {...readOnlyData},
                            name: this.name
                        }
                        this.set_addNodeParams(dataNewFormat)
                        await this.addNode()
                        this.name = ''
                        this.category = ''
                        this.symbol='circle'
                        this.$emit("chChangeAddNodeModalVisible", false)
                    } else {
                        this.snackbarVisible = true
                    }
                }
            },
        },
    }
</script>

<style scoped>
    .label {
        font-size: 16px;
        color: black;
    }
</style>
