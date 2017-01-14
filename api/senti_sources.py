from sentiment import Sentiment
from pymongo import MongoClient
from alchemy import getDocEmotion

def send_sms_sentiment(senti):
    s = Sentiment(senti[uid])
    text = senti.get('sms')
    sentivals = getDocEmotion(text) 
    copy_sentivals(s, sentivals)
    client = MongoClient()
    db = client.hackaz17
    calc_sentiment(s, db)

def calc_sentiment():
    # whenever a reco is to be made or check to see the depression rating of patient
    pass

def set_user_fbtoken(utoken):
    if utoken.get('userid') is not None:
        client = MongoClient()
        db = client.hackaz17
        user = db.patients.find({id : utoken.get('userid')})
        if user is not None:
            user.set_fbtoken(utoken.get('token'))
        db = client.hackaz17
        db.sentiments.insert(user.__dict__)

def calc_sentiment(sentiments, db):
    senti = Sentiment(sentiments[uid])
    newsenti = Sentiment(sentiments[uid])
    collection = db['Sentiments']
    slist = list(collection.find({"user_id" : sentiments[uid]}))
    if not slist:
        newsenti = sentiments   
    else:
        for prevsenti in collection.find({"user_id" : sentiments[uid]}):
            newsenti.sad = prevsenti.sad * 0.5 + senti.sad * 0.5
            newsenti.angry = prevsenti.angry * 0.5 + senti.angry * 0.5
            newsenti.joy = prevsenti.joy * 0.5 + senti.joy * 0.5
            newsenti.fear = prevsenti.fear * 0.5 + senti.fear * 0.5
            newsenti.disgust = prevsenti.disgust * 0.5 + senti.disgust * 0.5
    newsenti.score = newsenti.sad * 0.60 + newsenti.angry * 0.15 + newsenti.disgust * 0.15 + newsenti.fear * .10

    collection.delete_many({"user_id": sentiments[uid]})

    collection.insert(newsenti.__dict__)
def copy_sentivals(s, copy):
    s.sad = copy.get('sad') if copy.get('sad') is not None else 0
    s.angry = copy.get('angry') if copy.get('angry') is not None else 0
    s.angry = copy.get('disgust') if copy.get('disgust') is not None else 0
    s.joy = copy.get('joy') if copy.get('joy') is not None else 0
    s.fear = copy.get('fear') if copy.get('fear') is not None else 0

def calc_fb_sentiment():
    dbclient = MongoClient()
    db = dbclient.hackaz17
    users = db.patients.find()
    for user in users:
        fbtoken = user.fbtoken
        fbclient = FbClient(fbtoken)
        feeds = fbclient.getUserFeed(sinceDate='20161010')
        for feed in feeds:
            s = Sentiment(user.id)
            sentivals = getDocEmotion(feed)
            copy_sentivals(s, sentivals)
            calc_sentiment(s, db)
        aboutme = fbclient.getAboutMe()
        sentivals = getDocEmotion(feed)
        copy_sentivals(s, sentivals)
        calc_sentiment(s, db)

