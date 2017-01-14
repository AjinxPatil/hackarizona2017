import json
import pdb
from watson_developer_cloud import AlchemyLanguageV1

alchemy_language = AlchemyLanguageV1(api_key='9613f41709e765d219ab414d9f9e2837e50bb0ff')


def getDocEmotion(docText):
    """ Returns a dictionary containing the sentiment of given text using alchemy api.
        Keys : sadness, anger, disgust, joy, fear
    """ 
    response = alchemy_language.emotion(text=docText)
    return response['docEmotions']

# print(getDocEmotion('there is no meaning or purpose left in life'))
# print(getDocEmotion('feels awesome to meet old friends again'))

