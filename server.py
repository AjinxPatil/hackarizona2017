from flask import Flask, request
app = Flask(__name__)

# import other api's (blueprints)
from ms import ms_api
from alchemy import getDocEmotion
from fb import FbClient
import json
import threading

# register the imported blueprints
app.register_blueprint(ms_api)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/postFbToken', methods=['POST'])
def getFacebookSentiment():
    # print(request.data)
    accessToken = str(request.data, "utf-8")
    # print(accessToken)
    fbClient = FbClient(accessToken)
    relationShipStatus = fbClient.getRelationshipStatus()
    feeds = fbClient.getUserFeed()
    print("### SOCIAL MEDIA SENTIMENT ###")
    for feed in feeds:
        t = threading.Thread(target=extractAndPrint, args=[feed])
        t.start()
    latestLikes = fbClient.getLatestLikes()
    for like in latestLikes:
        # print("Like Caption :", like)
        t = threading.Thread(target=extractAndPrint, args=[like])
        t.start()
        # print(getDocEmotion(feed))
    aboutMe = fbClient.getAboutMe()
    print("About me", aboutMe)
    print(getDocEmotion(aboutMe))
    return json.dumps({'success':True}), 200, {'ContentType':'application/json'}

@app.route('/postSms', methods=['POST'])
def send_sms_sentiment():
    messageList = str(request.data).split(';')
    print("### SMS CHAT SENTIMENT ###")
    for message in messageList:
        t = threading.Thread(target=extractAndPrint, args=[message])
        t.start()
    return json.dumps({'success':True}), 200, {'ContentType':'application/json'}

def extractAndPrint(message):
    try:
        sentivals = getDocEmotion(message)
        print(message)
        print(sentivals)
    except:
        print()

