import Vue from 'vue';
import Vuex from 'vuex';
import getters from "./getters";
import graph from "./modules/graph"
import graphList from "./modules/graphList"
Vue.use(Vuex)

export default new Vuex.Store({
  modules:{
    graph,
    graphList
  },
  state:{

  },
  mutations:{

  },
  actions:{

  },
  getters
})
