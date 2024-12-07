package informational_systems.lab1.items;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "spacemarine")
public class SpaceMarine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCoordinatesId() {
        return coordinatesId;
    }

    public void setCoordinatesId(Integer coordinatesId) {
        this.coordinatesId = coordinatesId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getWeaponTypeId() {
        return weaponTypeId;
    }

    public void setWeaponTypeId(Integer weaponTypeId) {
        this.weaponTypeId = weaponTypeId;
    }

    public Integer getMeleeWeaponId() {
        return meleeWeaponId;
    }

    public void setMeleeWeaponId(Integer meleeWeaponId) {
        this.meleeWeaponId = meleeWeaponId;
    }

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "coordinates_id", nullable = false)
    private Integer coordinatesId;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "chapter_id", nullable = false)
    private Integer chapterId;

    @Column
    private Integer health;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "weapon_type_id", nullable = false)
    private Integer weaponTypeId;

    @Column(name = "melee_weapon_id", nullable = false)
    private Integer meleeWeaponId;

    // Getters and Setters
}