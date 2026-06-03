package org.oathforge.toolkit.jpa.persistence.id;

import java.util.EnumSet;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;

import com.github.f4b6a3.uuid.UuidCreator;

public class TimeOrderedUuidGenerator implements BeforeExecutionGenerator {

	private static final long serialVersionUID = 2728632643894757658L;

	@Override
	public EnumSet<EventType> getEventTypes() {
		return EventTypeSets.INSERT_ONLY;
	}

	@Override
	public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue,
			EventType eventType) {
		return currentValue == null ? UuidCreator.getTimeOrdered().toString() : currentValue;
	}
}
