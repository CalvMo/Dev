import RPi.GPIO as GPIO
import time
#from time import sleep
from firebase import firebase
import os
from twilio.rest import Client
import smtplib 
from email.mime.multipart import MIMEMultipart 
from email.mime.text import MIMEText 
from email.mime.base import MIMEBase 
from email import encoders

firebase = firebase.FirebaseApplication('/', None)
account_sid = ''
auth_token = '
client = Client(account_sid, auth_token)

firebase.put("/", "/Alarm", "off")
firebase.put("/", "/Fan", "off")
firebase.put("/", "/Room 1", "off")
firebase.put("/", "/Room 2", "off")
firebase.put("/", "/Finger", "Offline")




light_1 = 20
light_2 = 16
motion = 17
buzzer = 27
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(buzzer,GPIO.OUT)
GPIO.setup(light_1,GPIO.OUT)
GPIO.setup(light_2,GPIO.OUT)
GPIO.setup(motion, GPIO.IN)


GPIO.output(light_1,GPIO.HIGH)
GPIO.output(light_2,GPIO.HIGH)

#os.system("python3 dht-firebase.py")



def     capture(imageName):
        os.system("sudo service motion stop")
        os.system("fswebcam "+imageName)

        #camera.close()
        os.system("sudo service motion start")

def sendWhatsApp():
    message = client.messages \
    .create(
         #media_url=['https://images.unsplash.com/photo-1545093149-618ce3bcf49d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=668&q=80'],
         from_='whatsapp:+14155238886',
         body="Good day, \nPlease note that an intruder has been detected.\nOpen Intrusion Detector App to view live feed\nRegards",
         to='whatsapp:'
     )
    
    

def     sendNotification(file_name):
        fromaddr = ""
        toaddr = ""
        msg = MIMEMultipart()
        msg['From'] = fromaddr  
        msg['To'] = toaddr 
        msg['Subject'] = "Intrusion Detected" 
        body = 'Good day\n\nPlease note there are intruder(s) detected on your premises \nThe attached image was captured \n\nPlease open mobile application to view live feed. \nThank you \n\nRegards'
        msg.attach(MIMEText(body, 'plain')) 
        filename = file_name
        attachment = open(filename, "rb") 
        p = MIMEBase('application', 'octet-stream') 
        p.set_payload((attachment).read()) 
        encoders.encode_base64(p) 
        p.add_header('Content-Disposition', "attachment; filename= %s" % filename) 
        msg.attach(p)
        try:
            s = smtplib.SMTP('smtp.gmail.com', 587) 
            s.starttls() 
            s.login(fromaddr, "") 
            text = msg.as_string() 
            s.sendmail(fromaddr, toaddr, text)
            s.quit()
            try:
                sendWhatsApp()
            except:
                print("Whatsapp Unavailable")
            return True
        except Exception:
            return False


while True:
    Room1 = firebase.get("/Room 1/", None)
    Room2 = firebase.get("/Room 2/", None)
    Alarm = firebase.get("/Alarm/", None)
    Finger = firebase.get("/Finger/", None)
    
    if Finger == "CheckIn":
        os.system("sudo python2 /usr/share/doc/python-fingerprint/examples/example_search.py")
        firebase.put("/", "/Finger", "Checked")
            
    if Room1 =="On":
        GPIO.output(light_1,GPIO.LOW)
    else:
        GPIO.output(light_1,GPIO.HIGH)
    if Room2 =="On":
        GPIO.output(light_2,GPIO.LOW)
    else:
        GPIO.output(light_2,GPIO.HIGH)
    if Alarm == "On":
        while True:       
            
            i=GPIO.input(motion)
            if i==0:
                print("No intruders",i)
                time.sleep(0.1)
            elif i==1:
                print("Intruder detected", i)
                GPIO.output(light_1,GPIO.LOW)
                GPIO.output(light_2,GPIO.LOW)
                GPIO.output(buzzer,GPIO.HIGH)
                capture("Image.jpg")
                if(sendNotification('Image.jpg')):
                    os.system("rm -f Image.jpg")
                    time.sleep(10)
                    GPIO.output(buzzer,GPIO.LOW)
            Alarm = firebase.get("/Alarm/", None)
            if Alarm =="Off":
                GPIO.output(buzzer,GPIO.LOW)
                break
    
        
        
    
    
    