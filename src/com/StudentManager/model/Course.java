package com.StudentManager.model;

public class StudentGroup {
    private String id;
    private String name;
    private GroupType type;
    private String parentId; // ID của khoa (nếu type là LOP)

    public StudentGroup(String id, String name, GroupType type, String parentId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parentId = parentId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id;  }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public GroupType getType() { return type; }
    public void setType(GroupType type) { this.type = type; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    @Override
    public String toString() {
        return "StudentGroup{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", parentId='" + parentId + '\'' +
                '}';
    }

    public String toCsvString() {
        return String.join(",", id, name, type.name(), parentId != null ? parentId : "");
    }
}
