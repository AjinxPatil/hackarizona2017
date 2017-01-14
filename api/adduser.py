import user_account
from pymongo import MongoClient

client = MongoClient('localhost', 27017)
db = client['hackaz17']
collection = db['user_accounts']

def addusertodb(payload):
    newuser = user_account.user_accounts(payload['user_id'], payload['username'], payload['password'], payload['timestamp'])
    collection.insert(newuser.__dict__)


