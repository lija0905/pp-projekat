// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class GotoLabel implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String gotoLabel;

    public GotoLabel (String gotoLabel) {
        this.gotoLabel=gotoLabel;
    }

    public String getGotoLabel() {
        return gotoLabel;
    }

    public void setGotoLabel(String gotoLabel) {
        this.gotoLabel=gotoLabel;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
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
        buffer.append("GotoLabel(\n");

        buffer.append(" "+tab+gotoLabel);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GotoLabel]");
        return buffer.toString();
    }
}
