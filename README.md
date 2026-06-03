# backend-common-toolkit-jpa

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![License](https://img.shields.io/badge/License-Apache%202.0-blue)
![Maven Central](https://img.shields.io/maven-central/v/io.github.oathforge/backend-common-toolkit-jpa)
![Release](https://img.shields.io/github/v/release/Oathforge/backend-common-toolkit-jpa)

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
  <groupId>io.github.oathforge</groupId>
  <artifactId>backend-common-toolkit-jpa</artifactId>
  <version>1.0.0</version>
</dependency>
```

Do not declare `backend-common-toolkit` separately when you use this module.

`backend-common-toolkit-jpa` already includes `backend-common-toolkit` transitively, so importing both dependencies is unnecessary.

If your service needs JPA support, declare only `backend-common-toolkit-jpa`.

## Quick example

`@Id` is still required. `@TimeOrderedUuid` only provides the ID generation strategy.

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
