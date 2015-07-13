# FindMoreBugs
A [findbugs](http://findbugs.sourceforge.net/) plugin adding new detectors.

----------


## Detectors

### CommandInjectionVulnerabilityDetector

*Reports: POSSIBLE_COMMAND_INJECTION*

Detects possible command injection in code. Specifically calls to:

```java
Runtime.getRuntime().exec(non_constant_string);

Class.forName(non_constant_string)
```

### HardCodedPasswordDetector

*Reports: HARDCODED_PASSWORD*

Searches for constant strings matching the regex:

```java
/(password|pw|passwd|pword|pwd|pw)\s*=\s*[^\s]+/

// Example that would be detected
fakedatabase.connect("username=foo;password=bar")
```

### PrintStackTraceDetector

*Reports: PRINT_STACK_TRACE*

Searches for any calls to

```java
(Exception object).printStackTrace()
```

### UnsafeAlgorithmDetector

*Reports: UNSAFE_ALGORITHM*

Looks for uses of cryptographic and hashing algorithms that are unsafe. (MD5, Blowfish)

```java
// Bad code - MD5
MessageDigest md5 = MessageDigest.getInstance("MD5")

// Bad code - Blowfish (ex 1)
KeyGenerator keygen = KeyGenerator.getInstance("Blowfish")

// Bad code - Blowfish (ex 2)
Cipher cipher = Cipher.getInstance("Blowfish")

```

## Installation

In the findbugs gui, go to Preferences and install FindMoreBugs.jar

## Compilation

If you want to build this project yourself, make sure all the jar files in the findbugs lib folder are in your build path. Also ensure findbugs.xml and messages.xml are in the root of your jar file.
