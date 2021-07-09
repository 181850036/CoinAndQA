# -*- coding: UTF-8 -*-
# @Time    : 2019/3/1 14:43
# @Author  : xiongzongyang
# @Site    :
# @File    : code.py.py
# @Software: PyCharm

import sys
from gevent import pywsgi
from preprocess_data import Question

from flask import Flask, request, jsonify
from flask_cors import cross_origin
import json, requests

app = Flask(__name__)
# render = web.template.render('templates/')

# urls = ('/', 'index','/add','add')
# app = web.application(urls, globals())

que = Question()
print("create question object finish! ")


# 主页显示类
@app.route('/')
def index():
    return 'hello'


# 处理问题类
@app.route('/getAnswer/<text>/', methods=['GET'], strict_slashes=False)
@cross_origin()
def add(text):
    question = text
    print("received question:", question)
    print("now get answer!")
    answer = dealquestion(question)
    print("得到的答案是：", answer)
    return jsonify(answer)


# 处理问题的方法
def dealquestion(question):
    # 查询知识图谱
    answer = que.question_process(question)
    # answer=12
    ret = dict()
    ret['success'] = True
    ret['message'] = ''
    ret['content'] = answer
    return ret


if __name__ == "__main__":
    # web.internalerror = web.debugerror
    server = pywsgi.WSGIServer(('0.0.0.0', 5000), app)
    server.serve_forever()
