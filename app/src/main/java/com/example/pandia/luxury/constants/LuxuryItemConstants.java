package com.example.pandia.luxury.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class LuxuryItemConstants {

    public static final String SUBCATEGORY_KEY_IDENTIFIER = "##$$";
    public static final String SUBCATEGORY_VALUE_IDENTIFIER = "%%";
    public static final String BUNDLE_UNIQUEID_KEY = "unique_id";

    public static final String LEGO_SUBCATEGORY_KEY = SUBCATEGORY_KEY_IDENTIFIER + "LEGO_SUBCAT";
    public static final String SUBCATEGORY_LEGO_SYSTEM = SUBCATEGORY_VALUE_IDENTIFIER + "LEGO_SYSTEM";
    public static final String SUBCATEGORY_LEGO_TECHNIC = SUBCATEGORY_VALUE_IDENTIFIER + "LEGO_TECHNIC";

    public static final String [] LEGO_ALL_SUBCATS = {
            SUBCATEGORY_LEGO_SYSTEM,
            SUBCATEGORY_LEGO_TECHNIC
    };

    public static final String MODEL_SUBCATEGORY_KEY = SUBCATEGORY_KEY_IDENTIFIER + "MODEL_SUBCAT";
    public static final String SUBCATEGORY_MODEL_GUNDAM = SUBCATEGORY_VALUE_IDENTIFIER + "MODEL_GUNDAM";
    public static final String SUBCATEGORY_MODEL_VEHICLE = SUBCATEGORY_VALUE_IDENTIFIER + "MODEL_VEHICLE";
    public static final String SUBCATEGORY_MODEL_NENDOROID = SUBCATEGORY_VALUE_IDENTIFIER + "MODEL_NENDOROID";
    public static final String SUBCATEGORY_MODEL_OTHER = SUBCATEGORY_VALUE_IDENTIFIER + "MODEL_OTHER";

    public static final String [] MODEL_ALL_SUBCATS = {
            SUBCATEGORY_MODEL_GUNDAM,
            SUBCATEGORY_MODEL_VEHICLE,
            SUBCATEGORY_MODEL_NENDOROID,
            SUBCATEGORY_MODEL_OTHER
    };

    public static final String BOOK_SUBCATEGORY_KEY = SUBCATEGORY_KEY_IDENTIFIER + "BOOK_SUBCAT";
    public static final String SUBCATEGORY_BOOK_BIOGRAPHY = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_BIOGRAPHY";
    public static final String SUBCATEGORY_BOOK_RAIL = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_RAIL";
    public static final String SUBCATEGORY_BOOK_AVIATION = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_AVIATION";
    public static final String SUBCATEGORY_BOOK_LEGO = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_LEGO";
    public static final String SUBCATEGORY_BOOK_3DBOOK = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_3DBOOK";
    public static final String SUBCATEGORY_BOOK_PHOTOGRAPHY = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_PHOTOGRAPHY";
    public static final String SUBCATEGORY_BOOK_CS_GENERAL = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_CS_GENERAL";
    public static final String SUBCATEGORY_BOOK_CS_AGILE = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_CS_AGILE";
    public static final String SUBCATEGORY_BOOK_CS_INTERVIEW = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_CS_INTERVIEW";
    public static final String SUBCATEGORY_BOOK_CS_ALGORITHM = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_CS_ALGORITHM";
    public static final String SUBCATEGORY_BOOK_CS_PROGRAMLANGUAGE = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_CS_PROGRAMLANGUAGE";
    public static final String SUBCATEGORY_BOOK_CS_GRAPHICS = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_CS_GRAPHICS";
    public static final String SUBCATEGORY_BOOK_CS_PROJECTMANAGEMENT = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_CS_PROJECTMANAGEMENT";
    public static final String SUBCATEGORY_BOOK_GAMETHEORY = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_GAMETHEORY";
    public static final String SUBCATEGORY_BOOK_MOVIE = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_MOVIE";
    public static final String SUBCATEGORY_BOOK_MATH = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_MATH";
    public static final String SUBCATEGORY_BOOK_NOVEL = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_NOVEL";
    public static final String SUBCATEGORY_BOOK_SCIENCE = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_SCIENCE";
    public static final String SUBCATEGORY_BOOK_HEALTH = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_HEALTH";
    public static final String SUBCATEGORY_BOOK_OTHER = SUBCATEGORY_VALUE_IDENTIFIER + "BOOK_OTHER";

    public static final String [] BOOK_ALL_SUBCATS = {
            SUBCATEGORY_BOOK_BIOGRAPHY,
            SUBCATEGORY_BOOK_RAIL,
            SUBCATEGORY_BOOK_AVIATION,
            SUBCATEGORY_BOOK_LEGO,
            SUBCATEGORY_BOOK_3DBOOK,
            SUBCATEGORY_BOOK_PHOTOGRAPHY,
            SUBCATEGORY_BOOK_CS_GENERAL,
            SUBCATEGORY_BOOK_CS_AGILE,
            SUBCATEGORY_BOOK_CS_INTERVIEW,
            SUBCATEGORY_BOOK_CS_ALGORITHM,
            SUBCATEGORY_BOOK_CS_PROGRAMLANGUAGE,
            SUBCATEGORY_BOOK_CS_GRAPHICS,
            SUBCATEGORY_BOOK_CS_PROJECTMANAGEMENT,
            SUBCATEGORY_BOOK_GAMETHEORY,
            SUBCATEGORY_BOOK_MOVIE,
            SUBCATEGORY_BOOK_MATH,
            SUBCATEGORY_BOOK_NOVEL,
            SUBCATEGORY_BOOK_SCIENCE,
            SUBCATEGORY_BOOK_HEALTH,
            SUBCATEGORY_BOOK_OTHER,
    };

    public static final String GAMEPLATFORM_SUBCATEGORY_KEY = SUBCATEGORY_KEY_IDENTIFIER + "GAMEPLATFORM_SUBCAT";
    public static final String SUBCATEGORY_GAMEPLATFORM_SWITCH = SUBCATEGORY_VALUE_IDENTIFIER + "GAMEPLATFORM_SWITCH";
    public static final String SUBCATEGORY_GAMEPLATFORM_PLAYSTATION4 = SUBCATEGORY_VALUE_IDENTIFIER + "GAMEPLATFORM_PLAYSTATION4";
    public static final String SUBCATEGORY_GAMEPLATFORM_XBOXONE = SUBCATEGORY_VALUE_IDENTIFIER + "GAMEPLATFORM_XBOXONE";
    public static final String SUBCATEGORY_GAMEPLATFORM_PC = SUBCATEGORY_VALUE_IDENTIFIER + "GAMEPLATFORM_PC";

    public static final String [] GAME_ALL_SUBCATS = {
            SUBCATEGORY_GAMEPLATFORM_SWITCH,
            SUBCATEGORY_GAMEPLATFORM_PLAYSTATION4,
            SUBCATEGORY_GAMEPLATFORM_XBOXONE,
            SUBCATEGORY_GAMEPLATFORM_PC
    };

    public static final int ITEM_DEFAULT_PRICE = 0;
    public static final String ITEM_DEFAULT_EXTRA_VALUE = SUBCATEGORY_VALUE_IDENTIFIER + "EMPTY_EXTRA";

    public enum LuxuryType {
        OTHER_TYPE(0),
        MODEL(10),
        LEGO(20),
        BOOK(30),
        GAME_CONSOLE(40),
        GAME_ACCESSORY(50),
        GAME(60),
        EARPHONE(70),
        COMPUTER(80),
        PHONE(90),
        CAMERA(100),
        LENS(110);

        private final int value;
        private static Map map = new HashMap<>();

        static {
            for (LuxuryType luxuryType : LuxuryType.values()) {
                map.put(luxuryType.value, luxuryType);
            }
        }

        public static LuxuryType valueOf(int luxuryType) {
            return (LuxuryType) map.get(luxuryType);
        }

        LuxuryType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static final Map<LuxuryType, String> LUXURY_TYPE_DISPLAY_NAME;

    static {
        Map <LuxuryType, String> map = new TreeMap<LuxuryType, String>();
        map.put(LuxuryType.OTHER_TYPE, "OTHER");
        map.put(LuxuryType.MODEL, "MODEL");
        map.put(LuxuryType.LEGO, "LEGO");
        map.put(LuxuryType.BOOK, "BOOK");
        map.put(LuxuryType.GAME_CONSOLE, "GAME_CONSOLE");
        map.put(LuxuryType.GAME_ACCESSORY, "GAME_ACCESSORY");
        map.put(LuxuryType.GAME, "GAME");
        map.put(LuxuryType.EARPHONE, "EARPHONE");
        map.put(LuxuryType.COMPUTER, "COMPUTER");
        map.put(LuxuryType.PHONE, "PHONE");
        map.put(LuxuryType.CAMERA, "CAMERA");
        map.put(LuxuryType.LENS, "LENS");
        LUXURY_TYPE_DISPLAY_NAME = Collections.unmodifiableMap(map);
    };

    public static final Map<LuxuryType, String> LUXURY_SUBTYPE_STRING_KEY;

    static {
        Map <LuxuryType, String> map = new HashMap<LuxuryType, String>();
        map.put(LuxuryType.MODEL, MODEL_SUBCATEGORY_KEY);
        map.put(LuxuryType.LEGO, LEGO_SUBCATEGORY_KEY);
        map.put(LuxuryType.BOOK, BOOK_SUBCATEGORY_KEY);
        map.put(LuxuryType.GAME, GAMEPLATFORM_SUBCATEGORY_KEY);
        LUXURY_SUBTYPE_STRING_KEY = Collections.unmodifiableMap(map);
    };

    public static final Map<String, HashSet<String>> LUXURY_SUBTYPE_DISPLAY_NAME;

    static {
        Map <String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
        map.put(MODEL_SUBCATEGORY_KEY, new HashSet<String>(Arrays.asList(MODEL_ALL_SUBCATS)));
        map.put(LEGO_SUBCATEGORY_KEY, new HashSet<String>(Arrays.asList(LEGO_ALL_SUBCATS)));
        map.put(BOOK_SUBCATEGORY_KEY, new HashSet<String>(Arrays.asList(BOOK_ALL_SUBCATS)));
        map.put(GAMEPLATFORM_SUBCATEGORY_KEY, new HashSet<String>(Arrays.asList(GAME_ALL_SUBCATS)));
        LUXURY_SUBTYPE_DISPLAY_NAME = Collections.unmodifiableMap(map);
    };
}
