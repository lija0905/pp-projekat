// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class FormParsErrorRParen extends MethodDeclPart {

    private ReturnMethodType ReturnMethodType;
    private LocalVarDecl LocalVarDecl;
    private StatementList StatementList;

    public FormParsErrorRParen (ReturnMethodType ReturnMethodType, LocalVarDecl LocalVarDecl, StatementList StatementList) {
        this.ReturnMethodType=ReturnMethodType;
        if(ReturnMethodType!=null) ReturnMethodType.setParent(this);
        this.LocalVarDecl=LocalVarDecl;
        if(LocalVarDecl!=null) LocalVarDecl.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public ReturnMethodType getReturnMethodType() {
        return ReturnMethodType;
    }

    public void setReturnMethodType(ReturnMethodType ReturnMethodType) {
        this.ReturnMethodType=ReturnMethodType;
    }

    public LocalVarDecl getLocalVarDecl() {
        return LocalVarDecl;
    }

    public void setLocalVarDecl(LocalVarDecl LocalVarDecl) {
        this.LocalVarDecl=LocalVarDecl;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ReturnMethodType!=null) ReturnMethodType.accept(visitor);
        if(LocalVarDecl!=null) LocalVarDecl.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ReturnMethodType!=null) ReturnMethodType.traverseTopDown(visitor);
        if(LocalVarDecl!=null) LocalVarDecl.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ReturnMethodType!=null) ReturnMethodType.traverseBottomUp(visitor);
        if(LocalVarDecl!=null) LocalVarDecl.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsErrorRParen(\n");

        if(ReturnMethodType!=null)
            buffer.append(ReturnMethodType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LocalVarDecl!=null)
            buffer.append(LocalVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsErrorRParen]");
        return buffer.toString();
    }
}
