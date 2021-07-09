<template>
    <v-card
            min-height="333"
    >
        <v-toolbar dense dark color="primary">
            <v-toolbar-title>
                选项
            </v-toolbar-title>
        </v-toolbar>
        <v-list-item-group>
            <v-divider></v-divider>
            <v-list-group
                    :value="true"
                    prepend-icon="mdi-shape-rectangle-plus">
                <template v-slot:activator>
                    <v-list-item-title>视图</v-list-item-title>
                </template>
                <v-divider></v-divider>
                <v-list-item>
                    <template v-slot:default>
                        <v-btn color="info" style="margin:auto" @click="showGraphDetail">列表显示
                        </v-btn>
                        <v-btn color="info" style="margin:auto" @click="showModifyViewModal">视觉优化</v-btn>
                    </template>
                </v-list-item>
            </v-list-group>
            <v-divider></v-divider>
            <v-list-group
                    :value="true"
                    prepend-icon="mdi-tools dark">
                <template v-slot:activator>
                    <v-list-item-title>编辑</v-list-item-title>
                </template>
                <v-divider></v-divider>
                <v-list-item>
                    <template v-slot:default>
                        <v-menu offset-y>
                            <template v-slot:activator="{on,attrs}">
                                <v-btn
                                        color="info"
                                        v-bind="attrs"
                                        v-on="on"
                                        style="margin:auto">
                                    调整布局
                                </v-btn>
                            </template>
                            <v-list>
                                <v-list-item @click="alterMode('none')">
                                    <v-list-item-title>坐标布局</v-list-item-title>
                                </v-list-item>
                                <v-list-item @click="alterMode('circular')">
                                    <v-list-item-title>环形布局</v-list-item-title>
                                </v-list-item>
                                <v-list-item @click="alterMode('force')">
                                    <v-list-item-title>力引导布局</v-list-item-title>
                                </v-list-item>
                                <v-list-item @click="alterMode('arrangement')">
                                    <v-list-item-title>排版布局</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                        <v-btn color="info" style="margin:auto" @click="enableDraggable">拖拽模式</v-btn>
                    </template>
                </v-list-item>
                <v-list-item>
                    <template v-slot:default>
                        <v-btn color="info" style="margin:auto" @click="resetMonitor">重置视角</v-btn>
                    </template>
                </v-list-item>
            </v-list-group>
            <v-divider></v-divider>
            <v-list-group
                    :value="true"
                    prepend-icon="mdi-folder dark">
                <template v-slot:activator>
                    <v-list-item-title>操作</v-list-item-title>
                </template>
                <v-divider></v-divider>
                <v-list-item>
                    <template v-slot:default>
                        <v-btn
                                color="success"
                                @click="saveLayout"
                                style="margin:auto"
                        >
                            保存
                        </v-btn>
                        <v-menu offset-y>
                            <template v-slot:activator="{on,attrs}">
                                <v-btn
                                        color="success"
                                        v-bind="attrs"
                                        v-on="on"
                                        style="margin:auto">
                                    导出
                                </v-btn>
                            </template>
                            <v-list>
                                <v-list-item @click="exportImg">
                                    <v-list-item-title>导出图片</v-list-item-title>
                                </v-list-item>
                                <v-list-item @click="exportJSON">
                                    <v-list-item-title>导出JSON</v-list-item-title>
                                </v-list-item>
                                <v-list-item @click="exportXML">
                                    <v-list-item-title>导出XML</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                        <v-dialog
                                v-model="deleteGraphConfirm"
                                width="500"
                        >
                            <template v-slot:activator="{ on, attrs }">
                                <v-btn
                                        color="error"
                                        v-bind="attrs"
                                        v-on="on"
                                        style="margin:auto"
                                >
                                    删除
                                </v-btn>
                            </template>
                            <v-card>
                                <v-toolbar dense dark color="red lighten-2">
                                    <v-toolbar-title>注意！</v-toolbar-title>
                                </v-toolbar>

                                <v-card-text style="margin-top: 5%">该图谱所有内容将被删除，是否确认？</v-card-text>
                                <v-divider></v-divider>
                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn
                                            color="error"
                                            @click="confirmDeleteGraph"
                                            style="margin-right: 5%"
                                    >
                                        确认
                                    </v-btn>
                                    <v-btn
                                            color="primary"
                                            @click="deleteGraphConfirm = false"
                                            style="margin: auto"
                                    >
                                        取消
                                    </v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </template>
                </v-list-item>
            </v-list-group>
        </v-list-item-group>
    </v-card>
</template>

<script>
    import {mapGetters, mapMutations, mapActions} from 'vuex'

    export default {
        data: () => ({
            deleteGraphConfirm: false,
            inDragFlag: false,
            saveNodes: [],
        }),
        computed: {
            ...mapGetters([
                'graph',
                'myChart',
                'currentGraphId',
                'tempAxs'
            ])
        },
        props: {
            mode: String
        },
        methods: {
            ...mapMutations([
                'set_tempAxs'
            ]),
            ...mapActions([
                'deleteGraph',
                'editNodes'
            ]),
            resetMonitor() {
                let option = {
                    series:
                        [
                            {
                                center: null,
                                zoom: 1
                            }
                        ]
                }
                this.myChart.setOption(option)
            },
            saveLayout() {
                if (this.mode === "circular") {
                    this.$emit("chChangeSnackbarText", "暂不支持保存环形布局")
                    this.$emit("chChangeSnackbarVisible", true, 4000)
                } else if (this.mode === "force") {
                    this.$emit("chChangeSnackbarText", "暂不支持保存力引导布局")
                    this.$emit("chChangeSnackbarVisible", true, 4000)
                } else {
                    let newNodes = []
                    for (let i = 0; i < this.graph.nodes.length; i++) {
                        let node = this.graph.nodes[i]
                        let entityProperties = {
                            symbolSize: node.symbolSize,
                            x: node.x,
                            y: node.y,
                            category: node.category,
                            value: node.value,
                            symbol: node.symbol
                        }
                        let temp = {
                            graphId: this.currentGraphId,
                            name: node.name,
                            entityProperties: entityProperties,
                        }
                        newNodes.push(temp)
                    }
                    this.editNodes(newNodes)
                    this.set_tempAxs([])
                }
            },
            alterMode(mode) {
                this.$emit("chChangeMode", mode)
            },
            showGraphDetail() {
                this.$emit("chChangeGraphDetailModalVisible", true)
            },
            showModifyViewModal() {
                this.$emit("chChangeModifyViewModalVisible", true)
            },
            enableDraggable() {
                if (this.inDragFlag) {
                    let that = this
                    let dragNodes = this.graph.nodes.filter((item, index) => {
                        return item.x !== this.saveNodes[index].x || item.y !== this.saveNodes[index].y
                    }).map(item => {
                        let readonlyProp = {
                            graphId: that.currentGraphId,
                            name: item.name,
                        }
                        let delItem = {
                            symbolSize: item.symbolSize,
                            x: item.x,
                            y: item.y,
                            category: item.category,
                            value: item.value,
                            symbol: item.symbol
                        }
                        return {
                            ...readonlyProp,
                            entityProperties: {
                                ...delItem
                            }
                        }
                    })
                    if (dragNodes.length > 0)
                        this.editNodes(dragNodes)
                    this.$emit("chChangeSnackbarVisible", false)
                    this.$emit("chChangeGraphicZ", 0)

                } else {
                    this.saveNodes = this.graph.nodes.map(node => {
                        return {
                            ...node
                        }
                    })
                    this.$emit("chChangeSnackbarText", "拖拽模式，再按一次返回")
                    this.$emit("chChangeSnackbarVisible", true, -1)
                    this.$emit("chChangeGraphicZ", 233)
                }
                this.inDragFlag ^= 1
            },
            async confirmDeleteGraph() {
                const res = await this.deleteGraph(this.currentGraphId)
                if (!res) { //删除成功才刷新页面
                    this.myChart.clear()
                    this.deleteGraphConfirm = false
                    this.$router.push({name: 'GraphList'})
                }
                this.deleteGraphConfirm = false
            },
            exportImg() {
                if (this.myChart) {
                    let exportE = document.createElement('a')
                    exportE.style.display = 'none'
                    // 获得svgURL
                    let svg = this.myChart.getDataURL()
                    let img = new Image()
                    let canvas = document.createElement('canvas')
                    let ctx = canvas.getContext('2d')
                    img.setAttribute("src", svg)
                    img.onload = () => {
                        // 将svg绘制在canvas上
                        canvas.width = img.width
                        canvas.height = img.height
                        //填充白色背景
                        ctx.fillStyle = '#ffffff'
                        ctx.fillRect(0, 0, canvas.width, canvas.height)
                        //绘制图谱内容
                        ctx.drawImage(img, 0, 0)

                        // 将canvas转成pngURL
                        exportE.href = canvas.toDataURL('image/png')
                        exportE.download = `nekoCOIN_${new Date().valueOf()}.png`
                        // 触发点击
                        document.body.appendChild(exportE)
                        exportE.click()
                        // 移除
                        document.body.removeChild(exportE)
                    }
                }
            },
            //去除label标签
            getNoLabelGraph() {
                let noLabelGraph = {
                    nodes: [],
                    links: [],
                    categories: this.graph.categories
                }
                this.graph.nodes.forEach((node) => {
                    noLabelGraph.nodes.push({
                        category: node.category,
                        name: node.name,
                        symbolSize: node.symbolSize,
                        x: node.x,
                        y: node.y,
                        value: node.value,
                    })
                })
                this.graph.links.forEach((link) => {
                    noLabelGraph.links.push({
                        source: link.source,
                        target: link.target,
                        value: link.value,
                    })
                })
                return noLabelGraph
            },
            exportJSON() {
                let content = JSON.stringify(this.getNoLabelGraph(), null, 2)//TODO
                //更改graph来源
                let exportE = document.createElement('a')
                exportE.style.display = 'none'
                exportE.download = `nekoCOIN_${new Date().valueOf()}.json`
                // 字符内容转变成blob地址
                let blob = new Blob([content])
                exportE.href = URL.createObjectURL(blob)
                // 触发点击
                document.body.appendChild(exportE)
                exportE.click()
                // 移除
                document.body.removeChild(exportE)
            },
            exportXML() {
                const xml2js = require('xml2js')
                let builder = new xml2js.Builder()
                let dataXml = builder.buildObject(this.getNoLabelGraph())
                let exportE = document.createElement('a')
                exportE.style.display = 'none'
                exportE.download = `nekoCOIN_${new Date().valueOf()}.xml`
                // 字符内容转变成blob地址
                let blob = new Blob([dataXml])
                exportE.href = URL.createObjectURL(blob)
                // 触发点击
                document.body.appendChild(exportE)
                exportE.click()
                // 然后移除
                document.body.removeChild(exportE)
            },
        }
    }
</script>