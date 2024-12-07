package informational_systems.lab1.items;

import jakarta.persistence.*;

@Entity
@Table(name = "userspacemarines")
@IdClass(UserSpaceMarinesId.class)
public class UserSpaceMarines {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSpaceMarineId() {
        return spaceMarineId;
    }

    public void setSpaceMarineId(Integer spaceMarineId) {
        this.spaceMarineId = spaceMarineId;
    }

    @Id
    @Column(name = "space_marine_id")
    private Integer spaceMarineId;

    // Getters and Setters
}