// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:29


package rs.ac.bg.etf.pp1.ast;

public class FactorChar extends Factor {

    private Character factorChar;

    public FactorChar (Character factorChar) {
        this.factorChar=factorChar;
    }

    public Character getFactorChar() {
        return factorChar;
    }

    public void setFactorChar(Character factorChar) {
        this.factorChar=factorChar;
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
        buffer.append("FactorChar(\n");

        buffer.append(" "+tab+factorChar);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorChar]");
        return buffer.toString();
    }
}
