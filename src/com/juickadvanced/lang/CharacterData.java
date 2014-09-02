package com.juickadvanced.lang;

/**
 * Created by san on 8/9/14.
 */
public abstract class CharacterData {

    /**
     * General category "Cn" in the Unicode specification.
     * @since   1.1
     */
    public static final byte UNASSIGNED = 0;

    /**
     * General category "Lu" in the Unicode specification.
     * @since   1.1
     */
    public static final byte UPPERCASE_LETTER = 1;

    /**
     * General category "Ll" in the Unicode specification.
     * @since   1.1
     */
    public static final byte LOWERCASE_LETTER = 2;

    /**
     * General category "Lt" in the Unicode specification.
     * @since   1.1
     */
    public static final byte TITLECASE_LETTER = 3;

    /**
     * General category "Lm" in the Unicode specification.
     * @since   1.1
     */
    public static final byte MODIFIER_LETTER = 4;

    /**
     * General category "Lo" in the Unicode specification.
     * @since   1.1
     */
    public static final byte OTHER_LETTER = 5;

    /**
     * General category "Mn" in the Unicode specification.
     * @since   1.1
     */
    public static final byte NON_SPACING_MARK = 6;

    /**
     * General category "Me" in the Unicode specification.
     * @since   1.1
     */
    public static final byte ENCLOSING_MARK = 7;

    /**
     * General category "Mc" in the Unicode specification.
     * @since   1.1
     */
    public static final byte COMBINING_SPACING_MARK = 8;

    /**
     * General category "Nd" in the Unicode specification.
     * @since   1.1
     */
    public static final byte DECIMAL_DIGIT_NUMBER        = 9;

    /**
     * General category "Nl" in the Unicode specification.
     * @since   1.1
     */
    public static final byte LETTER_NUMBER = 10;

    /**
     * General category "No" in the Unicode specification.
     * @since   1.1
     */
    public static final byte OTHER_NUMBER = 11;

    /**
     * General category "Zs" in the Unicode specification.
     * @since   1.1
     */
    public static final byte SPACE_SEPARATOR = 12;

    /**
     * General category "Zl" in the Unicode specification.
     * @since   1.1
     */
    public static final byte LINE_SEPARATOR = 13;

    /**
     * General category "Zp" in the Unicode specification.
     * @since   1.1
     */
    public static final byte PARAGRAPH_SEPARATOR = 14;

    /**
     * General category "Cc" in the Unicode specification.
     * @since   1.1
     */
    public static final byte CONTROL = 15;

    /**
     * General category "Cf" in the Unicode specification.
     * @since   1.1
     */
    public static final byte FORMAT = 16;

    /**
     * General category "Co" in the Unicode specification.
     * @since   1.1
     */
    public static final byte PRIVATE_USE = 18;

    /**
     * General category "Cs" in the Unicode specification.
     * @since   1.1
     */
    public static final byte SURROGATE = 19;

    /**
     * General category "Pd" in the Unicode specification.
     * @since   1.1
     */
    public static final byte DASH_PUNCTUATION = 20;

    /**
     * General category "Ps" in the Unicode specification.
     * @since   1.1
     */
    public static final byte START_PUNCTUATION = 21;

    /**
     * General category "Pe" in the Unicode specification.
     * @since   1.1
     */
    public static final byte END_PUNCTUATION = 22;

    /**
     * General category "Pc" in the Unicode specification.
     * @since   1.1
     */
    public static final byte CONNECTOR_PUNCTUATION = 23;

    /**
     * General category "Po" in the Unicode specification.
     * @since   1.1
     */
    public static final byte OTHER_PUNCTUATION = 24;

    /**
     * General category "Sm" in the Unicode specification.
     * @since   1.1
     */
    public static final byte MATH_SYMBOL = 25;

    /**
     * General category "Sc" in the Unicode specification.
     * @since   1.1
     */
    public static final byte CURRENCY_SYMBOL = 26;

    /**
     * General category "Sk" in the Unicode specification.
     * @since   1.1
     */
    public static final byte MODIFIER_SYMBOL = 27;

    /**
     * General category "So" in the Unicode specification.
     * @since   1.1
     */
    public static final byte OTHER_SYMBOL = 28;

    /**
     * General category "Pi" in the Unicode specification.
     * @since   1.4
     */
    public static final byte INITIAL_QUOTE_PUNCTUATION = 29;

    /**
     * General category "Pf" in the Unicode specification.
     * @since   1.4
     */
    public static final byte FINAL_QUOTE_PUNCTUATION = 30;


    abstract int getProperties(int ch);
    abstract int getType(int ch);
    abstract boolean isWhitespace(int ch);
    abstract boolean isMirrored(int ch);
    abstract boolean isJavaIdentifierStart(int ch);
    abstract boolean isJavaIdentifierPart(int ch);
    abstract boolean isUnicodeIdentifierStart(int ch);
    abstract boolean isUnicodeIdentifierPart(int ch);
    abstract boolean isIdentifierIgnorable(int ch);
    abstract int toLowerCase(int ch);
    abstract int toUpperCase(int ch);
    abstract int toTitleCase(int ch);
    abstract int digit(int ch, int radix);
    abstract int getNumericValue(int ch);
    abstract byte getDirectionality(int ch);

    //need to implement for JSR204
    int toUpperCaseEx(int ch) {
        return toUpperCase(ch);
    }

    char[] toUpperCaseCharArray(int ch) {
        return null;
    }

    boolean isOtherLowercase(int ch) {
        return false;
    }

    boolean isOtherUppercase(int ch) {
        return false;
    }

    boolean isOtherAlphabetic(int ch) {
        return false;
    }

    boolean isIdeographic(int ch) {
        return false;
    }

    // Character <= 0xff (basic latin) is handled by internal fast-path
    // to avoid initializing large tables.
    // Note: performance of this "fast-path" code may be sub-optimal
    // in negative cases for some accessors due to complicated ranges.
    // Should revisit after optimization of table initialization.

    static final CharacterData of(int ch) {
        if (ch >>> 8 == 0) {     // fast-path
            return CharacterDataLatin1.instance;
        } else {
            throw new RuntimeException("Not implemented");
        }
    }
}
