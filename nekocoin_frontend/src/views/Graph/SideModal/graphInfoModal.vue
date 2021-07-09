<template>
    <v-card
            class="mt-4 mx-auto"
            max-width="400"
    >
        <v-sheet
                class="v-sheet--offset mx-auto"
                color="cyan"
                elevation="9"
                max-width="calc(100% - 32px)"
        >
            <div
                    :class="[`text-h4`]"
                    v-text="currentGraphName"
                    style="color: #f6f6f6"
            >
            </div>
        </v-sheet>

        <v-card-text class="pt-0">
            <v-chip
                    class="ma-2"
                    color="indigo darken-3"
                    outlined
            >
                <v-icon left>mdi-checkbox-multiple-blank-circle</v-icon>
                节点数：{{entitiesCnt}}
            </v-chip>
            <v-chip
                    class="ma-2"
                    color="indigo darken-3"
                    outlined
            >
                <v-icon left>mdi-relation-many-to-many</v-icon>
                关系数：{{linksCnt}}
            </v-chip>
            <v-chip
                    class="ma-2"
                    color="indigo darken-3"
                    outlined
            >
                <v-icon left>mdi-home-group</v-icon>
                类目数：{{categoriesCnt}}
            </v-chip>


            <!--            <div class="title font-weight-light mb-2">
                            User Registrations
                        </div>
                        <div class="subheading font-weight-light grey&#45;&#45;text">
                            Last Campaign Performance
                        </div>-->
            <v-divider class="my-2"></v-divider>
            <v-icon
                    class="mr-2"
                    small
            >
                mdi-clock
            </v-icon>
            <span class="caption grey--text font-weight-light">图谱创建时间：{{createTime}}</span>
        </v-card-text>
    </v-card>
</template>

<script>
    import {mapGetters} from 'vuex'

    export default {
        props: {
            graph: Object
        },
        data: () => ({}),
        computed: {
            entitiesCnt: function () {
                return this.graph.nodes.length
            },
            linksCnt: function () {
                return this.graph.links.length
            },
            categoriesCnt: function () {
                return this.graph.categories.length
            },
            createTime: function () {
                return this.graph.graphProperties === undefined ?
                    "未知" : this.graph.graphProperties.createTime
            },
            ...mapGetters([
                'currentGraphName',
            ])
        },
    }
</script>
<style>
    .v-sheet--offset {
        top: -13px;
        position: relative;
    }
</style>