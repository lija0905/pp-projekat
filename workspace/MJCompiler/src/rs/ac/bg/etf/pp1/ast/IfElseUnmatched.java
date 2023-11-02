// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class IfElseUnmatched extends Unmatched {

    private Label Label;
    private IfStatement IfStatement;
    private ElseTerm ElseTerm;
    private Unmatched Unmatched;

    public IfElseUnmatched (Label Label, IfStatement IfStatement, ElseTerm ElseTerm, Unmatched Unmatched) {
        this.Label=Label;
        if(Label!=null) Label.setParent(this);
        this.IfStatement=IfStatement;
        if(IfStatement!=null) IfStatement.setParent(this);
        this.ElseTerm=ElseTerm;
        if(ElseTerm!=null) ElseTerm.setParent(this);
        this.Unmatched=Unmatched;
        if(Unmatched!=null) Unmatched.setParent(this);
    }

    public Label getLabel() {
        return Label;
    }

    public void setLabel(Label Label) {
        this.Label=Label;
    }

    public IfStatement getIfStatement() {
        return IfStatement;
    }

    public void setIfStatement(IfStatement IfStatement) {
        this.IfStatement=IfStatement;
    }

    public ElseTerm getElseTerm() {
        return ElseTerm;
    }

    public void setElseTerm(ElseTerm ElseTerm) {
        this.ElseTerm=ElseTerm;
    }

    public Unmatched getUnmatched() {
        return Unmatched;
    }

    public void setUnmatched(Unmatched Unmatched) {
        this.Unmatched=Unmatched;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Label!=null) Label.accept(visitor);
        if(IfStatement!=null) IfStatement.accept(visitor);
        if(ElseTerm!=null) ElseTerm.accept(visitor);
        if(Unmatched!=null) Unmatched.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Label!=null) Label.traverseTopDown(visitor);
        if(IfStatement!=null) IfStatement.traverseTopDown(visitor);
        if(ElseTerm!=null) ElseTerm.traverseTopDown(visitor);
        if(Unmatched!=null) Unmatched.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Label!=null) Label.traverseBottomUp(visitor);
        if(IfStatement!=null) IfStatement.traverseBottomUp(visitor);
        if(ElseTerm!=null) ElseTerm.traverseBottomUp(visitor);
        if(Unmatched!=null) Unmatched.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfElseUnmatched(\n");

        if(Label!=null)
            buffer.append(Label.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfStatement!=null)
            buffer.append(IfStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseTerm!=null)
            buffer.append(ElseTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Unmatched!=null)
            buffer.append(Unmatched.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfElseUnmatched]");
        return buffer.toString();
    }
}
