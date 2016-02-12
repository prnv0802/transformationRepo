package com.covisint.testdata;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import com.covisint.transform.dao.RouteDefDAO;
import com.covisint.transform.dao.impl.RouteDefDAOImpl;
import com.covisint.transform.route.RouteDef;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrepareTestData {

	@Autowired
	ResourceLoader resourceLoader;

	@Test
	public void testRouteDefInsert() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String yourPath = "D:\\Transformation\\poc\\";
		List<RouteDef> routeDefList = mapper.readValue(
				new File(yourPath + "TransformationService\\src\\test\\resources\\RouteDef.json"),
				new TypeReference<List<RouteDef>>() {
				});

		for (RouteDef routeDef : routeDefList) {
			RouteDefDAO dao = new RouteDefDAOImpl();
			dao.save(routeDef);
		}

	}
}
