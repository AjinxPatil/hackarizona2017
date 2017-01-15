from flask import Flask, request
app = Flask(__name__)

# import other api's (blueprints)
from ms import ms_api
from adduser import adduser_api
from senti_sources import senti_app
from alchemy import getDocEmotion

import json

from fb import FbClient
from alchemy import docEmotions

# register the imported blueprints
app.register_blueprint(ms_api)
# app.register_blueprint(adduser_api)
# app.register_blueprint(senti_app)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/postFbToken', methods=['POST'])
def getFacebookSentiment():
    accessToken = request.form['accessToken']
    fbClient = FbClient(accessToken)
    relationShipStatus = fbClient.getRelationshipStatus()
    feeds = this.getUserFeed()
    print("### SOCIAL MEDIA SENTIMENT ###")
    for feed in feeds:
        print("Feed Post :",feed)
        print(getDocEmotion(feed))
    latestLikes = fbClient.getLatestLikes()
    for like in latestLikes:
        print("Like Caption :", like)
        print(getDocEmotion(feed))
    aboutMe = fbClient.getAboutMe()
    print("About me", aboutMe)
    print(getDocEmotion(aboutMe))
    return json.dumps({'success':True}), 200, {'ContentType':'application/json'}

@senti_app.route('/postSms', methods=['POST'])
def send_sms_sentiment():
    text = request.form['sms']
    messageList = text.split(';')
    print("### SMS CHAT SENTIMENT ###")
    for message in messageList:
        sentivals = getDocEmotion(message) 
        print(message)
        print(sentivals)
    return json.dumps({'success':True}), 200, {'ContentType':'application/json'}
