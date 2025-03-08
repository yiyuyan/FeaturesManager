package cn.ksmcbrigade.fm.enums;

public enum Category{
    ITEM("ITEM"),
    BLOCK("BLOCK"),
    COMBAT("COMBAT"),
    MOVEMENT("MOVEMENT"),
    RENDER("RENDER"),
    MISC("MISC"),
    CUSTOM("__UNDEFINED__");

    public final String name;

    Category(String name){
        this.name = name;
    }
}
