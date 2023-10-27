package seedu.address.model.leave;

import seedu.address.model.person.ComparablePerson;
import seedu.address.model.person.Name;

/**
 * Represents the Employee field of a JsonAdaptedLeave object in storage
 */
public class PersonEntry implements ComparablePerson {

    private final Name name;

    public PersonEntry(String name) {
        this.name = new Name(name);
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public boolean isSamePerson(ComparablePerson otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
