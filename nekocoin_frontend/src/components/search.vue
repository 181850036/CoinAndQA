<template>
    <div v-on:keyup.enter="search" style="display:flex">
        <v-menu offset-y>
            <template v-slot:activator="{ on }">
                <v-text-field
                        @click="getEntity"
                        height="5px"
                        solo
                        hide-details
                        label="搜索"
                        v-model="text"
                        class="input-search"
                        autocomplete="on"
                        v-on="on"
                        ref="search"
                ></v-text-field>
                <div>
                    <v-btn depressed style="margin-left:20px;margin-top:7px" @click="renewHistoryAndSearch(text)">
                        <v-icon left>
                            mdi-magnify
                        </v-icon>
                        {{searchBtnText}}
                    </v-btn>
                </div>
            </template>
            <v-list v-if="this.hisList.length > 0" class="border-list" dense>
                <v-list-item v-for="(item, index) in this.hisList" :key="index" @click="itemClick(item)">
                    <v-list-item-title>{{ item}}</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
    </div>
</template>
<script>
    import {mapGetters, mapMutations} from 'vuex'
    import {message} from 'ant-design-vue'

    export default {
        data() {
            return {
                boxLabel: '搜索',
                text: '',
                showSelect: true,
                items: [],
                searchBtnText: '搜索',
                hisList: [],
                isSearching: false,
                nowGraph: []
            }
        },
        mounted() {
            this.getEntity()
            this.getNodesAndRelations()
        },
        watch: {
            //  goSearch(searchBtnText){
            //    if(searchBtnText) this.renewHistoryAndSearch
            //  }
        },
        computed: {
            ...mapGetters([
                'graph',
                'currentGraphId',
                'myChart',
                'currentGraphName'
            ]),
        },
        methods: {
            ...mapMutations([
                'set_graph',
                'set_graphNodeOpacity',
                'set_graphLinkOpacity'
            ]),
            itemClick(item) {
                this.text = item
                this.$refs.search.blur()
                // this.$router.push()
            },
            renewHistoryAndSearch(keyword) {
                this.nowGraph = JSON.parse(JSON.stringify(this.graph))
                this.$refs.search.blur()
                if (!this.isSearching) {
                    this.isSearching = true
                    this.searchBtnText = '取消'
                    keyword = keyword.trim()
                    let flag = false
                    if (keyword === '') {
                        message.error('没有匹配节点!')
                        return
                    }
                    // eslint-disable-next-line no-unused-vars
                    this.hisList.forEach((item, index) => {
                        if (item === keyword) {
                            flag = true
                        }
                    })
                    if (this.hisList.length >= 5 && flag === false) {
                        this.hisList.pop()
                    }
                    // 将搜索的记录保存到第一位
                    if (flag === false)
                        this.hisList.unshift(keyword)
                    // 保存到localStorage
                    localStorage.hisList = JSON.stringify(this.hisList)
                    let found = false
                    for (let i = 0; i < this.nowGraph.nodes.length; i++) {
                        if (!this.isSubElement(String(keyword).toLowerCase(), String(this.nowGraph.nodes[i].name).toLowerCase())) {
                            this.nowGraph.nodes[i].itemStyle = {
                                opacity: 0.03
                            }
                        } else {
                            found = true
                        }
                    }
                    if (!found) {
                        message.error('没有匹配节点!')
                        return
                    }
                    for (let i = 0; i < this.nowGraph.links.length; i++) {
                        if (!this.isSubElement(String(keyword).toLowerCase(), String(this.nowGraph.links[i].source).toLowerCase()) &&
                            !this.isSubElement(String(keyword).toLowerCase(), String(this.nowGraph.links[i].target).toLowerCase())) {
                            this.nowGraph.links[i].lineStyle = {
                                opacity: 0.03,
                            }
                        } else {
                            this.nowGraph.links[i].lineStyle = {
                                width: 1.5
                            }
                            for (let j = 0; j < this.nowGraph.nodes.length; j++) {
                                if((this.nowGraph.nodes[j].name === this.nowGraph.links[i].source || 
                                        this.nowGraph.nodes[j].name === this.nowGraph.links[i].target)){
                                    if (!(this.isSubElement(String(keyword).toLowerCase(), String(this.nowGraph.nodes[j].name).toLowerCase()))) {
                                        this.nowGraph.nodes[j].itemStyle = {
                                            opacity: 0.2
                                        }
                                    }
                                }

                            }
                        }
                    }
                } else if (this.isSearching) {
                    this.isSearching = false
                    this.searchBtnText = '搜索'
                    this.text = ''
                    for (let i = 0; i < this.nowGraph.nodes.length; i++) {

                        this.nowGraph.nodes[i].itemStyle = {
                            opacity: 1
                        }

                    }
                    for (let i = 0; i < this.nowGraph.links.length; i++) {
                        this.nowGraph.links[i].lineStyle = {
                            opacity: 0.5,
                            width: 1
                        }
                    }
                }
                this.updateMyChart()
            },
            getEntity() {
                // 从缓存获取搜索记录
                //this.nowGraph = JSON.parse(JSON.stringify(this.graph))
                let hisList = localStorage.hisList
                if (hisList) {
                    this.hisList = JSON.parse(hisList)
                }
            },
            getNodesAndRelations() {
            },
            search() {
                this.$refs.search.blur()
                // this.$router.push()
            },
            isSubElement(s, t) {//s是否为t的子序列
                let count = 0
                let s_arr = s.split("")
                let t_arr = t.split("")
                if (s_arr.length > t_arr.length) return false
                //	let t2_arr = [...new Set(t_arr)]
                // eslint-disable-next-line no-unused-vars
                let sIndex = 0
                let tIndex = 0
                while (sIndex < s_arr.length && tIndex < t_arr.length) {
                    if (s_arr[sIndex] === t_arr[tIndex]) {
                        count += 1
                        sIndex++
                        tIndex++
                    } else tIndex++
                }
                if (count >= s_arr.length) {
                    return true
                } else {
                    return false
                }
            },
            updateMyChart() {
                this.myChart.clear()
                let option = {
                    title: {
                        text: 'NekoCOIN System',
                        subtext: 'Default layout',
                        top: 'bottom',
                        left: 'center'
                    },
                    tooltip: {},
                    legend: [{
                        // selectedMode: 'single',
                        data: this.graph.categories.map(function (a) {
                            return a.name
                        })
                    }],
                    animationEasingUpdate: false,
                    /*animationDuration: 1500,
                    animationEasingUpdate: 'quinticInOut',*/
                    series: [
                        {
                            id: 'a',
                            name: this.currentGraphName,
                            type: 'graph',
                            layout: this.mode,
                            force: {
                                repulsion: 500,
                                gravity: 0.1
                            },
                            animationThreshold: 300,
                            animationDuration: 1500,
                            animationEasingUpdate: 'cubicInOut',
                            data: this.nowGraph.nodes,
                            links: this.nowGraph.links,
                            categories: this.nowGraph.categories,
                            roam: true,
                            draggable: false,
                            label: {
                                position: 'right',
                                formatter: '{b}'
                            },
                            lineStyle: {
                                color: 'source',
                                curveness: 0.3
                            },
                            emphasis: {
                                focus: 'adjacency',
                                lineStyle: {
                                    width: 10
                                }
                            },
                        }
                    ],
                    media: [
                        {
                            option: {
                                series: [{
                                    left: 'center',
                                    top: 'middle'
                                }]
                            }
                        }
                    ]
                }
                this.myChart.setOption(option)
            }
        }
    }
</script>

<style scoped>
    .width-20-percent {
        width: 20%;
    }

    .img-div {
        margin: 16vh 0 40px 0;
        text-align: center;
    }

    .v-menu__content {
        box-shadow: none !important;
    }

    .border-list {
        border: 1px solid #eee !important;
    }
</style>
