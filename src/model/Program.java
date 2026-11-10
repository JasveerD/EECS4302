package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Program extends Expression {
	public List<String> varsInOrder;
	public Map<String, Object> variables;
	public List<Expression> expressions;
	
	public Program() {
		this.variables = new HashMap<>();
		this.expressions = new ArrayList<Expression>();
	}

	@Override
	public void execute(Program p) {
		for (Expression expr : expressions) {
			expr.execute(p);
		}
	}
}
