package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.ExprLexer;
import antlr.ExprParser;
import model.*;
import output.HtmlGenerator;


public class ExpressionApp {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.print("one input file required");
		} else {
			String fileName = args[0];
			ExprParser parser = getParser(fileName);
			ParseTree AST = parser.prog();

			if (MyErrorListener.hasError) {
				// Syntax error: still generate an HTML report with the syntax error info
				ArrayList<String> errors = new ArrayList<>();
				errors.add("Syntax error in input file. Check your program syntax.");
				HtmlGenerator gen = new HtmlGenerator(fileName, errors,
						new ArrayList<>(), new HashMap<>());
				gen.generate("index.html");
				System.out.println("Output written to index.html");
			} else {
				AntlrToProgram progVisitor = new AntlrToProgram();
				Program prog = progVisitor.visit(AST);

				HtmlGenerator gen = new HtmlGenerator(fileName, progVisitor.semanticErrors,
						prog.varsInOrder, progVisitor.varTypes);
				gen.generate("index.html");
				System.out.println("Output written to index.html");
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
