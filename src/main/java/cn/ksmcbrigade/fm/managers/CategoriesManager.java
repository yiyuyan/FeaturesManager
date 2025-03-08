package cn.ksmcbrigade.fm.managers;

import cn.ksmcbrigade.fm.enums.Category;
import cn.ksmcbrigade.fm.feature.Feature;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CategoriesManager {
    private final Map<Category,ArrayList<Feature>> categories = new HashMap<>();

    public CategoriesManager(){
        this.init();
    }

    private void init(){
        for (Category value : Category.values()) {
            if(!value.name.equals("__UNDEFINED__")){
                categories.put(value,new ArrayList<>());
            }
        }
    }

    public ArrayList<Feature> getAllFeatures(){
        ArrayList<Feature> features = new ArrayList<>();
        for (ArrayList<Feature> value : categories.values()) {
            features.addAll(value);
        }
        return features;
    }

    public ArrayList<Feature> getFeatures(Category category){
        return categories.getOrDefault(category,new ArrayList<>());
    }

    public boolean has(Category category){
        return getAllCategories().contains(category);
    }

    @Nullable
    public Feature getFeature(String name){
        for (Category category : categories.keySet()) {
            for (Feature feature : categories.get(category)) {
                if(feature.getName().equals(name)){
                    return feature;
                }
            }
        }
        return null;
    }

    @Nullable
    public Feature getFeature(Class<?> name){
        return getFeature(name.getSimpleName());
    }

    public boolean has(Category category,Feature feature){
        return getAllCategories().contains(category) && getFeatures(category).contains(feature);
    }

    public Set<Category> getAllCategories(){
        return this.categories.keySet();
    }

    public void add(Category category,Feature feature){
        if(!this.categories.containsKey(category)) addNew(category);
        this.categories.get(category).add(feature);
    }

    public void addNew(Category category){
        this.categories.put(category,new ArrayList<>());
    }

    public void addCustomNew(String categoryName) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.addNew(Category.class.getDeclaredConstructor(String.class).newInstance(categoryName));
    }

    @Nullable
    public Category get(String name){
        for (Category category : this.getAllCategories()) {
            if(category.name.equals(name)) return category;
        }
        return null;
    }
}
