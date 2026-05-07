package com.backendtoolkit.common.persistence.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseModelAudit {

	@Version
	@Column(name = "version", nullable = false)
	private Long version;

	@CreatedDate
	@Column(name = "created_date", nullable = false)
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@CreatedBy
	@Column(name = "created_by", nullable = false, length = 100)
	private String createdBy;

	@LastModifiedBy
	@Column(name = "modified_by", length = 100)
	private String modifiedBy;
}
