package com.covisint.testdata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.covisint.transform.dao.ScriptDAO;
import com.covisint.transform.dao.ScriptSecurityPolicyDAO;
import com.covisint.transform.dao.ScriptTypeDAO;
import com.covisint.transform.model.DynamicAttribute;
import com.covisint.transform.model.DynamicAttributeSet;
import com.covisint.transform.model.DynamicAttributeType;
import com.covisint.transform.model.Script;
import com.covisint.transform.model.ScriptContextObject;
import com.covisint.transform.model.ScriptSecurityPolicy;
import com.covisint.transform.model.ScriptType;

import java.util.UUID;

/**
 * Test will prepare script data for PreProcessor test.
 * It would run the testcase and delete the data
 * @author PJ00452307 - pranavjha
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =  {"file:src/main/resources/META-INF/spring/camel-context.xml"})
public class TestPreprocessorScript {

    @Autowired
    ScriptDAO scriptDAO;

    @Autowired
    ScriptTypeDAO scriptTypeDAO;

    @Autowired
    ScriptSecurityPolicyDAO scriptSecurityPolicyDAO;

    @Before
    public void prepareTestData() throws Exception {

        Script script = null;
        ScriptType scriptType = null;
        ScriptSecurityPolicy scriptSecurityPolicy = null;
        String refName = "fordPreProcessor";
        String realm = "iot-sol-ford";

        if(scriptTypeDAO.getByRefName(refName, realm) ==  null) {
            scriptType = new ScriptType();
            scriptType.setRealm(realm);
            scriptType.setId(UUID.randomUUID().toString());
            scriptType.setRefName(refName);

            DynamicAttribute att = new DynamicAttribute();
            att.setRefName("dynamicAttribute1");
            att.setType(DynamicAttributeType.Object);
            att.setLabel("testAttributes");
            DynamicAttributeSet inputs = new DynamicAttributeSet();
            inputs.getAttributes().put("testAttributes", att);
            scriptType.setInputs(inputs);

            ScriptContextObject contextObject = new ScriptContextObject();
            contextObject.setName("someService");
            contextObject.setType("serviceBean");
            contextObject.setServiceName("someService");
            scriptType.getScriptContextObjects().put("someService", contextObject);

            ScriptContextObject contextObject1 = new ScriptContextObject();
            contextObject1.setName("anotherService");
            contextObject1.setType("serviceBean");
            contextObject1.setServiceName("anotherService");
            scriptType.getScriptContextObjects().put("anotherService", contextObject1);

            ScriptContextObject contextObject4 = new ScriptContextObject();
            contextObject4.setName("routeEngine");
            contextObject4.setType("javaBean");
            scriptType.getScriptContextObjects().put("routeEngine", contextObject4);


            scriptTypeDAO.create(scriptType);
        }

        if(scriptSecurityPolicyDAO.getByRefName(refName, realm) ==  null) {
            scriptSecurityPolicy = new ScriptSecurityPolicy();
            scriptSecurityPolicy.setId(UUID.randomUUID().toString());
            scriptSecurityPolicy.setRefName(refName);
            scriptSecurityPolicy.getAllowedPackages().put("comcovisinttransformdao",
                    "com.covisint.transform.dao");
            scriptSecurityPolicy.getAllowedPackages().put("comcovisinttransformdaoimpl",
                    "com.covisint.transform.dao.impl");

            scriptSecurityPolicy.getAllowedPackages().put("comcovisinttransformmodel",
                    "com.covisint.transform.model");
            scriptSecurityPolicy.getAllowedPackages().put("javalang",
                    "java.lang");
            scriptSecurityPolicy.getAllowedPackages().put("javautil",
                    "java.util");
            scriptSecurityPolicy.getAllowedPackages().put("comcovisinttransformservices",
                    "com.covisint.transform.services");
            scriptSecurityPolicy.getAllowedPackages().put("comeiscoreapiv1service",
                    "com.eis.core.api.v1.service");

            scriptSecurityPolicy.getAllowedPackages().put("comcovisinttransformutil",
                    "com.covisint.transform.util");

            scriptSecurityPolicy.getAllowedPackages().put("comfasterxmljacksondatabind",
                    "com.fasterxml.jackson.databind");

            scriptSecurityPolicy.getAllowedPackages().put("javaio",
                    "java.io");


            scriptSecurityPolicyDAO.create(scriptSecurityPolicy);
        }

        if(scriptDAO.getByRefName(refName, realm) ==  null) {
            script = new Script();
            script.setRealm(realm);
            script.setScriptSecurityPolicy(scriptSecurityPolicy);
            script.setScriptSecurityPolicyId(scriptSecurityPolicy.getId());
            script.setType(scriptType);
            script.setScriptTypeId(scriptType.getId());

            script.getInputs().put("x", 10);
            script.setScript("var z = x + x; z;");
            
            script.setRefName(refName);
            script.setFunctionType("fordPreProcessor");
            script.setActive(true);
            scriptDAO.create(script);
        }
    }
    
    @Test
    public void runPreProcessorTest()
    {
    	System.out.println("Data is prepared");
    }
}
