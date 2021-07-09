<template>
    <v-card style="margin-top:15px"
            min-height="230" v-show="nodeInfoVisible||edgeInfoVisible"
    >
        <v-toolbar dense dark color="primary">
            <v-toolbar-title>
                数据信息
            </v-toolbar-title>
        </v-toolbar>
        <v-container>
            <v-row align="center" no-gutters>
                <v-col cols="4">
                    <v-subheader>
                        类型
                    </v-subheader>
                </v-col>
                <v-col cols="2" style="font-size: 16px">
                    {{obj.dataType==="node"?"对象":"关系"}}
                </v-col>
            </v-row>
            <v-row align="center" no-gutters v-show="nodeInfoVisible">
                <v-col cols="4">
                    <v-subheader>
                        名称
                    </v-subheader>
                </v-col>
                <v-col cols="6">
                    <v-text-field dense v-model="name"></v-text-field>
                </v-col>
            </v-row>
            <v-row align="center" no-gutters v-show="nodeInfoVisible">
                <v-col cols="4">
                    <v-subheader>
                        类目
                    </v-subheader>
                </v-col>
                <v-col cols="6">
                    <v-text-field dense v-model="category"></v-text-field>
                </v-col>
            </v-row>
            <v-row align="center" no-gutters v-show="nodeInfoVisible">
                <v-col cols="4">
                    <v-subheader>
                        形状
                    </v-subheader>
                </v-col>
                <v-col cols="6">
                    <v-select
                            :items="symbolSelectList"
                            v-model="symbol"
                            append-outer
                            dense
                    >
                        <template v-slot:append-outer>
                            <v-icon v-text="symbolIcon"></v-icon>
                        </template>
                    </v-select>
                </v-col>
            </v-row>
            <v-row align="center" no-gutters v-show="edgeInfoVisible">
                <v-col cols="4">
                    <v-subheader>
                        源节点
                    </v-subheader>
                </v-col>
                <v-col cols="6">
                    <v-text-field dense v-model="source"></v-text-field>
                </v-col>
            </v-row>
            <v-row align="center" no-gutters v-show="edgeInfoVisible">
                <v-col cols="4">
                    <v-subheader>
                        目标
                    </v-subheader>
                </v-col>
                <v-col cols="6">
                    <v-text-field dense v-model="target"></v-text-field>
                </v-col>
            </v-row>
            <v-row align="center" no-gutters v-show="edgeInfoVisible">
                <v-col cols="4">
                    <v-subheader>
                        关系名
                    </v-subheader>
                </v-col>
                <v-col cols="6">
                    <v-text-field dense v-model="value"></v-text-field>
                </v-col>
            </v-row>
        </v-container>
        <v-card-actions>
            <v-btn color="info" style="margin: auto" @click="editInfo">更新</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import {mapGetters, mapMutations, mapActions} from 'vuex'

    export default {
        data:()=> ({
            newName: '',
            newCategory: '',
            newSource: '',
            newTarget: '',
            newValue: '',
            newSymbol: '',
            symbolIcon: ''
        }),
        computed:{
            ...mapGetters([
                'graph',
                'currentGraphId',
                'tempAxs',
                'symbolSelectList'
            ]),
            name: {
                get() {
                    return this.obj.data.name
                },
                set(newVal) {
                    this.newName = newVal
                }
            },
            category: {
                get() {
                    if (this.graph.categories[this.obj.data.category] === undefined)
                        return ''
                    return this.graph.categories[this.obj.data.category].name
                },
                set(newVal) {
                    this.newCategory = newVal
                }
            },
            source: {
                get() {
                    return this.obj.data.source
                },
                set(newVal) {
                    this.newSource = newVal
                }
            },
            target: {
                get() {
                    return this.obj.data.target
                },
                set(newVal) {
                    this.newTarget = newVal
                }
            },
            value: {
                get() {
                    return this.obj.data.value === undefined ? '' : this.obj.data.value
                },
                set(newVal) {
                    this.newValue = newVal
                }
            },
            symbol: {
                get() {
                    return this.obj.data.symbol===undefined?'circle':this.obj.data.symbol
                },
                set(newVal) {
                    this.newSymbol = newVal
                }
            },
        },
        props:{
            nodeInfoVisible:Boolean,
            edgeInfoVisible:Boolean,
            obj:Object,
            modifyNodeValueSymbolSize:Function
        },
        watch: {
            symbol: function (val) {
                this.watchChange(val)
            },
            newSymbol:function (val) {
                this.watchChange(val)
            }
        },
        methods: {
            ...mapMutations([
                'set_editNodeInfoParams',
                'set_editRelationInfoParams',
                'addCategoryToGraph',
            ]),
            ...mapActions([
                'editRelationInfo',
                'editNodeInfo',
                'insertCategory'
            ]),
            watchChange(val) {
                val=val===undefined||val===''?'circle':val
                this.symbolIcon = this.symbolSelectList.find(item => {
                    return item.value === val
                }).icon
            },

            //清空编辑信息
            resetBufferEdit() {
                this.newName = ''
                this.newCategory = ''
                this.newSource = ''
                this.newTarget = ''
                this.newValue = ''
                this.newSymbol = ''
            },
            editInfo() {
                if (this.obj.dataType === "node") {
                    this.tryEditNodeInfo()
                } else {
                    this.tryEditEdgeInfo()
                }
            },
            //编辑节点逻辑
            async tryEditNodeInfo() {
                //不合法
                if (this.newName === "" && this.newCategory === "" && this.newSymbol === "")
                    return
                //找到节点
                let editNode = this.graph.nodes.find(item => item.name === this.obj.data.name)
                //暂存信息
                let oldName = editNode.name
                if (this.newName !== '') {
                    //名判重
                    if (this.graph.nodes.find(item => item.name === this.newName) != null) {
                        this.$emit('chChangeSnackbarText', "名称已存在")
                        this.$emit('chChangeSnackbarVisible', true)
                        return
                    }
                    //更改所有关联边
                    this.graph.links.forEach((link) => {
                        if (link.source === this.obj.data.name)
                            link.source = this.newName
                        if (link.target === this.obj.data.name)
                            link.target = this.newName
                    })
                    if (this.tempAxs != []) {
                        this.tempAxs.forEach((tempAx) => {
                            if (tempAx.name === this.obj.data.name)
                                tempAx.name = this.newName
                        })
                    }
                    editNode.name = this.newName //数据缓存
                    this.obj.data.name = this.newName //输入窗
                }
                if (this.newCategory !== '') {
                    //判断是否需要新增类目
                    let findCategory = this.graph.categories.find(item => item.name === (this.newCategory))
                    let newCategoryIndex = undefined
                    if (findCategory === undefined) {
                        await this.insertCategory({
                            graphId: this.currentGraphId,
                            name: this.newCategory,
                            categoryProperties: {}
                        })
                        this.addCategoryToGraph({name: this.newCategory})
                        newCategoryIndex = this.graph.categories.length - 1
                    } else newCategoryIndex = this.graph.categories.indexOf(findCategory)

                    editNode.category = newCategoryIndex
                    this.obj.data.category = newCategoryIndex

                }
                if (this.newSymbol !== '') {
                    editNode.symbol = this.newSymbol
                    this.obj.data.symbol = this.newSymbol
                }
                //提交更改
                let readonlyData = {
                    symbolSize: editNode.symbolSize,
                    x: editNode.x,
                    y: editNode.y,
                    category: editNode.category,
                    value: editNode.value,
                    symbol: editNode.symbol
                }
                let formatData = {
                    name: oldName,
                    entity: {
                        name: editNode.name,
                        graphId: this.currentGraphId,
                        entityProperties: {
                            ...readonlyData
                        }
                    }
                }
                this.set_editNodeInfoParams(formatData)
                this.editNodeInfo()
                this.resetBufferEdit()
            },
            judEdgeEditValid() {
                let source = this.newSource === "" ? this.obj.data.source : this.newSource
                let target = this.newTarget === "" ? this.obj.data.target : this.newTarget
                let sourceNode = this.graph.nodes.find(item => item.name === source)
                let targetNode = this.graph.nodes.find(item => item.name === target)
                if (sourceNode !== undefined && targetNode !== undefined && sourceNode !== targetNode)
                    return [sourceNode, targetNode]
                else return null
            },
            //编辑边逻辑
            tryEditEdgeInfo() {
                if (this.newSource === "" && this.newTarget === "" && this.newValue === "")
                    return
                let editEdge = this.graph.links.find(item => (item.source === this.obj.data.source && item.target === this.obj.data.target))
                let data = {
                    graphId: this.currentGraphId,
                    source: this.obj.data.source,
                    target: this.obj.data.target,
                    newVal: {
                        source: this.obj.data.source,
                        target: this.obj.data.target,
                        value: this.obj.data.value
                    }
                }
                let res = this.judEdgeEditValid()
                if (res) {
                    if (this.newSource !== '') {
                        let oldNode = this.graph.nodes.find(item => item.name === editEdge.source)
                        editEdge.source = this.newSource
                        this.obj.data.source = this.newSource
                        data.newVal.source = this.newSource
                        this.modifyNodeValueSymbolSize(oldNode, '-')
                        this.modifyNodeValueSymbolSize(res[0], '+')
                    }
                    if (this.newTarget !== '') {
                        let oldNode = this.graph.nodes.find(item => item.name === editEdge.target)
                        editEdge.target = this.newTarget
                        this.obj.data.target = this.newTarget
                        data.newVal.target = this.newTarget
                        this.modifyNodeValueSymbolSize(oldNode, '-')
                        this.modifyNodeValueSymbolSize(res[1], '+')
                    }
                } else {
                    this.$emit('chChangeSnackbarText', "不存在的节点")
                    this.$emit('chChangeSnackbarVisible', true)
                    this.resetBufferEdit()
                    return
                }
                //关系名
                if (this.newValue !== '') {
                    editEdge.value = this.newValue
                    this.obj.data.value = this.newValue
                    data.newVal.value = this.newValue
                }
                this.set_editRelationInfoParams(data)
                this.editRelationInfo()
                this.resetBufferEdit()
            },
             handleChange(value) {
              this.symbol = value
    },
        }
    }
</script>