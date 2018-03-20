package org.nnhl.api;

import java.util.ArrayList;
import java.util.List;

public class Season
{
    private List<Game> games;

    public Season()
    {
        this.games = new ArrayList<>();
    }

    public void addGame(Game game)
    {
        games.add(game);
    }
}
