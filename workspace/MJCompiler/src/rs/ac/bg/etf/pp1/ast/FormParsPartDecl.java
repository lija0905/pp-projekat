// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class FormParsPartDecl extends FormPars {

    private FormParsPart FormParsPart;

    public FormParsPartDecl (FormParsPart FormParsPart) {
        this.FormParsPart=FormParsPart;
        if(FormParsPart!=null) FormParsPart.setParent(this);
    }

    public FormParsPart getFormParsPart() {
        return FormParsPart;
    }

    public void setFormParsPart(FormParsPart FormParsPart) {
        this.FormParsPart=FormParsPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsPart!=null) FormParsPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsPart!=null) FormParsPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsPart!=null) FormParsPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsPartDecl(\n");

        if(FormParsPart!=null)
            buffer.append(FormParsPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsPartDecl]");
        return buffer.toString();
    }
}
