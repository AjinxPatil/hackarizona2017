import user_account
from pymongo import MongoClient
from flask import request 

client = MongoClient('localhost', 27017)
db = client['hackaz17']
collection = db['user_accounts']

adduser_api = Blueprint('adduser_api', __name__) 

@adduser_api.route('/adduser', methods=['POST'])
def addusertodb():
    newuser = user_account.user_accounts(request.form['user_id'], request.form['username'], request.form['password'], request.form['timestamp'])
    collection.insert(newuser.__dict__)


