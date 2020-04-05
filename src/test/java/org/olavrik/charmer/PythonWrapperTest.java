package org.olavrik.charmer;

import org.junit.*;
import org.olavrik.charmer.model.PythonWrapper;

import java.io.IOException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class PythonWrapperTest {
    private PythonWrapper pythonWrapper;

    @Before
    public void initTest() throws IOException {
        this.pythonWrapper = new PythonWrapper();
        this.pythonWrapper.setPythonPath(System.getenv("PYTHON_PATH"));
        this.pythonWrapper.startProcess();
    }

    @Test
    public void checkPythonSum() throws IOException {

        ArrayList<String> actual=this.pythonWrapper.runCmd(new String[]{"2+2"}, true);
        assertEquals("4", actual.get(0));
    }

    @Test
    public void checkPythonPrint(){
        ArrayList<String> actual=this.pythonWrapper.runCmd(new String[]{"print(\"meow\")"}, true);
        assertEquals("meow", actual.get(0));
    }

    @Test
    public void checkPandas(){
        String[] commands=new String[]{"import pandas as pd", "print (pd.Series([1])[0])"};
        ArrayList<String> actual=this.pythonWrapper.runCmd(commands, true);
        assertEquals("1",actual.get(0));
    }


    @After
    public void afterTest() {
        pythonWrapper = null;
    }
}
