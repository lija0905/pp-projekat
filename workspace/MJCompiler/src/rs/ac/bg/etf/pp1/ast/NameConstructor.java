// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class NameConstructor extends ConstructorName {

    private String constructorName;

    public NameConstructor (String constructorName) {
        this.constructorName=constructorName;
    }

    public String getConstructorName() {
        return constructorName;
    }

    public void setConstructorName(String constructorName) {
        this.constructorName=constructorName;
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
        buffer.append("NameConstructor(\n");

        buffer.append(" "+tab+constructorName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NameConstructor]");
        return buffer.toString();
    }
}
