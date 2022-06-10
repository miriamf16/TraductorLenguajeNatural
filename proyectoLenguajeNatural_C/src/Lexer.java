import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public ArrayList<Token> tokens = new ArrayList<>();
    public int i = 0;

    public enum TokenType {
        INICIOPROGRAMA("en programa|en funcion"),
        BIBLIOTECA("incluir biblioteca"),
        TIPOBIBLIOTECA("matematica| estandarE/s| estandar| cadena| caracter"),
        ENUNVARIABLE("con variable"),
        TIPOVARIABLE("entera| decimal| char"),
        REPITE("repetir desde"),
        HASTA("hasta"),
        INCREMENTO("incremento"),
        LEER("leer"),
        TIPOLEER(" entero| flotante| Cadena"),
        ESCRIBIR("escribir en pantalla"),
        TIPOESCRIBIR("variable"),
        NUMERO("-?[0-9]+(\\.([0-9]+))?"),
        CADENA("\".*\""),
        OPARITMETICO("[*|/|+|-]"),
        ENUNCARITMETICO("operacion aritmetica"),
        OPRELACIONAL("<|>|==|<=|>=|!="),
        IGUAL("="),
        SI("si"),
        ENTONCES("entonces"),
        MIENTRAS("mientras"),
        CONECTOR(" hacer| entonces"),
        ENUNCIADO(" enunciadoLeer| enunciado escribir"),
        COMA(","),
        ESPACIOS("\\s+"),
        ENVARIABLE("en variable"),
        VARIABLE("[a-zA-Z][a-zA-Z0-9]*"),
        PUNTO("."),
        ESTRUCTURA("crearEstructura"),
        ERROR(".+");


        public final String pattern;

        private TokenType(String pattern) {
            this.pattern = pattern;
        }
    }

    public class Token {
        public TokenType type;
        public String data;

        public Token(TokenType type, String data) {
            this.type = type;
            this.data = data;
        }

        public String toString() {
            return String.format("(%s \"%s\")", type.name(), data);
        }
    }


    /*METODOS*/
    public ArrayList<Token> getTokens(String input) {
        ArrayList<Token> tokens = new ArrayList<Token>();
        StringBuffer tokenPatternsBuffer = new StringBuffer();

        for (TokenType tokenType : TokenType.values())
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        //System.out.println(tokenPatternsBuffer);

        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));
        Matcher matcher = tokenPatterns.matcher(input);

        while (matcher.find()) {
            for (TokenType t : TokenType.values()) {
                if (matcher.group(TokenType.ESPACIOS.name()) != null)
                    continue;
                else if (matcher.group(t.name()) != null) {
                    tokens.add(new Token(t, matcher.group(t.name())));
                    break;
                }
            }
        }
        return tokens;
    }

    public Token nextToken(){
        int limite = tokens.size();

        while(i<limite){
            i++;
            return tokens.get(i-1);
        }
        return new Token(TokenType.ERROR,"+");
    }

}
