package informational_systems.lab1.items;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "objecthistory")
public class ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "space_marine_id", nullable = false)
    private Integer spaceMarineId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

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