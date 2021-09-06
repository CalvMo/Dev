import RPi.GPIO as GPIO
from time import sleep
from firebase import firebase
import Adafruit_DHT



firebase = firebase.FirebaseApplication('https://raspberrypi-517dd.firebaseio.com/', None)
Room1 = firebase.get("/Temp/", None)
#firebase.get("/", "/Humidity", "0.00")

print(Room1)