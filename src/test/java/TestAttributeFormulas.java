import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAttributeFormulas {
    @Test
    public void testCost() {
        assertEquals(2.0, AttributeFormulas.cost(40000));
    }

    @Test
    public void testCostCap() {
        assertEquals(5.0, AttributeFormulas.cost(120000));
    }

    @Test
    public void testSportiness() {
        assertEquals(3.0, AttributeFormulas.sportiness(300));
    }

    @Test
    public void testSportinessCap() {
        assertEquals(5.0, AttributeFormulas.sportiness(600));
    }

    @Test
    public void testMileage() {
        assertEquals(3.0, AttributeFormulas.mileage(60000));
    }

    @Test
    public void testMileageCap() {
        assertEquals(5.0, AttributeFormulas.mileage(120000));
    }

    @Test
    public void testSeating() {
        assertEquals(2.5, AttributeFormulas.seating(4));
    }

    @Test
    public void testEconomy() {
        assertEquals(2.0, AttributeFormulas.economy(8.0));
    }

    @Test
    public void testEconomyCap() {
        assertEquals(5.0, AttributeFormulas.economy(25.0));
    }

    @Test
    public void testRecency() {
        assertEquals(1.0, AttributeFormulas.recency(2022));
    }

    @Test
    public void testRecencyCap() {
        assertEquals(5.0, AttributeFormulas.recency(1984));
    }

    @Test
    public void testRecencyLow() {
        assertEquals(0.0, AttributeFormulas.recency(2026));
    }
}
