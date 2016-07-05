package org.eclipse.epsilon.emc.jdt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

/**
 * This class provides visitor facilities for retrieving AST node information.
 * 
 * @author Cheng Yun
 */
public class Visitor extends ASTVisitor {
	// BodyDeclarations:
	private List<AnnotationTypeDeclaration> annotations = new ArrayList<AnnotationTypeDeclaration>();
	private List<EnumDeclaration> enumerations = new ArrayList<EnumDeclaration>();
	private List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private List<TypeDeclaration> classes = new ArrayList<TypeDeclaration>();
	private List<Initializer> initializers = new ArrayList<Initializer>();
	private List<LineComment> lineComments = new ArrayList<LineComment>();
	private List<BlockComment> blockComments = new ArrayList<BlockComment>();

	// Types:
	private List<ArrayType> arrayTypes = new ArrayList<ArrayType>();
	private List<ParameterizedType> parameterizedTypes = new ArrayList<ParameterizedType>();
	private List<PrimitiveType> primitiveTypes = new ArrayList<PrimitiveType>();
	private List<QualifiedType> qualifiedTypes = new ArrayList<QualifiedType>();
	private List<SimpleType> simpleTypes = new ArrayList<SimpleType>();
	private List<UnionType> unionTypes = new ArrayList<UnionType>();
	private List<WildcardType> wildcardTypes = new ArrayList<WildcardType>();

	// Statements:
	private List<VariableDeclarationStatement> variables = new ArrayList<VariableDeclarationStatement>();
	private List<IfStatement> ifStatements = new ArrayList<IfStatement>();
	private List<WhileStatement> whileStatements = new ArrayList<WhileStatement>();
	private List<DoStatement> doStatements = new ArrayList<DoStatement>();
	private List<SwitchStatement> switchStatements = new ArrayList<SwitchStatement>();
	private List<ForStatement> forStatements = new ArrayList<ForStatement>();
	private List<EnhancedForStatement> enhancedForStatements = new ArrayList<EnhancedForStatement>();
	private List<TryStatement> tryStatements = new ArrayList<TryStatement>();
	private List<ReturnStatement> returnStatements = new ArrayList<ReturnStatement>();
	private List<SynchronizedStatement> synchronizedStatements = new ArrayList<SynchronizedStatement>();
	private List<ExpressionStatement> expressionStatements = new ArrayList<ExpressionStatement>();
	private List<AssertStatement> assertStatements = new ArrayList<AssertStatement>();
	private List<ThrowStatement> throwStatements = new ArrayList<ThrowStatement>();
	private List<SwitchCase> switchCases = new ArrayList<SwitchCase>();
	private List<Block> blocks = new ArrayList<Block>();
	private List<BreakStatement> breakStatements = new ArrayList<BreakStatement>();
	private List<EmptyStatement> emptyStatements = new ArrayList<EmptyStatement>();
	private List<ContinueStatement> continueStatements = new ArrayList<ContinueStatement>();
	private List<ConstructorInvocation> constructorInvocations = new ArrayList<ConstructorInvocation>();
	private List<SuperConstructorInvocation> superConstructorInvocations = new ArrayList<SuperConstructorInvocation>();
	private List<LabeledStatement> labeledStatements = new ArrayList<LabeledStatement>();
	private List<TypeDeclarationStatement> typeDeclarationStatements = new ArrayList<TypeDeclarationStatement>();

	// Expressions:
	private List<ArrayAccess> arrayAccesses = new ArrayList<ArrayAccess>();
	private List<ArrayCreation> arrayCreations = new ArrayList<ArrayCreation>();
	private List<ArrayInitializer> arrayInitializers = new ArrayList<ArrayInitializer>();
	private List<Assignment> assignments = new ArrayList<Assignment>();
	private List<BooleanLiteral> booleanLiterals = new ArrayList<BooleanLiteral>();
	private List<CastExpression> castExpressions = new ArrayList<CastExpression>();
	private List<CharacterLiteral> characterLiterals = new ArrayList<CharacterLiteral>();
	private List<ClassInstanceCreation> classInstanceCreations = new ArrayList<ClassInstanceCreation>();
	private List<ConditionalExpression> conditionalExpressions = new ArrayList<ConditionalExpression>();
	private List<FieldAccess> fieldAccesses = new ArrayList<FieldAccess>();
	private List<InstanceofExpression> instanceofExpressions = new ArrayList<InstanceofExpression>();
	private List<MarkerAnnotation> markerAnnotations = new ArrayList<MarkerAnnotation>();
	private List<MethodInvocation> methodInvocations = new ArrayList<MethodInvocation>();
	private List<NormalAnnotation> normalAnnotations = new ArrayList<NormalAnnotation>();
	private List<NumberLiteral> numberLiterals = new ArrayList<NumberLiteral>();
	private List<ParenthesizedExpression> parenthesizedExpressions = new ArrayList<ParenthesizedExpression>();
	private List<PostfixExpression> postfixExpressions = new ArrayList<PostfixExpression>();
	private List<PrefixExpression> prefixExpressions = new ArrayList<PrefixExpression>();
	private List<QualifiedName> qualifiedNames = new ArrayList<QualifiedName>();
	private List<SimpleName> simpleNames = new ArrayList<SimpleName>();
	private List<SingleMemberAnnotation> singleMemberAnnotations = new ArrayList<SingleMemberAnnotation>();
	private List<SuperFieldAccess> superFieldAccesses = new ArrayList<SuperFieldAccess>();
	private List<SuperMethodInvocation> superMethodInvocations = new ArrayList<SuperMethodInvocation>();
	private List<ThisExpression> thisExpressions = new ArrayList<ThisExpression>();
	private List<VariableDeclarationExpression> variableDeclarationExpressions = new ArrayList<VariableDeclarationExpression>();
	private List<StringLiteral> stringLiterals = new ArrayList<StringLiteral>();
	private List<TypeLiteral> typeLiterals = new ArrayList<TypeLiteral>();
	private List<NullLiteral> nullLiterals = new ArrayList<NullLiteral>();
	private List<InfixExpression> infixExpressions = new ArrayList<InfixExpression>();
	
	private List<CatchClause> catchClauses = new ArrayList<CatchClause>();
	// visitors for BodyDeclarations
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		annotations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		enumerations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		fields.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(Initializer node) {
		initializers.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		classes.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(LineComment node) {
		lineComments.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(BlockComment node) {
		blockComments.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ArrayType node) {
		arrayTypes.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ParameterizedType node) {
		parameterizedTypes.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(PrimitiveType node) {
		primitiveTypes.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(QualifiedType node) {
		qualifiedTypes.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SimpleType node) {
		simpleTypes.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(UnionType node) {
		unionTypes.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(WildcardType node) {
		wildcardTypes.add(node);
		return super.visit(node);
	}

	// visitors of statement nodes
	@Override
	public boolean visit(IfStatement node) {
		ifStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(WhileStatement node) {
		whileStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(DoStatement node) {
		doStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SwitchStatement node) {
		switchStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ForStatement node) {
		forStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		enhancedForStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclarationStatement node) {
		typeDeclarationStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(TryStatement node) {
		tryStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SynchronizedStatement node) {
		synchronizedStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ReturnStatement node) {
		returnStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(AssertStatement node) {
		assertStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		expressionStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ThrowStatement node) {
		throwStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SwitchCase node) {
		switchCases.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(Block node) {
		blocks.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(BreakStatement node) {
		breakStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ContinueStatement node) {
		continueStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ConstructorInvocation node) {
		constructorInvocations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(EmptyStatement node) {
		emptyStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		superConstructorInvocations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(LabeledStatement node) {
		labeledStatements.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		variables.add(node);
		return super.visit(node);
	}

	// visitors of expression nodes
	@Override
	public boolean visit(ArrayAccess node) {
		arrayAccesses.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ArrayCreation node) {
		arrayCreations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ArrayInitializer node) {
		arrayInitializers.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(Assignment node) {
		assignments.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		booleanLiterals.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(CastExpression node) {
		castExpressions.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		characterLiterals.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		classInstanceCreations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		conditionalExpressions.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(FieldAccess node) {
		fieldAccesses.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(InstanceofExpression node) {
		instanceofExpressions.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		methodInvocations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(MarkerAnnotation node) {
		markerAnnotations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(NormalAnnotation node) {
		normalAnnotations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(NumberLiteral node) {
		numberLiterals.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ParenthesizedExpression node) {
		parenthesizedExpressions.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(PostfixExpression node) {
		postfixExpressions.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(PrefixExpression node) {
		prefixExpressions.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		superFieldAccesses.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(QualifiedName node) {
		qualifiedNames.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SimpleName node) {
		simpleNames.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SingleMemberAnnotation node) {
		singleMemberAnnotations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		superMethodInvocations.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ThisExpression node) {
		thisExpressions.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		variableDeclarationExpressions.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(StringLiteral node) {
		stringLiterals.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeLiteral node) {
		typeLiterals.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(InfixExpression node) {
		infixExpressions.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(NullLiteral node) {
		nullLiterals.add(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(CatchClause node) {
		catchClauses.add(node);
		return super.visit(node);
	}

	// getters
	// getters for BodyDeclaration nodes
	public List<TypeDeclaration> getClasses() {
		return classes;
	}

	public List<EnumDeclaration> getEnumDeclarations() {
		return enumerations;
	}

	public List<Initializer> getInitializers() {
		return initializers;
	}

	public List<FieldDeclaration> getFields() {
		return fields;
	}

	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	public List<VariableDeclarationStatement> getVariables() {
		return variables;
	}

	public List<LineComment> getLineComments() {
		return lineComments;
	}

	public List<BlockComment> getBlockComments() {
		return blockComments;
	}

	public List<ArrayType> getArrayTypes() {
		return arrayTypes;
	}

	public List<ParameterizedType> getParameterizedTypes() {
		return parameterizedTypes;
	}

	public List<PrimitiveType> getPrimitiveTypes() {
		return primitiveTypes;
	}

	public List<QualifiedType> getQualifiedTypes() {
		return qualifiedTypes;
	}

	public List<SimpleType> getSimpleTypes() {
		return simpleTypes;
	}

	public List<UnionType> getUnionTypes() {
		return unionTypes;
	}

	public List<WildcardType> getWildcardTypes() {
		return wildcardTypes;
	}

	// getters of statement nodes
	public List<AssertStatement> getAssertStatements() {
		return assertStatements;
	}

	public List<ThrowStatement> getThrowStatements() {
		return throwStatements;
	}

	public List<SwitchCase> getSwitchCases() {
		return switchCases;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public List<BreakStatement> getBreakStatements() {
		return breakStatements;
	}

	public List<EmptyStatement> getEmptyStatements() {
		return emptyStatements;
	}

	public List<ContinueStatement> getContinueStatements() {
		return continueStatements;
	}

	public List<ConstructorInvocation> getConstructorInvocations() {
		return constructorInvocations;
	}

	public List<SuperConstructorInvocation> getSuperConstructorInvocations() {
		return superConstructorInvocations;
	}

	public List<LabeledStatement> getLabeledStatements() {
		return labeledStatements;
	}

	// getters of expression nodes

	public List<IfStatement> getIfStatements() {
		return ifStatements;
	}

	public List<WhileStatement> getWhileStatements() {
		return whileStatements;
	}

	public List<DoStatement> getDoStatements() {
		return doStatements;
	}

	public List<SwitchStatement> getSwitchStatements() {
		return switchStatements;
	}

	public List<ForStatement> getForStatements() {
		return forStatements;
	}

	public List<EnhancedForStatement> getEnhancedForStatements() {
		return enhancedForStatements;
	}

	public List<TypeDeclarationStatement> getTypeDeclarationStatements() {
		return typeDeclarationStatements;
	}

	public List<TryStatement> getTryStatements() {
		return tryStatements;
	}

	public List<SynchronizedStatement> getSynchronizedStatements() {
		return synchronizedStatements;
	}

	public List<ReturnStatement> getReturnStatements() {
		return returnStatements;
	}

	public List<AnnotationTypeDeclaration> getAnnotations() {
		return annotations;
	}

	public List<ArrayAccess> getArrayAccesses() {
		return arrayAccesses;
	}

	public List<ArrayCreation> getArrayCreations() {
		return arrayCreations;
	}

	public List<ArrayInitializer> getArrayInitializers() {
		return arrayInitializers;
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public List<BooleanLiteral> getBooleanLiterals() {
		return booleanLiterals;
	}

	public List<CastExpression> getCastExpressions() {
		return castExpressions;
	}

	public List<CharacterLiteral> getCharacterLiterals() {
		return characterLiterals;
	}

	public List<ClassInstanceCreation> getClassInstanceCreations() {
		return classInstanceCreations;
	}

	public List<ConditionalExpression> getConditionalExpressions() {
		return conditionalExpressions;
	}

	public List<FieldAccess> getFieldAccesses() {
		return fieldAccesses;
	}

	public List<InstanceofExpression> getInstanceofExpressions() {
		return instanceofExpressions;
	}

	public List<EnumDeclaration> getEnumerations() {
		return enumerations;
	}

	public List<MarkerAnnotation> getMarkerAnnotations() {
		return markerAnnotations;
	}

	public List<MethodInvocation> getMethodInvocations() {
		return methodInvocations;
	}

	public List<NormalAnnotation> getNormalAnnotations() {
		return normalAnnotations;
	}

	public List<NumberLiteral> getNumberLiterals() {
		return numberLiterals;
	}

	public List<ParenthesizedExpression> getParenthesizedExpressions() {
		return parenthesizedExpressions;
	}

	public List<PostfixExpression> getPostfixExpressions() {
		return postfixExpressions;
	}

	public List<PrefixExpression> getPrefixExpressions() {
		return prefixExpressions;
	}

	public List<QualifiedName> getQualifiedNames() {
		return qualifiedNames;
	}

	public List<SimpleName> getSimpleNames() {
		return simpleNames;
	}

	public List<SingleMemberAnnotation> getSingleMemberAnnotations() {
		return singleMemberAnnotations;
	}

	public List<SuperFieldAccess> getSuperFieldAccesses() {
		return superFieldAccesses;
	}

	public List<SuperMethodInvocation> getSuperMethodInvocations() {
		return superMethodInvocations;
	}

	public List<ThisExpression> getThisExpressions() {
		return thisExpressions;
	}

	public List<VariableDeclarationExpression> getVariableDeclarationExpressions() {
		return variableDeclarationExpressions;
	}

	public List<ExpressionStatement> getExpressionStatements() {
		return expressionStatements;
	}

	public List<StringLiteral> getStringLiterals() {
		return stringLiterals;
	}

	public List<TypeLiteral> getTypeLiterals() {
		return typeLiterals;
	}

	public List<NullLiteral> getNullLiterals() {
		return nullLiterals;
	}

	public List<InfixExpression> getInfixExpressions() {
		return infixExpressions;
	}

	public List<CatchClause> getCatchClauses() {
		return catchClauses;
	}
}