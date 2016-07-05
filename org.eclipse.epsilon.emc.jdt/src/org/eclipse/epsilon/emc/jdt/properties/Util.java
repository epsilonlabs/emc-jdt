package org.eclipse.epsilon.emc.jdt.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.epsilon.emc.jdt.JdtReader;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Statement;

/**
 * Utility class, currently providing three methods: getAnnotations, getModifiers
 * and getStatements.
 * @author Cheng Yun
 *
 */
public class Util {
	/**
	 * This method returns annotations found in the given ASTNode
	 * @param node ASTNode
	 * @return a list of annotations
	 */
	public static List<Annotation> getAnnotations(ASTNode node){
		List<MarkerAnnotation> markers = JdtReader
				.getMarkerAnnotations(node);
		List<NormalAnnotation> normals = JdtReader
				.getNormalAnnotations(node);
		List<SingleMemberAnnotation> singles = JdtReader
				.getSingleMemberAnnotations(node);
		List<Annotation> annotations = new ArrayList<Annotation>();
		
		//get Annotation nodes whose parent is the context
		for(MarkerAnnotation m : markers){
			if(m.getParent().equals(node)){
				annotations.add(m);
			}
		}
		for(NormalAnnotation n : normals){
			if(n.getParent().equals(node)){
				annotations.add(n);
			}
		}
		for(SingleMemberAnnotation s : singles){
			if(s.getParent().equals(node)){
				annotations.add(s);
			}
		}
		return annotations;
	}

	/**
	 * This method converts modifiers constants to String.
	 * <p>
	 * For example, if the modifier of a method is "public", you will get an
	 * integer 1 when you call MethodDeclaration.getModifiers(). By calling this
	 * method you can convert this constant to "public" string.
	 * 
	 * @param modifier
	 *            the constant of modifiers
	 * @return modifiers in String format
	 * @see org.eclipse.jdt.core.dom.Modifier
	 */
	public static String [] getModifiers(int modifier) {
		HashMap<Integer, String> modifiers = new HashMap<Integer, String>();
		modifiers.put(0x0000, "");
		modifiers.put(0x0001, "public");
		modifiers.put(0x0002, "private");
		modifiers.put(0x0004, "protected");
		modifiers.put(0x0008, "static");
		modifiers.put(0x0010, "final");
		modifiers.put(0x0400, "abstract");
		modifiers.put(0x0020, "synchronized");
		modifiers.put(0x0100, "native");
		modifiers.put(0x0800, "strictf");
		modifiers.put(0x0040, "volatile");
		Integer[] key = modifiers.keySet().toArray(
				new Integer[modifiers.keySet().size()]);
		String [] returnedModifiers;
		// if the method or class only has one modifier, such as
		// "public void doSomething()"
		if (modifiers.containsKey(modifier)) {
			returnedModifiers = new String[]{modifiers.get(modifier)};
			return returnedModifiers;
		}

		// if the method or class only has two modifiers, such as
		// "public static void doSomething()"
		for (int i = 0; i < key.length; i++) {
			for (int j = i + 1; j < key.length; j++) {
				if ((key[i] + key[j]) == modifier) {
					returnedModifiers = new String[] { modifiers.get(key[i]),
							modifiers.get(key[j]) };
					return returnedModifiers;

				}
			}
		}

		// if the method or class only has three modifiers, such as
		// "public static final void doSomething()"
		for (int i = 0; i < key.length; i++) {
			for (int j = i + 1; j < key.length; j++) {
				for (int k = j + 1; k < key.length; k++) {
					if ((key[i] + key[j] + key[k]) == modifier) {
						returnedModifiers = new String[] { modifiers.get(key[i]),
								modifiers.get(key[j]), modifiers.get(key[k])};
						return returnedModifiers;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * This method returns a list of Statements found in the given ASTNode.
	 * 
	 * @param node ASTNode
	 * @return a list of Statement nodes
	 */
	@SuppressWarnings("unchecked")
	public static List<Statement> getStatements(ASTNode node){
		List<Block> blocks = JdtReader.getBlocks(node);
		List <Statement> statments = new ArrayList<Statement>();
		for(int i=0; i<blocks.size(); i++){
			if(blocks.get(i)!=null){
				statments.addAll(blocks.get(i).statements());
			}
		}
		return statments;
	}
}
