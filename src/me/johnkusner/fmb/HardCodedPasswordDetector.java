package me.jkush321.fmb;

import java.util.regex.Pattern;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class HardCodedPasswordDetector extends OpcodeStackDetector {
	private BugReporter bugReporter;
	
	public HardCodedPasswordDetector(BugReporter bugReporter)
	{
		this.bugReporter = bugReporter;
	}

	/*
	 * Regex Patterns
	 */
	Pattern hardcodedPassword = Pattern.compile("(password|pw|passwd|pword|pwd|pw)\\s*=\\s*[^\\s]+", Pattern.CASE_INSENSITIVE);
	
	@Override
	public void sawOpcode(int seen) {
		// Look for constant with PASSWORD=, PW=, etc.
		if (stack.getStackDepth() > 0)
		{
			// Pop the object off the stack
			Item top = stack.getStackItem(0);
			Object val = top.getConstant();
			//log("Val: " + val.toString());
			// If it is a hardcoded string
			if (val instanceof String)
			{
				String constString = (String) val;
				// If the password regex is positive
				if (hardcodedPassword.matcher(constString).find())
				{
					// That's a bug, report it.
					bugReporter.reportBug(new BugInstance("HARDCODED_PASSWORD", HIGH_PRIORITY).addClassAndMethod(this).addSourceLine(this));
				}
			}
		}
	}
}

