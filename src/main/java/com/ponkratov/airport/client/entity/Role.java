package com.ponkratov.airport.client.entity;

public class Role implements Entity {
    private int roleID;
    private String roleName;

    public Role(int roleID, String roleName) {
        this.roleID = roleID;
        this.roleName = roleName;
    }

    public Role() {
    }

    public int getRoleID() {
        return roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toReport() {
        return null;
    }
}
