package org.olavrik.charmer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.olavrik.charmer.controller.DataProvider;

import java.io.IOException;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class DataProviderTest {
    private DataProvider dataProvider;
    String filePath= Paths.get("").toAbsolutePath().toString()+
            "/src/test/java/org/olavrik/charmer/test.csv";

    @Before
    public void initTest() {
        dataProvider = new DataProvider();
    }

    @Test
    public void checkFileExist(){
        assertEquals(new Boolean(true), dataProvider.checkFile(filePath));
    }

    @Test
    public void checkCallPython() throws InterruptedException {
        assertEquals(new Boolean(true), dataProvider.checkPython(System.getenv("PYTHON_PATH")));
    }

    @Test
    public void checkSizeData() throws IOException {
        dataProvider.openCSVSession(filePath);
        assertEquals("6", dataProvider.getCSVSize());
    }

    @Test
    public void checkDataHeader() throws IOException {
        dataProvider.openCSVSession(filePath);
        assertArrayEquals(new String[]{"Id","\'name\'", "\'mass_to_earth\'"}, dataProvider.getCSVHeader());
    }

    @Test
    public void checkCSVContent() throws IOException {
        dataProvider.openCSVSession(filePath);
        assertArrayEquals(new String[][]{{"0", "\"Earth, planet\"", "1.0"}, {"1", "Moon", "0.606"}, {"2","Mars", "0.107"}}, dataProvider.getCSVContent());

    }

    @After
    public void afterTest() {
        dataProvider = null;
    }
}
