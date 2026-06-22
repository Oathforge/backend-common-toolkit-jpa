# Base Persistence

## Purpose

This module provides a shared JPA foundation for auditing and identifier generation in services that work with relational entities.

## Components

- `BaseModelAudit`
- `TimeOrderedUuid`
- `TimeOrderedUuidGenerator`

## BaseModelAudit

`BaseModelAudit` encapsulates commonly repeated audit fields:
- `version`: optimistic locking control
- `createdDate`: creation timestamp
- `modifiedDate`: last modification timestamp
- `createdBy`: user or process that created the entity
- `modifiedBy`: user or process that performed the last modification

`BaseModelAudit` only provides the mapped fields and Spring Data auditing annotations. Each JPA service must still enable auditing in its own Spring configuration.

Required baseline:
- `@EnableJpaAuditing`

Required for `createdBy` and `modifiedBy` population:
- an `AuditorAware<?>` bean that resolves the current user according to the service's authentication setup

Recommended approach:
- read the current user from the Spring Security context, or replace that part with the security abstraction already used by the service
- if some writes can happen without an authenticated user, optionally return a default value such as `"system"`

The bean name is arbitrary. It only needs to match `auditorAwareRef`.

The example below uses `SecurityContextHolder` because it is the most common Spring setup, but that part can be replaced with any project-specific way of obtaining the current user.

Example:

```java
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

  @Bean
  AuditorAware<String> auditorProvider() {
    return () -> {
      try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
          return Optional.of("system");
        }
        return Optional.ofNullable(authentication.getName()).or(() -> Optional.of("system"));
      } catch (Exception e) {
        return Optional.of("system");
      }
    };
  }
}
```

Without that service-level configuration, Spring Data JPA auditing will not populate the audit fields automatically.

## Audited entity example

`@Id` is still required. `@TimeOrderedUuid` only provides the ID generation strategy.

```java
@Entity
@Table(name = "app_user")
public class User extends BaseModelAudit {

  @Id
  @TimeOrderedUuid
  @Column(length = 36, nullable = false, updatable = false)
  private String id;

  @Column(nullable = false)
  private String email;
}
```

## TimeOrderedUuid

`TimeOrderedUuid` generates 36-character `String` identifiers with time ordering.

```java
@Id
@TimeOrderedUuid
@Column(length = 36, nullable = false, updatable = false)
private String id;
```

## When to use it

- Entities whose IDs are exposed through an API.
- Models that need a consistent identifier convention.
- JPA services that require shared auditing.
