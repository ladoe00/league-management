package org.nnhl.api;

import java.security.Principal;

import com.google.common.base.Preconditions;

public class Player extends PersistableObject implements Principal
{
    private final String firstName;

    private final String lastName;

    private final String email;

    private final Position position;

    public Player(String firstName, String lastName, String email, Position position)
    {
        this.firstName = Preconditions.checkNotNull(firstName, "firstName cannot be null");
        this.lastName = Preconditions.checkNotNull(lastName, "lastName cannot be null");
        this.email = Preconditions.checkNotNull(email, "email cannot be null");
        this.position = Preconditions.checkNotNull(position, "position cannot be null");
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public Position getPosition()
    {
        return position;
    }

    @Override
    @JsonIgnore
    public String getName()
    {
        return email;
    }
}
