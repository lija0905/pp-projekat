package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public class SemanticAnalyzer extends VisitorAdaptor{
	
	public static final Struct boolType = new Struct(Struct.Bool);
	
	boolean errorDetected = false;
	Struct currentType;
	Obj currentMethod;
	int numFormPars = 0;
	int currentObjKind;
	Struct currentClass;
	ArrayList<Struct> actParsTypes = new ArrayList<Struct>();
	boolean arrayElement = false;
	boolean doFound = false;
	boolean returnFound = false;
	boolean factNewArray = false;
	boolean factNew = false;
	int flag = 0;
	ArrayList<String> goToLabels = new ArrayList<String>();
	ArrayList<String> Labels = new ArrayList<String>();
	int nVars;
	
	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit (ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	}
	
	public void visit(Program program) {
    	nVars = Tab.currentScope.getnVars();
    	Obj main = Tab.find("main");
    	if (main == Tab.noObj) {
    		report_error("Greska: Nije pronadjena main funckija!", program);
    	}
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}
	

	
	public void visit (Type type) {
		Obj typeNode = Tab.find(type.getType());
			if (typeNode == Tab.noObj) {
				if (type.getType().equals("bool")) typeNode = Tab.insert(Obj.Type, "bool", boolType);
				else {
				report_error("Nije pronadjen tip " + type.getType() + " u tabeli simbola!", null);
				type.struct = Tab.noType;
			   }
			}
			if (Obj.Type == typeNode.getKind()) {
					type.struct = typeNode.getType();
					currentType = type.struct;
					//System.out.print(b);
				} else {
					report_error("Greska: Ime " + type.getType() + " ne predstavlja tip!", null);
					type.struct = Tab.noType;
				}
	}
	
	//treba da se doda kad je u klasi da moze da se zove isto kao globalna metoda -- to posle
	public void visit(MethodReturnType methodName) {
		Obj methodObj = Tab.find(methodName.getMethodName());
		if (methodObj == Tab.noObj) {
			currentMethod = Tab.insert(Obj.Meth, methodName.getMethodName(), methodName.getType().struct);
			methodName.obj = currentMethod;
			Tab.openScope();
			if (currentClass!=null) {
				/*Tab.insert(Obj.Var, "this", currentClass.getType());
				report_info("Obradjivanje metode " + methodName.getMethodName() + " klase " + currentClass.getName(), methodName);*/
			}
			else report_info("Obradjivanje metode "+ methodName.getMethodName(), methodName);
		} else {
			report_error("Greska: Metoda " + methodName.getMethodName() + " je vec deklarisana!", null);
		}
	}
	
	//kada je metoda void
	public void visit (VoidReturnType methodName) {
		Obj methodObj = Tab.find(methodName.getMethodName());
		if (methodObj == Tab.noObj) {
			currentMethod = Tab.insert(Obj.Meth, methodName.getMethodName(), Tab.noType);
			Tab.openScope();
			methodName.obj = currentMethod;
			if (currentClass!=null) {
				/*Tab.insert(Obj.Var, "this", currentClass.getType());
				//System.out.println(currentClass.getType().getKind());
				report_info("Obradjivanje metode " + methodName.getMethodName() + " klase " + currentClass.getName(), methodName);*/
			}
			else report_info("Obradjivanje metode "+ methodName.getMethodName(), methodName);
		} else {
			report_error("Greska: Metoda " + methodName.getMethodName() + " je vec deklarisana!", null);
		}
	}
	
	
	
	public void visit (NoParametersMethod methodDecl) {
		if (currentMethod.getType() != Tab.noType && !returnFound) {
			report_error("Semanticka greska: Metoda ima povratnu vrednost!", methodDecl);
		}
		
		checkLabels();
		
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		report_info("Zavrseno obradjivanje metode "+ currentMethod.getName(), methodDecl);
		
		returnFound = false;
		currentMethod = null;
	}
	
	public void visit (MethodDeclaration methodDecl) {
		if (currentMethod.getType() != Tab.noType && !returnFound) {
			report_error("Semanticka greska: Metoda ima povratnu vrednost!", methodDecl);
		}
		
		checkLabels();
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		report_info("Zavrseno obradjivanje metode "+ currentMethod.getName(), methodDecl);

		numFormPars = 0;
		returnFound = false;
		currentMethod = null;
	}
	
	
	public void visit (ReturnStatement returnStat) {
		if (currentMethod!=null && currentMethod.getType()!=Tab.noType) {
			returnFound = true;
			Struct currMethType = currentMethod.getType();
	    	if(!currMethType.compatibleWith(returnStat.getExpr().struct)){
				report_error("Greska na liniji " + returnStat.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
	    	}
		} else if (currentMethod!=null && currentMethod.getType() == Tab.noType){
			report_error("Greska: Metoda je tipa void!", null);
		} else {
			report_error("Semanticka greska: return na pogresnom mestu!", null);
		}
	}
	
	//prepoznat je Formalni parametar
	public void visit(FormParsNoArray formPars) {
		Obj parsObj = Tab.find(formPars.getFparsName());
		if (parsObj == Tab.noObj) {
			Tab.insert(Obj.Var, formPars.getFparsName(), currentType);
			currentMethod.setLevel(++numFormPars);
			report_info("Deklarisan parametar " + formPars.getFparsName() +" metode " + currentMethod.getName(), formPars);
		} else {
			report_error("Greska: Promenljiva " + formPars.getFparsName() + " je vec deklarisana!", formPars);
		}
	}
	
	public void visit(FormParsArray formPars) {
		Obj parsObj = Tab.find(formPars.getFparsName());
		if (parsObj == Tab.noObj) {
			Tab.insert(Obj.Var, formPars.getFparsName(), new Struct(Struct.Array, currentType));
			currentMethod.setLevel(++numFormPars);
			report_info("Deklarisan parametar niz" + formPars.getFparsName() +" metode " + currentMethod.getName(), formPars);
		} else {
			report_error("Greska: Promenljiva " + formPars.getFparsName() + " je vec deklarisana!", formPars);
		}
	}
	
	
	//treba provera da li je lokalna ili globalna promenljiva
	public void visit (VarDeclNoArray varName) {
		Obj varObj = Tab.find(varName.getVarName());
		if (varObj == Tab.noObj) {
			if (currentClass!=null && currentMethod==null) {
				Tab.insert(Obj.Fld, varName.getVarName(), currentType);
				report_info("Deklarisano polje " + varName.getVarName(), varName);
			}
			if (currentMethod!=null) {
				Tab.insert(Obj.Var, varName.getVarName(), currentType);
				report_info("Deklarisana lokalna promenljiva "+ varName.getVarName(), varName);
			}
			else  {
				Tab.insert(Obj.Var, varName.getVarName(), currentType);
				report_info("Deklarisana globalna promenljiva "+ varName.getVarName(), varName);
			}
		} else {
			report_error("Greska: Promenljiva " + varName.getVarName() + " je vec deklarisana!", varName);
		}
	}
	
	//deklarisan je niz
    public void visit (VarDeclArray varArray) {
    	Obj arrayObj = Tab.find(varArray.getArrayName());
		if (arrayObj == Tab.noObj) {
			if (currentClass!=null && currentMethod==null) {
				Tab.insert(Obj.Fld, varArray.getArrayName(), new Struct(Struct.Array, currentType));
				report_info("Deklarisano polje " + varArray.getArrayName(), varArray);
			}
			if (currentMethod!=null) {
				Tab.insert(Obj.Var, varArray.getArrayName(), new Struct(Struct.Array, currentType));
				report_info("Deklarisan lokalni niz "+ varArray.getArrayName(), varArray);
			} else {
				Tab.insert(Obj.Var, varArray.getArrayName(), new Struct(Struct.Array, currentType));
				report_info("Deklarisan globalni niz "+ varArray.getArrayName(), varArray);
			}
		} else {
			report_error("Greska: Promenljiva " + varArray.getArrayName() + " je vec deklarisana!", varArray);
		}
	}
    
    //prepoznavanje constanti
    public void visit (ConstNumDecl constDecl) {
    	Obj constObj = Tab.find(constDecl.getNumConst());
    	if (constObj == Tab.noObj) {
    		if (currentType.getKind() != Struct.Int) {
    			report_error("Greska: Tip konstante " +  constDecl.getNumConst() + " je pogresan!", constDecl);
    		} else {
				constObj = Tab.insert(Obj.Con, constDecl.getNumConst(), currentType);
				constObj.setAdr(constDecl.getNum());
				report_info("Deklarisana konstanta "+ constDecl.getNumConst()+ " integer tipa", constDecl);
    		}
		} else {
			report_error("Greska: Promenljiva " +  constDecl.getNumConst() + " je vec deklarisana", constDecl);
		}
    }
    
    public void visit (ConstCharDecl constDecl) {
    	Obj constObj = Tab.find(constDecl.getCharConst());
    	if (constObj == Tab.noObj) {
    		if (currentType.getKind() != Struct.Char) {
    			report_error("Greska: Tip konstante " +  constDecl.getCharConst() + " je pogresan", constDecl);
    		} else {

				constObj = Tab.insert(Obj.Con, constDecl.getCharConst(), currentType);
				constObj.setAdr(constDecl.getCh());
				report_info("Deklarisana konstanta "+ constDecl.getCharConst() + " char tipa", constDecl);
    		}
		} else {
			report_error("Greska: Promenljiva " +  constDecl.getCharConst() + " je vec deklarisana", constDecl);
		}
    }
    
    public void visit (ConstBoolDecl constDecl) {
    	Obj constObj = Tab.find(constDecl.getBoolConst());
    	if (constObj == Tab.noObj) {
    		if (currentType.getKind() != Struct.Bool) {
    			report_error("Greska: Tip konstante " +  constDecl.getBoolConst() + " je pogresan", constDecl);
    		} else {
				
				constObj = Tab.insert(Obj.Con, constDecl.getBoolConst(), currentType);
				if (constDecl.getBool() == "true") constObj.setAdr(1);
				else constObj.setAdr(0);
				report_info("Deklarisana konstanta "+ constDecl.getBoolConst() + " bool tipa", constDecl);
    		}
		} else {
			report_error("Greska: Promenljiva " +  constDecl.getBoolConst() + " je vec deklarisana", constDecl);
		}
    }
    
    public void visit (FactorNumber numFact) {
    	numFact.struct = Tab.intType;
    	report_info("Pronadjen int factor", numFact);
    }
    
    public void visit (FactorChar factChar) {
    	factChar.struct = Tab.charType;
    }
    
    public void visit (FactorBool boolFact) {
    	boolFact.struct = boolType;
    }
    
    public void visit (FactorDesNoActPars desFact) {
    	desFact.struct = desFact.getDesignator().obj.getType();
    	if (desFact.struct.getKind() == Struct.Array) 
    		currentType = desFact.getDesignator().obj.getType().getElemType();
    	if (currentObjKind!=Obj.Var && currentObjKind!=Obj.Con) {
    		report_error("Greska: Trazena je promenljiva", desFact);
    	}
    }
    
    public void visit (FactorsListOne factorOne) {
    	factorOne.struct =factorOne.getFactor().struct; 
    }
    
    public void visit(FactorsList factList) {
    	int leftMul = factList.getFactorList().struct.getKind();
    	int rightMul = factList.getFactor().struct.getKind();
    	if (leftMul != rightMul || leftMul!=Struct.Int || rightMul!=Struct.Int) {
    		report_error("Greska: Cinioci moraju biti tipa int", factList);
    	} 
    	factList.struct = factList.getFactorList().struct;
        arrayElement = false;
    }
    
    public void visit(TermDecl termDecl) {
    	termDecl.struct = termDecl.getFactorList().struct;
    }
    
    public void visit(NoTermsList oneTerm) {
    	oneTerm.struct = oneTerm.getTerm().struct;
    }
    
    public void visit(TermsList terms) {
    	int leftAdd = terms.getTermList().struct.getKind();
    	int rightAdd = terms.getTerm().struct.getKind();
    	if (leftAdd != rightAdd || leftAdd!=Struct.Int || rightAdd!=Struct.Int) {
    		report_error("Greska: Sabirci moraju biti tipa int", terms);
    	}
    	terms.struct = terms.getTermList().struct;
    	report_info("Pronadjeno sabiranje", terms);
    }
    
    public void visit (ExprDecl exprDecl) {
    	exprDecl.struct = exprDecl.getTermList().struct;
    }
    
    public void visit (AssignDesignatorStat assignStat) {
    	int leftType = assignStat.getDesignator().obj.getType().getKind();
       	int rightType = assignStat.getExpr().struct.getKind(); 
    	if (assignStat.getDesignator().obj.getType().getKind() == Struct.Array && factNewArray) {
    		leftType = assignStat.getDesignator().obj.getType().getElemType().getKind();
    		report_info("Pronadjena inicijalizacija niza", assignStat);
    		
    	} else if (factNewArray){
    		report_error("Greska: Trazen je niz!", assignStat);
    	}
    	
    	if (assignStat.getDesignator().obj.getKind() == Obj.Con) {
    		report_error("Greska: Konstanti se ne moze dodeliti vrednost", assignStat);
    	}
    	if (leftType != rightType) {
    		report_error("Greska: Leva i desna strana moraju biti istog tipa", assignStat);
    	} else {
    		report_info("Pronadjena dodela vrednosti", assignStat);
    	}
    	//arrayElement = false;
 
    	factNewArray = false;
       	factNew = false;
    }
    
    public void visit (NoDesignatorList DesignatorVar) {
    	Obj desVar = Tab.find(DesignatorVar.getDesIdent());
    	currentType = desVar.getType();
    	DesignatorVar.obj = desVar;
    	
    	//if (desVar.getType().getKind() == Struct.Array) flag = 3;

    	if (desVar == Tab.noObj) {
			report_error("Greska: Promenljiva " +  DesignatorVar.getDesIdent() + " nije deklarisana", DesignatorVar);
    	} else currentObjKind = desVar.getKind();
    }
    
    public void visit (DesignatorListDecl desVar) {
   
    	if (desVar.getDesignatorList().obj.getType().getKind() == Struct.Array && !(desVar.getDesignatorPart() instanceof DesignatorPartExpr)) {
    		report_error("Greska: Trazen je niz!", desVar);
    	}
    	
    	if (desVar.getDesignatorPart() instanceof DesignatorPartExpr) {
    		 desVar.obj = new Obj(Obj.Elem,desVar.getDesignatorList().obj.getName(),desVar.getDesignatorList().obj.getType().getElemType());
    	} else desVar.obj = desVar.getDesignatorList().obj;
    	
    	if (desVar.getDesignatorPart() instanceof DesignatorPartIdent) {
    		if (desVar.getDesignatorList().obj.getType().getKind() != Struct.Class) {
        		report_error("Greska: Trazena je klasa!", desVar);

    		}
    		Obj[] fields = desVar.getDesignatorList().obj.getType().getMembers().toArray(new Obj[0]);
    		desVar.obj = Tab.noObj;
    		for (int i = 0; i < fields.length; i++) {
    			if (fields[i].getName().equals(desVar.getDesignatorPart().obj.getName())) {
    				currentType = fields[i].getType();
    				desVar.obj = fields[i];
    			}
    		}
    		
    		if (desVar.obj == Tab.noObj) report_error("Greska: Ne postoji polje klase!", desVar);
    	}
    }
	
    public void visit (DesignatorPartIdent DesignatorVar) {
    	//System.out.println(DesignatorVar.getDesIdentPart());
     	DesignatorVar.obj = new Obj(Obj.Fld, DesignatorVar.getDesIdentPart(), new Struct(Struct.None));

    	//System.out.println(desVar.getType().getKind());
    	/*currentType = desVar.getType();
   
    	/*if (desVar == Tab.noObj) {
			report_error("Greska: Promenljiva " +  DesignatorVar.getDesIdentPart() + " nije deklarisana", DesignatorVar);
    	} else*/ //currentObjKind = desVar.getKind();
    }
    
    //prepoznavanje niza
    public void visit(DesignatorPartExpr desArray) {
    	if (currentType.getKind() == Struct.Array) {
    		currentType = currentType.getElemType();
    	}
    	if (desArray.getExpr().struct.getKind() != Struct.Int) {
    		report_error("Greska: Izraz mora biti tipa int", desArray);
    	} else {
    		report_info("Pronadjen pristup nizu", desArray);    		
    	}
    }
    
    public void visit(DesignatorDecl designator) {

    	designator.obj = designator.getDesignatorList().obj;
    	//System.out.println(designator.obj.getType().getKind());
    }
    
    public void visit(ExprDeclMinus exprMinus) {
    	exprMinus.struct = exprMinus.getTermList().struct;
    	int kind = exprMinus.struct.getKind();
    	if (kind == Struct.Array) {
    		kind = exprMinus.struct.getElemType().getKind();
    	}
    	if (kind!= Struct.Int) {
			report_error("Greska: Promenljiva mora biti tipa int", exprMinus);
    	}
    }
    
    //inkrementiranje
    public void visit(IncDesignator incDes) {
    	if (currentObjKind!=Obj.Var) {
    		report_error("Greska: Trazena je promenljiva", incDes);
    	}
    	int desType = incDes.getDesignator().obj.getType().getKind(); 
    	if ( desType != Struct.Int) {
			report_error("Greska: Promenljiva mora biti tipa int", incDes);
    	} else {
			report_info("Prepoznato inkrementiranje", incDes);
    	}
    }
 
    //dekrementiranje
    public void visit(DecDesignator decDes) {
    	if (currentObjKind!=Obj.Var) {
    		report_error("Greska: Trazena je promenljiva", decDes);
    	}
    	int desType = decDes.getDesignator().obj.getType().getKind(); 
    	if ( desType != Struct.Int) {
			report_error("Greska: Promenljiva mora biti tipa int", decDes);
    	} else {
			report_info("Prepoznato inkrementiranje", decDes);
    	}
    	arrayElement = false;
    }
    
    //print Statement
    public void visit(PrintStatement printStmt) {
    	int stmKind = printStmt.getExpr().struct.getKind();
    	if (stmKind != Struct.Int && stmKind != Struct.Bool && stmKind != Struct.Char) {
			report_error("Greska: Tip nije odgovarajuci", printStmt);
    	} else {
    		report_info("Prepoznata print funckija", printStmt);
    	}
    	arrayElement = false;
    }
    
    //read Statement
    public void visit(ReadStatement readStmt) {
    	int stmKind = readStmt.getDesignator().obj.getType().getKind();
    	if (stmKind != Struct.Int && stmKind != Struct.Bool && stmKind != Struct.Char) {
			report_error("Greska: Tip nije odgovarajuci", readStmt);
    	} else {
    		report_info("Prepoznata read funckija", readStmt);
    	}
    }
    
    
    //poziv metode
    public void visit(NoActPartDes factDes) {
    	if (currentObjKind!=Obj.Meth) {
    		report_error("Greska: Trazena je metoda", factDes);
    	} else {
    		if (factDes.getDesignator().obj.getLevel() > 0) {
    			report_error("Greska: Neispravan broj parametara prosledjen", factDes);
    		}
    		report_info("Prepoznat poziv metode bez parametara", factDes);
    	}
    }
    
    public void visit(ActParsDecl actPars) {
    	if (actPars.getExpr().struct.getKind() == Struct.Array && flag == 2) actParsTypes.add(actPars.getExpr().struct.getElemType());
    	else actParsTypes.add(actPars.getExpr().struct);
    	currentObjKind = Obj.Meth;
    	arrayElement = false;

    }
    
    public void visit(ExprListDecl actPars) {
    	if (actPars.getExpr().struct.getKind() == Struct.Array && flag == 2) {
    		actParsTypes.add(actPars.getExpr().struct.getElemType());
    	}
    	else actParsTypes.add(actPars.getExpr().struct);
    	currentObjKind = Obj.Meth;
    	arrayElement = false;
    }
    
    //poziv metoda koja ima parametre
    public void visit(ActParsDesignator actDes) {
    	if (currentObjKind!=Obj.Meth) {
    		report_error("Greska: Trazena je metoda", actDes);
    	} else {
    		Obj[] locals = actDes.getDesignator().obj.getLocalSymbols().toArray(new Obj[0]);
    		Struct[] pars = actParsTypes.toArray(new Struct[0]);
    		
    		int numOfPars = actDes.getDesignator().obj.getLevel();
    		
    		if (numOfPars != pars.length) report_error("Greska: Neispravan broj parametara prosledjen", actDes);

    		for (int i = 1; i < numOfPars; i++) {
    			/*System.out.println(pars[i-1].getKind());
    			System.out.println(locals[i].getType().getKind());*/
    			if (locals[i].getType().getKind()!=pars[i-1].getKind()) {
    				report_error("Greska: Neispravan tip parametra prosledjen", actDes);
    			
    			}
    		}
    		
			/*System.out.println(pars[numOfPars-1].getKind());
			System.out.println(locals[0].getType().getKind());*/
    		if (locals[0].getType().getKind()!=pars[numOfPars-1].getKind()) {

    			report_error("Greska: Neispravan tip parametra prosledjen", actDes);
    		}
    		
    		actParsTypes.clear();
    		report_info("Prepoznat poziv metode sa paramterima", actDes);
    	}
    }
    
    public void visit(FactorDesignator factDes) {
    	if (currentObjKind!=Obj.Meth) {
    		report_error("Greska: Trazena je metoda", factDes);
    	} else {
    		Obj[] locals = factDes.getDesignator().obj.getLocalSymbols().toArray(new Obj[0]);
    		Struct[] pars = actParsTypes.toArray(new Struct[0]);
    		
    		int numOfPars = factDes.getDesignator().obj.getLevel();
    		
    		if (numOfPars != pars.length) report_error("Greska: Neispravan broj parametara prosledjen", factDes);

    		for (int i = 1; i < numOfPars; i++) {
    			if (locals[i].getType().getKind()!=pars[i-1].getKind()) {
    				report_error("Greska: Neispravan tip parametra prosledjen", factDes);
    			
    			}
    		}
    		
    		if (locals[0].getType().getKind()!=pars[numOfPars-1].getKind()) {
    			report_error("Greska: Neispravan tip parametra prosledjen", factDes);
    		}
    		
  
    		if (factDes.getDesignator().obj.getType() == Tab.noType) {
    			report_error("Greska: metoda mora da ima povratu vrednost!", factDes);
    		} 
    		factDes.struct = factDes.getDesignator().obj.getType();
   
    		
    		actParsTypes.clear();
    		report_info("Prepoznat poziv metode sa paramterima", factDes);
    	}
    }
    
    public void visit(NoFactorsDes nofactDes) {
    	if (currentObjKind!=Obj.Meth) {
    		report_error("Greska: Trazena je metoda", nofactDes);
    	} else {
    		if (nofactDes.getDesignator().obj.getLevel() > 0) {
    			report_error("Greska: Neispravan broj parametara prosledjen", nofactDes);
    		}
    		
    		if (nofactDes.getDesignator().obj.getType() == Tab.noType) {
    			report_error("Greska: metoda mora da ima povratu vrednost!", nofactDes);
    		} 
    		nofactDes.struct = nofactDes.getDesignator().obj.getType();
    		
    		report_info("Prepoznat poziv metode bez parametara", nofactDes);
    	}
    }
    
    public void visit(RelopCondFact condFact) {
    	int leftType = condFact.getExpr().struct.getKind();
    	int rightType = condFact.getExpr().struct.getKind();
    	if (leftType != rightType) {
			report_error("Greska: Ne mogu se porediti dva razilicita tipa", condFact);
    	}
    }
    
    //za if prepoznavanje
    public void visit(CondFactsNoList condFact) {
    	
    }
    
    public void visit(CondFactsList condFact) {
    	
    }

    public void visit(CondFactExpr condFact) {
	//ne znam da li je neophodno
    }
    
    public void visit(Condition cond) {
    	cond.struct = new Struct(Struct.Bool); 
    }
    
    public void visit(IfUnmatched ifStat) {
    	
    }
    
    public void visit(IfUnmatchedLabel ifStat) {
    	
    }
    
    public void visit(IfElseUnmatched ifStat) {
    	
    }

     public void visit(IfElseUnLabel ifStat) {
	
     }
     
     public void visit(IfElseStatement ifStat) {
    	 
     }
    
    public void visit(IdentDo doIdent) {
    	doFound = true;
    }
    
    public void visit(WhileStmt doStat) {
    	if (doStat.getCondition().struct.getKind() != Struct.Bool) {
    		report_error("Greska: Uslov mora biti tipa bool", doStat);
    	}
    	doFound = false;
    }
    
    public void visit(BreakStatement breakStmt) {
    	if (doFound) {
    		report_info("Prepoznat break", breakStmt);
    	} else {
    		report_error("Greska: Break se moze nalaziti samo unutar do-while petlje", breakStmt);
    	}
    }
    
    public void visit(ContinueStatement conStmt) {
    	if (doFound) {
    		report_info("Prepoznat continue", conStmt);
    	} else {
    		report_error("Greska: Continue se moze nalaziti samo unutar do-while petlje", conStmt);
    	}
    }
    
    //goto Statement
    public void visit(GoToStatement gotoStmt) {
    	report_info("Goto Prepoznat", gotoStmt);
    	goToLabels.add(gotoStmt.getGotoLabel().getGotoLabel());
    }
    
    public void visit(Label label) {
    	Labels.add(label.getLabelIdent());
    
    }
    
    public void checkLabels() {
    	boolean missingLabel = false;
		for (int i = 0; i < goToLabels.size(); i++) {
			if (!Labels.contains(goToLabels.get(i))) missingLabel = true;
		}
		
		if (missingLabel) {
			report_error("Greska: Labela nije definisana!", null);
		}
	
	goToLabels.clear();
	Labels.clear();
  }
    
    public void visit(FactorNew factNew) {
    	if (factNew.getExpr().struct.getKind()!=Struct.Int) {
    		report_error("Greska: Tip mora niti int", factNew);
    	}
        	factNew.struct = factNew.getType().struct;
        	this.factNewArray = true;
        	//report_info("Pronadjena inicijalizacija niza", factNew);
    }
    
    public void visit(FactorNoExprNew factNew) {
    	factNew.struct = factNew.getType().struct;
    	this.factNew = true;
    }
	
    public void visit(FactorExpr factExpr) {
    	factExpr.struct = factExpr.getExpr().struct;
    }
    
    public void visit(LocalVarsDecl localVars) {
    	report_info("Pronadjena deklaracija lokalnih parametara", localVars);
    }
    
    //klase
    /*public void visit(ParentClass classPar) {
    	Obj objClass = Tab.find(classPar.getParentName());
    	if (objClass != Tab.noObj) report_error("Greska: Klasa sa tim imenom je vec deklarisana!", classPar);
    	else {
    		objClass = Tab.insert(Obj.Type, classPar.getParentName(), new Struct(Struct.Class));
    		currentClass = objClass;
    		Tab.openScope();
    		report_info("Obradjivanje klase " + currentClass.getName(), classPar);
    	}
    }
    
    public void visit(ChildClass classChild) {
    	Obj objClass = Tab.find(classChild.getChildName());
    	if (objClass != Tab.noObj) report_error("Greska: Klasa sa tim imenom je vec deklarisana!", classChild);
    	else {
    		objClass = Tab.insert(Obj.Type, classChild.getChildName(), new Struct(Struct.Class));
    		currentClass = objClass;
    		Tab.openScope();
    		report_info("Obradjivanje klase " + currentClass.getName(), classChild);
    	}
    }
    
    public void visit(NameConstructor constrName) {
    	Obj constrObj = Tab.find(constrName.getConstructorName());
		if (constrObj.getKind() != Obj.Meth) {

			if (!currentClass.getName().equals(constrName.getConstructorName())) {
				report_error("Greska: Ime konstruktora mora biti isto kao ime klase!", constrName);
			} else {
				currentMethod = Tab.insert(Obj.Meth, constrName.getConstructorName(), Tab.noType);
				Tab.openScope();
				report_info("Obradjivanje konstruktora klase  " + currentClass.getName(), constrName);
			}
		} else if (constrObj.getKind() == Obj.Meth){
			report_error("Greska: Konstruktor klase " + currentClass.getName() + " je vec deklarisan!", null);
		}
    }
    
    public void visit (ConstructorDecl constrDecl) {
		checkLabels();
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		report_info("Zavrseno obradjivanje konstruktora klase "+ currentClass.getName(), constrDecl);

		returnFound = false;
		currentMethod = null;
    	
    }
    
    public void visit(ClassDeclLocalBody classDecl) {
		report_info("Zavrseno obradjivanje klase "+ currentClass.getName(), classDecl);
    	obradjenaKlasa();
    }*/
    
    /*public void visit(LocalVarClass classDecl) {
    	report_info("Zavrseno obradjivanje klase "+ currentClass.getName(), classDecl);
    	obradjenaKlasa();
    }
    
    public void visit(LocalVarClassNoBody classDecl) {
    	report_info("Zavrseno obradjivanje klase "+ currentClass.getName(), classDecl);
    	obradjenaKlasa();
    }
    
    public void obradjenaKlasa() {
    	Tab.chainLocalSymbols(currentClass);
		Tab.closeScope();

		currentClass = null;
    } */
    
    //record //treba da se podeli da bi se scope otvorio.. 
    public void visit(RecordName recordDecl) {
    	Obj objClass = Tab.find(recordDecl.getRecordName());
    	if (objClass != Tab.noObj) report_error("Greska: Rekord sa tim imenom je vec deklarisan!", recordDecl);
    	else {
    		currentClass = new Struct(Struct.Class);
    		recordDecl.obj = Tab.insert(Obj.Type, recordDecl.getRecordName(), currentClass);
    		Tab.openScope();
    		report_info("Obradjivanje klase " + recordDecl.getRecordName(), recordDecl);
    	}
    }
    
    public void visit(RecordDecl recordDecl) {
    	Tab.chainLocalSymbols(currentClass);
    	Tab.closeScope();
    	
    	report_info("Zavresno obradjivanje rekorda ", recordDecl);
    	currentClass = null;
    
    }
    
	 public boolean passed(){
	    	return !errorDetected;
	    }

}
