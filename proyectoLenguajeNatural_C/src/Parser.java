import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Parser {
    ArrayList<Lexer.Token> tokens;
    int tokenIndex = 0;
    SymbolTable symTable ;
    int tokenIndexAux ;
    int tokenBiblioteca;
    File file ;

    public Parser(ArrayList<Lexer.Token> tokens){
       try {

           file = new File("src/traduccion.c");
           if(!file.exists())
               file.createNewFile();
       }catch(Exception e){
           e.printStackTrace();
       }

        this.tokens = tokens;
        symTable = new SymbolTable();
        symTable.initTypeSystem();
        programa();
    }
    /*METODOS */
    public boolean token(String name){
        if(tokens.get(tokenIndex).type.name().equals(name)){
            System.out.println(tokens.get(tokenIndex).type.name()+ " "
            + tokens.get(tokenIndex).data);
            tokenIndex++;
            return true;
        }
        return false;
    }


    private void programa() {
        biblioteca();

        if (tokens.size() > tokenIndex){
            validaBiblioteca();
        }

        if (tokens.size() > tokenIndex) {
            programa_funcion();
        }
        if (tokens.size() > tokenIndex) {
            variable();
        }
        if (tokens.size() > tokenIndex) {
            ciclo_for();
        }
        if(tokens.size() > tokenIndex) {
            enunciadoLeer();
        }
        if (tokens.size() > tokenIndex) {
            enunciadoImprime();
        }
        if (tokens.size() > tokenIndex) {
            ciclo_while();
        }
        if (tokens.size() > tokenIndex) {
            estructura_if();
        }
        cierrePrograma_funcion();

    }

    private void  biblioteca(){
        if(token("BIBLIOTECA"))
            if(token("TIPOBIBLIOTECA"))
               if( match(tokenIndex-1,"matematica")) {
                   agrega("\n#include<math.h>\n");
               }
                else {
                   if (match(tokenIndex-1, " estandarE/s")) {
                       tokenBiblioteca = tokenIndex+3;
                       agrega("#include<stdio.h>\n");
                   }
                   else{
                       if (match(tokenIndex-1," estandar")){
                           agrega("\n#include<stdlib.h\n>");
                       }
                       else{
                           if(match(tokenIndex-1," cadena")){
                               agrega("\n#include<string.h>\n");
                           }
                           else{
                               if(match(tokenIndex-1," caracter")){
                                   agrega("\n#include<ctype.h>\n");
                               }
                           }
                       }
                   }
               }

    }

    private void programa_funcion(){
        if(token("INICIOPROGRAMA"))
            if(match(tokenIndex-1,"en programa")) {
                tokenIndexAux = tokenIndex-1;
                agrega("\nint main(){\n");
            }
            else if(match (tokenIndex-1,"en funcion"))
            {
                tokenIndexAux = tokenIndex;
                agrega("\nmiFuncion(){\n");
            }
    }

    private void variable(){
        if(token("ENUNVARIABLE"))
            if(token("TIPOVARIABLE")){
                if( match(tokenIndex-1,"entera")) {
                    BuiltInTypeSymbol tipo = new BuiltInTypeSymbol("int");
                    symTable.define(new VariableSymbol("x",tipo));
                    agrega("\tint x;\n");
                } else {
                    if (match(tokenIndex - 1, " decimal")) {
                        BuiltInTypeSymbol tipo = new BuiltInTypeSymbol("float");
                        symTable.define(new VariableSymbol("y", tipo));
                        agrega("\tfloat y;\n");
                    } else {
                        {
                            BuiltInTypeSymbol tipo = new BuiltInTypeSymbol("char");
                            symTable.define(new VariableSymbol("z", tipo));
                            agrega("\tchar z[10];\n");
                        }
                    }
                }
            }

    }

    private void ciclo_for(){
        int auxNum=0;
        int auxNum2=0;
        int inc=0;
        String cadena = "";
        if(token("REPITE"))
            if(token("NUMERO")){
                auxNum = tokenIndex-1;
            }
        if(token("HASTA"))
            if(token("NUMERO")){
                auxNum2 = tokenIndex-1;
            }
        if(token("INCREMENTO"))
            if(token("NUMERO")) {
                inc = tokenIndex - 1;

                agrega("\tint i;\n\tfor(i=");
                cadena = tokens.get(auxNum).data;
                agrega(cadena);
                agrega(";i<");
                cadena = tokens.get(auxNum2).data;
                agrega(cadena);
                agrega(";i");
                if (tokens.get(inc).data.equals("1")) {
                    agrega("++)");
                } else {
                    agrega("=i+");
                    agrega(tokens.get(inc).data);
                    agrega(")\n");
                }
            }

        if(token("ENUNCIADO")){
            if(tokens.get(tokenIndex-1).data.equals(" enunciadoLeer")){
                agrega("\t{\n");
                enunciadoLeer();
                agrega("\t}\n");
            }
            else{
                agrega("\t{\n");
                enunciadoImprime();
                agrega("\t}\n");
            }
        }
    }

    private void validaBiblioteca(){
        String cadena="";
        if (!match(tokenBiblioteca," estandarE/s")){
            cadena = tokens.get(tokenBiblioteca+1).data;
            if(cadena.equals( "matematica")){
                agregaInicio("#include<stdio.h>\n #include<math.h>");
            }
        }
    }

    private void enunciadoLeer(){
        int aux=0;
        int aux2=0;
        String cadena="";

        if(token("LEER"))
            if(token("TIPOLEER"))
                if(token("ENVARIABLE")){
                if (match(tokenIndex-2," entero")){
                    cadena = "\t scanf(\"%d\",&";
                    agrega(cadena);
                    cadena = symTable.getNombreSimb("x");
                    agrega(cadena);
                    agrega(");\n");
                }
                else {
                    if (match(tokenIndex-2," flotante")){
                        cadena = "\t scanf(\"%f\",&";
                        agrega(cadena);
                        cadena = symTable.getNombreSimb("y");
                        agrega(cadena);
                        agrega(");\n");
                    } else {
                            cadena = "\t scanf(\"%s\",&";
                            agrega(cadena);
                            cadena = symTable.getNombreSimb("z");
                            agrega(cadena);
                            agrega(");\n");
                    }
                }
            }

    }

    private void enunciadoImprime(){

        String cadena;
        if(token("ESCRIBIR"))
            if(token("CADENA")) {
                agrega("\tprintf(");
                cadena = tokens.get(tokenIndex-1).data;
                agrega(cadena);
                agrega(");\n");

            }
            else{
                if(token("TIPOESCRIBIR"))
                    if(token("TIPOVARIABLE")){
                        if( match(tokenIndex-1,"entera")) {
                            cadena = "\t printf(\"%d\",";
                            agrega(cadena);
                            cadena = symTable.getNombreSimb("x");
                            agrega(cadena);
                            agrega(");\n");

                        } else {
                            if (match(tokenIndex - 1, " decimal")) {
                                cadena = "\n\t printf(\"%f\",";
                                agrega(cadena);
                                cadena = symTable.getNombreSimb("y");
                                agrega(cadena);
                                agrega(");\n");
                            } else {
                                cadena = "\t printf(\"%s\",";
                                agrega(cadena);
                                cadena = symTable.getNombreSimb("z");
                                agrega(cadena);
                                agrega(");\n");
                            }
                        }


                    }
            }
    }

    private void ciclo_while(){
        String cadena;
        if(token("MIENTRAS"))
            if(token("VARIABLE"))
                if(token("OPRELACIONAL"))
                    if(token("NUMERO"))
                        if(token("CONECTOR")){
                            agrega("\twhile(");
                            cadena =tokens.get(tokenIndex-4).data;
                            cadena += tokens.get(tokenIndex-3).data;
                            cadena += tokens.get(tokenIndex-2).data;
                            agrega(cadena);
                            agrega(")\n");
                        }

        if(token("ENUNCIADO")){
            System.out.println(tokens.get(tokenIndex-1).data);
            if(tokens.get(tokenIndex-1).data.equals(" enunciadoLeer")){
                agrega("\t{\n");
                enunciadoLeer();
                agrega("\t}\n");
            }
            else{
                agrega("\t{\n");
                enunciadoImprime();
                agrega("\t}\n");
            }
        }

    }

    private void estructura_if(){
        String  cadena;
        if(token("SI"))
            if(token("VARIABLE"))
                if(token("OPRELACIONAL"))
                    if(token("NUMERO"))
                        if(token("CONECTOR")){
                            agrega("\tif(");
                            cadena =tokens.get(tokenIndex-4).data;
                            cadena += tokens.get(tokenIndex-3).data;
                            cadena += tokens.get(tokenIndex-2).data;
                            agrega(cadena);
                            agrega(")\n");
                        }
        if(token("ENUNCIADO")){
            if(tokens.get(tokenIndex-1).data.equals(" enunciadoLeer")){
                agrega("\t{\n");
                enunciadoLeer();
                agrega("\t}\n");
            }
            else{
                agrega("\t{\n");
                enunciadoImprime();
                agrega("\t}\n");
            }
        }
    }

    private void cierrePrograma_funcion(){
        int aux = tokenIndex;
        tokenIndex = tokenIndexAux;
        if(match(tokenIndex,"en programa"))
        {
            agrega("return 0;\n}\n");
            tokenIndex = aux;
        }
        else
        {
            agrega("}\n ");
        }
    }

    private void estructura(){
        String cadena;

        if(token("ESTRUCTURA")) {
            agrega("struct");
            if (token("VARIABLE")) {
                cadena = tokens.get(tokenIndex - 1).data;
                agrega(cadena);
                agrega("{\n");
            }
            if (token("ENUNVARIABLE"))
                if (token("TIPOVARIABLE"))
                    if (token("VARIABLE")) {
                        cadena = tokens.get(tokenIndex - 1).data;
                        if (tokens.get(tokenIndex - 1).data.equals(" entera")) {
                            agrega("int ");
                            agrega(cadena);
                            agrega(";\n};\n");
                        }

                    }

        }


    }
    private void agrega(String texto){
        try {
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter((fw));
            bw.write(texto);
            bw.close();
        }
        catch (Exception e){
            System.out.println("Error al escribir en archivo"+ e);
        }
    }

    private void agregaInicio(String texto){
        try {
            FileWriter fw = new FileWriter(file,false);
            BufferedWriter bw = new BufferedWriter((fw));
           bw.append(texto);
            bw.close();
        }
        catch (Exception e){
            System.out.println("Error al escribir en archivo"+ e);
        }
    }

    private boolean match(int tokenIndexAct ,String cadena) {
        if (tokens.get(tokenIndexAct).data.equals(cadena))
            return true;

        return false;
    }

}

