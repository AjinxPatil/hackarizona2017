import requests
import facebook
from datetime import datetime
import pdb

class FbClient:
    def __init__(self, token):
        self.graph = facebook.GraphAPI(access_token=token)
        self.me = self.graph.get_object("me")
        
    def getUserFeed(self, sinceDate=None):
        """ Returns a list of natural language paragraphs extracted from user's feed """
        if sinceDate is not None:
            feeds = self.graph.get_connections("me", connection_name="feed", since=datetime.strptime(sinceDate, '%Y%m%d'))
        else:
            feeds = self.graph.get_connections("me", connection_name="feed")
        
        text = []
        for feed in feeds['data']:
            if feed['type'] == 'link':
                text.append(feed['name'])
            if 'description' in feed:
                text.append(feed['description'])
            if 'caption' in feed:
                text.append(feed['caption'])
            if 'comments' in feed:
                for comment in feed['comments']['data']:
                    text.append(comment['message'])
        return text

    def getRelationshipStatus(self):
        return self.graph.get_object("me", fields="relationship_status")['relationship_status']

    def getLatestLikes(self):
        """ Returns list of names of recently liked nodes"""
        userLikes = self.graph.get_object("me", fields="likes", limit="10")
        likes = []
        for like in userLikes['likes']['data']:
            likes.append(like['name'])
        return likes

    def getAboutMe(self):
        return self.me['bio']

### Usage ###
client = FbClient('ACCESS_TOKEN')
print(client.getUserFeed(sinceDate='20161010'))
print(client.getRelationshipStatus())
print(client.getLatestLikes())
print(client.getAboutMe())
