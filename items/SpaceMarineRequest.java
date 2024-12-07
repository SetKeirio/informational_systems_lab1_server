package informational_systems.lab1.items;

import informational_systems.lab1.items.Coordinates;

public class SpaceMarineRequest {
    private String name;
    private Coordinates coordinatesId; // Custom object for coordinates
    private int chapterId; // ID of the chapter
    private Integer health; // Optional health value
    private String category; // Category name instead of ID
    private String weaponType; // Weapon type name instead of ID
    private String meleeWeapon; // Melee weapon name instead of ID
    private int userId; // ID of the user creating the Space Marine

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinatesId() {
        return coordinatesId;
    }

    public void setCoordinatesId(Coordinates coordinatesId) {
        this.coordinatesId = coordinatesId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }

    public String getMeleeWeapon() {
        return meleeWeapon;
    }

    public void setMeleeWeapon(String meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}