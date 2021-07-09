import {axios} from "../utils/request";

const api = {
  Pre: 'relationship'
}

export function buildRelationAPI(data) {
  return axios({
    url: `${api.Pre}/buildRelation`,
    method: 'POST',
    data
  })
}

export function deleteRelationAPI(graphId,sourceName, targetName) {
  return axios({
    url: `${api.Pre}/deleteByName`,
    method: 'POST',
    params: {graphId:graphId,sourceName: sourceName, targetName: targetName},
  })
}

export function editRelationInfoAPI(data) {
  return axios({
    url: `${api.Pre}/editRelationInfo`,
    method: 'POST',
    data
  })
}
