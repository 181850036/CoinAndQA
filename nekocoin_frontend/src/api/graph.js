import {axios} from '../utils/request'

const api = {
    Pre: 'graph'
}

export function uploadGraphFileAPI(data) {
    return axios({
        url: `${api.Pre}/uploadGraphFile`,
        method: 'POST',
        data,
    })
}

export function getGraphJSONAPI(graphId) {
    return axios({
        url: `${api.Pre}/getAllNodesAndRelations`,
        method: 'GET',
        params: {graphId: graphId},
    })
}

export function getGraphListAPI() {
    return axios({
        url: `${api.Pre}/getGraphList`,
        method: 'GET'
    })
}

export function deleteGraphAPI(graphId) {
    return axios({
        url: `${api.Pre}/deleteGraph`,
        method: 'POST',
        params: {graphId: graphId}
    })
}
