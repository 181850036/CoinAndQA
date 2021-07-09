import {axios} from "../utils/request"

const api = {
    Pre: 'node'
}

export function addNodeAPI(data) {
    return axios({
        url: `${api.Pre}/insertEntity`,
        method: 'POST',
        data
    })
}
export function createGraphAPI(data) {
    return axios({
        url: `${api.Pre}/insertGraph`,
        method: 'POST',
        data
    })
}
export function deleteNodeAPI(data) {
    return axios({
        url: `${api.Pre}/delete`,
        method: 'POST',
        params: {graphId: data.graphId, name: data.name},
    })
}

export function editNodeInfoAPI(data) {
    return axios({
        url: `${api.Pre}/editNodeInfo`,
        method: 'POST',
        data
    })
}

export function editNodesAPI(data) {
    return axios({
        url: `${api.Pre}/editNodes`,
        method: 'POST',
        data
    })
}

export function deleteAllAPI() {
    return axios({
        url: `${api.Pre}/deleteAll`,
        method: 'POST',
    })
}

export function insertCategoryAPI(data) {
    return axios({
        url: `${api.Pre}/insertCategory`,
        method: 'POST',
        data
    })
}
