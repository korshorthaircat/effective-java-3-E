package ch12.item89.enumsingleton;

import java.util.Arrays;

// Enum singleton - the preferred approach - Page 311
public enum EnumElvis {
    INSTANCE;
    private String[] favoriteSongs =
        { "Hound Dog", "Heartbreak Hotel" };
    public void printFavorites() {
        System.out.println(Arrays.toString(favoriteSongs));
    }
}