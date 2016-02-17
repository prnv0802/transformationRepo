package com.covisint.transform.services;

import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;

import com.covisint.transform.dao.ScriptDAO;
import com.covisint.transform.model.Script;
import com.covisint.transform.util.JSRunner;

public class PreProcessor {

	@Autowired
	protected JSRunner jsRunner;
	
	@Autowired
	protected ScriptDAO scriptDAO;
	
	public void readData() throws ScriptException
	{
		Script script = scriptDAO.getByRefName("fordPreProcessor", "iot-sol-ford");
		if(script == null)
		{
			System.out.println("readData method read in PreProcessor in Java code");
		}
		else
		{
			Script returnScript = jsRunner.runJS(script, null);
			System.out.println("Preprocessor**********************"+returnScript.getResult());
		}
		
	}
}
