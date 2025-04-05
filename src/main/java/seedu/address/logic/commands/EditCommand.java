package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Class;
import seedu.address.model.person.Email;
import seedu.address.model.person.Favourite;
import seedu.address.model.person.Grade;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "[" + PREFIX_ROLE + "ROLE] "
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_EDITED_FIELD_HAS_SAME_VALUE = "Some of the fields you're trying to" +
            " edit already have the same value as before. This is not allowed. \n";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        String messageUnchangedValues = getMessageOfUnchangedValues(personToEdit, editedPerson, editPersonDescriptor);
        if (!messageUnchangedValues.isEmpty()) {
            throw new CommandException(MESSAGE_EDITED_FIELD_HAS_SAME_VALUE + messageUnchangedValues);
        }

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Role updatedRole = editPersonDescriptor.getRole().orElse(personToEdit.getRole());
        Grade updatedGrade = editPersonDescriptor.getGrade().orElse(personToEdit.getGrade());
        Class updatedClass = editPersonDescriptor.getStudentClass().orElse(personToEdit.getStudentClass());
        Favourite updatedFavourite = editPersonDescriptor.getFavourite().orElse(personToEdit.getFavourite());

        Name updatedFamilyMember = editPersonDescriptor.getRelativeName().orElse(personToEdit.getRelativeName());
        Phone updatedFamilyMemberPhone = editPersonDescriptor.getRelativePhone()
                .orElse(personToEdit.getRelativePhone());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags, updatedRole,
                updatedGrade, updatedClass, updatedFamilyMember, updatedFamilyMemberPhone, updatedFavourite);
    }

    public List<String> getUnchangedValues(Person person, Person editedPerson,
                                           EditPersonDescriptor editPersonDescriptor) {
        List<String> unchangedValues = new ArrayList<>();

        if (!editPersonDescriptor.isNameNull()) {
            if (Objects.equals(person.getName(), editedPerson.getName())) {
                unchangedValues.add("name");
            }
        }

        if (!editPersonDescriptor.isPhoneNull()) {
            if (Objects.equals(person.getPhone(), editedPerson.getPhone())) {
                unchangedValues.add("phone");
            }
        }

        if (!editPersonDescriptor.isEmailNull()) {
            if (Objects.equals(person.getEmail(), editedPerson.getEmail())) {
                unchangedValues.add("email");
            }
        }

        if (!editPersonDescriptor.isAddressNull()) {
            if (Objects.equals(person.getAddress(), editedPerson.getAddress())) {
                unchangedValues.add("address");
            }
        }

        if (!editPersonDescriptor.isTagsNull()) {
            if (Objects.equals(person.getTags(), editedPerson.getTags())) {
                unchangedValues.add("tags");
            }
        }

        if (!editPersonDescriptor.isRoleNull()) {
            if (Objects.equals(person.getRole(), editedPerson.getRole())) {
                unchangedValues.add("role");
            }
        }

        if (!editPersonDescriptor.isGradeNull()) {
            if (Objects.equals(person.getGrade(), editedPerson.getGrade())) {
                unchangedValues.add("grade");
            }
        }

        if (!editPersonDescriptor.isStudentClassNull()) {
            if (Objects.equals(person.getStudentClass(), editedPerson.getStudentClass())) {
                unchangedValues.add("student class");
            }
        }

        if (!editPersonDescriptor.isRelativeNameNull()) {
            if (Objects.equals(person.getRelativeName(), editedPerson.getRelativeName())) {
                unchangedValues.add("relative name");
            }
        }

        if (!editPersonDescriptor.isRelativePhoneNull()) {
            if (Objects.equals(person.getRelativePhone(), editedPerson.getRelativePhone())) {
                unchangedValues.add("relative phone");
            }
        }

        return unchangedValues;
    }

    public String getMessageOfUnchangedValues (Person person, Person editedPerson,
                                               EditPersonDescriptor editPersonDescriptor) {
        List<String> unchangedValues = getUnchangedValues(person, editedPerson, editPersonDescriptor);
        String message = "";

        if (!unchangedValues.isEmpty()) {
            message = "These are the fields: "
                    + String.join(", ", unchangedValues) + ".";
        }
        return message;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;
        private Role role;
        private Grade grade;
        private Class studentClass;
        private Favourite favourite;
        private Name relativeName;
        private Phone relativePhone;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
            setRole(toCopy.role);
            setGrade(toCopy.grade);
            setStudentClass(toCopy.studentClass);
            setFavourite(toCopy.favourite);
            setRelativeName(toCopy.relativeName);
            setRelativePhone(toCopy.relativePhone);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags, role, grade, studentClass,
                    relativeName, relativePhone, favourite);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public boolean isNameNull() {
            return this.name == null;
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public boolean isPhoneNull() {
            return this.phone == null;
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public boolean isEmailNull() {
            return this.email == null;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public boolean isAddressNull() {
            return this.address == null;
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        public boolean isTagsNull() {
            return this.tags == null;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public Optional<Role> getRole() {
            return Optional.ofNullable(role);
        }

        public boolean isRoleNull() {
            return this.role == null;
        }

        public void setGrade(Grade grade) {
            this.grade = grade;
        }

        public Optional<Grade> getGrade() {
            return Optional.ofNullable(grade);
        }

        public boolean isGradeNull() {
            return this.grade == null;
        }

        public void setStudentClass(Class studentClass) {
            this.studentClass = studentClass;
        }

        public Optional<Class> getStudentClass() {
            return Optional.ofNullable(studentClass);
        }

        public boolean isStudentClassNull() {
            return this.studentClass == null;
        }

        public void setRelativeName(Name relativeName) {
            this.relativeName = relativeName;
        }

        public Optional<Name> getRelativeName() {
            return Optional.ofNullable(relativeName);
        }

        public boolean isRelativeNameNull() {
            return this.relativeName == null;
        }

        public void setRelativePhone(Phone relativePhone) {
            this.relativePhone = relativePhone;
        }

        public Optional<Phone> getRelativePhone() {
            return Optional.ofNullable(relativePhone);
        }

        public boolean isRelativePhoneNull() {
            return this.relativePhone == null;
        }

        public void setFavourite(Favourite favourite) {
            this.favourite = favourite;
        }

        public Optional<Favourite> getFavourite() {
            return Optional.ofNullable(favourite);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(role, otherEditPersonDescriptor.role)
                    && Objects.equals(grade, otherEditPersonDescriptor.grade)
                    && Objects.equals(studentClass, otherEditPersonDescriptor.studentClass)
                    && Objects.equals(relativeName, otherEditPersonDescriptor.relativeName)
                    && Objects.equals(relativePhone, otherEditPersonDescriptor.relativePhone);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("tags", tags)
                    .add("role", role)
                    .add("grade", grade)
                    .add("class", studentClass)
                    .add("relative's name", relativeName)
                    .add("relative's phone", relativePhone)
                    .toString();
        }
    }
}
