from sentiment import Sentiment
from pymongo import MongoClient

def send_sms_sentiment(senti):
    s = Sentiment(senti[uid])
    if senti.get('sad') is not None:
        s.sad = senti.get('sad')
    if senti.get('angry') is not None:
        s.angry = senti.get('angry')
    if senti.get('disgust') is not None:
        s.angry = senti.get('disgust')
    if senti.get('joy') is not None:
        s.joy = senti.get('joy')
    if senti.get('fear') is not None:
        s.fear = senti.get('fear')
    
    client = MongoClient()
    db = client.hackaz17
    db.sentiments.insert(s.__dict__)

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

def update_sentiment(sentiments, db):
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
