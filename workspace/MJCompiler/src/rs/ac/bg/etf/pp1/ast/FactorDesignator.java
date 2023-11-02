// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:29


package rs.ac.bg.etf.pp1.ast;

public class FactorDesignator extends Factor {

    private Designator Designator;
    private ActParsFactor ActParsFactor;

    public FactorDesignator (Designator Designator, ActParsFactor ActParsFactor) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ActParsFactor=ActParsFactor;
        if(ActParsFactor!=null) ActParsFactor.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public ActParsFactor getActParsFactor() {
        return ActParsFactor;
    }

    public void setActParsFactor(ActParsFactor ActParsFactor) {
        this.ActParsFactor=ActParsFactor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(ActParsFactor!=null) ActParsFactor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ActParsFactor!=null) ActParsFactor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ActParsFactor!=null) ActParsFactor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDesignator(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsFactor!=null)
            buffer.append(ActParsFactor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDesignator]");
        return buffer.toString();
    }
}
