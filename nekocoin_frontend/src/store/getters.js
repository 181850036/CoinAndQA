const getters={
  //graphList
  graphList:state=>state.graphList.graphList,
  set_graphList:state=>state.graphList.set_graphList,
  //graph
  source:state=>state.graph.source,
  set_source:state=>state.graph.set_source,
  answer:state=>state.graph.answer,
  set_answer:state=>state.graph.set_answer,
  currentGraphId:state=>state.graph.currentGraphId,
  set_currentGraphId:state=>state.graph.set_currentGraphId,
  currentGraphName:state=>state.graph.currentGraphName,
  set_currentGraphName:state=>state.graph.set_currentGraphName,
  graph:state=>state.graph.graph,
  tempAxs: state=>state.graph.tempAxs,
  set_graph:state=>state.graph.set_graph,
  set_graphNodeXY:state=>state.graph.set_graphNodeXY,
  set_tempAxs:state=>state.graph.set_tempAxs,
  save_to_tempAxs:state=>state.graph.save_to_tempAxs,
  myChart:state=>state.graph.myChart,
  set_myChart:state=>state.graph.set_myChart,
  addGraphModalVisible: state=>state.graph.addGraphModalVisible, //header与modal共用，使用vuex
  set_addGraphModalVisible: state=>state.graph.set_addGraphModalVisible,
  uploadGraphFileLoading:state=>state.graph.uploadGraphFileLoading,
  set_uploadGraphFileLoading:state=>state.graph.set_uploadGraphFileLoading,
  addNodeParams:state=>state.graph.addNodeParams,
  addEdgeParams:state=>state.graph.addEdgeParams,
  set_addNodeParams:state=>state.graph.set_addNodeParams,
  set_addEdgeParams:state=>state.graph.set_addEdgeParams,
  addNodeToGraph:state=>state.graph.addNodeToGraph,
  addEdgeToGraph:state=>state.graph.addEdgeToGraph,
  addCategoryToGraph:state=>state.graph.addCategoryToGraph,
  editNodeInfoParams: state=>state.graph.editNodeInfoParams,
  set_editNodeInfoParams:state=>state.graph.set_editNodeInfoParams,
  editRelationInfoParams: state=>state.graph.editRelationInfoParams,
  set_editRelationInfoParams:state=>state.graph.set_editRelationInfoParams,
  symbolSelectList: state=>state.graph.symbolSelectList,
  getNowGraph:state=>state.graph.nowGraph
  //getNodesInGraph:state=>state.graph.nodes
}
export default getters
