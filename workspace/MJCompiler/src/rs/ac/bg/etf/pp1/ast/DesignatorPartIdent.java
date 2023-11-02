// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class DesignatorPartIdent extends DesignatorPart {

    private String desIdentPart;

    public DesignatorPartIdent (String desIdentPart) {
        this.desIdentPart=desIdentPart;
    }

    public String getDesIdentPart() {
        return desIdentPart;
    }

    public void setDesIdentPart(String desIdentPart) {
        this.desIdentPart=desIdentPart;
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
        buffer.append("DesignatorPartIdent(\n");

        buffer.append(" "+tab+desIdentPart);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorPartIdent]");
        return buffer.toString();
    }
}
