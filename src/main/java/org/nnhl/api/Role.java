package org.nnhl.api;

public enum Role
{
    ADMIN("ADMIN"), MANAGER("MANAGER");

    public class Names
    {
        public static final String ADMIN = "ADMIN";

        public static final String MANAGER = "MANAGER";
    }

    private final String name;

    private Role(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
