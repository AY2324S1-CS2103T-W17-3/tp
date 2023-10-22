package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMESTAMP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEIGHT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddTransactionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Description;
import seedu.address.model.transaction.Timestamp;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.expense.Expense;
import seedu.address.model.transaction.expense.Weight;

/**
 * Parses input arguments and creates a new AddTransactionCommand object.
 */
public class AddTransactionCommandParser implements Parser<AddTransactionCommand> {

    public static final String VALIDATION_REGEX = String.format("^%s%s %s%s %s%s( %s%s)?( %s%s %s%s)+$",
            PREFIX_DESCRIPTION, "[^=]*", PREFIX_NAME, "[^=]*", PREFIX_COST, "[^=]*",
            PREFIX_TIMESTAMP, "[^=]*", PREFIX_NAME, "[^=]*", PREFIX_WEIGHT, "[^=]*");

    public static final String MESSAGE_DUPLICATE_EXPENSE = "Name %s is duplicated in expense string";

    /**
     * Parses the given {@code String} of arguments in the context of the AddTransactionCommand
     * and returns an AddTransactionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AddTransactionCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_COST, PREFIX_DESCRIPTION, PREFIX_WEIGHT, PREFIX_TIMESTAMP);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_COST, PREFIX_DESCRIPTION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddTransactionCommand.MESSAGE_USAGE));
        }

        if (!args.trim().matches(VALIDATION_REGEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddTransactionCommand.MESSAGE_USAGE));
        }
        List<String> names = argMultimap.getAllValues(PREFIX_NAME);
        List<String> weights = argMultimap.getAllValues(PREFIX_WEIGHT);
        assert names.size() == weights.size() + 1;
        Name payee = ParserUtil.parseName(names.get(0));
        if (payee.equals(Name.SELF)) {
            payee = Name.SELF;
        }
        Amount amount = ParserUtil.parseAmount(argMultimap.getValue(PREFIX_COST).get());
        Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());
        Map<Name, Weight> expenseMap = new HashMap<>();
        for (int i = 0; i < weights.size(); i++) {
            Name name = ParserUtil.parseName(names.get(i + 1));
            if (name.equals(Name.SELF)) {
                name = Name.SELF;
            }
            if (name.equals(Name.OTHERS)) {
                name = Name.OTHERS;
            }
            Weight weight = ParserUtil.parseWeight(weights.get(i));
            if (expenseMap.containsKey(name)) {
                if (!name.equals(Name.OTHERS)) {
                    throw new ParseException(String.format(MESSAGE_DUPLICATE_EXPENSE, name.fullName));
                }
                Weight previousWeight = expenseMap.get(name);
                weight = new Weight(previousWeight.value.add(weight.value));
            }
            expenseMap.put(name, weight);
        }
        Set<Expense> expenses = expenseMap.keySet().stream()
                .map(x -> new Expense(x, expenseMap.get(x))).collect(Collectors.toSet());
        Timestamp timestamp;
        if (argMultimap.getValue(PREFIX_TIMESTAMP).isPresent()) {
            timestamp = ParserUtil.parseTimestamp(argMultimap.getValue(PREFIX_TIMESTAMP).get());
        } else {
            timestamp = Timestamp.now();
        }
        Transaction transaction = new Transaction(amount, description, payee, expenses, timestamp);
        return new AddTransactionCommand(transaction);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
