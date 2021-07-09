<template>
    <v-dialog max-width="30%" v-model="addGraphModalVisible" persistent>
        <v-card>
            <v-toolbar dense dark color="primary">
                <v-toolbar-title>新建图谱</v-toolbar-title>
            </v-toolbar>
            <v-container v-show="chooseMode">
                <v-btn large class="ma-5" @click="setMode('create')">新建空白图谱</v-btn>

                <v-btn large class="ma-5" @click="setMode('upload')">导入已有图谱</v-btn>

            </v-container>
            <v-container v-show="uploadMode">
                <v-form
                        ref="uploadFileForm"
                        v-model="uploadFormValid">
                    <v-file-input
                            required
                            :rules="[v => !!v || '清选择文件！']"
                            show-size accept=".json"
                            @change="changeFile"
                            :disabled="uploadGraphFileLoading"
                            :loading="uploadGraphFileLoading"
                            label="点击选择文件，文件格后缀为：.json"></v-file-input>
                </v-form>
            </v-container>
            <v-container v-show="createMode">
                <v-form
                        ref="createGraphForm"
                        >
                    <v-text-field v-model="graphName"
                                  :rules="graphNameRules"
                                  :counter="20"
                                  label="输入想要创建的图谱名，不可与已有图谱重名"
                                  required>
                    </v-text-field>
                </v-form>
            </v-container>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                        color="success"
                        @click="uploadFile"
                        v-show="uploadMode"
                >
                    上传
                </v-btn>
                <v-btn
                        color="success"
                        @click="createGraph"
                        v-show="createMode"
                >
                    新建
                </v-btn>
                <v-btn
                        color="error"
                        @click="closeDialog"
                >
                    退出
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {mapGetters, mapMutations, mapActions} from 'vuex'

    export default {
        data: () => ({
            uploadFormValid: false,
            fileInfo: '',
            uploadMode: false,
            createMode: false,
            chooseMode: true,
            graphName: '',
            graphNameRules: [
                v => !!v || '请输入图谱名！',
                v => v.length <= 20 || '图谱名长度不可超过20',
            ],
        }),
        computed: {
            ...mapGetters([
                'addGraphModalVisible',
                'uploadGraphFileLoading',
                'currentGraphId',
            ])
        },
        methods: {
            ...mapMutations([
                'set_addGraphModalVisible',
                'set_uploadGraphFileLoading',
                'set_addNodeParams'
            ]),
            ...mapActions([
                'uploadGraphFile',
                'getGraph',
                'addGraph',
                'getGraphList'
            ]),
            setMode(mode){
                if(mode === 'create') {
                    this.createMode = true
                    this.uploadMode = false
                }
                if(mode === 'upload') {
                    this.uploadMode = true
                    this.createMode = false
                }
            },
            // 上传文件方法
            changeFile(data) {
                if (data) {
                    this.fileInfo = data
                }
            },
            async createGraph(){
                if (this.graphName.length > 0 && this.graphName.length <= 20){
                    let dataFormat = {
                        graphId: this.graphName,
                        name: this.graphName,
                        graphProperties: {}
                    }
                    this.set_addNodeParams(dataFormat)
                    await this.addGraph()
                    await this.getGraphList()
                    this.closeDialog()
                }
            },
            uploadFile() {
                if (!this.uploadGraphFileLoading && this.fileInfo && this.$refs.uploadFileForm.validate()) {
                    this.set_uploadGraphFileLoading(true)
                    if (window.FileReader) {
                        let fr = new FileReader()
                        let content = ''
                        let that = this
                        fr.onload = async (e) => {
                            content = e.target.result
                            let graphObj = JSON.parse(content)
/*                            let newObj = {
                                name: "neko",
                                graphId: "ft",
                                entities: graphObj.entities.map((e) => {
                                    let name = e.name
                                    delete e.name
                                    return {
                                        graphId:"ft",
                                        name: name,
                                        entityProperties: {
                                            ...e
                                        },
                                        contextProperties: null
                                    }
                                }),
                                links:graphObj.links.map((e)=>{
                                    return {
                                        graphId:"ft",
                                        ...e,
                                        value:"null",
                                        style:null
                                    }
                                }),
                                categories:graphObj.categories.map((e)=>{
                                    return {
                                        graphId:"ft",
                                        ...e,
                                        categoryProperties:{}
                                    }
                                })
                            }
                            console.log(newObj)*/
                            //调试用
                            let dataFormat = {
                                graphId: graphObj.graphId,
                                name: graphObj.name,
                                graphProperties: graphObj.graphProperties
                            }
                            this.set_addNodeParams(dataFormat)
                            await this.addGraph()
                            await that.uploadGraphFile(graphObj)
                            await that.getGraphList()
                        }
                        fr.readAsText(this.fileInfo, 'UTF-8')
                    } else {
                        alert("Not supported by your browser!")
                    }
                    //this.uploadGraphFile(JSON.parse(this.fileInfo))
                }
            },
            closeDialog() {
                this.set_addGraphModalVisible(false)
            }
        }
    }
</script>
