import java.io.*;

import static java.lang.System.exit;

public class azulSLR1 {

    static String a, RENGLON, LEX;
    static String Entrada;
    static int Posicion = 0;

    static String[] terminales = new String[31];
    static String[] noTerminales = new String[17];

    static int numeroDeProducciones = 37;
    static String[] parteIzquierda = new String[numeroDeProducciones];
    static int[] tamanoParteDerecha = new int[numeroDeProducciones];

    static int[][] M = new int[68][terminales.length + noTerminales.length];

    static String[] pila = new String[1117];
    static int tope = -1;

    // reglas sematnticas de los shifts
    static String Temp;
    static String Tipo;
    static String TipoDec;
    static String VarIzq;
    static String TipoEsp;
    static String DecV = "";

    // para la generacion de variables y etiquetas
    static int var = -1;
    static int etq = -1;

    // tabla de Simbolos, guarda variables generadas
    static String[][] tablaSimbolos = new String[117][2];
    static int xTabla = 0;

    // para las reglas semanticas de los reduce
    static String[] PROG_c = new String[117];
    static int topePROG_C = -1;
    static String[] PRIN_c = new String[117];
    static int topePrin_c = -1;
    static String[] BLQ_c = new String[117];
    static int topeBLQ_c = -1;
    static String[] INST_c = new String[117];
    static int topeINST_c = -1;
    static String[] CICLO_c = new String[117];
    static int topeCICLO_c = -1;
    static String[] ASIG_c = new String[117];
    static int topeASIG_c = -1;
    static String[] COND_c = new String[117];
    static int topeCOND_c = -1;
    static String[] EXP_c = new String[117];
    static int topeEXP_c = -1;
    static String PosA, PosB, PosC;
    static String[] E_v = new String[117], F_v = new String[117], S_v = new String[117];
    static int topeE_v = -1, topeF_v = -1, topeS_v = -1;
    static String[] E_t = new String[117], F_t = new String[117], S_t = new String[117];
    static int topeE_t = -1, topeF_t = -1, topeS_t = -1;
    static String X;
    static String[] E_c = new String[117], F_c = new String[117], S_c = new String[117];
    static int topeE_c = -1, topeF_c = -1, topeS_c = -1;
    static String[] OP_c = new String[117];
    static int topeOP_c = -1;

    static String aux, aux2;

    public static void lee_token(File xFile) {
        try {
            FileReader fr = new FileReader(xFile);
            BufferedReader br = new BufferedReader(fr);
            br.skip(Posicion);
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

    public static void error() {
        System.out.println(" ! ERROR: En el token [" + a + "] cerca del renglón " + RENGLON + "\n");
        exit(4);
    }

    public static File xArchivo(String xName) {
        return new File(xName);
    }

    public static void pausa() {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        try {
            entrada.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int terminal(String X) {
        for (int i = 0; i < terminales.length; i++) {
            if (X.equals(terminales[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int noTerminal(String X) {
        for (int i = 0; i < noTerminales.length; i++) {
            if (X.equals(noTerminales[i])) {
                return i + terminales.length;
            }
        }
        return -1;
    }

    public static void push(String x) {
        if (tope >= 9999) {
            System.out.println("Error: pila llena");
            exit(4);
        }
        if (!x.equals("epsilon")) {
            pila[++tope] = x;
        }
    }

    public static String pop() {
        if (tope < 0) {
            System.out.println("Error: pila vacia");
            exit(4);
        }
        return pila[tope--];
    }

    public static void printPila() {
        System.out.println("Pila --> ");
        for (int i = 0; i <= tope; i++) {
            System.out.println(pila[i] + " ");
        }
        System.out.println();
        pausa();
    }

    public static String GenVar() {
        var += 1;
        return "V" + var;
    }

    public static String ChkTipo(String A, String B) { //REVISADO
        if (A.equals(B)) {
            return A;
            //return "Tipo de A y B si son iguales";
        }
        System.exit(4);
        return null;
    }

    public static void Agr_tab(String X, String Y) {
        if (existeVariable(X) == -1) {
            tablaSimbolos[xTabla][0] = X;
            tablaSimbolos[xTabla][1] = Y;
            xTabla += 1;
        } else {
            error();
        }
    }

    //Verificar si existe la variable en la tablaSimbolos
    public static int existeVariable(String variable) {
        // retorn a el indice de variable si esta en la tabla
        for (int i = 0; i < xTabla; i++) {
            if (tablaSimbolos[i][0].equals(variable)) {
                return i;
            }
        }
        // si no esta en la tablla, retorna -1
        return -1;
    }

    public static String obtenTipo(String X) { //Verifica si existe en la tabla
        int indice = existeVariable(X);
        if (indice != -1) {
            // System.out.println("La variable ["+X+"] existe y es de tipo: " +tablaSimbolos[indice][1]);
            return tablaSimbolos[indice][1];
        }
        System.exit(4);
        return null;
    }

    public static String instAri(String A, String B) { //REVISADO
        if (B.equals("decimal")) {
            return A + "F";
        } else {
            return A + "E";
        }
    }

    public static String GenEtq() {
        etq += 1;
        return "E" + etq;
    }

    public static String VarTemps() {
        String vars = "";
        for (int i = 0; i <= var; i++) {
            vars += "\tPALABRA\tV" + i + "\n";
        }
        return vars;
    }

    //Rutina imprimir tabla de simbolos
    public static void printTabla() {
        System.out.println("\tVariable\tTipo");
        for (int i = 0; i <= xTabla; i++)
            System.out.println("\t" + tablaSimbolos[i][0] + "\t\t" + tablaSimbolos[i][1]);
        pausa();
    }

    public static void main(String[] args) {
        Entrada = args[0] + ".sal";
        if (!xArchivo(Entrada).exists()) {
            System.out.println("\n\nERROR: El archivo no existe [" + Entrada + "]");
            exit(4);
        }

// terminales
        terminales[0]="datos";          terminales[16]="fin_mientras";
        terminales[1]="fin_datos";      terminales[17]="asig";
        terminales[2]=":";              terminales[18]="+";
        terminales[3]="id";             terminales[19]="-";
        terminales[4]=",";              terminales[20]="*";
        terminales[5]="{";              terminales[21]="/";
        terminales[6]="}";              terminales[22]="ent";
        terminales[7]="entero";         terminales[23]="dec";
        terminales[8]="decimal";        terminales[24]="may";
        terminales[9]="cierto";         terminales[25]="men";
        terminales[10]="(";             terminales[26]="mayi";
        terminales[11]=")";             terminales[27]="meni";
        terminales[12]="haz";           terminales[28]="igual";
        terminales[13]="falso";         terminales[29]="dif";
        terminales[14]="fin_cond";      terminales[30]="fin";
        terminales[15]="mientras";

        // no terminales
        noTerminales[0]="PROGP";        noTerminales[9]="COND";
        noTerminales[1]="PROG";         noTerminales[10]="CICLO";
        noTerminales[2]="DATSEC";       noTerminales[11]="ASIG";
        noTerminales[3]="VARS";         noTerminales[12]="EXP";
        noTerminales[4]="LISTA";        noTerminales[13]="E";
        noTerminales[5]="PRIN";         noTerminales[14]="F";
        noTerminales[6]="TIPO";         noTerminales[15]="S";
        noTerminales[7]="BLQ";          noTerminales[16]="OP";
        noTerminales[8]="INST";

        // parte izquierda de la produccion
        parteIzquierda[0]="PROGP";      parteIzquierda[19]="ASIG";
        parteIzquierda[1]="PROG";       parteIzquierda[20]="EXP";
        parteIzquierda[2]="DATSEC";     parteIzquierda[21]="E";
        parteIzquierda[3]="DATSEC";     parteIzquierda[22]="E";
        parteIzquierda[4]="VARS";       parteIzquierda[23]="E";
        parteIzquierda[5]="VARS";       parteIzquierda[24]="F";
        parteIzquierda[6]="LISTA";      parteIzquierda[25]="F";
        parteIzquierda[7]="LISTA";      parteIzquierda[26]="F";
        parteIzquierda[8]="PRIN";       parteIzquierda[27]="S";
        parteIzquierda[9]="TIPO";       parteIzquierda[28]="S";
        parteIzquierda[10]="TIPO";      parteIzquierda[29]="S";
        parteIzquierda[11]="BLQ";       parteIzquierda[30]="S";
        parteIzquierda[12]="BLQ";       parteIzquierda[31]="OP";
        parteIzquierda[13]="INST";      parteIzquierda[32]="OP";
        parteIzquierda[14]="INST";      parteIzquierda[33]="OP";
        parteIzquierda[15]="INST";      parteIzquierda[34]="OP";
        parteIzquierda[16]="COND";      parteIzquierda[35]="OP";
        parteIzquierda[17]="COND";      parteIzquierda[36]="OP";
        parteIzquierda[18]="CICLO";

        // tamano parte derecha de la produccion
        tamanoParteDerecha[0]=1;        tamanoParteDerecha[19]=3;
        tamanoParteDerecha[1]=2;        tamanoParteDerecha[20]=3;
        tamanoParteDerecha[2]=3;        tamanoParteDerecha[21]=3;
        tamanoParteDerecha[3]=0;        tamanoParteDerecha[22]=3;
        tamanoParteDerecha[4]=4;        tamanoParteDerecha[23]=1;
        tamanoParteDerecha[5]=3;        tamanoParteDerecha[24]=3;
        tamanoParteDerecha[6]=3;        tamanoParteDerecha[25]=3;
        tamanoParteDerecha[7]=1;        tamanoParteDerecha[26]=1;
        tamanoParteDerecha[8]=3;        tamanoParteDerecha[27]=1;
        tamanoParteDerecha[9]=1;        tamanoParteDerecha[28]=1;
        tamanoParteDerecha[10]=1;       tamanoParteDerecha[29]=1;
        tamanoParteDerecha[11]=2;       tamanoParteDerecha[30]=3;
        tamanoParteDerecha[12]=1;       tamanoParteDerecha[31]=1;
        tamanoParteDerecha[13]=1;       tamanoParteDerecha[32]=1;
        tamanoParteDerecha[14]=1;       tamanoParteDerecha[33]=1;
        tamanoParteDerecha[15]=1;       tamanoParteDerecha[34]=1;
        tamanoParteDerecha[16]=9;       tamanoParteDerecha[35]=1;
        tamanoParteDerecha[17]=7;       tamanoParteDerecha[36]=1;
        tamanoParteDerecha[18]=6;

        // matriz
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                M[i][j] = 0;
            }
        }
        // shifts
        M[0][0]=3;
        M[2][5]=5;
        M[3][7]=8;      M[3][8]=9;
        M[5][9]=15;     M[5][3]=16;     M[5][15]=17;
        M[6][1]=18;
        M[7][2]=19;
        M[10][6]=20;
        M[11][9]=15;    M[11][3]=16;    M[11][15]=17;
        M[15][10]=22;
        M[16][17]=23;
        M[17][10]=24;
        M[19][3]=26;
        M[22][22]=31;   M[22][23]=32;   M[22][3]=33;    M[22][10]=34;
        M[23][22]=31;   M[23][23]=32;   M[23][3]=33;    M[23][10]=34;
        M[24][22]=31;   M[24][23]=32;   M[24][3]=33;    M[24][10]=34;
        M[25][7]=8;     M[25][8]=9;
        M[26][4]=38;
        M[27][11]=39;
        M[28][24]=41;   M[28][25]=42;   M[28][26]=43;   M[28][27]=44;   M[28][28]=45;   M[28][29]=46;   M[28][18]=47;   M[28][19]=48;
        M[29][20]=49;   M[29][21]=50;
        M[34][22]=31;   M[34][23]=32;   M[34][3]=33;    M[34][10]=34;
        M[35][18]=47;   M[35][19]=48;
        M[36][11]=52;
        M[38][3]=26;
        M[39][12]=54;
        M[40][22]=31;   M[40][23]=32;   M[40][3]=33;    M[40][10]=34;
        M[47][22]=31;   M[47][23]=32;   M[47][3]=33;    M[47][10]=34;
        M[48][22]=31;   M[48][23]=32;   M[48][3]=33;    M[48][10]=34;
        M[49][22]=31;   M[49][23]=32;   M[49][3]=33;    M[49][10]=34;
        M[50][22]=31;   M[50][23]=32;   M[50][3]=33;    M[50][10]=34;
        M[51][11]=60;   M[51][18]=47;   M[51][19]=48;
        M[52][9]=15;    M[52][3]=16;    M[52][15]=17;
        M[54][9]=15;    M[54][3]=16;    M[54][15]=17;
        M[55][18]=47;   M[55][19]=48;
        M[56][20]=49;   M[56][21]=50;
        M[57][20]=49;   M[57][21]=50;
        M[61][16]=63;
        M[62][13]=64;   M[62][14]=65;
        M[64][9]=15;    M[64][3]=16;    M[64][15]=17;
        M[66][14]=67;
        // go to's
        M[0][32]=1;     M[0][33]=2;
        M[2][36]=4;
        M[3][34]=6;     M[3][37]=7;
        M[5][38]=10;    M[5][39]=11;    M[5][40]=12;    M[5][42]=13;    M[5][41]=14;
        M[11][38]=21;   M[11][39]=11;   M[11][40]=12;   M[11][42]=13;   M[11][41]=14;
        M[19][35]=25;
        M[22][43]=27;   M[22][44]=28;   M[22][45]=29;   M[22][46]=30;
        M[23][44]=35;   M[23][45]=29;   M[23][46]=30;
        M[24][43]=36;   M[24][44]=28;   M[24][45]=29;   M[24][46]=30;
        M[25][34]=37;   M[25][37]=7;
        M[28][47]=40;
        M[34][44]=51;   M[34][45]=29;   M[34][46]=30;
        M[38][35]=53;
        M[40][44]=55;   M[40][45]=29;   M[40][46]=30;
        M[47][45]=56;   M[47][46]=30;
        M[48][45]=57;   M[48][46]=30;
        M[49][46]=58;
        M[50][46]=59;
        M[52][38]=61;   M[52][39]=11;   M[52][40]=12;   M[52][42]=13;   M[52][41]=14;
        M[54][38]=62;   M[54][39]=11;   M[54][40]=12;   M[54][42]=13;   M[54][41]=14;
        M[64][38]=66;   M[64][39]=11;   M[64][40]=12;   M[64][41]=14;   M[64][42]=13;
        // reduce
        M[0][5] = -3;
        // estado de aceptacion
        M[1][30]=1117;
        M[4][30]=-1;
        M[8][2]=-9;
        M[9][2]=-10;
        M[11][6]=-12;   M[11][13]=-12;  M[11][14]=-12;  M[11][16]=-12;
        M[12][9]=-13;   M[12][3]=-13;   M[12][15]=-13;  M[12][6]=-13;   M[12][13]=-13;  M[12][14]=-13;  M[12][16]=-13;
        M[13][9]=-14;   M[13][3]=-14;   M[13][15]=-14;  M[13][6]=-14;   M[13][13]=-14;  M[13][14]=-14;  M[13][16]=-14;
        M[14][9]=-15;   M[14][3]=-15;   M[14][15]=-15;  M[14][6]=-15;   M[14][13]=-15;  M[14][14]=-15;  M[14][16]=-15;
        M[18][5]=-2;
        M[20][30]=-8;
        M[21][6]=-11;   M[21][13]=-11;  M[21][14]=-11;  M[21][16]=-11;
        M[25][1]=-5;
        M[26][7]=-7;    M[26][8]=-7;    M[26][1]=-7;
        M[29][24]=-23;  M[29][25]=-23;  M[29][26]=-23;  M[29][27]=-23;  M[29][28]=-23;  M[29][29]=-23;  M[29][18]=-23;
        M[29][19]=-23;  M[29][11]=-23;  M[29][9]=-23;   M[29][3]=-23;   M[29][15]=-23;  M[29][6]=-23;   M[29][13]=-23;
        M[29][14]=-23;  M[29][16]=-23;
        M[30][20]=-26;  M[30][21]=-26;  M[30][24]=-26;  M[30][25]=-26;  M[30][26]=-26;  M[30][27]=-26;  M[30][28]=-26;
        M[30][29]=-26;  M[30][18]=-26;  M[30][19]=-26;  M[30][11]=-26;  M[30][9]=-26;   M[30][3]=-26;   M[30][15]=-26;
        M[30][6]=-26;   M[30][13]=-26;  M[30][14]=-26;  M[30][16]=-26;
        M[31][20]=-27;  M[31][21]=-27;  M[31][24]=-27;  M[31][25]=-27;  M[31][26]=-27;  M[31][27]=-27;  M[31][28]=-27;
        M[31][29]=-27;  M[31][18]=-27;  M[31][19]=-27;  M[31][11]=-27;  M[31][9]=-27;   M[31][3]=-27;   M[31][15]=-27;
        M[31][6]=-27;   M[31][13]=-27;  M[31][14]=-27;  M[31][16]=-27;
        M[32][20]=-28;  M[32][21]=-28;  M[32][24]=-28;  M[32][25]=-28;  M[32][26]=-28;  M[32][27]=-28;  M[32][28]=-28;
        M[32][29]=-28;  M[32][18]=-28;  M[32][19]=-28;  M[32][11]=-28;  M[32][9]=-28;   M[32][3]=-28;   M[32][15]=-28;
        M[32][6]=-28;   M[32][13]=-28;  M[32][14]=-28;  M[32][16]=-28;
        M[33][20]=-29;  M[33][21]=-29;  M[33][24]=-29;  M[33][25]=-29;  M[33][26]=-29;  M[33][27]=-29;  M[33][28]=-29;
        M[33][29]=-29;  M[33][18]=-29;  M[33][19]=-29;  M[33][11]=-29;  M[33][9]=-29;   M[33][3]=-29;   M[33][15]=-29;
        M[33][6]=-29;   M[33][13]=-29;  M[33][14]=-29;  M[33][16]=-29;
        M[35][9]=-19;   M[35][3]=-19;   M[35][15]=-19;  M[35][6]=-19;   M[35][13]=-19;  M[35][14]=-19;  M[35][16]=-19;
        M[37][1]=-4;
        M[41][22]=-31;  M[41][23]=-31;  M[41][3]=-31;   M[41][10]=-31;
        M[42][22]=-32;  M[42][23]=-32;  M[42][3]=-32;   M[42][10]=-32;
        M[43][22]=-33;  M[43][23]=-33;
        M[43][3]=-33;   M[43][10]=-33;
        M[44][22]=-34;  M[44][23]=-34;  M[44][3]=-34;   M[44][10]=-34;
        M[45][22]=-35;  M[45][23]=-35;  M[45][3]=-35;   M[45][10]=-35;
        M[46][22]=-36;  M[46][23]=-36;  M[46][3]=-36;   M[46][10]=-36;
        M[53][7]=-6;    M[53][8]=-6;    M[53][1]=-6;
        M[55][11]=-20;
        M[56][24]=-21;  M[56][25]=-21;  M[56][26]=-21;  M[56][27]=-21;  M[56][28]=-21;  M[56][29]=-21;  M[56][18]=-21;
        M[56][19]=-21;  M[56][11]=-21;  M[56][9]=-21;   M[56][3]=-21;   M[56][15]=-21;  M[56][6]=-21;   M[56][13]=-21;
        M[56][14]=-21;  M[56][16]=-21;
        M[57][24]=-22;  M[57][25]=-22;  M[57][26]=-22;  M[57][27]=-22;  M[57][28]=-22;  M[57][29]=-22;  M[57][18]=-22;
        M[57][19]=-22;  M[57][11]=-22;  M[57][9]=-22;   M[57][3]=-22;   M[57][15]=-22;  M[57][6]=-22;   M[57][13]=-22;
        M[57][14]=-22;  M[57][16]=-22;
        M[58][20]=-24;  M[58][21]=-24;  M[58][24]=-24;  M[58][25]=-24;  M[58][26]=-24;  M[58][27]=-24;  M[58][28]=-24;
        M[58][29]=-24;  M[58][18]=-24;  M[58][19]=-24;  M[58][11]=-24;  M[58][9]=-24;   M[58][3]=-24;   M[58][15]=-24;
        M[58][6]=-24;   M[58][13]=-24;  M[58][14]=-24;  M[58][16]=-24;
        M[59][20]=-25;  M[59][21]=-25;  M[59][24]=-25;  M[59][25]=-25;  M[59][26]=-25;  M[59][27]=-25;  M[59][28]=-25;
        M[59][29]=-25;  M[59][18]=-25;  M[59][19]=-25;  M[59][11]=-25;  M[59][9]=-25;   M[59][3]=-25;   M[59][15]=-25;
        M[59][6]=-25;   M[59][13]=-25;  M[59][14]=-25;  M[59][16]=-25;
        M[60][20]=-30;  M[60][21]=-30;  M[60][24]=-30;  M[60][25]=-30;  M[60][26]=-30;  M[60][27]=-30;  M[60][28]=-30;
        M[60][29]=-30;  M[60][18]=-30;  M[60][19]=-30;  M[60][11]=-30;  M[60][9]=-30;   M[60][3]=-30;   M[60][15]=-30;
        M[60][6]=-30;   M[60][13]=-30;  M[60][14]=-30;  M[60][16]=-30;
        M[63][9]=-18;   M[63][3]=-18;   M[63][15]=-18;  M[63][6]=-18;   M[63][13]=-18;  M[63][14]=-18;  M[63][16]=-18;
        M[65][9]=-17;   M[65][3]=-17;   M[65][15]=-17;  M[65][6]=-17;   M[65][13]=-17;  M[65][14]=-17;  M[65][16]=-17;
        M[67][9]=-16;   M[67][3]=-16;   M[67][15]=-16;  M[67][6]=-16;   M[67][13]=-16;  M[67][14]=-16;  M[67][16]=-16;


        // inicia el parser
        push("0");
        lee_token(xArchivo(Entrada));
        String s;
        int e;
        int m;

        do {
            s = pila[tope];
            m = M[Integer.parseInt(s)][terminal(a)];
            /*
            System.out.println("LEX: [" +LEX +"]");
            System.out.println("s: [" +s +"]");
            System.out.println("a: [" +a +"]");
            System.out.println("m: [" +m +"]");
            pausa();
             */

            if (m == 1117) {
                //printTabla();
                System.out.println("-> Parser SLR terminado con exito   :)");
                System.out.println("---Codigo generado:---\n" + PROG_c[topePROG_C]);
                //pausa();
                exit(0);
            } else {
                if (m > 0) {
                    push(a);
                    push(String.valueOf(m));

                    cod_shift(m);

                    lee_token(xArchivo(Entrada));
                } else if (m < 0) {

                    cod_reduce(-m);

                    for (int i = 0; i < tamanoParteDerecha[m * -1] * 2; i++) {
                        pop();
                    }
                    e = Integer.parseInt(pila[tope]);
                    push(parteIzquierda[m * -1]);
                    if (M[e][noTerminal(parteIzquierda[m * -1])] == 0) {
                        error();
                    } else {
                        push(String.valueOf(M[e][noTerminal(parteIzquierda[m * -1])]));
                    }
                } else {
                    error();
                }
            }
        } while (true);
    }

    public static void cod_shift(int S) {
        switch (S) {
            case 8, 9 -> {
                TipoDec = LEX;
            }
            case 16 -> {
                VarIzq = LEX;
                TipoEsp = obtenTipo(VarIzq);
            }
            case 26 -> {
                Agr_tab(LEX, TipoDec);
                DecV = DecV + "\tPALABRA\t" + LEX + "\n";
                //PALABRA LEX;
            }
            case 31, 32 -> {
                Temp = LEX;
            }
            case 33 -> {
                Temp = LEX;
                Tipo = obtenTipo(Temp);
            }
        }
    }

    public static void cod_reduce(int R) {
        //R = -R;
        switch (R) {
            case 1 -> { //CHECKIT
                // PROG ->  DATASEC PRIN
                PROG_c[++topePROG_C] = DecV + VarTemps() + "\n" + PRIN_c[topePrin_c--] + "\n\tVUEL\tO\n\tFIN\n";
                //PROG_c = DecV + VarTemps() + "\n" + PRIN_c[topePrin_c] + "\tVUEL\tO\n\tFIN";
            }
            case 8 -> {
                // PRIN ->  { BLQ }
                PRIN_c[++topePrin_c] = BLQ_c[topeBLQ_c--];
            }
            case 11 -> {
                // BLQ  ->  INST BLQ
                aux = BLQ_c[topeBLQ_c--];
                BLQ_c[++topeBLQ_c] = INST_c[topeINST_c--] + aux;
            }
            case 12 -> {
                // BLQ -> INST
                BLQ_c[++topeBLQ_c] = INST_c[topeINST_c--];
            }
            case 13 -> {
                // INST -> COND
                INST_c[++topeINST_c] = COND_c[topeCOND_c--];
            }
            case 14 -> {
                // INST -> ASIG
                INST_c[++topeINST_c] = ASIG_c[topeASIG_c--];
            }
            case 15 -> {
                // INST -> CICLO
                INST_c[++topeINST_c] = CICLO_c[topeCICLO_c--];
            }
            case 16 -> { //CHECKIT
                // COND -> cierto ( EXP ) haz BLQ falso BLQ fin_cond
                PosA = GenEtq();
                PosB = GenEtq();
                aux = EXP_c[topeEXP_c--] + PosA + BLQ_c[topeBLQ_c--] + "\n\tSAL\t" + PosB;
                aux = aux + "\n(" + PosA + ")\tMUE\tRC,RC" + BLQ_c[topeBLQ_c--];
                COND_c[++topeCOND_c] = aux + "\n(" + PosB + ")\tMUE\tRC,RC";
            }
            case 17 -> {
                // COND -> cierto ( EXP ) haz BLQ fin_cond
                PosA = GenEtq();
                PosB = GenEtq();
                aux = EXP_c[topeEXP_c--] + PosA + "\n\tSAL\t" + PosB;
                aux = aux + "\n(" + PosA + ")\tMUE\tRC,RC" + BLQ_c[topeBLQ_c--];
                COND_c[++topeCOND_c] = aux + "\n(" + PosB + ")\tMUE\tRC,RC";
            }
            case 18 -> {
                // CICLO -> mientras ( EXP ) BLQ fin_mientras
                PosA = GenEtq();
                PosB = GenEtq();
                PosC = GenEtq();
                aux = "\n(" + PosA + ")\tMUE\tRC,RC" + EXP_c[topeEXP_c--] + PosB + "n\tSAL\t" + PosC;
                aux = aux + "\n(" + PosB + ")\tMUE\tRC,RC" + BLQ_c[topeBLQ_c--] + "\n\tSAL\t" + PosA;
                CICLO_c[++topeCICLO_c] = aux + "\n(" + PosC + ")\tMUE\tRC,RC";

            }
            case 19 -> {
                // ASIG -> id asig E
                ChkTipo(TipoEsp, E_t[topeE_t--]);
                ASIG_c[++topeASIG_c] = E_c[topeE_c--] + "\n\tMUE\t" + E_v[topeE_v--] + "," + VarIzq + "\n";
            }
            case 20 -> {
                // EXP  ->  E OP E
                aux2 = E_t[topeE_t--];
                aux = E_t[topeE_t--];
                ChkTipo(aux, aux2);
                // se obtiene E_v 2
                Tipo = aux;
                aux2 = E_c[topeE_c--];
                aux = E_c[topeE_c--];
                Temp = aux + aux2;
                aux2 = E_v[topeE_v--];
                aux = E_v[topeE_v--];
                EXP_c[++topeEXP_c] = Temp + "\n\tMUE\t" + aux + ", RA\n\tMUE\t" + aux2 + ", RB " + "\n\t" + instAri("CMP", E_t[topeE_t--]) + "RA, RB " + OP_c[topeOP_c--];

                //aux = E_v[topeE_v--] + ", RB " + instAri("CMP", E_t[topeE_t--]) + "RA, RB " + OP_c[topeOP_c--];
                // se obtiene E_v 1
                //aux = E_v[topeE_v--] + ", RA MUE" + aux;
                // se obtiene E2
                //aux = E_c[topeE_c--] + "MUE" + aux;
                // se obtiene E1
                //aux = E_c[topeE_c--] + aux;
                // el resultado se guarda en EXP_c
                //EXP_c[++topeEXP_c] = aux;
            }
            case 21 -> {
                // E -> E + F
                aux = ChkTipo(E_t[topeE_t], F_t[topeF_t]);
                E_t[++topeE_t] = aux;
                X = GenVar();
                aux = E_c[topeE_c--] + F_c[topeF_c--] + "\n\tMUE\t" + E_v[topeE_v--] + ",RA";
                aux = aux + "\n\t" + instAri("SUM", E_t[topeE_t--]) + "\t" + F_v[topeF_v--];
                aux = aux + "\n\tMUE\tRA," + X;
                E_c[++topeE_c] = aux;
                E_v[++topeE_v] = X;


                //aux = E_t[topeE_t--];
                //E_t[++topeE_t] = ChkTipo(aux, F_t[topeF_t--]);
                //X = GenVar();
                //aux = E_c[topeE_c--];
                //E_c[++topeE_c] = aux + F_c[topeF_c--] + "MUE" + E_v[topeE_v--] +", RA " + instAri("SUM", E_t[topeE_t--]) +
                //       F_v[topeF_v--] + "MUE RA, " +X;
                //E_v[++topeE_v] = X;
            }
            case 22 -> {
                // E -> E - F
                aux = ChkTipo(E_t[topeE_t], F_t[topeF_t]);
                E_t[++topeE_t] = aux;
                X = GenVar();
                aux = E_c[topeE_c--] + F_c[topeF_c--] + "\n\tMUE\t" + E_v[topeE_v--] + ",RA";
                aux = aux + "\n\t" + instAri("SUB", E_t[topeE_t--]) + "\t" + F_v[topeF_v--];
                aux = aux + "\n\tMUE\tRA," + X;
                E_c[++topeE_c] = aux;
                E_v[++topeE_v] = X;


                //aux = E_t[topeE_t--];
                //E_t[++topeE_t] = ChkTipo(aux, F_t[topeF_t--]);
                //X = GenVar();
                //aux = E_c[topeE_c--];
                //E_c[++topeE_c] = aux + F_c[topeF_c--] + "MUE" + E_v[topeE_v--] +", RA " + instAri("SUB", E_t[topeE_t--]) +
                // F_v[topeF_v--] + "MUE RA, " +X;
                //E_v[++topeE_t] = X;
            }
            case 23 -> {
                // E -> F
                E_c[++topeE_c] = F_c[topeF_c--];
                E_v[++topeE_v] = F_v[topeF_v--];
                E_t[++topeE_t] = F_t[topeF_t--];
            }
            case 24 -> {
                // F -> F * S
                aux = ChkTipo(F_t[topeF_t], S_t[topeS_t--]);
                F_t[++topeF_t] = aux;
                X = GenVar();
                aux = F_c[topeF_c--] + S_c[topeS_c--] + "\n\tMUE\t" + F_v[topeF_v--] + ",RA";
                aux = aux + "\n\t" + instAri("MUL", F_t[topeF_t--]) + "\t" + S_v[topeS_v--];
                aux = aux + "\n\tMUE\tRA," + X;
                F_c[++topeF_c] = aux;
                F_v[++topeF_v] = X;

                //aux = F_t[topeF_t--];
                //F_t[++topeF_t] = ChkTipo(aux, S_t[topeS_t--]);
                //X = GenVar();
                //aux = F_c[topeF_c--];
                //F_c[++topeF_c] = aux + S_c[topeS_c--] + "MUE" + F_v[topeF_v--] +", RA " + instAri("MUL", F_t[topeF_t--]) +
                //  S_v[topeS_v--] + "MUE RA, " +X +"\n";
                //F_v[++topeF_v] = X;
            }
            case 25 -> {
                // F -> F / S
                aux = ChkTipo(F_t[topeF_t], S_t[topeS_t--]);
                F_t[++topeF_t] = aux;
                X = GenVar();
                aux = F_c[topeF_c--] + S_c[topeS_c--] + "\n\tMUE\t" + F_v[topeF_v--] + ",RA";
                aux = aux + "\n\t" + instAri("DIV", F_t[topeF_t--]) + "\t" + S_v[topeS_v--];
                aux = aux + "\n\tMUE\tRA," + X;
                F_c[++topeF_c] = aux;
                F_v[++topeF_v] = X;


                //aux = F_t[topeF_t--];
                //F_t[++topeF_t] = ChkTipo(aux, S_t[topeS_t--]);
                //X = GenVar();
                //aux = F_c[topeF_c--];
                //F_c[++topeF_c] = aux + S_c[topeS_c--] + "MUE" + F_v[topeF_v--] +", RA " + instAri("DIV", F_t[topeF_t--]) +
                //      S_v[topeS_v--] + "MUE RA, " +X;
                //F_v[++topeF_v] = X;
            }
            case 26 -> {
                // F -> S
                F_c[++topeF_c] = S_c[topeS_c--];
                F_v[++topeF_v] = S_v[topeS_v--];
                F_t[++topeF_t] = S_t[topeS_t--];
            }
            case 27 -> {
                // S -> ent
                S_c[++topeS_c] = "";
                S_v[++topeS_v] = Temp + 'e';
                S_t[++topeS_t] = "entero";
            }
            case 28 -> {
                // S -> dec
                S_c[++topeS_c] = "";
                S_v[++topeS_v] = Temp + 'f';
                S_t[++topeS_t] = "decimal";
            }
            case 29 -> {
                // S -> id
                S_c[++topeS_c] = "";
                S_v[++topeS_v] = Temp;
                S_t[++topeS_t] = Tipo;
            }
            case 30 -> {
                // S -> ( E )
                S_c[++topeS_c] = E_c[topeE_c--];
                S_v[++topeS_v] = E_v[topeE_v--];
                S_t[++topeS_t] = E_t[topeE_t--];
            }
            case 31 -> {
                // OP -> may
                OP_c[++topeOP_c] = "\n\tSMAY\t";
            }
            case 32 -> {
                // OP -> men
                OP_c[++topeOP_c] = "\n\tSMEN\t";
            }
            case 33 -> {
                // OP -> mayi
                OP_c[++topeOP_c] = "\n\tSMAI\t";
            }
            case 34 -> {
                // OP -> meni
                OP_c[++topeOP_c] = "\n\tSMEI\t";
            }
            case 35 -> {
                // OP -> igual
                OP_c[++topeOP_c] = "\n\tSIG\t";
            }
            case 36 -> {
                // OP -> dif
                OP_c[++topeOP_c] = "\n\tSDIF\t";
            }
        }
    }
}
