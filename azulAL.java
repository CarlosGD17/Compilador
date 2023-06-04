import java.io.*;

public class azulAL {
    static int a_a = 0; // Apuntador de avance
    static int a_i = 0;   // Apuntador de inicio
    static int filesize = 0;
    static boolean fin_archivo = false;
    public static char[] linea;
    static int DIAG;
    static int ESTADO;
    static int c;
    static String LEX = "";
    static String entrada = "";
    static String salida = "";
    static String MiToken = "";
    static int Renglon = 1;
    static String[] reservada = new String[15];

    public static boolean creaEscribeArchivo(File xFile, String mensaje) {
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter(xFile, true));
            fileOut.println(mensaje);
            fileOut.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static boolean es_any(int x){
        // todo excepto \t, \n, eof
        return x != 10 && x != 13 && x != 255;
    }

    public static boolean es_delim(int x) {
        // si es CR, LF, TAB, espacio
        return x == 9 || x == 10 || x == 13 || x == 32;
    }

    public static String ob_lex() {
        StringBuilder x = new StringBuilder();
        for (int i = a_i; i < a_a; i++)
            x.append(linea[i]);
        return (x.toString());
    }

    public static File xArchivo(String xName) {
        return new File(xName);
    }

    public static int lee_car() {
        if (a_a <= filesize - 1) {
            if (linea[a_a] == 10) {
                Renglon++;
            }
            return (linea[a_a++]);
        } else {
            fin_archivo = true;
            return 255;
        }
    }

    public static char[] abreLeeCierra(String xName) {
        File xFile = new File(xName);
        char[] data;
        try {
            FileReader fin = new FileReader(xFile);
            filesize = (int) xFile.length();
            data = new char[filesize + 1];
            fin.read(data, 0, filesize);
            data[filesize] = ' ';
            filesize++;
            return (data);
        } catch (IOException ignored) {
        }
        return null;
    }

    public static String pausa() {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        String nada = null;
        try {
            nada = entrada.readLine();
            return (nada);
        } catch (Exception e) {
            System.err.println(e);
        }
        return ("");
    }

    public static void error() {
        System.out.print("ERROR: en el caracter: [" + (char) c + "]");
        System.exit(4);
    }

    public static boolean esLetra() {
        return ((c >= 65 && c <= 90) || (c >= 97 && c <= 122));
    }

    public static boolean esDigito() {
        return ((c >= 48 && c <= 57));
    }

    public static boolean es_reservada(String x){
        for(int i = 0; i <= 1; i++){
            if(reservada[i].equals(x)) {
                return true;
            }
        }
        return false;
    }

    public static int DIAGRAMA() {
        a_a = a_i;
        switch (DIAG) {
            case 0 -> DIAG = 3;
            case 3 -> DIAG = 7;
            case 7 -> DIAG = 12;
            case 12 -> DIAG = 15;
            case 15 -> DIAG = 19;
            case 19 -> DIAG = 30;
            case 30 -> DIAG = 47;
            case 47 -> error();
        }
        return (DIAG);
    }

    public static String TOKEN() {
        do {
            //System.out.println("Diagrama: " +ESTADO);
            switch (ESTADO) {
                case 0 -> {
                    c = lee_car();
                    if(es_delim(c)){
                        ESTADO = 1;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 1 -> {
                    c = lee_car();
                    if(es_delim(c)) {
                        ESTADO = 1;
                    } else{
                        ESTADO = 2;
                    }
                }
                case 2 -> {
                    a_a--;
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("nosirve");
                }
                case 3 -> {
                    c = lee_car();
                    if(esLetra()){
                        ESTADO = 4;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 4 -> {
                    c = lee_car();
                    if(esLetra() || esDigito()){
                        ESTADO = 4;
                    } else if(c == '_'){
                        ESTADO = 5;
                    } else{
                        ESTADO = 6;
                    }
                }
                case 5 -> {
                    c = lee_car();
                    if(esLetra() || esDigito()){
                        ESTADO = 4;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 6 -> {
                    a_a--;
                    LEX = ob_lex();
                    a_i = a_a;
                    if(es_reservada(LEX)){
                        return LEX;
                    }
                    return ("id");
                }
                case 7 -> {
                    c = lee_car();
                    if(esDigito()){
                        ESTADO = 8;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 8 -> {
                    c = lee_car();
                    if(esDigito()){
                        ESTADO = 8;
                    } else if(c == '.'){
                        ESTADO = 9;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 9 -> {
                    c = lee_car();
                    if(esDigito()){
                        ESTADO = 10;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 10 -> {
                    c = lee_car();
                    if(esDigito()){
                        ESTADO = 10;
                    } else{
                        ESTADO = 11;
                    }
                }
                case 11 -> {
                    a_a--;
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("dec");
                }
                case 12 -> {
                    c = lee_car();
                    if(esDigito()){
                        ESTADO = 13;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 13 -> {
                    c = lee_car();
                    if(esDigito()){
                        ESTADO = 13;
                    } else{
                        ESTADO = 14;
                    }
                }
                case 14 -> {
                    a_a--;
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("ent");
                }
                case 15 -> {
                    c = lee_car();
                    if(c == '/'){
                        ESTADO = 16;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 16 -> {
                    c = lee_car();
                    if(c == '/'){
                        ESTADO = 17;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 17 -> {
                    c = lee_car();
                    if(es_any(c)){
                        ESTADO = 17;
                    } else{
                        ESTADO = 18;
                    }
                }
                case 18 -> {
                    a_a--;
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("nosirve");
                }
                case 19 -> {
                    c = lee_car();
                    switch (c){
                        case '+' -> ESTADO = 20;
                        case '-' -> ESTADO = 21;
                        case '*' -> ESTADO = 22;
                        case '/' -> ESTADO = 23;
                        case '(' -> ESTADO = 24;
                        case ')' -> ESTADO = 25;
                        case '{' -> ESTADO = 26;
                        case '}' -> ESTADO = 27;
                        case ':' -> ESTADO = 28;
                        case '.' -> ESTADO = 29;
                        default -> ESTADO = DIAGRAMA();
                    }
                }
                case 20 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("+");
                }
                case 21 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("-");
                }
                case 22 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("*");
                }
                case 23 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("/");
                }
                case 24 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("(");
                }
                case 25 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return (")");
                }
                case 26 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("{");
                }
                case 27 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("}");
                }
                case 28 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return (":");
                }
                case 29 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return (".");
                }
                case 30 -> {
                    c = lee_car();
                    switch (c){
                        case '=' -> ESTADO = 31;
                        case '<' -> ESTADO = 37;
                        case '>' -> ESTADO = 42;
                        case '!' -> ESTADO = 45;
                        default -> ESTADO = DIAGRAMA();
                    }
                }
                case 31 -> {
                    c = lee_car();
                    switch (c){
                        case '/' -> ESTADO = 32;
                        case '>' -> ESTADO = 34;
                        case '<' -> ESTADO = 35;
                        default -> ESTADO = 36;
                    }
                }
                case 32 -> {
                    c = lee_car();
                    if(c == '='){
                        ESTADO = 33;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 33 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("dif");
                }
                case 34 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("mayi");
                }
                case 35 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("meni");
                }
                case 36 -> {
                    a_a--;
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("igual");
                }
                case 37 -> {
                    c = lee_car();
                    switch (c){
                        case '>' -> ESTADO = 38;
                        case '=' -> ESTADO = 39;
                        case '-' -> ESTADO = 40;
                        default -> ESTADO = 41;
                    }
                }
                case 38 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("dif");
                }
                case 39 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("meni");
                }
                case 40 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("asig");
                }
                case 41 -> {
                    a_a--;
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("men");
                }
                case 42 -> {
                    c = lee_car();
                    if(c == '=') {
                        ESTADO = 43;
                    } else{
                        ESTADO = 44;
                    }
                }
                case 43 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("mayi");
                }
                case 44 -> {
                    a_a--;
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("may");
                }
                case 45 -> {
                    c = lee_car();
                    if(c == '='){
                        ESTADO = 46;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 46 -> {
                    LEX = ob_lex();
                    a_i = a_a;
                    return ("dif");
                }
                case 47 -> {
                    c = lee_car();
                    if(c == 255){
                        ESTADO = 48;
                    } else{
                        ESTADO = DIAGRAMA();
                    }
                }
                case 48 -> {
                    a_i = a_a;
                    return ("nosirve");
                }
            }
        } while (true);
    }

    public static void main(String[] args) {

        entrada = args[0] + ".prg";
        salida = args[0] + ".sal";;

        reservada[0]="datos";
        reservada[1]="fin_datos";
        reservada[2]="entero";
        reservada[3]="decimal";
        reservada[4]="cierto";
        reservada[5]="haz";
        reservada[6]="falso";
        reservada[7]="fin_cond";
        reservada[8]="mientras";
        reservada[9]="fin_mientras";


        if (!xArchivo(entrada).exists()) {
            System.out.println("\n\nERROR: El archivo " + entrada + " no existe :(");
            System.exit(4);
        }

        linea = abreLeeCierra(entrada);

        do {
            ESTADO = 0;
            DIAG = 0;
            MiToken = TOKEN();
            if (!MiToken.equals("basura")) {
                creaEscribeArchivo(xArchivo(salida), MiToken);
                creaEscribeArchivo(xArchivo(salida), LEX);
                creaEscribeArchivo(xArchivo(salida), Renglon + "");
            }
            /*
            System.out.println("Este es el token hallado: [" + MiToken + "]");
            System.out.println("Este es el lexema: [" + LEX + "]");
            System.out.println("Hallado en la linea: [" +Renglon +"]");
            pausa();
             */
            a_i = a_a;
        } while (!fin_archivo);
        creaEscribeArchivo(xArchivo(salida), "fin");
        creaEscribeArchivo(xArchivo(salida), "fin");
        creaEscribeArchivo(xArchivo(salida), "666");
        System.out.println("\n-> Analisis lexicografico exitoso   :)");
    }
}
