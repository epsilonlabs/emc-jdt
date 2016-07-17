package org.eclipse.epsilon.emc.jdt;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.epsilon.eol.dom.EqualsOperatorExpression;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.operations.AbstractOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.IAbstractOperationContributor;
import org.eclipse.epsilon.eol.execute.operations.declarative.SelectOperation;
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

/**
 * Variant of <code>TypeDeclaration.all</code> that integrates case sensitive
 * pattern matching into its <code>.select(td|pattern='...')</code> method.
 */
@SuppressWarnings("restriction")
public class SearchableTypeCollection extends AbstractCollection<Object> implements IAbstractOperationContributor {

	private final class SelectSearchParticipant extends AbstractOperation {
		
		protected boolean appliesTo(Parameter iterator, Expression condition) {
			if (condition instanceof EqualsOperatorExpression) {
				EqualsOperatorExpression equalsOperatorExpression = (EqualsOperatorExpression) condition;
				if (equalsOperatorExpression.getFirstOperand() instanceof PropertyCallExpression) {
					PropertyCallExpression propertyCallExpression = (PropertyCallExpression) equalsOperatorExpression.getFirstOperand();
					if (propertyCallExpression.getTargetExpression() instanceof NameExpression) {
						NameExpression nameExpression = (NameExpression) propertyCallExpression.getTargetExpression();
						if (nameExpression.getName().equals(iterator.getName()) && propertyCallExpression.getPropertyNameExpression().getName().equals("name")) {
							return true;
						}
					}
				}
			}
			return false;
		}
		
		@Override
		public Object execute(Object target,
				NameExpression operationNameExpression, List<Parameter> iterators,
				List<Expression> expressions, IEolContext context)
				throws EolRuntimeException {
			
			if (!appliesTo(iterators.get(0), expressions.get(0))) return new SelectOperation().execute(target, operationNameExpression, iterators, expressions, context);
			
			System.out.println("Yay!");
			
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
									} else if (type instanceof BinaryType) {
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

	/** The visitor is only used if the script tries to iterate through the entire TypeDeclaration.all list. */
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
