package com.covisint.testdata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.covisint.transform.dao.ScriptDAO;
import com.covisint.transform.model.Script;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =  {"file:src/main/resources/META-INF/spring/camel-context.xml"})
public class TestScript {

	@Autowired
	protected ScriptDAO scriptDAO;
	
	@Test
	public void fetchScript()
	{
		Script script = scriptDAO.getByRefName("fordPreProcessor", "iot-sol-ford");
		System.out.println("Script Output "+script.getResult());
	}
}
