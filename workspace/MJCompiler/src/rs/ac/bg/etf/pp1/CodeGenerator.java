package rs.ac.bg.etf.pp1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import rs.ac.bg.etf.pp1.CounterVisitor.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class CodeGenerator extends VisitorAdaptor{
	
	private int mainPc;
	
	private boolean added_length = false;
	private boolean factNewFound = false;
	private boolean whileFound = false;
	private boolean fieldArray = false;
	private int mulOp;
	private int addOp;
	private int relOp;
	
	private ArrayList<Integer> gotoAddresses = new ArrayList<>();
	private ArrayList<String> labels = new ArrayList<>();
	
	private ArrayList<ArrayList<Integer>> ifAddresses = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> jmpAddresses = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> jmpCondAddresses = new ArrayList<>();

	private ArrayList<Integer> doAddresses = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> breakAddresses = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> continueAddresses = new ArrayList<>();
 	
	public int getMainPc() { return mainPc; }
	
	/* print statement */
	public void visit(PrintStatement printStmt) {
		//ako je bool ispisuje true/false?
		int kind = printStmt.getExpr().struct.getKind();
		if (kind == Struct.Array) kind = printStmt.getExpr().struct.getElemType().getKind();
		if (kind == Struct.Char) {
			if (!added_length) Code.loadConst(1);
			Code.put(Code.bprint);
			
		} else { //ovo je else, treba da se prepoznaje sve za Factor->stavlja na stek? 
			if (!added_length) Code.loadConst(5);
			Code.put(Code.print);
		}	
		added_length = false;
	}
	
	/* read statemet */
	public void visit(ReadStatement readStmt) {
		int kind = readStmt.getDesignator().obj.getType().getKind();
		
		if (kind == Struct.Char) Code.put(Code.bread);
		else Code.put(Code.read);
		
		if (readStmt.getDesignator().obj.getKind() == Obj.Elem) { Code.put(Code.astore);
		} else Code.store(readStmt.getDesignator().obj);
	}
	
	/* adding ord and chr and len*/
	public void visit(ProgName progName) {
		Obj ord = Tab.find("ord");
		ord.setAdr(Code.pc);
		Code.put(Code.enter); Code.put(1); Code.put(1);
		Code.put(Code.load); Code.put(0); Code.put(Code.exit); Code.put(Code.return_);
		
		/* ord */
		Obj chr = Tab.find("chr");
		chr.setAdr(Code.pc);
		Code.put(Code.enter); Code.put(1); Code.put(1);
		Code.put(Code.load); Code.put(0); Code.put(Code.exit); Code.put(Code.return_);
		
		/* len */
		Obj len = Tab.find("len");
		len.setAdr(Code.pc);
		Code.put(Code.enter); Code.put(1); Code.put(1);
		Code.put(Code.load); Code.put(0); Code.put(Code.arraylength); Code.put(Code.exit); Code.put(Code.return_);
		
	}
	
	public void visit (PrintNumberConst fctNum) {
		Obj con = Tab.insert(Obj.Con, "$", new Struct(Struct.Int));
		con.setLevel(0);
		con.setAdr(fctNum.getN1());
		
		added_length = true;
		
		Code.load(con);
	}
	
	public void visit(FactorNumber fctNum) {
		Obj con = Tab.insert(Obj.Con, "$", fctNum.struct);
		con.setLevel(0);
		con.setAdr(fctNum.getFactorNum());
		
		Code.load(con);
	}
	
	public void visit(FactorBool fctBool) {
		Obj con = Tab.insert(Obj.Con, "$", fctBool.struct);
		con.setLevel(0);
		if (fctBool.getFactorBool().equals("true")) con.setAdr(1);
		else con.setAdr(0);
	
		Code.load(con);
	}
	
	public void visit(FactorChar fctChar) {
		Obj con = Tab.insert(Obj.Con, "$", fctChar.struct);
		con.setLevel(0);
		con.setAdr((int)fctChar.getFactorChar());
		
		Code.load(con);
	}
	
	public void visit(MethodReturnType methDecl) {
		if ("main".equals(methDecl.getMethodName())) {
			mainPc = Code.pc;
		}
		
		SyntaxNode parent = methDecl.getParent();
		
		methDecl.obj.setAdr(Code.pc);
		SyntaxNode methodNode = methDecl.getParent();
		
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
	
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());

		
	}
	
	public void visit(VoidReturnType methDecl) {
		if ("main".equals(methDecl.getMethodName())) {
			mainPc = Code.pc;
		}
		
		SyntaxNode parent = methDecl.getParent();
		
		methDecl.obj.setAdr(Code.pc);
		SyntaxNode methodNode = methDecl.getParent();
		
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
	
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());

	}
	
	//assign
	public void visit(AssignDesignatorStat assignStmt) {
		if (factNewFound) {
			Code.store(assignStmt.getDesignator().obj);
			factNewFound = false;
		} else if (assignStmt.getDesignator().obj.getKind() == Obj.Elem){
			/*Obj arr = Tab.find(assignStmt.getDesignator().obj.getName());
			Code.load(arr);*/
			/*Code.put(Code.dup_x2);
			Code.put(Code.pop);*/
			if (assignStmt.getDesignator().obj.getType().getKind() == Struct.Char) Code.put(Code.bastore);
			else Code.put(Code.astore);		
		} else {
		
		Code.store(assignStmt.getDesignator().obj);
		}
	}
	
	public void visit(NoDesignatorList designator){
		
	}
	
	
	//moram da vidim kako je niz
	public void visit(DesignatorDecl designator) {
		SyntaxNode parent = designator.getParent();
			
		if((AssignDesignatorStat.class != parent.getClass() && ActParsDesignator.class != parent.getClass() && NoActPartDes.class != parent.getClass() 
				&& ReadStatement.class != parent.getClass()) && NoFactorsDes.class != parent.getClass() && FactorDesignator.class != parent.getClass()){
			
			if (designator.obj.getKind() == Obj.Elem) {
			        if (parent.getClass() == IncDesignator.class || parent.getClass() == DecDesignator.class) Code.put(Code.dup2);
					if (designator.obj.getType().getKind() == Struct.Char)Code.put(Code.baload);
					else Code.put(Code.aload);
				} else {
					Code.load(designator.obj);
				}
			}
		
	}
	
	public void visit(DesignatorListDecl des) {
		if (des.getDesignatorPart() instanceof DesignatorPartExpr) {
			if (des.getDesignatorList().obj.getType().getKind() == Struct.Array && des.getDesignatorList().obj.getKind() == Obj.Fld) {
				Code.put(Code.dup_x1); Code.put(Code.pop);
			}
			Code.load(des.getDesignatorList().obj);
			Code.put(Code.dup_x1);
			Code.put(Code.pop);
		}
		if (des.getDesignatorPart() instanceof DesignatorPartIdent) {
			Code.load(des.getDesignatorList().obj);
		}
	}
	
	/* inicijalizovanje niza */
	public void visit(FactorNew factNew) {
		factNewFound = true;
		Code.put(Code.newarray);
		if (factNew.struct.getKind() == Struct.Char) Code.put(0);
		else Code.put(1);
		
	}
	
	/* operatori */
	
	public void visit(FactorsList factList) {
		Code.put(mulOp);
	}
	
	public void visit(TermsList termList) {
		
		if (termList.getAddop() instanceof Plusop) {
			Code.put(Code.add);
		} 
		if (termList.getAddop() instanceof Minusop) {
			Code.put(Code.sub);
		}
	}
	
	public void visit(ExprDeclMinus neg) {
		Code.put(Code.neg);
	}
	
	public void visit(Mulop mul) {
		mulOp = Code.mul;
	}
	
	public void visit(Divop div) {
		mulOp = Code.div;
	}
	
	public void visit(Modop mod) {
		mulOp = Code.rem;
	}
	
	public void visit(Plusop plus) {
		addOp = Code.add;
	}
	
	public void visit(Minusop minus) {
		addOp = Code.sub;
	}
	/* inc i dec Designatori */
	public void visit(IncDesignator incDes) {
		Code.loadConst(1);
		Code.put(Code.add);
		if (incDes.getDesignator().obj.getKind() == Obj.Elem) Code.put(Code.astore);
		else Code.store(incDes.getDesignator().obj);
		
	}
	
	public void visit(DecDesignator decDes) {
		Code.loadConst(1);
		Code.put(Code.sub);
		if (decDes.getDesignator().obj.getKind() == Obj.Elem) Code.put(Code.astore);
		else Code.store(decDes.getDesignator().obj);
	}
	
	
	/* goto statement */
	public void visit(GoToStatement gotoStmt) {
		Obj label = Tab.find(gotoStmt.getGotoLabel().getGotoLabel());
		int goto_address = label.getAdr() - Code.pc;
		Code.put(Code.jmp);
		if (label == Tab.noObj) {
			gotoAddresses.add(Code.pc);
			labels.add(gotoStmt.getGotoLabel().getGotoLabel());
			Code.put2(0 - Code.pc + 1);
		}
		else Code.put2(goto_address);
	}
	
	public void visit(Label label) {
		Obj labelObj = Tab.insert(Obj.Con, label.getLabelIdent(), new Struct(Struct.None));
		labelObj.setAdr(Code.pc);
		for (int i = 0; i < gotoAddresses.size(); i++) {
			if (labels.get(i).equals(labelObj.getName())) {
				Code.fixup(gotoAddresses.get(i));
				gotoAddresses.remove(i);
				labels.remove(i);
			}
		}
	}
	
	//poziv funkcije - funkcija bez parametara
	public void visit(NoFactorsDes funcCall) {
		Obj o = funcCall.getDesignator().obj;
		int address = o.getAdr() - Code.pc;
		Code.put(Code.call); Code.put2(address);
	}
	
	//poziv funkcija - funkcija sa parametrima
	public void visit(FactorDesignator funcCall) {
		Obj o = funcCall.getDesignator().obj;
		int address = o.getAdr() - Code.pc;
		Code.put(Code.call); Code.put2(address);
			
	}
	
	public void visit(ActParsDesignator funcCall) {
		Obj o = funcCall.getDesignator().obj;
		int address = o.getAdr() - Code.pc;
		Code.put(Code.call); Code.put2(address);
		if (o.getType().getKind()!=Struct.None) Code.put(Code.pop);
		
	}
	
	public void visit(NoActPartDes funcCall) {
		Obj o = funcCall.getDesignator().obj;
		int address = o.getAdr() - Code.pc;
		Code.put(Code.call); Code.put2(address);
		if (o.getType().getKind()!=Struct.None) Code.put(Code.pop);
	}
	
	public void visit(DesignatorPartExpr desExpr) {
	}
	
	/* relOp */
	
	public void visit(EquOp equOp) {
		relOp = Code.eq;
	}
	
	public void visit(NeqOp neqOp) {
		relOp = Code.ne;
	}
	
	public void visit(GtOp gtOp) {
		relOp = Code.gt;
	}
	
	public void visit(GteOp gtOp) {
		relOp = Code.ge;
	}
	
	public void visit(LtOp gtOp) {
		relOp = Code.lt;
	}
	
	public void visit(LteOp gtOp) {
		relOp = Code.le;
	}
	
	
	/* conditions */
	
	public void visit(IfTerm ifTerm) {
		ifAddresses.add(new ArrayList<Integer>());
		jmpAddresses.add(new ArrayList<Integer>());
		jmpCondAddresses.add(new ArrayList<Integer>());
	}
	
	public void visit(CondFactExpr condFact) {
		//System.out.println("3");
		//Code.loadConst(0);
		//ifAddresses.add(Code.pc + 1);
		//Code.putFalseJump(Code.gt, 0);
		/*int address = doAddresses.get(doAddresses.size()-1) - Code.pc;*/
		//Code.put(Code.jcc + Code.gt);
	}
	
	public void visit(RelopCondFact relopFact) {
		/*ifAddresses.add(Code.pc + 1);
		Code.putFalseJump(relOp, 0);*/
	}
	
	
	public void visit(CondFactsNoList condFct) {
		
			if (condFct.getCondFact() instanceof CondFactExpr) {
				Code.loadConst(0);
				ifAddresses.get(ifAddresses.size()-1).add(Code.pc + 1);
				Code.putFalseJump(Code.gt, 0);
			}
			
			if (condFct.getCondFact() instanceof RelopCondFact) {
				ifAddresses.get(ifAddresses.size()-1).add(Code.pc + 1);
				Code.putFalseJump(relOp, 0);
			}
		
	}
	
	public void visit(CondFactsList condFct) {

		if (condFct.getCondFact() instanceof CondFactExpr) {
			Code.loadConst(0);
			ifAddresses.get(ifAddresses.size()-1).add(Code.pc + 1);
			Code.putFalseJump(Code.gt, 0);
		}
		
		if (condFct.getCondFact() instanceof RelopCondFact) {
			ifAddresses.get(ifAddresses.size()-1).add(Code.pc + 1);
			Code.putFalseJump(relOp, 0);
		}
	}
	
	public void visit(OrTerm orTerm) {
		patchAddresses(ifAddresses.get(ifAddresses.size()-1));
	}
	public void visit(CondTermsList condTerm) {
		//Code.put(Code.jmp); jmpAddresses.add(Code.pc); Code.put2(0 - Code.pc + 1);
		/*if (condTerm.getCondTermList() instanceof CondTermsNoList) {
			Code.put(Code.jmp); jmpAddresses.add(Code.pc); Code.put2(0 - Code.pc + 1);
		}*/
	}
	
	public void visit(CondTermsNoList condTerm) {
		if (whileFound) {
			Code.put(Code.jmp); Code.put2(doAddresses.get(doAddresses.size()-1) - Code.pc);
		} else {
			Code.put(Code.jmp); jmpCondAddresses.get(jmpCondAddresses.size()-1).add(Code.pc); Code.put2(0 - Code.pc + 1);
		}
	}
	
	public void visit(Condition cond) {
		SyntaxNode parent = cond.getParent();
		if (!whileFound) patchAddresses(jmpCondAddresses.get(jmpCondAddresses.size()-1));
		else {
			Code.put(Code.jmp); Code.put2(doAddresses.get(doAddresses.size()-1) - Code.pc);
		}
		whileFound = false;
	}
	
	public void visit(IfUnmatchedLabel ifStmt) {
		/*for (int i = 0; i < ifAddresses.get(ifAddresses.size()-1).size(); i++) {
			System.out.println(ifAddresses.get(ifAddresses.size()-1).get(i));
		}*/
		//System.out.println(ifAddresses.size());
		patchAddresses(jmpAddresses.get(jmpAddresses.size()-1));
		patchAddresses( ifAddresses.get(ifAddresses.size()-1));
		removeLists(ifAddresses.get(ifAddresses.size()-1), jmpAddresses.get(jmpAddresses.size()-1), jmpCondAddresses.get(jmpCondAddresses.size()-1));

	}
	
	public void visit(IfUnmatched ifStmt) {
		patchAddresses(jmpAddresses.get(jmpAddresses.size()-1));
		patchAddresses(ifAddresses.get(ifAddresses.size()-1));
		removeLists(ifAddresses.get(ifAddresses.size()-1), jmpAddresses.get(jmpAddresses.size()-1), jmpCondAddresses.get(jmpCondAddresses.size()-1));

	}
	
	public void visit(ElseTerm elseTerm) {
		patchAddresses(ifAddresses.get(ifAddresses.size()-1));
		//ifAddresses.remove(ifAddresses.size()-1);
	}
	
	public void visit(IfStat ifStat) {
		Code.put(Code.jmp); jmpAddresses.get(jmpAddresses.size()-1).add(Code.pc); 
		Code.put2(0 - Code.pc + 1);
	}
	
	public void visit(IfElseStatement ifStat) {
		/*for (int i = 0; i < jmpAddresses.size(); i++) {
			System.out.println(jmpAddresses.get(i)); 
		}*/
		patchAddresses(jmpAddresses.get(jmpAddresses.size()-1));
		removeLists(ifAddresses.get(ifAddresses.size()-1), jmpAddresses.get(jmpAddresses.size()-1), jmpCondAddresses.get(jmpCondAddresses.size()-1));

	}
	
	public void visit(IfElseUnmatched ifElse) {
		patchAddresses(ifAddresses.get(ifAddresses.size()-1));
		patchAddresses(jmpAddresses.get(jmpAddresses.size()-1));
		removeLists(ifAddresses.get(ifAddresses.size()-1), jmpAddresses.get(jmpAddresses.size()-1), jmpCondAddresses.get(jmpCondAddresses.size()-1));

	}
	
	public void visit(IfElseUnLabel ifElse) {
		patchAddresses(ifAddresses.get(ifAddresses.size()-1));
		patchAddresses(jmpAddresses.get(jmpAddresses.size()-1));
		removeLists(ifAddresses.get(ifAddresses.size()-1), jmpAddresses.get(jmpAddresses.size()-1), jmpCondAddresses.get(jmpCondAddresses.size()-1));
	}
	
	protected void patchAddresses(ArrayList<Integer> local) {
		for (int i = 0; i < local.size(); i++) {
			Code.fixup(local.get(i)); 
		}
		
		/*for (int i = 0; i < local.size(); i++) {
			System.out.println(local.get(i));
		}*/
		local.clear();
		//global.remove(global.size()-1);
	}
	
	protected void removeLists(ArrayList<Integer> ifAddr, ArrayList<Integer> jmpAddr, ArrayList<Integer> jmpCondAddr) {
		ifAddresses.remove(ifAddr);
		jmpAddresses.remove(jmpAddr);
		jmpCondAddresses.remove(jmpCondAddr);
	}
	
	
	/* do - while petlja */
	public void visit(WhileStmt whileStmt) {
		//Code.put2(doAddresses.get(doAddresses.size()-1) - Code.pc + 1);
	
	}
	
	public void visit(WhileTerm whileTerm) {
		ifAddresses.add(new ArrayList<Integer>());
		patchAddresses(continueAddresses.get(continueAddresses.size()-1));
		continueAddresses.remove(continueAddresses.get(continueAddresses.size()-1));
		whileFound = true;
	}
	
	public void visit(IdentDo doIdent) {
		breakAddresses.add(new ArrayList<Integer>());
		continueAddresses.add(new ArrayList<Integer>());
	    doAddresses.add(Code.pc + 1);
	}
	
	public void visit(DoStatement doStat) {
		doAddresses.remove(doAddresses.size()-1);
		patchAddresses(ifAddresses.get(ifAddresses.size()-1));
		patchAddresses(breakAddresses.get(breakAddresses.size()-1));
		ifAddresses.remove(ifAddresses.get(ifAddresses.size()-1));
		breakAddresses.remove(breakAddresses.get(breakAddresses.size()-1));
	}
	
	public void visit(BreakStatement breakStmt) {
		Code.put(Code.jmp);breakAddresses.get(breakAddresses.size()-1).add(Code.pc); Code.put2(0 - Code.pc + 1);
	}
	
	public void visit(ContinueStatement contStmt) {
		Code.put(Code.jmp); continueAddresses.get(continueAddresses.size()-1).add(Code.pc); Code.put2(0 - Code.pc + 1);
	}
	
	/* records */
	public void visit(FactorNoExprNew factNew) {
		factNewFound = true;
		
		Code.put(Code.new_);
		Code.put2(factNew.struct.getNumberOfFields()*2);
	}
	
	
	public void visit(MethodDeclaration methDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(NoParametersMethod methDecl){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	

}
