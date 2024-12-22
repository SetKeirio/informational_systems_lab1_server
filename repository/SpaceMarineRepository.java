package informational_systems.lab1.repository;

import informational_systems.lab1.items.SpaceMarine;
import informational_systems.lab1.items.SpaceMarineResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceMarineRepository extends JpaRepository<SpaceMarine, Integer> {
    List<SpaceMarine> findByCategoryId(Integer categoryId);

    @Query("SELECT new informational_systems.lab1.items.SpaceMarineResponse(sm.id, sm.name, concat(c.x, ', ', c.y), sm.creationDate, concat(ch.name, ', ', ch.marinesCount)," +
            " sm.health, ac.categoryName, w.weaponName, mw.meleeWeaponName, u.username)" +
            " FROM SpaceMarine" +
            " sm JOIN Coordinates c ON sm.coordinatesId = c.id JOIN Chapter" +
            " ch ON sm.chapterId = ch.id" +
            " LEFT JOIN AstartesCategory ac ON sm.categoryId = ac.id JOIN Weapon w ON" +
            " sm.weaponTypeId = w.id JOIN MeleeWeapon mw ON sm.meleeWeaponId = mw.id" +
            " JOIN UserSpaceMarines usm ON sm.id = usm.spaceMarineId" +
            " JOIN User u ON usm.userId = u.id")
    List<SpaceMarineResponse> findAllSpaceMarinesWithDetails();

    @Query("SELECT new informational_systems.lab1.items.SpaceMarineResponse(sm.id, sm.name, concat(c.x, ', ', c.y), sm.creationDate, concat(ch.name, ', ', ch.marinesCount)," +
            " sm.health, ac.categoryName, w.weaponName, mw.meleeWeaponName, u.username)" +
            " FROM SpaceMarine sm" +
            " JOIN Coordinates c ON sm.coordinatesId = c.id" +
            " JOIN Chapter ch ON sm.chapterId = ch.id" +
            " LEFT JOIN AstartesCategory ac ON sm.categoryId = ac.id" +
            " JOIN Weapon w ON sm.weaponTypeId = w.id" +
            " JOIN MeleeWeapon mw ON sm.meleeWeaponId = mw.id" +
            " JOIN UserSpaceMarines usm ON sm.id = usm.spaceMarineId" +
            " JOIN User u ON usm.userId = u.id" +
            " WHERE sm.health < :maxHealth")
    List<SpaceMarineResponse> findSpaceMarinesWithHealthLessThan(@Param("maxHealth") int maxHealth);

    @Query("SELECT new informational_systems.lab1.items.SpaceMarineResponse(sm.id, sm.name, concat(c.x, ', ', c.y), sm.creationDate, concat(ch.name, ', ', ch.marinesCount)," +
            " sm.health, ac.categoryName, w.weaponName, mw.meleeWeaponName, u.username)" +
            " FROM SpaceMarine sm" +
            " JOIN Coordinates c ON sm.coordinatesId = c.id" +
            " JOIN Chapter ch ON sm.chapterId = ch.id" +
            " LEFT JOIN AstartesCategory ac ON sm.categoryId = ac.id" +
            " JOIN Weapon w ON sm.weaponTypeId = w.id" +
            " JOIN MeleeWeapon mw ON sm.meleeWeaponId = mw.id" +
            " JOIN UserSpaceMarines usm ON sm.id = usm.spaceMarineId" +
            " JOIN User u ON usm.userId = u.id" +
            " WHERE sm.id = :id")
    SpaceMarineResponse findSpaceMarineById(@Param("id") int id);

    @Modifying
    @Query("UPDATE SpaceMarine sm SET sm.categoryId = null WHERE sm.categoryId = :categoryId")
    int setCategoryToNull(@Param("categoryId") Integer categoryId);

    @Query("SELECT new informational_systems.lab1.items.SpaceMarineResponse(sm.id, sm.name, concat(c.x, ', ', c.y), sm.creationDate, concat(ch.name, ', ', ch.marinesCount)," +
            " sm.health, ac.categoryName, w.weaponName, mw.meleeWeaponName, u.username)" +
            " FROM SpaceMarine sm" +
            " JOIN Coordinates c ON sm.coordinatesId = c.id" +
            " JOIN Chapter ch ON sm.chapterId = ch.id" +
            " LEFT JOIN AstartesCategory ac ON sm.categoryId = ac.id" +
            " JOIN Weapon w ON sm.weaponTypeId = w.id" +
            " JOIN MeleeWeapon mw ON sm.meleeWeaponId = mw.id" +
            " JOIN UserSpaceMarines usm ON sm.id = usm.spaceMarineId" +
            " JOIN User u ON usm.userId = u.id" +
            " WHERE sm.categoryId IS NULL AND sm.id IN (SELECT sm.id FROM SpaceMarine sm WHERE sm.categoryId = :categoryId)")
    List<SpaceMarineResponse> findUpdatedSpaceMarinesWithNullCategory(@Param("categoryId") Integer categoryId);

    boolean existsByName(String name);


}
