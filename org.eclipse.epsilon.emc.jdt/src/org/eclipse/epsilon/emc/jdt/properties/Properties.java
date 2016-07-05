package org.eclipse.epsilon.emc.jdt.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.emc.jdt.JdtReader;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

/**
 * The enumeration for method, field, statement and expression properties.
 * 
 * @author Cheng Yun
 *
 */
public enum Properties {
	ANNOS("annotations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return Util.getAnnotations(node);
		}
	}), NAME("fullName", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			if(node instanceof MethodDeclaration){
				return ((MethodDeclaration)node).getName().getFullyQualifiedName();
			}
			if(node instanceof Name){
				return  ((Name)node).getFullyQualifiedName();
			}
			return null;
		}
	}), MODIFIERS("modifiers", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			if (node instanceof VariableDeclarationStatement) {
				VariableDeclarationStatement vs = (VariableDeclarationStatement)node;
				return Util.getModifiers(vs.getModifiers());
			}else if(node instanceof VariableDeclarationExpression){
				VariableDeclarationExpression ve = (VariableDeclarationExpression)node;
				return Util.getModifiers(ve.getModifiers());
			}else if(node instanceof MethodDeclaration){
				MethodDeclaration method = (MethodDeclaration)node;
				return Util.getModifiers(method.getModifiers());
			}else if(node instanceof FieldDeclaration){
				FieldDeclaration field = (FieldDeclaration)node;
				return Util.getModifiers(field.getModifiers());
			}else{
				return null;
			}			
		}
	}), RETURNTYPE("returnType", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			if(node instanceof MethodDeclaration){
				return ((MethodDeclaration)node).getReturnType2();
			}
			return null;
		}
	}), RETURNS("returnStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			if(node instanceof MethodDeclaration){
				return JdtReader.getReturnStatements((MethodDeclaration)node);
			}
			return null;
		}
	}), STATEMENTS("statements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			if ((node instanceof MethodDeclaration)
					&& ((MethodDeclaration) node).getBody() != null) {
				return ((MethodDeclaration) node).getBody().statements();
			}
			return JdtReader.getStatements(node);
		}
	}), INITS("initializers", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getInitializers(node);
		}
	}), ARRAYTYPES("arrayTypes", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getArrayTypes(node);
		}
	}), PARATYPES("parameterizedTypes", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getParameterizedTypes(node);
		}
	}), PRITYPES("primitiveTypes", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getPrimitiveTypes(node);
		}
	}), QUALTYPES("qualifiedTypes", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getQualifiedTypes(node);
		}
	}), SIMPTYPES("simpleTypes", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getSimpleTypes(node);
		}
	}), UNIONTYPES("unionTypes", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getUnionTypes(node);
		}
	}), WCTYPES("wildcardTypes", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getWildcardTypes(node);
		}
	}), IFSTATES("ifStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getIfStatements(node);
		}
	}), WHILES("whileStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getWhileStatements(node);
		}
	}), DOSTATES("doStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getDoStatements(node);
		}
	}), SWITCHS("switchStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getSwitchStatements(node);
		}
	}), FORSTATES("forStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getForStatements(node);
		}
	}), EFORSTATES("enhancedForStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getEnhancedForStatements(node);
		}
	}), TRYSTATES("tryStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getTryStatements(node);
		}
	}), SYNCS("synchronizedStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getSynchronizedStatements(node);
		}
	}), VARIABLES("variableStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getVariableDeclarationStatements(node);
		}
	}), ASSERTS("assertStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getAssertStatements(node);
		}
	}), THROWS("throwStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getThrowStatements(node);
		}
	}), SWITCHCASES("switchCases", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getSwitchCases(node);
		}
	}), BLOCKS("blocks", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getBlocks(node);
		}
	}), BREAKS("breakStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getBreakStatements(node);
		}
	}), CONTINUES("continueStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getContinueStatements(node);
		}
	}), CONSINVOCS("constructorInvocations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getConstructorInvocations(node);
		}
	}), EMPTYS("emptyStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getEmptyStatements(node);
		}
	}), SUPERCONINVOCS("superConstructorInvocations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getSuperConstructorInvocations(node);
		}
	}), LABSTATES("labeledStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getLabeledStatements(node);
		}
	}), EXPSTATES("expressionStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getExpressionStatements(node);
		}
	}), ARRAYACS("arrayAccesses", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getArrayAccesses(node);
		}
	}), ARRAYCRS("arrayCreations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getArrayCreations(node);
		}
	}), ARRAYINIS("arrayInitializers", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getArrayInitializers(node);
		}
	}), ASSIGNS("assignments", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getAssignments(node);
		}
	}), BOOLS("booleanLiterals", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getBooleanLiterals(node);
		}
	}), CASTS("castExpressions", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getCastExpressions(node);
		}
	}), CHARS("characterLiterals", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getCharacterLiterals(node);
		}
	}), CLASSINSCREAS("classInstanceCreations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getClassInstanceCreations(node);
		}
	}), CONEXPS("conditionalExpressions", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getConditionalExpressions(node);
		}
	}), FIELDACS("fieldAccesses", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getFieldAccesses(node);
		}
	}), INSTANCEOFS("instanceofExpressions", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getInstanceofExpressions(node);
		}
	}), METHODINVOCS("methodInvocations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getMethodInvocations(node);
		}
	}), NUMBERS("numberLiterals", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getNumberLiterals(node);
		}
	}), PAREXPS("parenthesizedExpressions", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getParenthesizedExpressions(node);
		}
	}), POSTFIXS("postfixExpressions", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getPostfixExpressions(node);
		}
	}), PREFIXS("prefixExpressions", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getPrefixExpressions(node);
		}
	}), SUPERFIELDACS("superFieldAccesses", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getSuperFieldAccesses(node);
		}
	}), SUPERMINVOCS("superMethodInvocations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getSuperMethodInvocations(node);
		}
	}), THISEXPS("thisExpressions", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getThisExpressions(node);
		}
	}), VARIABLEEXPS("variableDeclarationExpressions", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getVariableDeclarationExpressions(node);
		}
	}), QUALINAMES("qualifiedNames", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getQualifiedNames(node);
		}
	}), SNAMES("simpleNames", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getSimpleNames(node);
		}
	}), STRINGS("stringLiterals", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getStringLiterals(node);
		}
	}), NULLS("nullLiterals", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getNullLiterals(node);
		}
	}), CATCHCLAUSE("catchClauses", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getCatchClauses(node);
		}
	}), INFIXEXPS("infixExpressions", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getInfixExpressions(node);
		}
	}), TYPELITERS("typeLiterals", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode node) {
			return JdtReader.getTypeLiterals(node);
		}
	}), CLASS("typeDeclarations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getTypeDeclarations(object);
		}
	}), ENUM("enumDeclarations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getEnumDeclarations(object);
		}
	}), METHODS("methodDeclarations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getMethodDeclarations(object);
		}
	}), INNERBLOCKS("innerBlocks", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			List<Block> blocks = new ArrayList<Block>();
			for(Block temp : JdtReader.getBlocks(object)){
				blocks.add(temp);
			}
			return blocks;
		}
	}), PARENTFILE("parentCompilationUnit", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getParentASTNode(object, CompilationUnit.class);
		}
	}), PARENTENUM("parentEnumDeclaration", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getParentASTNode(object, EnumDeclaration.class);
		}
	}), PARENTCLASS("parentTypeDeclaration", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getParentASTNode(object, TypeDeclaration.class);
		}
	}), PARENTMETHOD("parentMethodDeclaration", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getParentASTNode(object, MethodDeclaration.class);
		}
	});

    private String command;
    private ASTNodeProperty property;
    private static Map<String, Properties> seProperties;
    
    //static block for property map initialisation, 
    //set it static to avoid repeated initialisation on each call
    static {
    	seProperties = new HashMap<String, Properties>();
        for (Properties p : Properties.values()) {
        	seProperties.put(p.command, p);    
        }
    }

    Properties(String value, ASTNodeProperty property) {
        this.command= value;
        this.property = property;
    }
    
    /**
     * returns the requested property, throws RuntimeException if command not found
     * @param command a specific value of the enumeration
     * @return a property
     * @throws RuntimeException
     */
    public static ASTNodeProperty getProperty(String command) {
//        if(!seProperties.containsKey(command)) {
//            return null;
//        }
        return seProperties.get(command).property;
    }
}
