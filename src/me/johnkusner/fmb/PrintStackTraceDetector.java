package me.jkush321.fmb;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class PrintStackTraceDetector extends OpcodeStackDetector {
	private BugReporter bugReporter;
	
	public PrintStackTraceDetector(BugReporter br) {
		bugReporter = br;
	}
	
	@Override public void sawOpcode(int seen) {
		// Look for INVOKEVIRTUAL
		if (seen == INVOKEVIRTUAL) {
			// Class is java/lang/Exception
			if (this.getClassConstantOperand().equals("java/lang/Exception")) {
				// If printStackTrace is called
				if (this.getNameConstantOperand().equals("printStackTrace")) {
					// Easy enough, that's the bug. Report it.
					bugReporter.reportBug(new BugInstance("PRINT_STACK_TRACE", NORMAL_PRIORITY)
							.addClassAndMethod(this).addSourceLine(this));
				}
			}
		}
	}
	
}
