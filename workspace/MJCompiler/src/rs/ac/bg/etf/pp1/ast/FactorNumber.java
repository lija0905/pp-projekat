// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:29


package rs.ac.bg.etf.pp1.ast;

public class FactorNumber extends Factor {

    private Integer factorNum;

    public FactorNumber (Integer factorNum) {
        this.factorNum=factorNum;
    }

    public Integer getFactorNum() {
        return factorNum;
    }

    public void setFactorNum(Integer factorNum) {
        this.factorNum=factorNum;
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
        buffer.append("FactorNumber(\n");

        buffer.append(" "+tab+factorNum);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorNumber]");
        return buffer.toString();
    }
}
