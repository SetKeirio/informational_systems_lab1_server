package informational_systems.lab1.items;

import jakarta.persistence.*;

@Entity
@Table(name = "meleeweapon")
public class MeleeWeapon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "melee_weapon_name", nullable = false, unique = true, length = 50)
    private String meleeWeaponName;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMeleeWeaponName() {
        return meleeWeaponName;
    }

    public void setMeleeWeaponName(String meleeWeaponName) {
        this.meleeWeaponName = meleeWeaponName;
    }
}