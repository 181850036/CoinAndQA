FROM python:3.7
ADD ./QA_with_NB /code
ADD ./requirements.txt /code/
WORKDIR /code
RUN pip install -r requirements.txt -i https://pypi.doubanio.com/simple/
CMD ["python","/code/code.py"]
