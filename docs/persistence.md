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

## Audited entity example

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
