import random
import time
import os
import sys
import requests
import tweepy
import credentials
#very simple tool used to post imaged, called by TwitterBot.java


#setting up credentials and tweepy
auth = tweepy.OAuthHandler(credentials.CONSUMER_KEY, credentials.CONSUMER_SECRET)
auth.set_access_token(credentials.ACCESS_TOKEN, credentials.ACCESS_SECRET)
api = tweepy.API(auth)

#checks if this is a text or image tweet
if(sys.argv[1]=="image"):
	tmp = sys.argv[2].replace("\\","\\")
	print(sys.argv[2])
	f = open("posted.txt","a+")
	f.write(tmp+"\n")
	f.close()
	api.update_with_media(tmp)
else:
	print("this is a text tweet")

