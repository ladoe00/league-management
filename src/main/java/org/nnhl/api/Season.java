package org.nnhl.api;

import java.util.ArrayList;
import java.util.List;

public class Season
{
    private final String leagueName;

    private List<Game> games;

    public Season(String leagueName)
    {
        this.leagueName = leagueName;
        this.games = new ArrayList<>();
    }

    public String getLeagueName()
    {
        return leagueName;
    }

    public void addGame(Game game)
    {
        games.add(game);
    }
}
