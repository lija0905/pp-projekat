// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class GoToStatement extends SingleStatement {

    private GotoLabel GotoLabel;

    public GoToStatement (GotoLabel GotoLabel) {
        this.GotoLabel=GotoLabel;
        if(GotoLabel!=null) GotoLabel.setParent(this);
    }

    public GotoLabel getGotoLabel() {
        return GotoLabel;
    }

    public void setGotoLabel(GotoLabel GotoLabel) {
        this.GotoLabel=GotoLabel;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GotoLabel!=null) GotoLabel.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GotoLabel!=null) GotoLabel.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GotoLabel!=null) GotoLabel.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GoToStatement(\n");

        if(GotoLabel!=null)
            buffer.append(GotoLabel.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GoToStatement]");
        return buffer.toString();
    }
}
