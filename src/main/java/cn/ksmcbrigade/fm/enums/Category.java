package cn.ksmcbrigade.fm.enums;

import java.util.List;

public class Category{
    public static final Category ITEM = new Category("ITEM");
    public static final Category BLOCK = new Category("BLOCK");
    public static final Category COMBAT = new Category("COMBAT");
    public static final Category MOVEMENT = new Category("MOVEMENT");
    public static final Category RENDER = new Category("RENDER");
    public static final Category MISC = new Category("MISC");
    public static final Category CUSTOM = new Category("__UNDEFINED__");

    public String name;

    public Category(String name){
        this.name = name;
    }

    public static List<Category> values(){
        return List.of(ITEM,BLOCK,COMBAT,MOVEMENT,RENDER,MISC,CUSTOM);
    }
}
