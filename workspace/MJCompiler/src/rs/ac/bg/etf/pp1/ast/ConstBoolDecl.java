// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class ConstBoolDecl extends ConstDeclPart {

    private String boolConst;
    private String bool;

    public ConstBoolDecl (String boolConst, String bool) {
        this.boolConst=boolConst;
        this.bool=bool;
    }

    public String getBoolConst() {
        return boolConst;
    }

    public void setBoolConst(String boolConst) {
        this.boolConst=boolConst;
    }

    public String getBool() {
        return bool;
    }

    public void setBool(String bool) {
        this.bool=bool;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstBoolDecl(\n");

        buffer.append(" "+tab+boolConst);
        buffer.append("\n");

        buffer.append(" "+tab+bool);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstBoolDecl]");
        return buffer.toString();
    }
}
