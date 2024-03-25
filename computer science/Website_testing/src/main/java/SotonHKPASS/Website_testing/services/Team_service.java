package SotonHKPASS.Website_testing.services;

import java.util.List;

import SotonHKPASS.Website_testing.entity.Team_quantity;

public interface Team_service {
    public abstract void createTeam(Team_quantity team);

    public abstract void updateTeam(String id, Team_quantity team);

    public abstract void deleteTeam(String id);

    public abstract List<Team_quantity> getTeam_quantities();

}