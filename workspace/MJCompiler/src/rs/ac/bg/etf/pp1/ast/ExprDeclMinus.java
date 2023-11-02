// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:29


package rs.ac.bg.etf.pp1.ast;

public class ExprDeclMinus extends Expr {

    private MinusSign MinusSign;
    private TermList TermList;

    public ExprDeclMinus (MinusSign MinusSign, TermList TermList) {
        this.MinusSign=MinusSign;
        if(MinusSign!=null) MinusSign.setParent(this);
        this.TermList=TermList;
        if(TermList!=null) TermList.setParent(this);
    }

    public MinusSign getMinusSign() {
        return MinusSign;
    }

    public void setMinusSign(MinusSign MinusSign) {
        this.MinusSign=MinusSign;
    }

    public TermList getTermList() {
        return TermList;
    }

    public void setTermList(TermList TermList) {
        this.TermList=TermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MinusSign!=null) MinusSign.accept(visitor);
        if(TermList!=null) TermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MinusSign!=null) MinusSign.traverseTopDown(visitor);
        if(TermList!=null) TermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MinusSign!=null) MinusSign.traverseBottomUp(visitor);
        if(TermList!=null) TermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprDeclMinus(\n");

        if(MinusSign!=null)
            buffer.append(MinusSign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TermList!=null)
            buffer.append(TermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprDeclMinus]");
        return buffer.toString();
    }
}
