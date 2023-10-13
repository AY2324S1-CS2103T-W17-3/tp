package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DETAILS;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;


/**
 * Adds a transaction to the address book.
 */
public class AddTransactionCommand extends Command {

    public static final String COMMAND_WORD = "addTransaction";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a transaction to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_COST + "COST "
            + PREFIX_DETAILS + "DETAILS "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_COST + "25.00 "
            + PREFIX_DETAILS + "bread";

    public static final String MESSAGE_SUCCESS = "New transaction added: %1$s";

    public static final String MESSAGE_DUPLICATE_TRANSACTION = "This transaction already exists in the address book";

    private final Transaction toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Transaction}
     */
    public AddTransactionCommand(Transaction transaction) {
        requireNonNull(transaction);
        toAdd = transaction;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasTransaction(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TRANSACTION);
        }

        model.addTransaction(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddTransactionCommand)) {
            return false;
        }

        AddTransactionCommand otherAddTransactionCommand = (AddTransactionCommand) other;
        return toAdd.equals(otherAddTransactionCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }

}
