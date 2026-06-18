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
  <version>1.1.0</version>
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

## Auditing activation

`BaseModelAudit` defines the audit fields and Spring Data annotations, but each JPA service must still enable Spring Data JPA auditing in its own application context.

At minimum, the service must declare `@EnableJpaAuditing`.

If the service wants Spring to populate `createdBy` and `modifiedBy`, it must also provide an `AuditorAware<?>` bean that resolves the current user from the authentication mechanism used by that service.

Typical approach:

- read the current user from the Spring Security context, or replace that part with the service's own user-resolution mechanism
- if the operation can run without an authenticated user, optionally return a default value such as `"system"`

The bean name is not fixed. It only has to match the value used in `auditorAwareRef`.

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

Without this configuration, extending `BaseModelAudit` is not enough to make JPA auditing run.

## Detailed documentation

- [Base persistence](docs/persistence.md)
- [JPA encryption with converters](docs/encryption.md)
