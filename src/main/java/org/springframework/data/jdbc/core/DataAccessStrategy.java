/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jdbc.core;

import java.util.Map;

import org.springframework.data.jdbc.core.mapping.JdbcPersistentProperty;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.lang.Nullable;

/**
 * Abstraction for accesses to the database that should be implementable with a single SQL statement per method and
 * relates to a single entity as opposed to {@link JdbcAggregateOperations} which provides interactions related to
 * complete aggregates.
 *
 * @author Jens Schauder
 * @since 1.0
 */
public interface DataAccessStrategy {

	/**
	 * Inserts a the data of a single entity. Referenced entities don't get handled.
	 * 
	 * @param instance the instance to be stored. Must not be {@code null}.
	 * @param domainType the type of the instance. Must not be {@code null}.
	 * @param additionalParameters name-value pairs of additional parameters. Especially ids of parent entities that need
	 *          to get referenced are contained in this map. Must not be {@code null}.
	 * @param <T> the type of the instance.
	 */
	<T> void insert(T instance, Class<T> domainType, Map<String, Object> additionalParameters);

	/**
	 * Updates the data of a single entity in the database. Referenced entities don't get handled.
	 *
	 * @param instance the instance to save. Must not be {@code null}.
	 * @param domainType the type of the instance to save. Must not be {@code null}.
	 * @param <T> the type of the instance to save.
	 */
	<T> void update(T instance, Class<T> domainType);

	/**
	 * deletes a single row identified by the id, from the table identified by the domainType. Does not handle cascading
	 * deletes.
	 *
	 * @param id the id of the row to be deleted. Must not be {@code null}.
	 * @param domainType the type of entity to be deleted. Implicitely determines the table to operate on. Must not be
	 *          {@code null}.
	 */
	void delete(Object id, Class<?> domainType);

	/**
	 * Deletes all entities reachable via {@literal propertyPath} from the instance identified by {@literal rootId}.
	 *
	 * @param rootId Id of the root object on which the {@literal propertyPath} is based. Must not be {@code null}.
	 * @param propertyPath Leading from the root object to the entities to be deleted. Must not be {@code null}.
	 */
	void delete(Object rootId, PropertyPath propertyPath);

	/**
	 * Deletes all entities of the given domain type.
	 * 
	 * @param domainType the domain type for which to delete all entries. Must not be {@code null}.
	 * @param <T> type of the domain type.
	 */
	<T> void deleteAll(Class<T> domainType);

	/**
	 * Deletes all entities reachable via {@literal propertyPath} from any instance.
	 *
	 * @param propertyPath Leading from the root object to the entities to be deleted. Must not be {@code null}.
	 */
	void deleteAll(PropertyPath propertyPath);

	/**
	 * Counts the rows in the table representing the given domain type.
	 *
	 * @param domainType the domain type for which to count the elements. Must not be {@code null}.
	 * @return the count. Guaranteed to be not {@code null}.
	 */
	long count(Class<?> domainType);

	/**
	 * Loads a single entity identified by type and id.
	 *
	 * @param id the id of the entity to load. Must not be {@code null}.
	 * @param domainType the domain type of the entity. Must not be {@code null}.
	 * @param <T> the type of the entity.
	 * @return Might return {@code null}.
	 */
	@Nullable
	<T> T findById(Object id, Class<T> domainType);

	/**
	 * Loads alls entities of the given type.
	 *
	 * @param domainType the type of entities to load. Must not be {@code null}.
	 * @param <T> the type of entities to load.
	 * @return Guaranteed to be not {@code null}.
	 */
	<T> Iterable<T> findAll(Class<T> domainType);

	/**
	 * Loads all entities that match one of the ids passed as an argument. It is not guaranteed that the number of ids
	 * passed in matches the number of entities returned.
	 * 
	 * @param ids the Ids of the entities to load. Must not be {@code null}.
	 * @param domainType the type of entities to laod. Must not be {@code null}.
	 * @param <T> type of entities to load.
	 * @return the loaded entities. Guaranteed to be not {@code null}.
	 */
	<T> Iterable<T> findAllById(Iterable<?> ids, Class<T> domainType);

	/**
	 * Finds all entities reachable via {@literal property} from the instance identified by {@literal rootId}.
	 *
	 * @param rootId Id of the root object on which the {@literal propertyPath} is based.
	 * @param property Leading from the root object to the entities to be found.
	 */
	<T> Iterable<T> findAllByProperty(Object rootId, JdbcPersistentProperty property);

	/**
	 * returns if a row with the given id exists for the given type.
	 * 
	 * @param id the id of the entity for which to check. Must not be {@code null}.
	 * @param domainType the type of the entity to check for. Must not be {@code null}.
	 * @param <T> the type of the entity.
	 * @return {@code true} if a matching row exists, otherwise {@code false}.
	 */
	<T> boolean existsById(Object id, Class<T> domainType);
}
