package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     *   Ignores case, but a partial word match is required.
     *   <br>examples:<pre>
     *       containsSubstringIgnoreCase("Alice Charles", "ali") == true
     *       containsSubstringIgnoreCase("Alice Charles", "rles") == true
     *       containsSubstringIgnoreCase("Bernice Yu", "nice u") == true
     *       </pre>
     * @param sentence The full name to be searched, it cannot be null.
     * @param word The keyword to search within the full name, it cannot be null or empty.
     * @return true If the {@code sentence} contains the {@code word}.
     */
    public static boolean containsSubstringIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String trimmedWord = word.trim();
        checkArgument(!trimmedWord.isEmpty(), "Word parameter cannot be empty");

        String preppedSentence = sentence.toLowerCase();
        String preppedWord = trimmedWord.toLowerCase();

        return preppedSentence.contains(preppedWord);
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
