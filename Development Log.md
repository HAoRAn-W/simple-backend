# Development Log
```shell
error:object references an unsaved transient instance - save the transient instance before flushing: one.whr.simple.entity.Role
```
this is because when saveing new users, I didn't retrieve role from db, instead i created a new instance of role:
```java
roleSet.add(new Role(EnumRole.ROLE_USER));
```

correction:
```java
 Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roleSet.add(userRole);
```

Http-Only is secure