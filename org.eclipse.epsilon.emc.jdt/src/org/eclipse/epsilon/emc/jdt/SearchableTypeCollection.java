package org.eclipse.epsilon.emc.jdt;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.epsilon.eol.dom.EqualsOperatorExpression;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.operations.AbstractOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.IAbstractOperationContributor;
import org.eclipse.epsilon.eol.execute.operations.declarative.SelectOperation;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.core.SourceType;

/**
 * Variant of <code>TypeDeclaration.all</code> that integrates case sensitive
 * pattern matching into its <code>.select(td|pattern='...')</code> method.
 */
@SuppressWarnings("restriction")
public class SearchableTypeCollection extends AbstractCollection<Object> implements IAbstractOperationContributor {

	private final class SelectSearchParticipant extends SelectOperation {
		@Override
		public Object execute(Object target, NameExpression operationNameExpression, List<Parameter> iterators,
				List<Expression> expressions, IEolContext context) throws EolRuntimeException {

			EqualsOperatorExpression equalsOperatorExpression = (EqualsOperatorExpression) expressions.get(0);

			SearchPattern pattern = SearchPattern
					.createPattern(
							context.getExecutorFactory().executeAST(equalsOperatorExpression.getSecondOperand(),
									context) + "",
							IJavaSearchConstants.TYPE, IJavaSearchConstants.DECLARATIONS,
							SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE);

			IJavaSearchScope scope = SearchEngine.createJavaSearchScope(javaProjects, IJavaSearchScope.SOURCES);

			SearchEngine engine = new SearchEngine();
			try {
				final List<Object> results = new ArrayList<Object>();
				engine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
						new SearchRequestor() {

							@Override
							public void acceptSearchMatch(SearchMatch match) throws CoreException {
								/*
								 * Note: DOM nodes and internal nodes seem to be
								 * created through different processes, but we
								 * have the ICompilationUnit in common.
								 */
								if (match.getElement() instanceof SourceType) {
									final SourceType srcType = (SourceType) match.getElement();

									/*
									 * TODO: so far, it seems we need to parse
									 * the file.
									 *
									 * TODO: take into account nested types /
									 * package names (how to get fully qualified
									 * name from DOM node?)
									 */

									ASTParser parser = ASTParser.newParser(AST.JLS8);
									parser.setKind(ASTParser.K_COMPILATION_UNIT);
									parser.setResolveBindings(true);
									parser.setSource(srcType.getCompilationUnit());

									ASTNode domAST = parser.createAST(null);
									domAST.accept(new ASTVisitor() {
										@Override
										public boolean visit(org.eclipse.jdt.core.dom.TypeDeclaration node) {
											final String domName = node.getName().getIdentifier();
											if (domName.equals(srcType.getElementName())) {
												results.add(node);
											}
											return true;
										}
									});
								} else {
									results.add(match.getElement());
								}
							}
						}, null);
				return results;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	protected IJavaProject[] javaProjects = null;

	/**
	 * The visitor is only used if the script tries to iterate through the
	 * entire TypeDeclaration.all list.
	 */
	protected ReflectiveASTVisitor visitor;

	public SearchableTypeCollection(IJavaProject[] javaProjects, ReflectiveASTVisitor visitor) {
		this.javaProjects = javaProjects;
		this.visitor = visitor;
	}

	@Override
	public AbstractOperation getAbstractOperation(String name) {
		if ("select".equals(name)) {
			return new SelectSearchParticipant();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Object> iterator() {
		try {
			return (Iterator<Object>) getAllTypes().iterator();
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
	}

	private Collection<?> getAllTypes() throws JavaModelException {
		return visitor.getAllOfType(TypeDeclaration.class.getSimpleName());
	}

	@Override
	public int size() {
		try {
			return getAllTypes().size();
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
	}

}
