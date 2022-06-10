import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*     AUTOR:     CRUZ SANCHEZ MIRIAM FERNANDA
        *         GPO.571
        *
        *                                           PROYECTO FINAL
        *                                       TEORIA DE COMPILADORES
        *                              TRADUCTOR DE LENGUAJE NATURAL A LENGUAJE C
        *
        * DESCRIPCION:
        * Desde un archivo de texto se obtienen los diferentes enunciados en lenguaje natural
        * con sus reglas ya que el lenguaje natural puede ser muy confuso y dificil de determinar.
        * Se determinan tokens para cada enunciado y asi identificarlos y traducirlos a lenguaje C
        * se realiza la traduccion por medio de reglas definidas y se agrega al archivo .c estas nuevas
        * instrucciones .
*/
public class PseudoCompiler {
    Lexer pLexer;
    Parser pParser;

    public PseudoCompiler(){
        pLexer = new Lexer();
        String input = "";

        try{
            /*SE LEE EL ARCHIVO FUENTE EN LENGUAJE NATURAL*/
            FileReader reader = new FileReader("src/prog.txt");
            int character;

            while((character = reader.read()) != -1 ){
                input += (char) character;
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        /*SE PASA AL LEXER PARA CONTINUAR CON EL PARSER */
        ArrayList<Lexer.Token> tokens = pLexer.getTokens(input);
        pParser = new Parser(tokens);

    }

    public static void main(String[] args) {

        new PseudoCompiler();

    }
}
