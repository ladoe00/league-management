package org.nnhl.api;

import java.util.Optional;

public abstract class PersistableObject
{
    private Optional<Integer> id;

    protected PersistableObject()
    {
        id = Optional.empty();
    }

    public Optional<Integer> getId()
    {
        return id;
    }

    public void setId(Optional<Integer> id)
    {
        this.id = id;
    }

    public void setId(Integer id)
    {
        this.id = Optional.ofNullable(id);
    }
}
