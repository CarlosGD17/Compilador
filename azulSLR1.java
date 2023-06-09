import java.io.*;

import static java.lang.System.exit;

public class azulSLR1 {

    static String a, RENGLON, LEX;
    static String Entrada, Salida;
    static int Posicion = 0;

    static String[] terminales = new String[8];
    static String[] noTerminales = new String[7];

    static String[] pila = new String[1000];
    static int tope = -1;

    public static void lee_token(File xFile) {
        try {
            FileReader fr = new FileReader(xFile);
            BufferedReader br = new BufferedReader(fr);
            long NoSirve = br.skip(Posicion);
            String linea = br.readLine();
            Posicion = Posicion + linea.length() + 2;
            a = linea;
            linea = br.readLine();
            Posicion = Posicion + linea.length() + 2;
            LEX = linea;
            linea = br.readLine();
            Posicion = Posicion + linea.length() + 2;
            RENGLON = linea;
            fr.close();
            //System.out.print(".");

        } catch (IOException e) {
            System.out.println("Errorzote");
        }
    }

    public static void error(){
        System.out.println(" ! ERROR: En el token [" + a + "] cerca del renglón " +RENGLON +"\n");
        exit(4);
    }

    public static File xArchivo(String xName) {
        return new File(xName);
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

    public static int terminal(String X){
        for(int i = 0; i < terminales.length; i++){
            if (X.equals(terminales[i])){
                return i;
            }
        }
        return -1;
    }

    public static int noTerminal(String X){
        for(int i = 0; i < noTerminales.length; i++){
            if (X.equals(noTerminales[i])){
                return i + 8;
            }
        }
        return -1;
    }

    public static void push(String x){
        if(tope >= 9999){
            System.out.println("Error: pila llena");
            exit(4);
        }
        if(!x.equals("epsilon")){
            pila[++tope] = x;
        }
    }

    public static String pop(){
        if(tope < 0){
            System.out.println("Error: pila vacia");
            exit(4);
        }
        return pila[tope--];
    }

    public static void printPila(){
        System.out.println("Pila --> ");
        for(int i = 0; i <= tope; i++){
            System.out.println(pila[i] + " ");
        }
        System.out.println();
        pausa();
    }

    public static void main(String[] args) {
        Entrada = args[0] +".sal";
        if(!xArchivo(Entrada).exists()){
            System.out.println("\n\nERROR: El archivo no existe [" +Entrada +"]");
            exit(4);
        }



    }

}
