package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Toggles the favourite status of a person in the address book.
 */
public class FavouriteCommand extends Command {

    public static final String COMMAND_WORD = "favourite";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Toggles the favourite status of the person identified by the index number used in the displayed "
            + "list.\n" + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_FAVOURITE_SUCCESS = "Toggled favourite status for: %1$s";

    private final Index index;

    /**
     * @param index of the person in the filtered person list to toggle favourite
     */
    public FavouriteCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToFavourite = lastShownList.get(index.getZeroBased());
        Person updatedPerson = personToFavourite.toggleFavourite();

        model.setPerson(personToFavourite, updatedPerson);
        return new CommandResult(String.format(MESSAGE_FAVOURITE_SUCCESS, Messages.format(updatedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FavouriteCommand)) {
            return false;
        }

        FavouriteCommand otherFavouriteCommand = (FavouriteCommand) other;
        return index.equals(otherFavouriteCommand.index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .toString();
    }
}
