package com.programyourhome.adventureroom.module.immerse.model;

import com.programyourhome.adventureroom.model.resource.AbstractExternalResource;
import com.programyourhome.immerse.domain.Room;

public class RoomExternalResource extends AbstractExternalResource<Room> {

    public RoomExternalResource(Room room) {
        super(room);
    }

    public Room getRoom() {
        return this.getWrappedObject();
    }

    @Override
    public String getId() {
        // TODO: sanitize name to id with sanitizer function as used in service project.
        return "" + this.getRoom().getName();
    }

    @Override
    public String getName() {
        return this.getRoom().getName();
    }

    @Override
    public String getDescription() {
        return this.getRoom().getDescription();
    }

}
