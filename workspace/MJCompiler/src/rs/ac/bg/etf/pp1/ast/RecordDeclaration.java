// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class RecordDeclaration extends DeclPart {

    private RecordDecl RecordDecl;

    public RecordDeclaration (RecordDecl RecordDecl) {
        this.RecordDecl=RecordDecl;
        if(RecordDecl!=null) RecordDecl.setParent(this);
    }

    public RecordDecl getRecordDecl() {
        return RecordDecl;
    }

    public void setRecordDecl(RecordDecl RecordDecl) {
        this.RecordDecl=RecordDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RecordDecl!=null) RecordDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RecordDecl!=null) RecordDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RecordDecl!=null) RecordDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("RecordDeclaration(\n");

        if(RecordDecl!=null)
            buffer.append(RecordDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RecordDeclaration]");
        return buffer.toString();
    }
}
