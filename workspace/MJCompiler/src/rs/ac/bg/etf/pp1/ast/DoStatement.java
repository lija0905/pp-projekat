// generated with ast extension for cup
// version 0.8
// 26/0/2022 3:3:28


package rs.ac.bg.etf.pp1.ast;

public class DoStatement extends SingleStatement {

    private DoWhile DoWhile;
    private Statement Statement;
    private WhileStmt WhileStmt;

    public DoStatement (DoWhile DoWhile, Statement Statement, WhileStmt WhileStmt) {
        this.DoWhile=DoWhile;
        if(DoWhile!=null) DoWhile.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileStmt=WhileStmt;
        if(WhileStmt!=null) WhileStmt.setParent(this);
    }

    public DoWhile getDoWhile() {
        return DoWhile;
    }

    public void setDoWhile(DoWhile DoWhile) {
        this.DoWhile=DoWhile;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public WhileStmt getWhileStmt() {
        return WhileStmt;
    }

    public void setWhileStmt(WhileStmt WhileStmt) {
        this.WhileStmt=WhileStmt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoWhile!=null) DoWhile.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileStmt!=null) WhileStmt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoWhile!=null) DoWhile.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileStmt!=null) WhileStmt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoWhile!=null) DoWhile.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileStmt!=null) WhileStmt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoStatement(\n");

        if(DoWhile!=null)
            buffer.append(DoWhile.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileStmt!=null)
            buffer.append(WhileStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoStatement]");
        return buffer.toString();
    }
}
