package com.covisint.testdata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.covisint.transform.dao.CorrelationDAO;
import com.covisint.transform.dao.ScriptDAO;
import com.covisint.transform.model.Script;
import com.covisint.transform.util.JSRunner;

import static org.junit.Assert.*;

import javax.script.ScriptException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =  {"file:src/main/resources/META-INF/spring/camel-context.xml"})
public class TestJSRunner {

    String refName = "testRefName";
    String dataDomain = "com.mycompanyxyz";


    @Autowired
    ScriptDAO scriptDAO;

    @Autowired
    CorrelationDAO correlationDAO;

    @Test
    public void testRunJS() throws ScriptException {


        JSRunner runner = new JSRunner();

        Script script = new Script();
        script.getInputs().put("x", 10);
        script.setScript("var z = x + x; z;");
        script.setRefName("Test Script1");

        script = runner.runJS(script, null);
        System.out.println("************************"+script.getResult()+"*******************************");
       // assertEquals(20.0, script.getResult());

        /*
        JSRunner runner2 = new JSRunner();
        Script script2 = new Script();

        script2.setScript(buildTestScript());
        script2.setRefName("Test Script2");
        script2.setDataDomain("com.mycompanyxyz");
        scriptDAO.save(script2);
        Script dbScript = scriptDAO.getByRefName("Test Script2", "com.mycompanyxyz");
        dbScript.setInputs(inputs);
        dbScript = runner2.runJS(dbScript);
        Double amount = (Double) dbScript.getResult();
        assertEquals(21.0, amount.doubleValue(), 0);

        scriptDAO.delete(dbScript);
         */


    }


}
