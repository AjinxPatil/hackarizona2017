import os
from eve import Eve

port = 5000
host = '127.0.0.1'

app = Eve()

if __name__ == '__main__':
    app.run(host=host, port=port)

