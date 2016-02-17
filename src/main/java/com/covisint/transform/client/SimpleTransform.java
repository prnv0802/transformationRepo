package com.covisint.transform.client;

import org.apache.camel.spring.Main;

/**
 * Starting point of application. Run it as java application.
 * It will read spring configuration file given in ${classpath}/META-INF/spring/camel-context.xml.
 * 
 * Route Configuration are given in camel-context.xml file.
 */

public class SimpleTransform {

	public static void main(String args[]) throws Exception {
		new Main().run(args);
	}
}
