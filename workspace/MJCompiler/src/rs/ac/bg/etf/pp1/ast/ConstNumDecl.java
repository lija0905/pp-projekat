// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class ConstNumDecl extends ConstDeclPart {

    private String numConst;
    private Integer num;

    public ConstNumDecl (String numConst, Integer num) {
        this.numConst=numConst;
        this.num=num;
    }

    public String getNumConst() {
        return numConst;
    }

    public void setNumConst(String numConst) {
        this.numConst=numConst;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num=num;
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
        buffer.append("ConstNumDecl(\n");

        buffer.append(" "+tab+numConst);
        buffer.append("\n");

        buffer.append(" "+tab+num);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstNumDecl]");
        return buffer.toString();
    }
}
