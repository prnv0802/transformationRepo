package com.covisint.testdata;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.covisint.transform.dao.RouteDefDAO;
import com.covisint.transform.model.RouteDef;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =  {"file:src/main/resources/camel-context.xml"})
public class PrepareTestData {

	@Autowired
	RouteDefDAO routeDefDAO;

	@Test
	public void testRouteDefInsert() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String yourPath = "D:\\Transformation\\poc\\";
		List<RouteDef> routeDefList = mapper.readValue(
				new File(yourPath + "TransformationService\\src\\test\\resources\\RouteDef.json"),
				new TypeReference<List<RouteDef>>() {
				});

		for (RouteDef routeDef : routeDefList) {
			routeDefDAO.create(routeDef);
		}

	}
}
