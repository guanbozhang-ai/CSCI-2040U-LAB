public class AttributeFormulas {

    /**
     * Increases by 1 every $20k.
     * Currently maxes out at $100k with an ATTRIBUTE_MAX of 5.
     *
     * @param c The integer price
     * @return The cost attribute value
     */
    public static double cost(int c) {
        return Math.min(UserAttributeMap.ATTRIBUTE_MAX, ((double) c) / 20000.0);
    }

    /**
     * Increases by 1 every 100HP.
     * Currently maxes out at 500 HP with an ATTRIBUTE_MAX of 5.
     *
     * @param s The integer horsepower
     * @return The sportiness attribute value
     */
    public static double sportiness(int s) {
        return Math.min(UserAttributeMap.ATTRIBUTE_MAX, ((double) s) / 100.0);
    }

    /**
     * Increases by 1 every 20k km.
     * Currently maxes out at 100k km with an ATTRIBUTE_MAX of 5.
     *
     * @param m The integer mileage
     * @return The mileage attribute value
     */
    public static double mileage(int m) {
        return Math.min(UserAttributeMap.ATTRIBUTE_MAX, ((double) m) / 20000.0);
    }

    /**
     * Increases linearly from 0 to 8 seats.
     *
     * @param s The integer seating
     * @return The seating attribute value
     */
    public static double seating(int s) {
        return Math.min(UserAttributeMap.ATTRIBUTE_MAX,
                UserAttributeMap.ATTRIBUTE_MAX * ((double) s) / 8.0);
    }

    /**
     * Increases by 1 every 4L/100km.
     * Currently maxes out at 20L/100km with an ATTRIBUTE_MAX of 5.
     *
     * @param e The double fuel economy
     * @return The economy attribute value
     */
    public static double economy(double e) {
        return Math.min(UserAttributeMap.ATTRIBUTE_MAX, e / 4.0);
    }

    /**
     * Increases by 1 for every 3 years before 2025.
     * Currently maxes out at 2010 with an ATTRIBUTE_MAX of 5.
     *
     * @param y The int year
     * @return The recency attribute value
     */
    public static double recency(int y) {
        y = Math.max(0, -y + 2025);
        return Math.min(UserAttributeMap.ATTRIBUTE_MAX, ((double) y) / 3.0);
    }
}
