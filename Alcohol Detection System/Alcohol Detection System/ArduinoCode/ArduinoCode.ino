// include the library code:
#include <LiquidCrystal.h>
#include <Keypad.h>

// initialize the library by associating any needed LCD interface pin
// with the arduino pin number it is connected to
const int rs = 13, en = 12, d4 = 11, d5 = 10, d6 = A4, d7 = A5;
const byte ROWS = 4; //four rows
const byte COLS = 4; //four columns
char keys[ROWS][COLS] = {
  {'1','2','3','A'},
  {'4','5','6','B'},
  {'7','8','9','C'},
  {'*','0','#','D'}
};
byte rowPins[ROWS] = {7,6,5,4}; //connect to the row pinouts of the keypad
byte colPins[COLS] = {3,2,9,8}; //connect to the column pinouts of the keypad
Keypad mykeypad = Keypad( makeKeymap(keys), rowPins, colPins, ROWS, COLS );

LiquidCrystal lcd(rs, en, d4, d5, d6, d7);
const int MQ3=A0;
const int Buzzer=A1;
const int LED=A2;
int value;
String EnteredValue = "";
String Fnumber="";
String numberD="";
int empEntered = 0;
String stringa;
const unsigned long TimeOut = 10;

void setup() {
  Serial.begin(9600);
  pinMode(MQ3, INPUT);
  pinMode(Buzzer, OUTPUT);
  pinMode(LED, OUTPUT);
  digitalWrite(Buzzer,LOW);
  digitalWrite(LED,LOW);
  // set up the LCD's number of columns and rows:
  lcd.begin(16, 2);
  lcd.print("AD System"); 
    delay (2000);
  
}

void loop() {

 employeeNum();
  
}

void employeeNum(){

  if (empEntered == 0){
     delay(100); 
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("Force #:      ");
    Fnumber=getInput();
    lcd.setCursor(0,1);
    lcd.print(Fnumber);
    delay(1000);
    empEntered = 1;
  }
  else {
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("Licence #:      ");
    lcd.setCursor(0,1);
    EnteredValue ="";
    getInput();
    AlcoholDetection();
    getRequestID();
    
  }
  
}

String getInput(){
  //lcd.clear();
  lcd.setCursor(0,1);
  while(1){
      
      int num = EnteredValue.length();
      char key = mykeypad.getKey();
      if(key){
        
        if(key=='#'){
          //EnteredValue="";
          lcd.clear();

          break;
           
        }
        if(key=='C')
        {
          lcd.clear();
          lcd.print("Cancelling...");
          delay(1000);
          EnteredValue = "";
          break;
        }
              else
        {
          
          EnteredValue+=key;
          //Serial.print(key);
          lcd.print(key);
        }
      }

  }
   lcd.clear();
  lcd.setCursor(0,0);
  return EnteredValue;
}

void AlcoholDetection()
{
  String data = "";
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Alc Reading:");

  delay (4000);
  value = analogRead(MQ3);
  lcd.setCursor(0, 1);
  lcd.print(value);
  data = Fnumber+ ":" + EnteredValue +";"+ value;
  Serial.println(data);
  if(value>440)
  {
    digitalWrite(Buzzer,HIGH);
    digitalWrite(LED,HIGH);
    lcd.setCursor(6, 1);
    lcd.print(" - Drunk  "); 
        delay (5000);
    digitalWrite(Buzzer,LOW);
  }else{
    digitalWrite(Buzzer,LOW);
    digitalWrite(LED,LOW);
    lcd.setCursor(6, 1);
    lcd.print(" - Sober  ");
    delay (5000);
  }


}
  void getRequestID(){
    lcd.clear();
    lcd.print("Waiting...");
    if (Serial.available()) {
    delay(2000);  //wait some time for the data to fully be read
    lcd.clear();
    stringa = "";
    unsigned long T = 0; // timer
    T = millis();
    
   while (millis() - T < TimeOut) {while (Serial.available() > 0) {
      stringa += char(Serial.read());
      T = millis();
       
    }
    lcd.print(stringa);
    delay(2000);
   }
   
  }
  }
