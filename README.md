# backend-common-toolkit-jpa

`backend-common-toolkit-jpa` is the companion module for `backend-common-toolkit` in projects that use JPA/Hibernate.

## What it includes

- `BaseModelAudit`
- `TimeOrderedUuid`
- `TimeOrderedUuidGenerator`
- `EncryptedStringConverter`

## When to use it

Use this module only if your service needs JPA persistence. If your project uses MongoDB, Redis, or no relational database at all, you do not need this module.

## Maven dependency

```xml
<dependency>
  <groupId>com.oathforge</groupId>
  <artifactId>backend-common-toolkit-jpa</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

You do not need to declare `backend-common-toolkit` separately. `backend-common-toolkit-jpa` already brings it in transitively.

## Quick example

```java
@Entity
public class User extends BaseModelAudit {

  @Id
  @TimeOrderedUuid
  @Column(length = 36, nullable = false, updatable = false)
  private String id;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "api_secret")
  private String apiSecret;
}
```

## Detailed documentation

- [Base persistence](docs/persistence.md)
- [JPA encryption with converters](docs/encryption.md)
