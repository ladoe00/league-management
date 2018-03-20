package org.nnhl.api;

import java.time.LocalDate;

public class Game extends PersistableObject
{
    private final LocalDate date;

    public Game(LocalDate date)
    {
        this.date = date;
    }

    public LocalDate getDate()
    {
        return date;
    }
}
