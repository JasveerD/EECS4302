package model;

import java.util.ArrayList;
import java.util.List;

public class Program extends Expression {
	public List<Expression> expressions;
	
	public Program() {
		this.expressions = new ArrayList<Expression>();
	}
}
