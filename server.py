from flask import Flask, request
app = Flask(__name__)

# import other api's (blueprints)
from ms import ms_api

# register the imported blueprints
app.register_blueprint(ms_api)
app.register_blueprint(adduser_api)

@app.route('/')
def hello_world():
    return 'Hello, World!'
