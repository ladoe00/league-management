package org.nnhl.api;

public class League extends PersistableObject
{
    private final String name;

    public League(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
