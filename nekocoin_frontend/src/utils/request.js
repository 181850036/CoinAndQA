import Vue from 'vue'
import axios from 'axios'
import {VueAxios} from './axios'
import {message} from 'ant-design-vue'
import store from '../store'

const service=axios.create({
  baseURL:process.env.NODE_ENV==='production'?'/api':'/api',
  withCredentials:true,
  timeout:30000,
})
const err=(error)=>{
  if(error.response){
    const data=error.response.data
    const token=Vue.ls.get('ACCESS_TOKEN')
    if(error.response.status===403){
      message.error('Forbidden')
    }
    if(error.response.status===401&&!(data.result && data.result.isLogin)){
      message.error('Unauthorized')
      if(token){
        store.dispatch('Logout').then(()=>{
          setTimeout(()=>{
            window.location.reload()
          },1500)
        })
      }
    }
  }
  return Promise.reject(error)
}

service.interceptors.request.use((config)=>{
  const requestConfig={
    ...config,
    url:`${config.url}`
  }
  return requestConfig
},err)

service.interceptors.response.use((response)=>{
  switch (response.status) {
    case 200:
      if(response.data.success)
        return response.data.content
      message.error(response.data.message)
      break
    case 404:
      console.log(response)
      return false
    default:
      message.error(response.data.message)
  }
})

const installer={
  vm:{},
  install(Vue) {
    Vue.use(VueAxios,service)
  }
}

export {
  installer as VueAxios,
  service as axios
}
