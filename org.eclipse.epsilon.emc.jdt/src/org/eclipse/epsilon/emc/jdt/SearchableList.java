package org.eclipse.epsilon.emc.jdt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.epsilon.eol.dom.EqualsOperatorExpression;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.operations.AbstractOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.IAbstractOperationContributor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.env.ISourceType;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.parser.SourceTypeConverter;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.core.BinaryType;
import org.eclipse.jdt.internal.core.BinaryTypeConverter;
import org.eclipse.jdt.internal.core.SourceType;

@SuppressWarnings("restriction")
public class SearchableList<T> extends ArrayList<T> implements IAbstractOperationContributor {

	private static final long serialVersionUID = 1L;

	private final class SelectSearchParticipant extends AbstractOperation {
		@Override
		public Object execute(Object target,
				NameExpression operationNameExpression, List<Parameter> iterators,
				List<Expression> expressions, IEolContext context)
				throws EolRuntimeException {
			
			EqualsOperatorExpression equalsOperatorExpression = (EqualsOperatorExpression) expressions.get(0);

			SearchPattern pattern = SearchPattern.createPattern(
					context.getExecutorFactory().executeAST(equalsOperatorExpression.getSecondOperand(), context) + "", IJavaSearchConstants.TYPE,
					IJavaSearchConstants.DECLARATIONS,
					SearchPattern.R_PATTERN_MATCH
							| SearchPattern.R_CASE_SENSITIVE);
			
			IJavaSearchScope scope = SearchEngine.createJavaSearchScope( javaProjects, IJavaSearchScope.SOURCES);
			
			SearchEngine engine = new SearchEngine(); 
			try {
				final List<Object> results = new ArrayList<Object>();
				engine.search(pattern, new SearchParticipant[] { SearchEngine
						.getDefaultSearchParticipant() }, scope, new SearchRequestor() {

							@Override
							public void acceptSearchMatch(SearchMatch match) throws CoreException {
								/*
								 * Note: this code is based on the org.eclipse.jdt.internal.codeassist.CompletionEngine class,
								 * particularly the complete(...) method.
								 */
								if (match.getElement() instanceof IType) {
									final IType type = (IType) match.getElement();
									final IResource resource = type.getCompilationUnit().getResource();

									final ProblemReporter problemReporter = new ProblemReporter(DefaultErrorHandlingPolicies.ignoreAllProblems(), new CompilerOptions(), new DefaultProblemFactory());
									final CompilationResult compilationResult = new CompilationResult(resource.getName().toCharArray(), 1, 1, 0);

									if (type instanceof SourceType) {
										convertSourceType(results, (SourceType) match.getElement(), problemReporter, compilationResult);
									} else if (match.getElement() instanceof BinaryType) {
										convertBinaryType(results, type, problemReporter, compilationResult);
									}
								}
								else {
									results.add(match.getElement());
								}
							}

							private void convertBinaryType(final List<Object> results, final IType type,
									final ProblemReporter problemReporter,
									final CompilationResult compilationResult) throws JavaModelException {
								final CompilationUnitDeclaration compilationUnit = new CompilationUnitDeclaration(problemReporter, compilationResult, 0);
								final TypeDeclaration typeDeclaration = new BinaryTypeConverter(problemReporter, compilationResult, null /* no need to remember type names */).buildTypeDeclaration(type, compilationUnit);
								results.add(typeDeclaration);
							}

							private void convertSourceType(final List<Object> results, final SourceType sourceType, final ProblemReporter problemReporter, CompilationResult compilationResult) throws JavaModelException {
								final ISourceType info = (ISourceType) sourceType.getElementInfo();
								final CompilationUnitDeclaration compilationUnit = SourceTypeConverter.buildCompilationUnit(
									new ISourceType[] {info},//sourceTypes[0] is always toplevel here
									SourceTypeConverter.FIELD_AND_METHOD // need field and methods
									| SourceTypeConverter.MEMBER_TYPE, // need member types
									problemReporter, // no need for field initialization
									compilationResult);

								if (compilationUnit.types != null) {
									TypeDeclaration typeDeclaration = compilationUnit.types[0];
									results.add(typeDeclaration);
								}
							}
						}, null);
				return results;
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	protected IJavaProject[] javaProjects = null;
	
	public SearchableList(IJavaProject[] javaProjects) {
		this.javaProjects = javaProjects;
	}
	
	@Override
	public AbstractOperation getAbstractOperation(String name) {
		if ("select".equals(name)) {
			return new SelectSearchParticipant();
		}
		
		return null;
	}

}
