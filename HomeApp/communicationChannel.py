from Adafruit_IO import Client, Feed, Data
import time
import RPi.GPIO as GPIO

import picamera
import os
import smtplib 
from email.mime.multipart import MIMEMultipart 
from email.mime.text import MIMEText 
from email.mime.base import MIMEBase 
from email import encoders

aio = Client("","")

currentFan = 0
currentMotionSensor = 0
currentTemp = 0
currentLight = 0
currentReset = 0

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)

GPIO.setup(37, GPIO.OUT)
GPIO.setup(35, GPIO.OUT)
GPIO.setup(11, GPIO.IN) #Read output from PIR motion sensor

def     capture(imageName):
        os.system("sudo service motion stop")
        os.system("fswebcam "+imageName)

        #camera.close()
        os.system("sudo service motion start")

def     sendNotification(file_name):
        fromaddr = ""
        toaddr = ""
        msg = MIMEMultipart()
        msg['From'] = fromaddr  
        msg['To'] = toaddr 
        msg['Subject'] = "Intrusion at Home" 
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
            return True
        except Exception:
            return False


print("Retrieving Input!")

GPIO.output(37, 1)
GPIO.output(35, 1)

try:
    while True:
        print("reading")
        fan = aio.receive("fan").value
        motionSensor = aio.receive("motion").value
        temperature = aio.receive("temp").value
        light = aio.receive("bulb").value
        resetButton = aio.receive("reset").value
        
        #print("Input fan value "+fan)
        if fan != currentFan:
            print("Fan value : "+fan)
            currentFan = fan
            GPIO.output(35, int(fan))
          
        if motionSensor != currentMotionSensor:
            print("Motion value : "+motionSensor)
            currentMotionSensor = motionSensor
            if motionSensor == "1":
                while True:
                    i=GPIO.input(11)
                    if i==0:
                        print "No intruders",i
                        time.sleep(0.1)
                    elif i==1:
                        print "Intruder detected", i
                        capture("Image.jpg")
                        if(sendNotification('Image.jpg')):
                            os.system("rm -f Image.jpg")
                            time.sleep(10)
                    motionSensor = aio.receive("motion").value
                    print("reading")
                    if motionSensor == "0":
                        break        
            
        if temperature != currentTemp:
            print("temperature : "+temperature)
            currentTemp = temperature
            
        if light != currentLight:
            print("Smart Bulb : "+light)
            currentLight = light
            GPIO.output(37, int(light))

        if resetButton != currentReset:
            print("operation : "+resetButton)
            currentReset = resetButton

except KeyboardInterrupt:
    GPIO.cleanup()
        
    
