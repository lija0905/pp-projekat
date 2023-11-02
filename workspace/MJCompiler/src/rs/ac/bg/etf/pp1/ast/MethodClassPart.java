// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class MethodClassPart extends ClassMethods {

    private MethodDeclPart MethodDeclPart;

    public MethodClassPart (MethodDeclPart MethodDeclPart) {
        this.MethodDeclPart=MethodDeclPart;
        if(MethodDeclPart!=null) MethodDeclPart.setParent(this);
    }

    public MethodDeclPart getMethodDeclPart() {
        return MethodDeclPart;
    }

    public void setMethodDeclPart(MethodDeclPart MethodDeclPart) {
        this.MethodDeclPart=MethodDeclPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodDeclPart!=null) MethodDeclPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodDeclPart!=null) MethodDeclPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodDeclPart!=null) MethodDeclPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodClassPart(\n");

        if(MethodDeclPart!=null)
            buffer.append(MethodDeclPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodClassPart]");
        return buffer.toString();
    }
}
