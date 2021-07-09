import {axios} from '../utils/request'
import axi from 'axios'
const CancelToken=axi.CancelToken

const api = {
    multiJumpPre: 'getAnswer',
    singleJumpPre: 'question'
}
// eslint-disable-next-line no-unused-vars
export function getMultiJumpAnswerAPI(path,state) {
    return axios({
        url: `${api.multiJumpPre}/`+path,
        method: 'GET',
        cancelToken:new CancelToken(function executor(c) {
            // executor 函数接收一个 cancel 函数作为参数
            state.source=c
        })
    })
}
// eslint-disable-next-line no-unused-vars
export function getSingleJumpAnswerAPI(path,state) {
    return axios({
        url: `${api.singleJumpPre}/`+path,
        method: 'GET',
        cancelToken:new CancelToken(function executor(c) {
            // executor 函数接收一个 cancel 函数作为参数
            state.source=c
        })
    })
}