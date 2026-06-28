import java.util.ArrayList;

public class BuildRules {

    //Settlements
    public static boolean canBuildSettlement(Settlement settlement, Player player, ArrayList<Settlement> settlements, ArrayList<Street> streets, Game game) {
        if (!isSettlementFree(settlement)) {
            game.addConsole("Hier steht schon eine Siedlung");
            return false;
        }

        if (!hasSettlementResources(player)) {
            game.addConsole("Du hast nicht genug Ressourcen");
            return false;
        }

        if (!obeysSettlementDistanceRule(settlement, settlements)) {
            game.addConsole("Eine andere Siedlung ist zu nah");
            return false;
        }

        if (!hasConnectedStreet(settlement, player, streets)) {
            game.addConsole("Hier ist keine Straße angrenzend");
            return false;
        }

        paySettlementResources(player);
        
        return true;
    }

    //Startphase Settlements
    public static boolean canBuildStartSettlement(Settlement settlement, ArrayList<Settlement> settlements, Game game) {
        if (!isSettlementFree(settlement)) {
            game.addConsole("Hier steht schon eine Siedlung");
            return false;
        }

        if (!obeysSettlementDistanceRule(settlement, settlements)) {
            game.addConsole("Eine andere Siedlung ist zu nah");
            return false;
        }

        return true;
    }

    //Streets
    public static boolean canBuildStreet(Street street, Player player, ArrayList<Settlement> settlements, ArrayList<Street> streets) {
        if (!isStreetFree(street)) {
            return false;
        }

        if (!hasStreetResources(player)) {
            return false;
        }

        if (!hasConnectedSettlement(street, player, settlements) && !hasConnectedStreet(street, player, streets)) {
            return false;
        }

        payStreetResources(player);

        return true;
    }

    //Startphase Streets
    public static boolean canBuildStartStreet(Street street, Player player, ArrayList<Settlement> settlements) {
        if (!isStreetFree(street)) {
            return false;
        }

        if (!hasConnectedSettlement(street, player, settlements)) {
            return false;
        }

        return true;
    }

    //Cities
    public static boolean canBuildCity(Settlement settlement, Player player) {
        if (!isOwnSettlement(settlement, player)) {
            return false;
        }

        if (settlement.getCity()) {
            return false;
        }

        if (!hasCityResources(player)) {
            return false;
        }

        payCityResources(player);

        return true;
    }

    // Helpers

    // Settlement
    public static boolean isSettlementFree(Settlement settlement) {
        return !settlement.getBuild();
    }

    public static boolean obeysSettlementDistanceRule(Settlement settlement, ArrayList<Settlement> settlements) {
        for (Settlement s : settlements) {
            if (s.getBuild()) {

                double dist = distance(settlement.getCenterX(), settlement.getCenterY(), s.getCenterX(), s.getCenterY());

                if (dist < 120) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean hasConnectedStreet(Settlement settlement, Player player, ArrayList<Street> streets) {
        for (Street street : streets) {
            if (street.getOwner() == player.getPlayerNumber()) {

                double dist = distance(settlement.getCenterX(), settlement.getCenterY(), street.getCenterX(), street.getCenterY());

                if (dist < 70) {
                    return true;
                }
            }
        }

        return false;
    }

    // Street
    public static boolean isStreetFree(Street street) {
        return !street.getBuild();
    }

    public static boolean hasConnectedSettlement(Street street, Player player, ArrayList<Settlement> settlements) {
        for (Settlement settlement : settlements) {
            if (settlement.getOwner() == player.getPlayerNumber()) {

                double dist = distance(street.getCenterX(), street.getCenterY(), settlement.getCenterX(), settlement.getCenterY());

                if (dist < 70) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasConnectedStreet(Street street, Player player, ArrayList<Street> streets) {
        for (Street other : streets) {
            if (other != street&& other.getOwner() == player.getPlayerNumber()) {

                double dist = distance(street.getCenterX(), street.getCenterY(), other.getCenterX(), other.getCenterY());

                if (dist < 120) {
                    return true;
                }
            }
        }

        return false;
    }

    // City
    public static boolean isOwnSettlement(Settlement settlement, Player player) {
        if (!settlement.getBuild()) {
            return false;
        }
        
        return settlement.getOwner() == player.getPlayerNumber();
    }

    // Resourcechecks
    public static boolean hasSettlementResources(Player player) {
        return player.getResource(Variables.Resource.WOOD) >= 1 && player.getResource(Variables.Resource.BRICK) >= 1 && player.getResource(Variables.Resource.WHEAT) >= 1 && player.getResource(Variables.Resource.SHEEP) >= 1;
    }

    public static boolean hasStreetResources(Player player) {
        return player.getResource(Variables.Resource.WOOD) >= 1 && player.getResource(Variables.Resource.BRICK) >= 1;
    }

    public static boolean hasCityResources(Player player) {
        return player.getResource(Variables.Resource.WHEAT) >= 2 && player.getResource(Variables.Resource.STONE) >= 3;
    }

    // Resource payment
    public static void paySettlementResources(Player player) {
        player.addResource(Variables.Resource.WOOD, -1);
        player.addResource(Variables.Resource.BRICK, -1);
        player.addResource(Variables.Resource.WHEAT, -1);
        player.addResource(Variables.Resource.SHEEP, -1);
    }

    public static void payStreetResources(Player player) {
        player.addResource(Variables.Resource.WOOD, -1);
        player.addResource(Variables.Resource.BRICK, -1);
    }

    public static void payCityResources(Player player) {
        player.addResource(Variables.Resource.WHEAT, -2);
        player.addResource(Variables.Resource.STONE, -3);
    }

    // Helper
    public static double distance(int x1, int y1, int x2, int y2) {
        return Math.hypot(x1 - x2, y1 - y2);
    }
}