package seedu.spendnsplit.logic.parser;

import static seedu.spendnsplit.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.spendnsplit.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.spendnsplit.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.spendnsplit.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.spendnsplit.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.spendnsplit.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.spendnsplit.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.spendnsplit.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.spendnsplit.testutil.TypicalIndexes.INDEX_FIRST_ELEMENT;
import static seedu.spendnsplit.testutil.TypicalIndexes.INDEX_SECOND_ELEMENT;
import static seedu.spendnsplit.testutil.TypicalIndexes.INDEX_THIRD_ELEMENT;

import org.junit.jupiter.api.Test;

import seedu.spendnsplit.commons.core.index.Index;
import seedu.spendnsplit.logic.Messages;
import seedu.spendnsplit.logic.commands.EditPersonCommand;
import seedu.spendnsplit.logic.descriptors.PersonDescriptor;
import seedu.spendnsplit.model.person.Address;
import seedu.spendnsplit.model.person.Email;
import seedu.spendnsplit.model.person.Name;
import seedu.spendnsplit.model.person.Phone;
import seedu.spendnsplit.model.tag.Tag;
import seedu.spendnsplit.testutil.PersonDescriptorBuilder;

public class EditPersonCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPersonCommand.MESSAGE_USAGE);

    private EditPersonCommandParser parser = new EditPersonCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditPersonCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Person} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_ELEMENT;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + TAG_DESC_HUSBAND
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + TAG_DESC_FRIEND;

        PersonDescriptor descriptor = new PersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditPersonCommand expectedCommand = new EditPersonCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_ELEMENT;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        PersonDescriptor descriptor = new PersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditPersonCommand expectedCommand = new EditPersonCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_ELEMENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        PersonDescriptor descriptor = new PersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditPersonCommand expectedCommand = new EditPersonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new PersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditPersonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new PersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditPersonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new PersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditPersonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new PersonDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditPersonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddPersonCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_ELEMENT;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // mulltiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + TAG_DESC_FRIEND + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY + TAG_DESC_FRIEND
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + EMAIL_DESC_BOB + TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_ELEMENT;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        PersonDescriptor descriptor = new PersonDescriptorBuilder().withTags().build();
        EditPersonCommand expectedCommand = new EditPersonCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}