<template>
  <v-dialog max-width="500" v-model="QAModalVisible" persistent>
    <v-card min-height="280">
      <v-toolbar dense dark color="primary">
        <v-toolbar-title>Q&A</v-toolbar-title>
        <v-btn color="error" @click="close" small absolute right>关闭</v-btn>
      </v-toolbar>
      <v-card-text v-show="!loadingVisible&getAnswer">
          <v-subheader style="border:0;font-size: 20px; font-weight:bold;margin-top: 15px;">
            nekoCoin告诉你:
          </v-subheader>
      </v-card-text>
      <v-container style="padding-bottom: 0">
        <v-row style="margin-left: 25px;font-size: 16px;margin-right: 25px" v-show="!loadingVisible&getAnswer">
          <v-textarea
              outlined
              name="Answer"
              :label="question"
              background-color="amber lighten-4"
              auto-grow
              v-model="ans"
              readonly
          ></v-textarea>
        </v-row>
        <v-row>
          <v-progress-circular
              style="position:absolute;left:0; right:0; top:0; bottom:0;margin:auto;"
              :size="100"
              color="info"
              :width="7"
              v-show="loadingVisible"
              indeterminate>
            正在加载
          </v-progress-circular>
        </v-row>
        <v-form ref="form" v-show="!loadingVisible&!getAnswer">
          <v-row align="center" no-gutters>
            <v-col cols="3">
              <v-subheader>
                模式
              </v-subheader>
            </v-col>
            <v-col cols="6">
              <v-radio-group
                  v-model="ifSingleJump"
                  row
              >
                <v-radio
                    label="单跳"
                    :value=true
                ></v-radio>
                <v-radio
                    label="多跳"
                    :value=false
                ></v-radio>
              </v-radio-group>
            </v-col>
          </v-row>
          <v-row align="center" no-gutters v-show="ifSingleJump">
            <v-col cols="3">
              <v-subheader>
                问题
              </v-subheader>
            </v-col>
            <v-col cols="6">
              <v-text-field
                  :rules="rule"
                  v-model="question"
                  placeholder="例:夏侯渊的儿子是"
              ></v-text-field>
            </v-col>
            <v-col cols="1">
                <v-tooltip bottom>
                  <template v-slot:activator="{on,attrs}">
                    <v-icon
                        color="primary"
                        dark
                        v-bind="attrs"
                        v-on="on"
                    >
                      mdi-help-circle-outline
                    </v-icon>
                  </template>
                  <span><strong style="color:skyblue;font-size: 16px">支持的属性</strong><br/>
                      人物:封号,封地,官职,生日,死期,籍贯,亲属,朋友,雇主,门客等<br/>
                      事件:起因,过程,影响,时间,地点,参与人物等<br/>
                </span>
                </v-tooltip>
            </v-col>
          </v-row>
          <v-row align="center" no-gutters v-show="!ifSingleJump">
            <v-col cols="3">
              <v-subheader>
                问题
              </v-subheader>
            </v-col>
            <v-col cols="6">
              <v-text-field
                  :rules="rule"
                  v-model="question"
                  placeholder="例:公元222年在濡须口发生的事件是"
              ></v-text-field>
            </v-col>
            <v-col cols="1">
              <v-tooltip bottom>
                <template v-slot:activator="{on,attrs}">
                  <v-icon
                      color="primary"
                      dark
                      v-bind="attrs"
                      v-on="on"
                  >
                    mdi-help-circle-outline
                  </v-icon>
                </template>
                <span><strong style="color:skyblue;font-size: 16px">目前支持的模板</strong><br/>
                      某地某年发生了什么事<br/>
                      某地某年有谁出生<br/>
                      某人的亲属关系<br/>
                      某人的社会关系<br/>
                      介绍某人<br/>
                      介绍某地<br/>
                </span>
              </v-tooltip>
            </v-col>
          </v-row>
        </v-form>
      </v-container>
      <v-card-actions>
        <v-img
            max-height="70"
            max-width="120"
            :src=imgSrc
            absolute bottom left
            v-show="!loadingVisible"
        ></v-img>
        <v-btn color="info" @click="submit" absolute bottom right v-show="!loadingVisible&!getAnswer">提交</v-btn>
        <v-btn
            icon
            color="blue"
            @click="back"
            absolute bottom right
            v-show="!loadingVisible&getAnswer"
        >
          <v-icon>mdi-keyboard-return</v-icon>
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from "vuex";
import catImg from '../../../assets/cat.png'

export default {
  data: () => {
    return {
      rule: [v => !!v || '请输入'],
      imgSrc:catImg,
      ifSingleJump:true,
      loadingVisible:false,
      tipsVisible:false,
      getAnswer:false,
      ans:'',
      question:'',
    }
  },
  name: "QAModal",
  props: {
    QAModalVisible: Boolean
  },
  computed:{
    ...mapGetters([
      'answer',
      'source',
    ]),
  },
  methods: {
    ...mapMutations([
      'set_answer'
    ]),
    ...mapActions([
      'getMultiJumpAnswer',
      'getSingleJumpAnswer'
    ]),
    cancelQuest(){
      if(typeof this.source==='function'){
        this.source("终止请求")
      }
    },
    close() {
      this.back()
      this.$emit("chChangeQAModalVisible", false)
    },
    back(){
      this.cancelQuest()
      this.ans=''
      this.question=''
      this.set_answer({})
      this.loadingVisible=false
      this.getAnswer=false
    },
    //解析json对象
    handleAnswer(data,separator){
      //如果是数组
      if(Array.isArray(data)) {
        data.forEach(i => {
          this.ans += i+','
        })
        this.ans=this.ans.slice(0,this.ans.length-1)
      }
      //如果是对象
      else if(Object.prototype.toString.call(data) === '[object Object]') {
        for (let key in data) {
          this.ans += key + ': '
          this.handleAnswer(data[key],separator)
          this.ans +=separator
        }
      }
      else
        this.ans+=data
    },
    async submit(){
      if (this.$refs.form.validate()) {
        this.loadingVisible = true
        if (this.ifSingleJump) {
          this.cancelQuest()
          await this.getSingleJumpAnswer(this.question)
          this.loadingVisible = false
          if (this.answer != "服务出错" && this.answer != "终止请求") {
            if (this.answer.ans != undefined)
              this.handleAnswer(this.answer.ans, '\n')
            else
              this.ans = "暂无答案"

            this.getAnswer = true
          }
        } else {
          this.cancelQuest()
          await this.getMultiJumpAnswer(this.question)
          this.loadingVisible = false
          if (this.answer != "服务出错" && this.answer != "终止请求") {
            if (Array.isArray(this.answer)) {
              if (this.answer.length == 0)
                this.ans = "暂无答案"
              else if (this.answer.length == 1) {
                if (JSON.stringify(this.answer[0].ans) == '{}')
                  this.ans = "暂无答案"
                else
                  this.handleAnswer(this.answer[0].ans, '\n')
              } else {
                this.answer.forEach(i => {
                  this.handleAnswer(i.ans, ',')
                  this.ans = this.ans.slice(0, this.ans.length - 1) + '\n'
                })
              }
            } else
              this.ans = "暂无答案"

            this.getAnswer = true
          }
        }
      }
    },
  }
}
</script>

<style scoped>
  span{
    word-break:normal;
    width:auto;
    display:block;
    white-space:pre-wrap;
    word-wrap : break-word ;
    overflow: hidden ;
    text-align:justify;
  }
</style>