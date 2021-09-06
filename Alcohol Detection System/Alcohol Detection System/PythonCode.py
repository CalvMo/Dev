import serial
import time
import pyodbc
import datetime
import os

serial_port = 'com3';
baud_rate = 9600; #In arduino, Serial.begin(baud_rate)
ser = serial.Serial(serial_port, baud_rate, timeout=10)
time.sleep(2)    #wait for the Serial to initialize
#MSSQL Connection
connectionARS = pyodbc.connect('Driver={SQL Server};Server=ARS90;Database=ARSystem;Trusted_Connection=yes;')
cursor = connectionARS.cursor()


def getReqID():
    cursor.execute("SELECT MAX(Reference_No)+1 AS REQUEST FROM ARSystem.dbo.AD_Management")
#Getting next Request ID from DB
    for row in cursor:
        print((row.REQUEST))
        requestID = str(row.REQUEST)
    return requestID
def checkRTMC(LNum):
    print(cursor.execute("SELECT Count(Licence_Number) AS LICENCE, Status__c FROM ARSystem.dbo.AD_RTMC_Database where Licence_Number like ' "+str(LNum)+"'"))

    for row in cursor:
        print((row.LICENCE))
        driver = str(row.LICENCE)
    if (driver == 1):
        sts = row.Status__c
        if (sts == 0):
            ser.write(': Enabled')
        else :
            ser.write(': Suspended')
            
        Status = sts
    else:
        ser.write(':Invalid Lic')
        Status = 2
    return Status
        
def createRec(FNum,LNum,reading):
    pattern='%Y-%m-%d %H:%M:%S'
    timestamp = str(datetime.datetime.now())
    temp_dateTime=timestamp.find('.')
    currentDate = timestamp[0:temp_dateTime]
    os.environ['TZ']='UTC'
    currentDate = int(time.mktime(time.strptime(currentDate,pattern)))
    Create_Date = currentDate
    Last_Modified_Date = currentDate
    requestID = getReqID()
    #print requestID
    ser.write(requestID)
    Status = checkRTMC(LNum)
    parameters = "( Reference_No ,Submitted_By, Create_Date, Modified_Date, Last_Modified_By, Status, Description, License_Number, Measurement)"
    values = "'"+requestID+"',"+str(FNum)+","+str(Create_Date)+","+str(Last_Modified_Date)+",'Demo',"+str(Status)+",'ALcohol Detection System',"+"'"+str(LNum)+"',"+str(reading)
    query = "INSERT INTO ARSystem.dbo.AD_Management "+parameters + "VALUES ( "+values+" )"
    print(query)
    cursor.execute(query)
    connectionARS.commit()	
    #connectionARS.close()
   
             
while True:

        if (ser.inWaiting()>0):
            myData = ser.readline()
            print myData
            FNum = myData[0:myData.find(':')]
            LNum = myData[myData.find(':')+1:myData.find(';')]
            reading = myData[myData.find(';')+1:]
            createRec(FNum,LNum,reading)
            
            
            
            
            


    

