from flask import Flask, request

import ms

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/postPic', methods=['POST'])
def postPic():
    print(request.form)
    picBinary = request.form['picture']
    json = None
    params = None
    headers = dict()
    headers['Ocp-Apim-Subscription-Key'] = _key
    headers['Content-Type'] = 'application/octet-stream'
    response = processRequest(json=json, data=picBinary, headers=headers, params= params)
    # TODO: store in db
    print(response)

