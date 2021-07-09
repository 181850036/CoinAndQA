import {getGraphListAPI} from "../../api/graph";
import {message} from 'ant-design-vue'

const graphList={
    state:{
        graphList:[]
    },
    mutations:{
        set_graphList(state,data){
            state.graphList=data
        },
    },
    actions:{
        getGraphList:async({commit})=>{
            const res=await getGraphListAPI()
            if(res){
                commit('set_graphList',res)
            }else{
                message.error("未找到图谱列表！")
            }
        },
    }
}
export default graphList