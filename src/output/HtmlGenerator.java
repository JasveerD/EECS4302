package output;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HtmlGenerator {
    private String inputFileName;
    private List<String> semanticErrors;
    private List<String> varsInOrder;
    private Map<String, String> varTypes;

    public HtmlGenerator(String inputFileName, List<String> semanticErrors,
                         List<String> varsInOrder, Map<String, String> varTypes) {
        this.inputFileName = inputFileName;
        this.semanticErrors = semanticErrors;
        this.varsInOrder = varsInOrder;
        this.varTypes = varTypes;
    }

    public void generate(String outputPath) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <title>Type Checking Report</title>\n");
        html.append("  <style>\n");
        html.append("    body { font-family: monospace; margin: 40px; background: #fdfdfd; color: #222; }\n");
        html.append("    h1 { border-bottom: 2px solid #333; padding-bottom: 8px; }\n");
        html.append("    .file-name { color: #555; }\n");
        html.append("    .error-list { list-style: none; padding: 0; }\n");
        html.append("    .error-item { background: #fee; border-left: 4px solid #c00; padding: 8px 12px; margin: 6px 0; }\n");
        html.append("    .success { background: #efe; border-left: 4px solid #0a0; padding: 8px 12px; }\n");
        html.append("    table { border-collapse: collapse; margin-top: 12px; }\n");
        html.append("    th, td { border: 1px solid #ccc; padding: 6px 12px; text-align: left; }\n");
        html.append("    th { background: #eee; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        // Title
        html.append("  <h1>Result of Type Checking</h1>\n");
        html.append("  <p class=\"file-name\">Input file: <strong>")
            .append(escapeHtml(inputFileName))
            .append("</strong></p>\n");

        // Errors section
        html.append("  <h2>Errors</h2>\n");
        if (semanticErrors.isEmpty()) {
            html.append("  <p class=\"success\">No type errors found.</p>\n");
        } else {
            html.append("  <ul class=\"error-list\">\n");
            for (String error : semanticErrors) {
                html.append("    <li class=\"error-item\">")
                    .append(escapeHtml(error))
                    .append("</li>\n");
            }
            html.append("  </ul>\n");
        }

        // Symbol table section
        html.append("  <h2>Declared Variables</h2>\n");
        if (varsInOrder.isEmpty()) {
            html.append("  <p>No variables declared.</p>\n");
        } else {
            html.append("  <table>\n");
            html.append("    <tr><th>Variable</th><th>Type</th></tr>\n");
            for (String var : varsInOrder) {
                html.append("    <tr><td>")
                    .append(escapeHtml(var))
                    .append("</td><td>")
                    .append(escapeHtml(varTypes.getOrDefault(var, "unknown")))
                    .append("</td></tr>\n");
            }
            html.append("  </table>\n");
        }

        // Summary
        html.append("  <hr>\n");
        html.append("  <p>Total errors: ").append(semanticErrors.size()).append("</p>\n");

        html.append("</body>\n");
        html.append("</html>\n");

        // Write to file
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(html.toString());
        } catch (IOException e) {
            System.err.println("Error writing HTML output: " + e.getMessage());
        }
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}
