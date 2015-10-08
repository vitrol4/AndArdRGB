//pinos do LED
const int RED = 6;
const int GREEN = 5;
const int BLUE = 3;

//variaveis para coletar as cores enviadas pelo android 
int red = 255;
int green = 255;
int blue = 255;

void setup() {
    Serial.begin(9600);
     
    //este sao os pinos de saida
    //cada pino representa uma cor do RGB
    pinMode(RED, OUTPUT);
    pinMode(GREEN, OUTPUT);
    pinMode(BLUE, OUTPUT);
     
    //desliga todos os pinos ao comecar
    analogWrite(RED, red);
    analogWrite(GREEN, green);
    analogWrite(BLUE, blue);
     
    Serial.println("Controle Arduino RGB LEDs XConexao OK");
}

void loop() {
     
    while (Serial.available() > 0) {
        //O formato que o arduino recebe é "x1,x2,x3\n"
        //o separador é a virgula
        //x1 = vermelho
        //x2 = verde
        //x3 = azul
         
        red = Serial.parseInt();
        green = Serial.parseInt();
        blue = Serial.parseInt();
     
        //busca uma nova linha, isto é o final dos dados enviados
         if (Serial.read() == '\n') {
           //coloca os numeros entre 0 a 255
           //caso ocorra por acidente de vir um numero fora
           //desta faixa
           red = constrain(red, 0, 255);
           green = constrain(green, 0, 255);
           blue = constrain(blue, 0, 255);
         
           //envia o valor relativo para os pinos
           analogWrite(RED, red);
           analogWrite(GREEN, green);
           analogWrite(BLUE, blue);
         
            //imprime o que o arduino recebeu em cada cor
            Serial.print("Dado Enviado : ");
            Serial.print(red);
            Serial.print(green);
            Serial.println(blue);
         }
    }
}

