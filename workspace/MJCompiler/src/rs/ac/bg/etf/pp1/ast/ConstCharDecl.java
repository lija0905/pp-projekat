// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class ConstCharDecl extends ConstDeclPart {

    private String charConst;
    private Character ch;

    public ConstCharDecl (String charConst, Character ch) {
        this.charConst=charConst;
        this.ch=ch;
    }

    public String getCharConst() {
        return charConst;
    }

    public void setCharConst(String charConst) {
        this.charConst=charConst;
    }

    public Character getCh() {
        return ch;
    }

    public void setCh(Character ch) {
        this.ch=ch;
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
        buffer.append("ConstCharDecl(\n");

        buffer.append(" "+tab+charConst);
        buffer.append("\n");

        buffer.append(" "+tab+ch);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstCharDecl]");
        return buffer.toString();
    }
}
