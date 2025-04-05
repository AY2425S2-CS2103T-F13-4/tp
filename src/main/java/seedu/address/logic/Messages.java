package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_PERSON_NAME_AND_PHONE = "The person's name and "
            + "phone number provided cannot be found!";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_NO_RESULTS = "No contacts found.";
    public static final String MESSAGE_INVALID_NAME = "Names can only contain alphabetic and special characters. \n"
            + "If you're trying to search by phone number, enter digits without spaces";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Role: ")
                .append(person.getRole());

        if (person.getRole().equals(new Role("Student"))) {
            builder.append("; Grade: ")
                    .append(person.getGrade())
                    .append("; Class: ")
                    .append(person.getStudentClass())
                    .append("; Parent's Name: ")
                    .append(person.getRelativeName())
                    .append("; Parent's Phone: ")
                    .append(person.getRelativePhone());
        }

        if (person.getRole().equals(new Role("Parent"))) {
            builder.append("; Child's Name: ")
                    .append(person.getRelativeName())
                    .append("; Child's Phone: ")
                    .append(person.getRelativePhone())
                    .append("; Child's Grade: ")
                    .append(person.getGrade())
                    .append("; Child's Class: ")
                    .append(person.getStudentClass());
        }

        builder.append("; Tags: ");
        person.getTags().forEach(builder::append);

        return builder.toString();
    }

}
