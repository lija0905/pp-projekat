// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class FormParsArray extends FormParsPart {

    private Type Type;
    private String fparsName;

    public FormParsArray (Type Type, String fparsName) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.fparsName=fparsName;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getFparsName() {
        return fparsName;
    }

    public void setFparsName(String fparsName) {
        this.fparsName=fparsName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsArray(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+fparsName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsArray]");
        return buffer.toString();
    }
}
