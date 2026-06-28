import java.util.EnumMap;

public class Trade {

    private Player from;
    private Player to;

    private EnumMap<Variables.Resource, Integer> fromGives;
    private EnumMap<Variables.Resource, Integer> toGives;

    public Trade(Player from, Player to) {
        this.from = from;
        this.to = to;

        fromGives = new EnumMap<>(Variables.Resource.class);
        toGives = new EnumMap<>(Variables.Resource.class);

        for (Variables.Resource r : Variables.Resource.values()) {

            if (r == Variables.Resource.DESERT || r == Variables.Resource.DEFAULT) {
                continue;
            }

            fromGives.put(r, 0);
            toGives.put(r, 0);
        }
    }

    public void addFromGives(Variables.Resource r, int amount) {

        int neu = Math.max(0, fromGives.get(r) + amount);

        if (neu <= from.getResource(r)) {
            fromGives.put(r, neu);
        }
    }

    public void addToGives(Variables.Resource r, int amount) {

        int neu = Math.max(0, toGives.get(r) + amount);

        if (to == null || neu <= to.getResource(r)) {
            toGives.put(r, neu);
        }
    }

    public boolean canExecute() {

        for (Variables.Resource r : fromGives.keySet()) {

            if (from.getResource(r) < fromGives.get(r)) {
                return false;
            }

            if (to != null && to.getResource(r) < toGives.get(r)) {
                return false;
            }
        }

        return true;
    }

    public boolean canExecuteBankTrade() {

        if (to != null) {
            return false;
        }

        int totalGiven = 0;
        int totalReceived = 0;

        for (Variables.Resource r : fromGives.keySet()) {

            int give = fromGives.get(r);
            int receive = toGives.get(r);

            if (give > 0) {
                if (give % 4 != 0) {
                    return false;
                }

                totalGiven += give;
            }

            totalReceived += receive;
        }

        if (totalReceived == 0) {
            return false;
        }

        return totalGiven == totalReceived * 4;
    }

    public void execute() {

        
        if (!canExecute()) {
            return;
        }

        for (Variables.Resource r : fromGives.keySet()) {

            int give = fromGives.get(r);
            int receive = toGives.get(r);

            from.addResource(r, -give);

            if (to != null) {
                to.addResource(r, give);
            }

            if (to != null) {
                to.addResource(r, -receive);
            }

            from.addResource(r, receive);
        }
    }

    public Player getFrom() {
        return from;
    }

    public Player getTo() {
        return to;
    }

    public int getFromGives(Variables.Resource r) {
        return fromGives.get(r);
    }

    public int getToGives(Variables.Resource r) {
        return toGives.get(r);
    }

    public void setFromGives(Variables.Resource r, int amount) {
        fromGives.put(r, amount);
    }

    public void setToGives(Variables.Resource r, int amount) {
        toGives.put(r, amount);
    }
}