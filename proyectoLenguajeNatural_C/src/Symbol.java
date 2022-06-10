public class Symbol {

    String name;
    Type type;

    /*CONTRUCTORES*/
    public Symbol (String name){
        this.name= name;
    }

    public Symbol (String name, Type type){
        this(name);
        this.type = type;
    }

    /*METODOS*/
    public String getName(){
        return name;
    }

    public String toString(){
        if(type != null)
            return '<'+getName()+":"+type+'>';
        return getName();

    }
}
