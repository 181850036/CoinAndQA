<template>
    <v-app>
        <v-main class="grey lighten-3">
            <v-snackbar
                    v-model="snackbarVisible"
                    :timeout=this.snackbarTimeout
            >
                {{text}}
            </v-snackbar>
            <v-container fluid style="max-width: 90%">
                <v-row>
                    <v-col
                            cols="12"
                            sm="2"
                    >
                        <GraphInfoModal
                            :graph="graph"></GraphInfoModal>
                        <ModifyViewModal
                                @chChangeModifyViewModalVisible="faChangeModifyViewModalVisible"
                                :modifyViewModalVisible=this.modifyViewModalVisible></ModifyViewModal>

                    </v-col>

                    <v-col
                            cols="12"
                            sm="8"
                    >
                        <v-sheet
                                :elevation="3"
                                min-height="700px"
                                rounded="lg"
                        >
                            <v-progress-circular
                                    style="position:absolute;left:0; right:0; top:0; bottom:0;margin:auto;"
                                    :size="100"
                                    color="info"
                                    :width="7"
                                    v-show="loadingVisible"
                                    indeterminate>
                                正在加载
                            </v-progress-circular>
                            <!-- 放置关系图 -->
                            <div id="myChart" style="width: 100%;height:700px"></div>
                        </v-sheet>
                        <v-btn
                            @click="showQAModal"
                            class="mx-2"
                            fab
                            dark
                            large
                            color="purple"
                            style="margin-top: 15px"
                          >
                          <v-icon dark>
                            mdi-android
                          </v-icon>
                        </v-btn>
                    </v-col>

                    <v-col
                            cols="12"
                            sm="2"
                    >
                        <OptionModal
                                @chChangeGraphDetailModalVisible="faChangeGraphDetailModalVisible"
                                @chChangeModifyViewModalVisible="faChangeModifyViewModalVisible"
                                @chChangeSnackbarVisible="faChangeSnackbarVisible"
                                @chChangeSnackbarText="faChangeSnackbarText"
                                @chChangeGraphicZ="faChangeGraphicZ"
                                @chChangeMode="faChangeMode"
                                :mode=this.mode
                        ></OptionModal>
                        <InfoFieldModal
                                ref="infoFieldModal"
                                @chChangeSnackbarText="faChangeSnackbarText"
                                @chChangeSnackbarVisible="faChangeSnackbarVisible"
                                :modifyNodeValueSymbolSize="this.modifyNodeValueSymbolSize"
                                :obj=this.obj
                                :nodeInfoVisible=this.nodeInfoVisible
                                :edgeInfoVisible=this.edgeInfoVisible></InfoFieldModal>
                    </v-col>

                </v-row>
            </v-container>
        </v-main>
        <GraphDetailModal
                @chChangeGraphDetailModalVisible="faChangeGraphDetailModalVisible"
                :graphDetailModalVisible=this.graphDetailModalVisible
        ></GraphDetailModal>
        <AddGraphModal></AddGraphModal>
        <AddNodeModal
                @chChangeAddNodeModalVisible="faChangeAddNodeModalVisible"
                :message="axis"
                :addNodeModalVisible=this.addNodeModalVisible
        ></AddNodeModal>
        <QAModal
            :QAModalVisible=this.QAModalVisible
            @chChangeQAModalVisible="faChangeQAModalVisible"
        ></QAModal>
        <vue-context-menu :context-menu-data="nodeContextMenuData"
                          v-show="nodeContextMenuVisible"
                          @addEdge="toAddEdgeState"
                          @delete="delNode"
        ></vue-context-menu>
        <vue-context-menu :context-menu-data="edgeContextMenuData"
                          v-show="edgeContextMenuVisible"
                          @delete="delEdge"
        ></vue-context-menu>
        <vue-context-menu :context-menu-data="spaceContextMenuData"
                          v-show="spaceContextMenuVisible"
                          @createNode="createNode"
        ></vue-context-menu>
    </v-app>
</template>

<script>

    import GraphDetailModal from './EditGraphModal/graphDetailModal'
    import AddGraphModal from '../GraphList/addGraphModal'
    import AddNodeModal from "./EditGraphModal/addNodeModal"
    import QAModal from "./SideModal/QAModal"
    import OptionModal from "./SideModal/optionModal"
    import InfoFieldModal from "./SideModal/infoFieldModal"
    import GraphInfoModal from "./SideModal/graphInfoModal"
    import ModifyViewModal from "./SideModal/modifyViewModal"
    import {mapGetters, mapMutations, mapActions} from 'vuex'

    let echarts = require('echarts/lib/echarts')
    require('echarts/lib/chart/graph')
    require('echarts/lib/component/graphic')
    //下面的是需要的提示框
    require('echarts/lib/component/tooltip')
    require('echarts/lib/component/title')
    require('echarts/lib/component/legend')

    export default {
        data: () => ({
            nodeInfoVisible: false,
            edgeInfoVisible: false,
            graphDetailModalVisible: false,
            addNodeModalVisible: false,
            QAModalVisible:false,
            modifyViewModalVisible:false,
            colorVisible: false,
            loadingVisible: true,
            nodeContextMenuVisible: false,
            edgeContextMenuVisible: false,
            spaceContextMenuVisible: false,
            snackbarVisible: false,
            snackbarTimeout: 4000,
            addEdgeState: false,
            mode: 'none',
            graphicZ: 0,
            roamNumber: 1,

            sourceNode: '',
            text: "请点击目标节点",
            nodeContextMenuData: {
                menuName: 'nodeContextMenu',
                axis: {
                    x: null,
                    y: null
                },
                menulists: [
                    {
                        fnHandler: 'addEdge',
                        btnName: '添加关系'
                    },
                    {
                        fnHandler: 'delete',
                        btnName: '删除',
                    }],
            },
            edgeContextMenuData: {
                menuName: 'edgeContextMenu',
                axis: {
                    x: null,
                    y: null
                },
                menulists: [{
                    fnHandler: 'delete',
                    btnName: '删除',
                }],
            },
            spaceContextMenuData: {
                menuName: 'spaceContextMenu',
                axis: {
                    x: null,
                    y: null
                },
                menulists: [{
                    fnHandler: 'createNode',
                    btnName: '创建节点',
                }],
            },
            obj: {
                data: {
                    name: '',
                    graphId: '',
                    category: '',
                    symbol: '',
                    source: '',
                    target: '',
                    value: '',
                }
            },
            axis: [],

        }),
        components: {
            QAModal,
            InfoFieldModal,
            OptionModal,
            GraphDetailModal,
            AddGraphModal,
            AddNodeModal,
            GraphInfoModal,
            ModifyViewModal
        },
        computed: {
            ...mapGetters([
                'addEdgeParams',
                'editNodeInfoParams',
                'editRelationInfoParams',
                'graph',
                'getNowGraph',
                'currentGraphId',
                'currentGraphName',
                'myChart',
                'tempAxs'
            ]),

        },
        watch: {
            graphicZ: function (val) {
                if (val)
                    this.initDrag()
                else {
                    if (this.myChart) {
                        this.myChart.clear()
                        this.setChart()
                    }
                }
            },
            'graph':{
                handler:function () {
                    //监听graph变化刷新echarts组件
                    this.setChart()
                },deep:true
            }
        },
        mounted() {
            let temp = JSON.parse(JSON.stringify(this.graph))
            localStorage.nowGraph = JSON.stringify(temp)
            //初始化echarts
            this.set_myChart(echarts.init(document.querySelector('#myChart')))
            if (this.$route.params.currentGraphId !== undefined) {
                this.set_currentGraphId(this.$route.params.currentGraphId)
            }
            if (this.$route.params.currentGraphName !== undefined) {
                this.set_currentGraphName(this.$route.params.currentGraphName)
            }
            //等待从接口获取图数据
            this.getGraph(this.currentGraphId)
            //获取后设置加载进度条不可见
            this.loadingVisible = false
            const that = this

            //绑定窗口调整时echarts能跟随改变组件大
            window.addEventListener('resize', () => {
                if (that.myChart !== undefined) {
                    that.myChart.resize()
                    that.updatePosition(null)
                }
            })


            this.myChart.on('graphRoam', this.updatePosition)
            //绑定右键方法
            this.myChart.on('contextmenu', 'series', function (params) {
                event.preventDefault() //阻止默认事件
                that.obj = params
                //如果点击到实际内容
                if (params.seriesType === 'graph') {
                    const x = event.clientX
                    const y = event.clientY
                    if (params.dataType === 'node') {
                        //显示右键菜单，获取坐标
                        that.nodeContextMenuData.axis = {x, y}
                        that.nodeContextMenuVisible = true
                        //显示点操作框
                        that.alterToNodeInfo()
                    } else if (params.dataType === 'edge') { //处理边右键
                        that.edgeContextMenuData.axis = {x, y}
                        that.edgeContextMenuVisible = true
                        that.alterToEdgeInfo()
                    }
                }
            })
            //绑定左键点击
            this.myChart.on('click', 'series', function (params) {
                event.preventDefault()
                if (params.seriesType === 'graph') {
                    if (params.dataType === 'node') {
                        //如果是添加新边的点击动作
                        if (that.addEdgeState) {
                            that.addEdgeState = false
                            //添加该新边
                            that.addEdge({
                                source: that.sourceNode,
                                target: params.data.name,
                            })
                        } else { //显示点信息框
                            that.obj = params
                            that.alterToNodeInfo()
                        }
                    } else if (params.dataType === 'edge') {
                        that.obj = params
                        that.alterToEdgeInfo()
                    }
                }
            })
            //绑定空白处
            this.myChart.getZr().on('click', function (event) {
                that.$refs.infoFieldModal.resetBufferEdit()
                if (!event.target) {
                    that.closeInfo()
                }
            })
            this.myChart.getZr().on('contextmenu', function (event) {
                event.event.preventDefault()
                that.resetContextMenu()
                that.$refs.infoFieldModal.resetBufferEdit()
                if (!event.target) {
                    that.closeInfo()
                    //坐标转换，提供给新增节点功能
                    that.axis = that.myChart.convertFromPixel({
                        seriesIndex: 0,
                        xAxisIndex: 0
                    }, [event.offsetX, event.offsetY])
                    //调整右键菜单坐标
                    const x = event.event.clientX
                    const y = event.event.clientY
                    that.spaceContextMenuData.axis = {x, y}
                    that.spaceContextMenuVisible = true
                }
            })
        },
        methods: {
            ...mapMutations([
                'set_currentGraphId',
                'set_currentGraphName',
                'set_graph',
                'set_addNodeParams',
                'set_addEdgeParams',
                'set_myChart',
                'addEdgeToGraph',
                'save_to_tempAxs',
                'set_tempAxs',
                'set_graphNodeXY'
            ]),
            ...mapActions([
                'uploadGraphFile',
                'getGraph',
                'deleteNode',
                'buildRelation',
                'deleteRelation',
                'deleteAll'
            ]),
            //子组件触发的方法区
            faChangeQAModalVisible(visible){
              this.QAModalVisible=visible
            },
            faChangeMode(mode) {
              this.mode = mode
              if (mode==="none"&&this.tempAxs.length!==0) {
                  for(let i=0;i<this.graph.nodes.length;i++){
                    let node=this.graph.nodes[i]
                    let tempAx=this.tempAxs.find(tempAx =>tempAx.name === node.name)
                    if(tempAx!==undefined) {
                      node.x = tempAx.x
                      node.y = tempAx.y
                    }
                  }
                  this.set_tempAxs([])
              }
              if (mode === "arrangement") {
                if (this.tempAxs.length === 0) {
                  this.mode = "none"
                  let nodesLen = this.graph.nodes.length
                  let linksLen = this.graph.links.length
                  let categoryLen = this.graph.categories.length
                  let newNodesGroups = []
                  for (let i = 0; i < nodesLen; i++) {
                    let temp = {name: this.graph.nodes[i].name, x: this.graph.nodes[i].x, y: this.graph.nodes[i].y}
                    this.save_to_tempAxs(temp)
                  }
                  for (let i = 0; i < categoryLen; i++)
                    newNodesGroups.push([])
                  let minX = Number.MAX_SAFE_INTEGER, minY = Number.MAX_SAFE_INTEGER, maxX = Number.MIN_SAFE_INTEGER,
                      maxY = Number.MIN_SAFE_INTEGER
                  for (let i = 0; i < nodesLen; i++) {
                    let node = this.graph.nodes[i]
                    minX = Math.min(node.x, minX)
                    minY = Math.min(node.y, minY)
                    maxX = Math.max(node.x, maxX)
                    maxY = Math.max(node.x, maxY)
                    let temp = {node: node}
                    let linkCount = 0
                    for (let j = 0; j < linksLen; j++)
                      if (this.graph.links[j].source === temp.node.name || this.graph.links[j].target === temp.node.name)
                        linkCount++
                    temp.linkCount = linkCount
                    newNodesGroups[temp.node.category].push(temp)
                  }
                  let newNodes = []
                  let gapX = (maxX - minX) / (categoryLen - 1)
                  for (let i = 0; i < categoryLen; i++) {
                    newNodesGroups[i].sort((a, b) => b.linkCount - a.linkCount)
                    let gapY = newNodesGroups[i].length <= 1 ? 0 : (maxY - minY) / (newNodesGroups[i].length - 1)
                    for (let j = 0; j < newNodesGroups[i].length; j++) {
                      let node = newNodesGroups[i][j].node
                      node.x = minX + i * gapX
                      node.y = minY + j * gapY
                      newNodes.push(node)
                    }
                  }
                  this.graph.nodes = newNodes
                }
              }
              else
                this.setChart()
            },
            faChangeSnackbarText(message) {
                this.text = message
            },
            faChangeSnackbarVisible(visible, timeout = 4000) {
                this.snackbarVisible = visible
                this.snackbarTimeout = timeout
            },
            faChangeGraphDetailModalVisible(visible) {
                this.graphDetailModalVisible = visible
            },
            faChangeAddNodeModalVisible(visible) {
                this.addNodeModalVisible = visible
            },
            faChangeModifyViewModalVisible(visible){
                this.modifyViewModalVisible=visible
            },
            faChangeGraphicZ(val) {
                this.graphicZ = val
            },
            showQAModal(){
              this.QAModalVisible=true;
            },
            //清空右键状态
            resetContextMenu() {
                this.nodeContextMenuVisible = false
                this.edgeContextMenuVisible = false
                this.spaceContextMenuVisible = false
            },
            //显示节点信息窗
            alterToNodeInfo() {
                this.nodeInfoVisible = true
                this.edgeInfoVisible = false
            },
            //显示边信息窗
            alterToEdgeInfo() {
                this.edgeInfoVisible = true
                this.nodeInfoVisible = false
            },
            //关闭信息窗口
            closeInfo() {
                this.nodeInfoVisible = false
                this.edgeInfoVisible = false
            },
            modifyNodeValueSymbolSize(node, opt, num = 1) {
                if (opt === '+') {
                    node.value += num
                } else
                    node.value -= num
                node.symbolSize = 1 / 2 * node.value + 10
            },
            createNode() {
                this.addNodeModalVisible = true
            },
            delNode() {
                this.deleteNode({name: this.obj.data.name, graphId: this.currentGraphId})
                for (let i = 0; i < this.graph.links.length; i++) {
                    if (this.graph.links[i].source === this.obj.data.name || this.graph.links[i].target === this.obj.data.name) {
                        let tar = ''
                        if (this.graph.links[i].source === this.obj.data.name)
                            tar = this.graph.nodes.find(item => item.name === this.graph.links[i].target)

                        else
                            tar = this.graph.nodes.find(item => item.name === this.graph.links[i].source)
                        this.modifyNodeValueSymbolSize(tar, '-')
                        this.graph.links.splice(i, 1)
                        i--
                    }
                }
                for (let i = 0; i < this.graph.nodes.length; i++) {
                    if (this.graph.nodes[i].name === this.obj.data.name) {
                        this.graph.nodes.splice(i, 1)
                        break
                    }
                }
            },
            addEdge(data) {
                let src = this.graph.nodes.find(item => item.name === data.source)
                let tar = this.graph.nodes.find(item => item.name === data.target)
                this.modifyNodeValueSymbolSize(src, '+')
                this.modifyNodeValueSymbolSize(tar, '+')
                this.addEdgeToGraph(data)
                data.graphId = this.currentGraphId//TODO
                this.set_addEdgeParams(data)
                this.buildRelation()
            },
            toAddEdgeState() {
                this.addEdgeState = true
                this.sourceNode = this.obj.data.name
                this.text = "请点击目标节点"
                this.snackbarVisible = true
            },
            delEdge() {
                this.deleteRelation({
                    graphId: this.currentGraphId,
                    sourceName: this.obj.data.source,
                    targetName: this.obj.data.target
                })
                for (let i = 0; i < this.graph.links.length; i++) {
                    if (this.graph.links[i].source === this.obj.data.source && this.graph.links[i].target === this.obj.data.target) {
                        let srcNode = this.graph.nodes.find(item => item.name === this.obj.data.source)
                        let tarNode = this.graph.nodes.find(item => item.name === this.obj.data.target)
                        this.modifyNodeValueSymbolSize(srcNode, '-')
                        this.modifyNodeValueSymbolSize(tarNode, '-')
                        this.graph.links.splice(i, 1)
                        break
                    }
                }

            },
            updatePosition(event) {
                if (this.graphicZ === 233) {
                    if (event && event.type === 'graphroam' && event.zoom !== undefined) {
                        this.roamNumber *= event.zoom
                    }
                    let that = this
                    this.myChart.setOption({
                        // eslint-disable-next-line no-unused-vars
                        graphic: echarts.util.map(that.graph.nodes, function (item, dataIndex) {
                            let xy = that.myChart.convertToPixel({seriesIndex: 0}, [item.x, item.y])
                            return {
                                x: xy[0],
                                y: xy[1],
                                shape: {
                                    r: item.symbolSize / 2 * that.roamNumber
                                }
                            }
                        })
                    })
                }
            },
            setChart() {
                let ops={}
                let newLabelShow=13
                if (localStorage.viewOptions !== undefined) {
                    let op = JSON.parse(localStorage.viewOptions)
                    ops = {...op}
                    let min = 1, max = 20
                    //放缩
                    let tempNum = (ops.viewLabelRange - min) / (max - min)
                    //放缩到[0,maxx]
                    let maxx = 0
                    this.graph.nodes.forEach(node => {
                        if (node.value > maxx)
                            maxx = node.value
                    })
                    newLabelShow = 0 + tempNum * (maxx - 0)
                }

                //显示大于13的节点名
                this.graph.nodes.forEach((node) => {
                    node.label = {
                        show: node.value > newLabelShow
                    }
                })
                //显示大节点间的关系名
                this.graph.links.forEach((link) => {
                    link.label = {
                        show: this.graph.nodes.find(item => item.name === link.source).value > newLabelShow &&
                            this.graph.nodes.find(item => item.name === link.target).value > newLabelShow,
                        formatter: '{@value}'
                    }
                })
                let option = {
                    title: {
                        text: 'NekoCOIN System',
                        subtext: 'Default layout',
                        top: 'bottom',
                        left: 'center'
                    },
                    tooltip: {
                        position:'bottom',
                        // eslint-disable-next-line no-unused-vars
                        formatter:function (params,ticket,callback) {
                            // eslint-disable-next-line no-unused-vars
                            let text={
                                'cause':'原因',
                                'influence':'影响',
                                'process':'过程',
                                'gender':'性别',
                                'nativePlace':'籍贯',
                                'anothername':'字',
                                'attachToDynasty':'朝代',
                                'attachToMembershipGroup':'阵营',
                                'birthday':'出生时间',
                                'deadTime':'死亡时间',
                                'son':'儿子',
                                'spouse':'配偶',
                                'father':'父亲',
                                'mother':'母亲',
                                'alias':'别名',
                                'officialTitle':'官职',
                                'feudaTitle':'封号',
                                'biography':'传记',
                                'locNameNow':'现在位于',
                                'locatedin':'位于',
                                'existingPeriod':'存在时期',
                                'province':'省份',
                                'otherName':'别名',
                                'sameAs':'同于'
                            }
                            if(params.dataType==='edge') {
                                return params.name + '\t\t<b>' + params.value + '</b>'
                            }
                            function strInsert(str, length) {
                                if(str[0]==='[' && str[str.length-1]===']')
                                    str=str.substr(1,str.length-2).split(',').join('、')
                                if(str.length>600)length+=20;
                                let reg = new RegExp("([^\x00-\xff]|[\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b]|" +
                                    "[【】]|.|[0-9]){1," + length + "}", "g");
                                let ma = str.match(reg);
                                return ma.join("</br>");
                            }
                            let ret='<b>'+params.name+'</b></br>'
                            for(let key in params.data.contextProperties){
                                if(key in text){
                                    let tmp=strInsert(params.data.contextProperties[key],60)
                                    if(tmp.length>700){
                                        tmp='<span style="font-size: 1px; ">'+tmp+'</span>'
                                    }
                                    ret+='<b>'+text[key]+'</b> : '+tmp+'</br>'
                                }
                                //
                            }
                            return ret
                        }
                    },
                    legend: [{
                        // selectedMode: 'single',
                        data: this.graph.categories.map(function (a) {
                            return a.name
                        })
                    }],
                    animationEasingUpdate: false,
                    // animationDuration: 1500,
                    // animationEasingUpdate: 'quinticInOut',
                    series: [
                        {
                            id: 'a',
                            name: this.currentGraphName,
                            type: 'graph',
                            layout: this.mode,
                            force: {
                                repulsion: 500,
                                gravity: 0.1
                            },
                            animationThreshold: 300,
                            animationDuration: 1500,
                            animationEasingUpdate: 'cubicInOut',
                            data: this.graph.nodes,
                            links: this.graph.links,
                            categories: this.graph.categories,
                            edgeSymbol:['none','arrow'],
                            roam: true,
                            label: {
                                position: 'right',
                                formatter: '{b}'
                            },
                            lineStyle: {
                                color: 'source',
                                curveness: 0.3,
                            },
                            emphasis: {
                                focus: 'adjacency',
                                lineStyle: {
                                    width: 10
                                }
                            },
                        }
                    ],
                    media: [
                        {
                            option: {
                                series: [{
                                    left: 'center',
                                    top: 'middle'
                                }]
                            }
                        }
                    ]
                }
                this.myChart.setOption(option)
                if(localStorage.viewOptions !== undefined&&!this.modifyViewModalVisible){
                    this.myChart.setOption({
                        series:
                            [
                                {
                                    force: {
                                        repulsion: ops.viewRepulsion
                                    },
                                    label: {
                                        fontSize: ops.viewFontSize
                                    }
                                }
                            ]
                    })
                    this.graph.nodes.forEach(node => {
                        if (node.originSymbolSize === undefined)
                            node.originSymbolSize = node.symbolSize
                        node.symbolSize = node.originSymbolSize * ops.viewSymbolSize / 10
                    })
                }
            },
            initDrag() {
                let that = this
                this.myChart.setOption({
                    graphic: echarts.util.map(that.graph.nodes, function (dataItem, dataIndex) {
                        let xy = that.myChart.convertToPixel({seriesIndex: 0}, [dataItem.x, dataItem.y])
                        return {
                            type: 'circle',//可以设置与node形状保持一致T
                            //TODO
                            focus: 'self',
                            shape: {
                                r: dataItem.symbolSize / 2 * that.roamNumber
                            },
                            x: xy[0],
                            y: xy[1],
                            invisible: true,
                            draggable: true,
                            z: that.graphicZ,
                            ondrag: echarts.util.curry(onPointDragging, dataIndex),
                        }
                    })
                })

                function onPointDragging(dataIndex) {
                    let tempPos = that.myChart.convertFromPixel({seriesIndex: 0}, [this.x, this.y])
                    that.set_graphNodeXY({
                        index:dataIndex,
                        x:tempPos[0],
                        y:tempPos[1]
                    })
                    that.updatePosition(null)
                }
            }
        }
    }

</script>
