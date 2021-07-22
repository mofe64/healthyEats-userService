package com.example.healthyeatsuserservice.models;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.healthyeatsuserservice.models.Permission.*;

public enum Role {
    INDIVIDUAL(Sets.newHashSet(MEAL_READ, MEAL_WRITE, MEAL_PLAN_READ, MEAL_PLAN_WRITE, EVENT_READ, EVENT_WRITE)),
    ORGANIZATION(Sets.newHashSet(MEAL_READ, MEAL_WRITE, MEAL_PLAN_READ, MEAL_PLAN_WRITE, EVENT_READ, EVENT_WRITE)),
    ADMIN(Sets.newHashSet(MEAL_READ, MEAL_WRITE, MEAL_PLAN_READ, MEAL_PLAN_WRITE, INVENTORY_READ, INVENTORY_WRITE, USER_READ, EVENT_READ, EVENT_WRITE)),
    SUPER_ADMIN(Sets.newHashSet(ALL_ACCESS, MEAL_READ, MEAL_WRITE, MEAL_PLAN_READ, MEAL_PLAN_WRITE, INVENTORY_READ, INVENTORY_WRITE, USER_READ, USER_WRITE, EVENT_READ, EVENT_WRITE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }


}
