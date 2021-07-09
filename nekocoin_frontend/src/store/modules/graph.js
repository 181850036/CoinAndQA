import {uploadGraphFileAPI, getGraphJSONAPI, deleteGraphAPI} from '../../api/graph'
import {message} from 'ant-design-vue'
import {deleteRelationAPI, editRelationInfoAPI, buildRelationAPI} from "../../api/relationship"
import {
    addNodeAPI,
    createGraphAPI,
    deleteAllAPI,
    deleteNodeAPI,
    editNodeInfoAPI,
    insertCategoryAPI,
    editNodesAPI
} from "../../api/node"
import {getMultiJumpAnswerAPI,getSingleJumpAnswerAPI} from "../../api/getAnswer";

const graph = {
    state: {
        source:null,
        currentGraphId: '',
        currentGraphName: '',
        nowGraph: [],
        graph: {nodes: [], links: [], categories: [], graphProperties: {}},
        tempAxs: [],
        addGraphModalVisible: false,
        uploadGraphFileLoading: false,
        answer:{},
        addNodeParams: {
            graphId: '',
            name: '',
            entityProperties: {
                symbolSize: 0,
                x: 0,
                y: 0,
                value: 0,
                category: 0,
                symbol: '圆形'
            },
        },
        addEdgeParams: {
            graphId: '',
            source: '',
            target: '',
        },
        editRelationInfoParams: {
            graphId: '',
            source: '',
            target: '',
            newVal: {
                source: '',
                target: '',
                value: ''
            }
        },
        editNodeInfoParams: {},
        symbolSelectList: [
            {
                text: '圆形',
                value: 'circle',
                icon: 'mdi-circle'
            },
            {
                text: '三角形',
                value: 'triangle',
                icon: 'mdi-triangle'
            },
            {
                text: '矩形',
                value: 'rect',
                icon: 'mdi-square'
            },
            {
                text: '圆角矩形',
                value: 'roundRect',
                icon: 'mdi-square-rounded'
            },
            {
                text: '菱形',
                value: 'diamond',
                icon: 'mdi-cards-diamond'
            },
        ],
        myChart: {}

    },
    mutations: {
        set_source(state,data){
            state.source=data
        },
        set_answer(state,data){
            state.answer=data
        },
        set_addGraphModalVisible(state, data) {
            state.addGraphModalVisible = data
        },
        set_uploadGraphFileLoading(state, data) {
            state.uploadGraphFileLoading = data
        },
        set_currentGraphId(state, data) {
            state.currentGraphId = data
        },
        set_currentGraphName(state, data) {
            state.currentGraphName = data
        },
        set_graph(state, data) {
            state.graph = data
        },
        set_graphNodeOpacity(state, data) {
            state.graph.nodes[data.index] = data.item
        },
        set_graphLinkOpacity(state, data) {
            state.graph.links[data.index] = data.item

        },
        set_graphNodeXY(state, data) {
            state.graph.nodes[data.index].x = data.x
            state.graph.nodes[data.index].y = data.y
        },
        save_to_tempAxs(state, data) {
            state.tempAxs.push(data)
        },
        set_tempAxs(state, data) {
            state.tempAxs = data
        },
        set_myChart(state, data) {
            state.myChart = data
        },
        set_addNodeParams: function (state, data) {
            state.addNodeParams = data //TODO
        }
        ,
        set_addEdgeParams: function (state, data) {
            state.addEdgeParams = {
                ...state.addEdgeParams,
                ...data
            }
        },
        set_editNodeInfoParams: function (state, data) {
            state.editNodeInfoParams = {
                ...state.editNodeInfoParams,
                ...data
            }
        },
        set_editRelationInfoParams: function (state, data) {
            state.editRelationInfoParams = {
                ...state.editRelationInfoParams,
                ...data
            }
        },
        addNodeToGraph(state, data) {
            state.graph.nodes.push(data)
        },
        addEdgeToGraph(state, data) {
            state.graph.links.push(data)
        },
        addCategoryToGraph(state, data) {
            state.graph.categories.push(data)
        }
    },
    actions: {
        // eslint-disable-next-line no-unused-vars
        getMultiJumpAnswer:async({commit,state},path) =>{
            const res= await getMultiJumpAnswerAPI(path,state)
                // eslint-disable-next-line no-unused-vars
                .catch(error=>{
                    if(error.message=='终止请求') {
                        commit('set_answer','终止请求')
                    }
                    else{
                        message.error('服务出错')
                        commit('set_answer','服务出错')
                    }
                })
            if(res)
                commit('set_answer',res)

        },
        // eslint-disable-next-line no-unused-vars
        getSingleJumpAnswer:async({commit,state},path) =>{
            const res= await getSingleJumpAnswerAPI(path,state)
                // eslint-disable-next-line no-unused-vars
                .catch(error=>{
                    if(error.message=='终止请求') {
                        commit('set_answer','终止请求')
                    }
                    else{
                        message.error('服务出错')
                        commit('set_answer','服务出错')
                    }
                })
            if(res)
                commit('set_answer',res)

        },
        addGraph: async ({commit, state}) => {
            const res = await createGraphAPI(state.addNodeParams)
            if (res) {
                message.success('新建图谱成功！')
                commit('set_addNodeParams', {
                    name: '',
                    symbolSize: 0,
                    x: 0,
                    y: 0,
                    value: 0,
                    category: 0,
                    symbol: '圆形'
                })
            } else {
                message.error('新建图谱失败！')
            }
        },
        buildRelation: async ({commit, state}) => {
            const res = await buildRelationAPI(state.addEdgeParams)
            if (res) {
                message.success('上传成功！')
                commit('set_addEdgeParams', {
                    source: '',
                    target: ''
                })
            } else {
                message.error('上传失败！')
            }
        },
        // eslint-disable-next-line no-unused-vars
        deleteRelation: async ({commit, state}, data) => {
            const res = await deleteRelationAPI(data.graphId, data.sourceName, data.targetName)
            if (res) {
                message.success('删除成功！')
            } else {
                message.error('删除失败！')
            }
        },
        editRelationInfo: async ({commit, state}) => {
            const res = await editRelationInfoAPI(state.editRelationInfoParams)
            if (res) {
                message.success('编辑成功！')
                commit('set_editRelationInfoParams', {
                    graphId: '',
                    source: '',
                    target: '',
                    newVal: {
                        source: '',
                        target: '',
                        value: ''
                    }
                })
            } else {
                message.error('编辑失败！')
            }
        },
        addNode: async ({commit, state}) => {
            const res = await addNodeAPI(state.addNodeParams)
            if (res) {
                message.success('创建成功！')
                commit('set_addNodeParams', {
                    graphId: '',
                    name: '',
                    entityProperties: {
                        symbolSize: 0,
                        x: 0,
                        y: 0,
                        value: 0,
                        category: 0,
                        symbol: '圆形'
                    },
                })
            } else {
                message.error('创建失败！')
            }
        },
        // eslint-disable-next-line no-unused-vars
        deleteGraph: async ({commit, state}, graphId) => {
            const res = await deleteGraphAPI(graphId)
            if (res) {
                message.success('删除成功！')
            } else {
                message.error('删除失败！')
            }
        },

        // eslint-disable-next-line no-unused-vars
        editNodes: async ({commit, state}, data) => {
            const res = await editNodesAPI(data)
            if (res) {
                message.success('更新成功！')
            } else {
                message.error('更新失败！')
            }
        },
        // eslint-disable-next-line no-unused-vars
        insertCategory: async ({commit, state}, data) => {
            const res = await insertCategoryAPI(data)
            if (res) {
                message.success('新增类目成功！')
            } else {
                message.error('新增类目失败！')
            }
        },
        // eslint-disable-next-line no-unused-vars
        deleteNode: async ({commit, state}, name) => {
            const res = await deleteNodeAPI(name)
            if (res) {
                message.success('删除成功！')
            } else {
                message.error('删除失败！')
            }
        },
        // eslint-disable-next-line no-unused-vars
        deleteAll: async ({commit, state}) => {
            const res = await deleteAllAPI()
            if (res) {
                message.success('删除成功！')
            } else {
                message.error('删除失败！')
            }
            return res === null
        },
        editNodeInfo: async ({commit, state}) => {
            const res = await editNodeInfoAPI(state.editNodeInfoParams)
            if (res) {
                message.success('编辑成功！')
                commit('set_editNodeInfoParams', {
                    graphId: '',
                    name: '',
                    newVal: {
                        name: '',
                        category: '',
                        symbol: ''
                    }
                })
            } else {
                message.error('编辑失败！')
            }
        },
        // eslint-disable-next-line no-unused-vars
        uploadGraphFile: async ({commit, state}, data) => {
            const res = await uploadGraphFileAPI(data)
            if (res) {
                message.success('上传成功！')
                commit('set_addGraphModalVisible', false)
            } else {
                message.error('上传失败！')
            }
            commit('set_uploadGraphFileLoading', false)
        },
        // eslint-disable-next-line no-unused-vars
        getGraph: async ({commit, state}, data) => {
            const res = await getGraphJSONAPI(data)
            if (res) {
                //TODO
                //统一接口
                let graphVal = {
                    nodes: [],
                    links: [],
                    categories: [],
                    graphProperties: {}
                }
                graphVal.nodes = res.entities.map(node => {
                    return {
                        name: node.name,
                        ...node.entityProperties,
                        contextProperties: node.contextProperties
                    }
                })
                graphVal.links = res.links.map(link => {
                    return {
                        ...link,
                    }
                })
                graphVal.categories = res.categories.map(category => {
                    return {
                        ...category
                    }
                })
                graphVal.graphProperties = {
                    ...res.graphProperties
                }
                commit('set_graph', graphVal)
            }
        },
    }
}
export default graph
