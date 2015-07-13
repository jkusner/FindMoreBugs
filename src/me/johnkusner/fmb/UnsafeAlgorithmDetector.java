package me.jkush321.fmb;

import java.util.Arrays;
import java.util.List;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class UnsafeAlgorithmDetector extends OpcodeStackDetector {
	private BugReporter bugReporter;

	public UnsafeAlgorithmDetector(BugReporter br) {
		this.bugReporter = br;
	}

	List<String> badAlgorithms = Arrays.asList(new String[] { 
			"MD5",
			"Blowfish",
			// "SHA-1" ?
	});

	@Override
	public void sawOpcode(int seen) {
		// Look for calls to java.security.MessageDigest.getInstance,
		// javax.crypto.KeyGenerator.getInstance,
		// javax.crypto.Cipher.getInstance
		if (seen == INVOKESTATIC) {

			if (getClassConstantOperand().equals("java/security/MessageDigest")
					|| getClassConstantOperand().equals("javax/crypto/KeyGenerator")
					|| getClassConstantOperand().equals("javax/crypto/Cipher")) {

				if (getNameConstantOperand().equals("getInstance")) {

					// Look for the string on top of the stack
					if (stack.getStackDepth() == 1) {

						Item top = stack.getStackItem(0);
						Object o = top.getConstant();
						if (o instanceof String) {
							String str = (String) o;

							// Check if string is a bad algorithm
							if (badAlgorithms.contains(str)) {
								// report
								bugReporter.reportBug(new BugInstance("UNSAFE_ALGORITHM", HIGH_PRIORITY)
										.addClassAndMethod(this).addSourceLine(this));
							}
						}
					}
				}
			}
		}
	}
}
