package org.ckbj.chain.address;

public class AddressFormatException extends IllegalArgumentException {
    public AddressFormatException() {
        super();
    }

    public AddressFormatException(String format, Object... args) {
        super(String.format(format, args));
    }

    public static class InvalidCharacter extends AddressFormatException {
        public final char character;
        public final int position;

        public InvalidCharacter(char character, int position) {
            super("Invalid character '" + character + "' at position " + position);
            this.character = character;
            this.position = position;
        }
    }

    public static class InvalidDataLength extends AddressFormatException {
        public InvalidDataLength() {
            super();
        }

        public InvalidDataLength(String message) {
            super(message);
        }
    }

    public static class InvalidChecksum extends AddressFormatException {
        public InvalidChecksum() {
            super("Checksum does not validate");
        }

        public InvalidChecksum(String message) {
            super(message);
        }
    }

    public static class InvalidPrefix extends AddressFormatException {
        public InvalidPrefix() {
            super();
        }

        public InvalidPrefix(String message) {
            super(message);
        }
    }
}