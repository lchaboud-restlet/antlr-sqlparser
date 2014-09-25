package com.restlet.sqlimport.model.resdef;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Root of the Resdef model to describe the entity stores.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Resdef {

	private List<Entity> entities = new ArrayList<Entity>();

	public Entity getEntityForName(final String name) {
		for(final Entity entity : entities) {
			if(entity.getName().equalsIgnoreCase(name)) {
				return entity;
			}
		}
		return null;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(final List<Entity> entities) {
		this.entities = entities;
	}

}
