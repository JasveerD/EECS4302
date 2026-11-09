package app;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.ExprLexer;
import antlr.ExprParser;
import model.*;

public class ExpressionApp {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.print("one input file required");
		} else {
			String fileName = args[0];
			ExprParser parser = getParser(fileName);
			ParseTree AST = parser.prog();

			if (MyErrorListener.hasError) {

			} else {
				AntlrToProgram progVisitor = new AntlrToProgram();
				Program prog = progVisitor.visit(AST);
				if (progVisitor.semanticErrors.isEmpty()) {

				} else {
					for (String err : progVisitor.semanticErrors) {
						System.out.println(err);
					}
				}
			}
		}
	}

	private static ExprParser getParser(String fileName) {
		ExprParser parser = null;

		try {
			CharStream input = CharStreams.fromFileName(fileName);
			ExprLexer lexer = new ExprLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			parser = new ExprParser(tokens);

			parser.removeErrorListeners();
			parser.addErrorListener(new MyErrorListener());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parser;
	}

}
