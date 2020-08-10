package fieldtest.triggering.grammar.alergia.dotexport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fieldtest.triggering.grammar.alergia.MarkovChain;
import fieldtest.triggering.grammar.alergia.McState;
import fieldtest.triggering.grammar.alergia.McTransition;

/** 
 * 
 * A utility class for exporting Markov chains, represented 
 * by <code>MarkovChain</code> object, in Graphviz dot format. 
 * 
 */
public class DotExporter {
	private static final String stateLabelFormatString = //"%s / %s";
			"<<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">" +
					 "<TR><TD>%s</TD></TR><TR><TD>%s</TD></TR></TABLE>>";

	public String stateLabel(McState state){
		return String.format(stateLabelFormatString, state.getId(),state.getLabel());
	}
	public String stateString(McState state){
		return String.format("%s [shape=\"circle\" margin=0 label=%s];", state.getId(),stateLabel(state));
	}
	public String toDot(MarkovChain mc){
		StringBuilder sb = new StringBuilder();
		appendLine(sb, "digraph g {");
		appendLine(sb, "__start0 [label=\"\" shape=\"none\"];");
		for(McState s : mc.getStates())
			appendLine(sb,stateString(s));
		for(McState s : mc.getStates())
			appendTransitionLines(sb,s);

		appendLine(sb, String.format("__start0 -> %s;", mc.initialState().getId()));
		appendLine(sb, "}");
		return sb.toString();
	}
	private void appendTransitionLines(StringBuilder sb, McState s) {
		for(McTransition t : s.getTransitions().values()) {
			appendLine(sb,transitionString(t));
		}
	}
	private String transitionString(McTransition t) {
		return String.format("%s -> %s [label=\"%.2f\"];", 
				t.getSource().getId(),t.getTarget().getId(), t.getProbability());
	}

	public static void appendLine(StringBuilder sb, String line){
		sb.append(line);
		sb.append(System.lineSeparator());
	}
	
	public void writeToFile(MarkovChain mc, String fileName) throws IOException{
		File f = new File(fileName);
		f.getParentFile().mkdirs();
		try(FileWriter fw = new FileWriter(f)){
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(toDot(mc));
			bw.flush();
		}
	}
}
