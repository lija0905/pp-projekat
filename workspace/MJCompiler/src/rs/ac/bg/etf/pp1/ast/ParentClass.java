// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class ParentClass extends ClassName {

    private String parentName;

    public ParentClass (String parentName) {
        this.parentName=parentName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName=parentName;
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
        buffer.append("ParentClass(\n");

        buffer.append(" "+tab+parentName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ParentClass]");
        return buffer.toString();
    }
}
