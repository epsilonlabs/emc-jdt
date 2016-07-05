package org.eclipse.epsilon.emc.jdt.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.emc.jdt.JdtReader;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jface.text.Document;

/**
 * The enumeration for CompilationUnit properties.
 * 
 * @author Cheng Yun
 *
 */
public enum CompilationUnitProperties {
	NAME("name", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return ((CompilationUnit)object).getJavaElement().getElementName();
		}
	}), CLASSES("typeDeclarations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getTypeDeclarations((CompilationUnit) object);
		}
	}), ENUMS("enumDeclarations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getEnumDeclarations((CompilationUnit) object);
		}
	}), CSTATES("statements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return Util.getStatements((CompilationUnit) object);
		}
	}), STATEMETNS("typeDeclarationStatements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getTypeDeclarationStatements((CompilationUnit) object);
		}
	}), COMMENTS("comments", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			@SuppressWarnings("unchecked")
			List<Comment> unitComments = ((CompilationUnit) object).getCommentList();
			List<Comment> comments = new ArrayList<Comment>();
			for(Comment comment : unitComments){
				if(!(comment instanceof Javadoc)){
					comments.add(comment);
				}
			}
			return comments;
		}
	}), LCOMMENTS("lineComments", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			@SuppressWarnings("unchecked")
			List<Comment> unitComments = ((CompilationUnit) object).getCommentList();
			List<LineComment> lineComments = new ArrayList<LineComment>();
			for(Comment comment : unitComments){
				if(comment instanceof LineComment){
					lineComments.add((LineComment)comment);
				}
			}
			return lineComments;
		}
	}), BCOMMENTS("blockComments", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			@SuppressWarnings("unchecked")
			List<Comment> unitComments = ((CompilationUnit) object).getCommentList();
			List<LineComment> lineComments = new ArrayList<LineComment>();
			for(Comment comment : unitComments){
				if(comment instanceof LineComment){
					lineComments.add((LineComment)comment);
				}
			}
			return lineComments;
		}
	}), LOC("linesOfCode", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			Document doc;
			try {
				doc = new Document(((ICompilationUnit)((CompilationUnit) object).getJavaElement()).getSource());
				return doc.getNumberOfLines();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			return null;
		}
	});

    private String command;
    private ASTNodeProperty property;
    private static Map<String, CompilationUnitProperties> unitProperties;
    
    //static block for property map initialisation, 
    //set it static to avoid repeated initialisation on each call
    static {
    	unitProperties = new HashMap<String, CompilationUnitProperties>();
        for (CompilationUnitProperties p : CompilationUnitProperties.values()) {
        	unitProperties.put(p.command, p);    
        }
    }

    CompilationUnitProperties(String value, ASTNodeProperty property) {
        this.command= value;
        this.property = property;
    }
    
    /**
     * returns the requested property, throws RuntimeException if command not found
     * @param command a specific value of the enumeration
     * @return a ASTNodeProperty
     * @throws RuntimeException
     */
    public static ASTNodeProperty getProperty(String command) {
        if(!unitProperties.containsKey(command)) {
        	 return null;
        }
        return unitProperties.get(command).property;
    }
}
