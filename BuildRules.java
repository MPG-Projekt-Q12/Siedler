import java.util.ArrayList;

public class BuildRules {

    //Settlements

    public static boolean canBuildSettlement(Settlement settlement, Player player, ArrayList<Settlement> settlements, ArrayList<Street> streets) {

        if (!isSettlementFree(settlement)) {
            return false;
        }

        if (!hasSettlementResources(player)) {
            return false;
        }

        if (!obeysSettlementDistanceRule(settlement, settlements)) {
            return false;
        }

        if (!hasConnectedStreet(settlement, player, streets)) {
            return false;
        }

        paySettlementResources(player);

        return true;
    }

    //Startphase Settlements

    public static boolean canBuildStartSettlement(Settlement settlement, ArrayList<Settlement> settlements) {

        if (!isSettlementFree(settlement)) {
            return false;
        }

        if (!obeysSettlementDistanceRule(settlement, settlements)) {
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

        if (settlement.city) {
            return false;
        }

        if (!hasCityResources(player)) {
            return false;
        }

        payCityResources(player);

        return true;
    }

    //Subfunktions
    
    //Settlement

    public static boolean isSettlementFree(Settlement settlement) {

        return !settlement.build;
    }

    public static boolean obeysSettlementDistanceRule(Settlement settlement, ArrayList<Settlement> settlements) {

        for (Settlement s : settlements) {

            if (s.build) {

                double dist = distance(settlement.centerx, settlement.centery, s.centerx, s.centery);

                if (dist < 120) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean hasConnectedStreet(Settlement settlement, Player player, ArrayList<Street> streets) {

        for (Street street : streets) {

            if (street.owner == player.playerNumber) {

                double dist = distance(settlement.centerx, settlement.centery, street.centerx, street.centery);

                if (dist < 70) {
                    return true;
                }
            }
        }

        return false;
    }

    //Street

    public static boolean isStreetFree(Street street) {

        return !street.build;
    }

    public static boolean hasConnectedSettlement(Street street, Player player, ArrayList<Settlement> settlements) {

        for (Settlement settlement : settlements) {

            if (settlement.owner == player.playerNumber) {

                double dist = distance(street.centerx, street.centery, settlement.centerx, settlement.centery);

                if (dist < 70) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasConnectedStreet(Street street, Player player, ArrayList<Street> streets) {

        for (Street other : streets) {

            if (other != street&& other.owner == player.playerNumber) {

                double dist = distance(street.centerx, street.centery, other.centerx, other.centery);

                if (dist < 120) {
                    return true;
                }
            }
        }

        return false;
    }

    //City

    public static boolean isOwnSettlement(Settlement settlement, Player player) {

        if (!settlement.build) {
            return false;
        }

        return settlement.owner == player.playerNumber;
    }

    //Resourcechecks

    public static boolean hasSettlementResources(Player player) {

        return player.getResource(Variables.Resource.WOOD) >= 1
        && player.getResource(Variables.Resource.BRICK) >= 1
        && player.getResource(Variables.Resource.WHEAT) >= 1
        && player.getResource(Variables.Resource.SHEEP) >= 1;
    }

    public static boolean hasStreetResources(Player player) {

        return player.getResource(Variables.Resource.WOOD) >= 1
        && player.getResource(Variables.Resource.BRICK) >= 1;
    }

    public static boolean hasCityResources(Player player) {

        return player.getResource(Variables.Resource.WHEAT) >= 2
        && player.getResource(Variables.Resource.STONE) >= 3;
    }

    //Resource payment

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

    //Helper

    public static double distance(int x1, int y1, int x2, int y2) {

        return Math.hypot(x1 - x2, y1 - y2);
    }
}