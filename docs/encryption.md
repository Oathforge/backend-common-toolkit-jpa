# JPA Encryption with Converters

## Purpose

This module integrates the base toolkit encryption utilities with JPA through an `AttributeConverter`.

## Component

- `EncryptedStringConverter`

## What it does

- encrypts the value before it is persisted
- decrypts it automatically when the entity is read
- reuses `EncryptionUtil` and `backend-toolkit.security.encryption.key`

## Usage example

```java
@Entity
public class ExternalCredential {

  @Id
  private String id;

  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "api_secret")
  private String apiSecret;
}
```

## Configuration

```yml
backend-toolkit:
  security:
    encryption:
      key: ${APP_ENCRYPTION_KEY}
```

## When to use it

- integration secrets
- persisted tokens
- sensitive values that should not be stored in plain text
