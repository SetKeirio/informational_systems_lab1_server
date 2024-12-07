package informational_systems.lab1.items;

import java.io.Serializable;

public class UserSpaceMarinesId implements Serializable {
    private Integer userId;
    private Integer spaceMarineId;

    // Default constructor, equals, and hashCode methods

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
}
