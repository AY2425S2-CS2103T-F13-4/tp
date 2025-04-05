package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class FavouriteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndex_success() throws Exception {
        Index index = Index.fromOneBased(1);
        FavouriteCommand favouriteCommand = new FavouriteCommand(index);

        CommandResult result = favouriteCommand.execute(model);

        Person updatedPerson = model.getFilteredPersonList().get(0);
        assertEquals(String.format(FavouriteCommand.MESSAGE_FAVOURITE_SUCCESS,
                    Messages.format(updatedPerson)), result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        FavouriteCommand favouriteCommand = new FavouriteCommand(outOfBoundIndex);

        assertCommandFailure(favouriteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_favouriteStatusToggled_correctBehaviour() throws CommandException {
        Index index = Index.fromOneBased(1);
        FavouriteCommand favouriteCommand = new FavouriteCommand(index);

        Person updatedPerson = model.getFilteredPersonList().get(0).toggleFavourite();
        String expectedMessage = String.format(FavouriteCommand.MESSAGE_FAVOURITE_SUCCESS,
                Messages.format(updatedPerson));

        CommandResult result = favouriteCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Index index = Index.fromOneBased(1);
        FavouriteCommand favouriteCommand1 = new FavouriteCommand(index);
        assertEquals(favouriteCommand1, favouriteCommand1);
    }

    @Test
    public void equals_differentIndex_returnsFalse() {
        Index index1 = Index.fromOneBased(1);
        Index index2 = Index.fromOneBased(2);
        FavouriteCommand favouriteCommand1 = new FavouriteCommand(index1);
        FavouriteCommand favouriteCommand2 = new FavouriteCommand(index2);
        assertFalse(favouriteCommand1.equals(favouriteCommand2));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        FavouriteCommand favouriteCommand = new FavouriteCommand(Index.fromOneBased(1));
        String otherObject = "Not a FavouriteCommand";
        assertFalse(favouriteCommand.equals(otherObject));
    }

    @Test
    public void toString_test() {
        Index index = Index.fromOneBased(1);
        FavouriteCommand favouriteCommand = new FavouriteCommand(index);
        String expectedString = FavouriteCommand.class.getCanonicalName() + "{index=" + index + "}";
        assertEquals(favouriteCommand.toString(), expectedString);
    }
}
