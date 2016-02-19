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

       /* Script script = new Script();
        script.getInputs().put("x", 10);
        script.setScript("var z = x + x; z;");
        script.setRefName("Test Script1");*/
        
        //Script script = scriptDAO.getByRefName("fordPreProcessor", "iot-sol-ford");
        Script script = scriptDAO.getByRefName("bmwPreProcessor", "iot-sol-bmw");

        script = runner.runJS(script, null);
        System.out.println();
        System.out.println("************************"+script.getResult()+"*******************************");
        System.out.println();
    }


}
