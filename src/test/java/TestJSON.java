import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;

public class TestJSON {

    @Test
    public void testExtractStringList() {
        String json1 = "{\"foo\":\"bar\", " +
                "\"makes\":[\"Toyota\",\"Honda\"]," +
                "\"foo2\":2.0}";
        String json2 = "{\"makes\":[\"Toyota\",\"Honda\"]}";
        String json3 = "{\"foo\":\"bar\", " +
                "\"makes\":[\"Toyota\",\"Honda\",\"Subaru\"]," +
                "\"foo2\":2.0}";
        String json4 = "{\"makes\":[\"Toyota\"]}";
        String json5 = "{\"foo\":\"bar\", " +
                "\"makes\":[\"Toyota\"]," +
                "\"foo2\":2.0}";

        ArrayList<String> list = new ArrayList<>();
        list.add("Toyota");
        assertEquals(list, JSONHelper.extractStringList(json4, "makes"));
        assertEquals(list, JSONHelper.extractStringList(json5, "makes"));

        list.add("Honda");
        assertEquals(list, JSONHelper.extractStringList(json1, "makes"));
        assertEquals(list, JSONHelper.extractStringList(json2, "makes"));

        list.add("Subaru");
        assertEquals(list, JSONHelper.extractStringList(json3, "makes"));
    }
}
