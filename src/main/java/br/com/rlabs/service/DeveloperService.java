package br.com.rlabs.service;

import java.util.Collection;
import java.util.UUID;

import br.com.rlabs.entity.model.Developer;
import br.com.rlabs.exceptions.UniqueConstraintException;

/**
 * The Developer Service Interface.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
public interface DeveloperService {

	Collection<Developer> list();

	Developer get(Long id);

	Developer getByInternal(UUID internal);

	Developer persist(Developer developer) throws UniqueConstraintException;

	Developer update(Long id, Developer developer) throws UniqueConstraintException;

	Developer update(UUID internal, Developer developer) throws UniqueConstraintException;

	Developer delete(Long id);

	Developer delete(UUID internal);

	void validateConstraints(Developer developer) throws UniqueConstraintException;
}
