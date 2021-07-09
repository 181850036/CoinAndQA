<template>
    <v-card style="margin-top:15px" v-show="modifyViewModalVisible">
        <v-toolbar dense dark color="primary">
            <v-toolbar-title>视觉优化</v-toolbar-title>
        </v-toolbar>
        <v-container>
            <v-row align="center" no-gutters>
                <v-col cols="5">
                    <v-subheader>
                        斥力
                    </v-subheader>
                </v-col>
                <v-col cols="6">
                    <v-slider
                            style="margin-top: 19px"
                            hint="仅力导图生效"
                            dense persistent-hint
                            v-model="ops.viewRepulsion"
                            min="300"
                            max="1000"
                            @change="commonChange"
                    >
                    </v-slider>
                </v-col>
            </v-row>
            <v-row align="center" no-gutters>
                <v-col cols="5">
                    <v-subheader>
                        图标放缩
                    </v-subheader>
                </v-col>
                <v-col cols="6">
                    <v-slider
                            style="margin-top: 19px"
                            dense
                            v-model="ops.viewSymbolSize"
                            min="5"
                            max="20"
                            @change="symbolSizeChange"
                    >
                    </v-slider>
                </v-col>
            </v-row>
            <v-row align="center" no-gutters>
                <v-col cols="5">
                    <v-subheader>
                        文字大小
                    </v-subheader>
                </v-col>
                <v-col cols="6">
                    <v-slider
                            style="margin-top: 19px"
                            dense
                            v-model="ops.viewFontSize"
                            min="5"
                            max="30"
                            @change="commonChange"
                    >
                    </v-slider>
                </v-col>
            </v-row>
            <v-row align="center" no-gutters>
                <v-col cols="5">
                    <v-subheader>
                        标签范围
                    </v-subheader>
                </v-col>
                <v-col cols="6">
                    <v-slider
                            style="margin-top: 19px"
                            hint="最小全显示"
                            dense persistent-hint
                            v-model="ops.viewLabelRange"
                            min="1"
                            max="20"
                            @change="showLabelChange"
                    >
                    </v-slider>
                </v-col>
            </v-row>
        </v-container>
        <v-card-actions>
            <v-btn style="margin: auto" @click="close">关闭</v-btn>
            <v-btn color="info" style="margin: auto" @click="submit">保存</v-btn>
        </v-card-actions>
    </v-card>
</template>
<script>
    import {message} from 'ant-design-vue'
    import {mapGetters} from 'vuex'

    export default {
        data: () => (
            {
                ops: {
                    viewRepulsion: 500,
                    viewSymbolSize: 10, //放缩10倍
                    viewFontSize: 12,
                    viewLabelRange: 7
                }
            }
        ),
        computed: {
            ...mapGetters([
                'myChart',
                'graph'
            ])
        },
        props: {
            modifyViewModalVisible: Boolean
        },
        mounted() {
            if (localStorage.viewOptions !== undefined) {
                let op = JSON.parse(localStorage.viewOptions)
                this.ops = {...op}
            }
        },
        methods: {
            close() {
                this.$emit("chChangeModifyViewModalVisible", false)
            },
            submit() {
                localStorage.viewOptions = JSON.stringify(this.ops)
                message.success('保存成功！')
            },
            commonChange() {
                let option = {
                    series:
                        [
                            {
                                force: {
                                    repulsion: this.ops.viewRepulsion
                                },
                                label: {
                                    fontSize: this.ops.viewFontSize
                                }
                            }
                        ]
                }
                this.myChart.setOption(option)
            },
            symbolSizeChange() {
                let that = this
                this.graph.nodes.forEach(node => {
                    if (node.originSymbolSize === undefined)
                        node.originSymbolSize = node.symbolSize
                    node.symbolSize = node.originSymbolSize * that.ops.viewSymbolSize / 10
                })
            },
            showLabelChange() {
                let min = 1, max = 20
                //放缩
                let tempNum = (this.ops.viewLabelRange - min) / (max - min)
                //放缩到[0,maxx]
                let maxx = 0
                this.graph.nodes.forEach(node => {
                    if (node.value > maxx)
                        maxx = node.value
                })
                let newLabelShow = 0 + tempNum * (maxx - 0)
                this.graph.nodes.forEach(node => {
                    node.label.show = node.value >= newLabelShow
                })
                this.graph.links.forEach(link => {
                    link.label.show = this.graph.nodes.find(item => item.name === link.source).value >= newLabelShow &&
                        this.graph.nodes.find(item => item.name === link.target).value >= newLabelShow
                })
                this.myChart.setOption({
                    series:
                        [
                            {
                                data: this.graph.nodes,
                                links: this.graph.links,
                            }
                        ]
                })
            }
        }
    }

</script>