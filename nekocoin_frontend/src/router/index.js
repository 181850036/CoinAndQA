import Vue from 'vue'
import VueRouter from 'vue-router'

const routerPush=VueRouter.prototype.push
VueRouter.prototype.push=function push(location){
  return routerPush.call(this,location).catch(error=>error)
}

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Layout',
    redirect:'/list',
    component: ()=>import('@/views/Layout'),
    children:[
      {
        path: '/list',
        name:'GraphList',
        component:()=>import('@/views/GraphList/GraphList')
      },
      {
        path:'/test',
        name:'Helloworld',
        component:()=>import('@/views/Graph/HelloWorld')
      }
    ]
  },
]


const createRouter = () => new VueRouter({
  // mode: 'history', // require service support
  scrollBehavior: () => ({y: 0}),
  routes
})
const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
