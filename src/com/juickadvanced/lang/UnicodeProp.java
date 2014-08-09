package com.juickadvanced.lang;

import com.juickadvanced.Utils;

import java.util.HashMap;

/**
 * Created by san on 8/9/14.
 */
enum UnicodeProp {

    ALPHABETIC {
        public boolean is(int ch) {
            return Utils.isAlphabetic((char) ch);
        }
    },

    LETTER {
        public boolean is(int ch) {
            return Utils.isLetter((char) ch);
        }
    },

    IDEOGRAPHIC {
        public boolean is(int ch) {
            return Utils.isIdeographic((char) ch);
        }
    },

    LOWERCASE {
        public boolean is(int ch) {
            return Character.isLowerCase((char) ch);
        }
    },

    UPPERCASE {
        public boolean is(int ch) {
            return Character.isUpperCase((char) ch);
        }
    },

    TITLECASE {
        public boolean is(int ch) {
            return Utils.isTitleCase((char) ch);
        }
    },

    WHITE_SPACE {
        // \p{Whitespace}
        public boolean is(int ch) {
            return ((((1 << CharacterData.SPACE_SEPARATOR) |
                    (1 << CharacterData.LINE_SEPARATOR) |
                    (1 << CharacterData.PARAGRAPH_SEPARATOR)) >> CharacterDataLatin1.instance.getType(ch)) & 1)
                    != 0 || (ch >= 0x9 && ch <= 0xd) || (ch == 0x85);
        }
    },

    CONTROL {
        // \p{gc=Control}
        public boolean is(int ch) {
            return CharacterDataLatin1.instance.getType(ch) == CharacterData.CONTROL;
        }
    },

    PUNCTUATION {
        // \p{gc=Punctuation}
        public boolean is(int ch) {
            return ((((1 << CharacterData.CONNECTOR_PUNCTUATION) |
                    (1 << CharacterData.DASH_PUNCTUATION) |
                    (1 << CharacterData.START_PUNCTUATION) |
                    (1 << CharacterData.END_PUNCTUATION) |
                    (1 << CharacterData.OTHER_PUNCTUATION) |
                    (1 << CharacterData.INITIAL_QUOTE_PUNCTUATION) |
                    (1 << CharacterData.FINAL_QUOTE_PUNCTUATION)) >> CharacterDataLatin1.instance.getType(ch)) & 1)
                    != 0;
        }
    },

    HEX_DIGIT {
        // \p{gc=Decimal_Number}
        // \p{Hex_Digit}    -> PropList.txt: Hex_Digit
        public boolean is(int ch) {
            return DIGIT.is(ch) ||
                    (ch >= 0x0030 && ch <= 0x0039) ||
                    (ch >= 0x0041 && ch <= 0x0046) ||
                    (ch >= 0x0061 && ch <= 0x0066) ||
                    (ch >= 0xFF10 && ch <= 0xFF19) ||
                    (ch >= 0xFF21 && ch <= 0xFF26) ||
                    (ch >= 0xFF41 && ch <= 0xFF46);
        }
    },

    ASSIGNED {
        public boolean is(int ch) {
            return CharacterDataLatin1.instance.getType(ch) != CharacterData.UNASSIGNED;
        }
    },

    NONCHARACTER_CODE_POINT {
        // PropList.txt:Noncharacter_Code_Point
        public boolean is(int ch) {
            return (ch & 0xfffe) == 0xfffe || (ch >= 0xfdd0 && ch <= 0xfdef);
        }
    },

    DIGIT {
        // \p{gc=Decimal_Number}
        public boolean is(int ch) {
            return Character.isDigit((char)ch);
        }
    },

    ALNUM {
        // \p{alpha}
        // \p{digit}
        public boolean is(int ch) {
            return ALPHABETIC.is(ch) || DIGIT.is(ch);
        }
    },

    BLANK {
        // \p{Whitespace} --
        // [\N{LF} \N{VT} \N{FF} \N{CR} \N{NEL}  -> 0xa, 0xb, 0xc, 0xd, 0x85
        //  \p{gc=Line_Separator}
        //  \p{gc=Paragraph_Separator}]
        public boolean is(int ch) {
            return CharacterDataLatin1.instance.getType(ch) == CharacterData.SPACE_SEPARATOR ||
                    ch == 0x9; // \N{HT}
        }
    },

    GRAPH {
        // [^
        //  \p{space}
        //  \p{gc=Control}
        //  \p{gc=Surrogate}
        //  \p{gc=Unassigned}]
        public boolean is(int ch) {
            return ((((1 << CharacterData.SPACE_SEPARATOR) |
                    (1 << CharacterData.LINE_SEPARATOR) |
                    (1 << CharacterData.PARAGRAPH_SEPARATOR) |
                    (1 << CharacterData.CONTROL) |
                    (1 << CharacterData.SURROGATE) |
                    (1 << CharacterData.UNASSIGNED)) >> CharacterDataLatin1.instance.getType(ch)) & 1)
                    == 0;
        }
    },

    PRINT {
        // \p{graph}
        // \p{blank}
        // -- \p{cntrl}
        public boolean is(int ch) {
            return (GRAPH.is(ch) || BLANK.is(ch)) && !CONTROL.is(ch);
        }
    },

    WORD {
        //  \p{alpha}
        //  \p{gc=Mark}
        //  \p{digit}
        //  \p{gc=Connector_Punctuation}

        public boolean is(int ch) {
            return ALPHABETIC.is(ch) ||
                    ((((1 << CharacterData.NON_SPACING_MARK) |
                            (1 << CharacterData.ENCLOSING_MARK) |
                            (1 << CharacterData.COMBINING_SPACING_MARK) |
                            (1 << CharacterData.DECIMAL_DIGIT_NUMBER) |
                            (1 << CharacterData.CONNECTOR_PUNCTUATION)) >> CharacterDataLatin1.instance.getType(ch)) & 1)
                            != 0;
        }
    };

    private final static HashMap<String, String> posix = new HashMap<String, String>();
    private final static HashMap<String, String> aliases = new HashMap<String, String>();
    static {
        posix.put("ALPHA", "ALPHABETIC");
        posix.put("LOWER", "LOWERCASE");
        posix.put("UPPER", "UPPERCASE");
        posix.put("SPACE", "WHITE_SPACE");
        posix.put("PUNCT", "PUNCTUATION");
        posix.put("XDIGIT","HEX_DIGIT");
        posix.put("ALNUM", "ALNUM");
        posix.put("CNTRL", "CONTROL");
        posix.put("DIGIT", "DIGIT");
        posix.put("BLANK", "BLANK");
        posix.put("GRAPH", "GRAPH");
        posix.put("PRINT", "PRINT");

        aliases.put("WHITESPACE", "WHITE_SPACE");
        aliases.put("HEXDIGIT","HEX_DIGIT");
        aliases.put("NONCHARACTERCODEPOINT", "NONCHARACTER_CODE_POINT");
    }

    public static UnicodeProp forName(String propName) {
        propName = propName.toUpperCase();
        String alias = aliases.get(propName);
        if (alias != null)
            propName = alias;
        try {
            return valueOf (propName);
        } catch (IllegalArgumentException x) {}
        return null;
    }

    public static UnicodeProp forPOSIXName(String propName) {
        propName = posix.get(propName.toUpperCase());
        if (propName == null)
            return null;
        return valueOf (propName);
    }

    public abstract boolean is(int ch);
}