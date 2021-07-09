<template>
    <v-dialog
            v-model="deleteModalVisible"
            width="500"
    >
        <v-card>
            <v-toolbar dense dark color="red lighten-2">
                <v-toolbar-title>注意！</v-toolbar-title>
            </v-toolbar>

            <v-card-text style="margin-top: 5%">该图谱所有内容将被删除，是否确认？</v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                        color="error"
                        @click="confirmDeleteGraph"
                        style="margin-right: 5%"
                >
                    确认
                </v-btn>
                <v-btn
                        color="primary"
                        @click="captureClick"
                        style="margin: auto"
                >
                    取消
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
<script>
    import {mapActions} from 'vuex'

    export default {
        props: {
            deleteModalVisible: Boolean,
            deleteGraphId: String
        },
        methods: {
            ...mapActions([
                'getGraphList',
                'deleteGraph'
            ]),
            async confirmDeleteGraph() {
                const res = await this.deleteGraph(this.deleteGraphId)
                if (!res) { //删除成功才刷新页面
                    this.$emit('chChangeDeleteModalVisible', false)
                    this.getGraphList()
                }
                this.$emit('chChangeDeleteModalVisible', false)
            },
            captureClick(){
                this.$emit('chChangeDeleteModalVisible',false)
            }
        }
    }

</script>