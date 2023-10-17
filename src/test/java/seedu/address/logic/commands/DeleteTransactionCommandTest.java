package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showTransactionAtIndex;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ELEMENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ELEMENT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.transaction.Transaction;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteTransactionCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Transaction transactionToDelete = model.getFilteredTransactionList().get(INDEX_FIRST_ELEMENT.getZeroBased());
        DeleteTransactionCommand deleteCommand = new DeleteTransactionCommand(INDEX_FIRST_ELEMENT);

        String expectedMessage = String.format(DeleteTransactionCommand.MESSAGE_DELETE_TRANSACTION_SUCCESS,
            Messages.format(transactionToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTransaction(transactionToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTransactionList().size() + 1);
        DeleteTransactionCommand deleteCommand = new DeleteTransactionCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showTransactionAtIndex(model, INDEX_FIRST_ELEMENT);

        Transaction transactionToDelete = model.getFilteredTransactionList().get(INDEX_FIRST_ELEMENT.getZeroBased());
        DeleteTransactionCommand deleteCommand = new DeleteTransactionCommand(INDEX_FIRST_ELEMENT);

        String expectedMessage = String.format(DeleteTransactionCommand.MESSAGE_DELETE_TRANSACTION_SUCCESS,
            Messages.format(transactionToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTransaction(transactionToDelete);
        showNoTransaction(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showTransactionAtIndex(model, INDEX_FIRST_ELEMENT);

        Index outOfBoundIndex = INDEX_SECOND_ELEMENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getTransactionList().size());

        DeleteTransactionCommand deleteCommand = new DeleteTransactionCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteTransactionCommand deleteFirstCommand = new DeleteTransactionCommand(INDEX_FIRST_ELEMENT);
        DeleteTransactionCommand deleteSecondCommand = new DeleteTransactionCommand(INDEX_SECOND_ELEMENT);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteTransactionCommand deleteFirstCommandCopy = new DeleteTransactionCommand(INDEX_FIRST_ELEMENT);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different transaction -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteTransactionCommand deleteCommand = new DeleteTransactionCommand(targetIndex);
        String expected = DeleteTransactionCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoTransaction(Model model) {
        model.updateFilteredTransactionList(p -> false);

        assertTrue(model.getFilteredTransactionList().isEmpty());
    }
}