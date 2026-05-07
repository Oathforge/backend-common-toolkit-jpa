package com.backendtoolkit.common.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.backendtoolkit.common.enums.ExceptionEnum;
import com.backendtoolkit.common.exception.InternalServerErrorException;
import com.backendtoolkit.common.security.encryption.EncryptionProperties;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Converter
@Component
@ConditionalOnProperty(name = "backend-toolkit.security.encryption.key")
@RequiredArgsConstructor
public class EncryptedStringConverter implements AttributeConverter<String, String> {

	private final EncryptionProperties encryptionProperties;

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (attribute == null) {
			return null;
		}
		try {
			return EncryptionUtil.encrypt(attribute, encryptionProperties.getKey());
		} catch (Exception e) {
			throw new InternalServerErrorException(ExceptionEnum.ENC0001.name(), ExceptionEnum.ENC0001.getValue());
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		try {
			return EncryptionUtil.decrypt(dbData, encryptionProperties.getKey());
		} catch (Exception e) {
			throw new InternalServerErrorException(ExceptionEnum.ENC0002.name(), ExceptionEnum.ENC0002.getValue());
		}
	}
}
