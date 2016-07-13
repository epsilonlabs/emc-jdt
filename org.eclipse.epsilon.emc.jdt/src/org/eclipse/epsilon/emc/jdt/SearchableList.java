package org.eclipse.epsilon.emc.jdt;

import java.util.ArrayList;
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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.compiler.parser.SourceTypeConverter;
import org.eclipse.jdt.internal.core.SourceType;

public class SearchableList<T> extends ArrayList<T> implements IAbstractOperationContributor {
	
	protected IJavaProject[] javaProjects = null;
	
	public SearchableList(IJavaProject[] javaProjects) {
		this.javaProjects = javaProjects;
	}
	
	@Override
	public AbstractOperation getAbstractOperation(String name) {
		
		if ("select".equals(name)) {
			return new AbstractOperation() {
				
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
										if (match.getElement() instanceof SourceType) {
											SourceType sourceType = (SourceType) match.getElement();
											Object o = sourceType.getJavaProject().findType(sourceType.getFullyQualifiedName());
											System.out.println(o.getClass());
											results.add(sourceType.getCompilationUnit().getTypes()[0]);
										}*/
										results.add(match.getElement());
									}
								}, null);
						return results;
					}
					catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			};
		}
		
		return null;
	}

}
