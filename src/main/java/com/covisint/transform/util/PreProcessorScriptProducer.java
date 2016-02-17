package com.covisint.transform.util;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.covisint.transform.dao.CorrelationDAO;
import com.covisint.transform.dao.ScriptDAO;
import com.covisint.transform.model.Correlation;
import com.covisint.transform.model.DynamicAttribute;
import com.covisint.transform.model.DynamicAttributeType;
import com.covisint.transform.model.Script;

/**
 * Camel producer for Scripts
 *
 * @author aaldredge
 */
public class PreProcessorScriptProducer extends DefaultProducer {

	private static final Logger LOG = LoggerFactory.getLogger(PreProcessorScriptProducer.class);

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            endPoint instance
	 */
	public PreProcessorScriptProducer(PreProcessorScriptEndpoint endpoint) {
		super(endpoint);
	}

	/**
	 * @param exchange
	 *            created exchange
	 * @throws Exception
	 *             thrown by camel
	 */
	public void process(Exchange exchange) throws Exception {
		ScriptDAO scriptDAO = getEndpoint().getScriptDAO();
		if (scriptDAO == null) {
			throw new IllegalArgumentException("scriptDAO is not being initialized  ");
		}
		CorrelationDAO correlationDAO = getEndpoint().getCorrelationDAO();
		if (correlationDAO == null) {
			throw new IllegalArgumentException("correlationDAO is not being initialized  ");
		}

		// get UserProfileDAO associated with Endpoint

		Script script = scriptDAO.getByRefName(getEndpoint().getScriptName(), getEndpoint().getRealm());
		if (script != null) {
			boolean exceptionOccurred = false;
			try {
				if (!script.isBlacklisted()) {
					// I guess should do this?
					List<String> blackListedPackages = new ArrayList<String>();
					Correlation correlation = correlationDAO.getByRefName("BlackListScriptPackages",
							Constants.FORD_REALM);
					if (correlation != null) {
						blackListedPackages = (List<String>) correlation.getValue();
					}
					// set inputs
					if (script.getType() != null && script.getType().getInputs() != null
							&& script.getType().getInputs().getAttributes() != null) {

						for (DynamicAttribute attribute : script.getType().getInputs().getAttributes().values()) {
							String attributeName = attribute.getRefName();

							if (attributeName != null) {
								if ("body".equalsIgnoreCase(attributeName)) {

									if (DynamicAttributeType.String.equals(attribute.getType())
											|| DynamicAttributeType.Text.equals(attribute.getType())) {
										attribute.setValue(exchange.getIn().getBody(String.class));
									} else if (DynamicAttributeType.Object.equals(attribute.getType())) {

										attribute.setValue(exchange.getIn().getBody());

									} else {

										// the others are weird for a body, so
										// not supported
										throw new UnsupportedOperationException("Casting of exchange body to "
												+ attribute.getType() + " not supported.");

									}
								} else if (attributeName.toLowerCase().startsWith("header_")) {

									String headerKey = attributeName.substring("header_".length());
									attribute.setValue(exchange.getIn().getHeader(headerKey));

								} else if (attributeName.toLowerCase().startsWith("property_")) {

									String propertyKey = attributeName.substring("property_".length());
									attribute.setValue(exchange.getProperty(propertyKey));

								} else if (getEndpoint().getParameters() != null
										&& getEndpoint().getParameters().get(attributeName) != null) {

									attribute.setValue(getEndpoint().getParameters().get(attributeName));

								} else {
									if (attribute.isRequired()) {
										throw new IllegalStateException("Attribute:" + attribute.getRefName()
												+ " could not be resolved to a header or property value make sure"
												+ " your variables start with header_ or property_");
									} else {
										if (LOG.isWarnEnabled()) {
											LOG.warn("Attribute:" + attribute.getRefName()
													+ " could not be resolved to a header or property value make sure"
													+ " your variables start with header_ or property_."
													+ "attribute is not required so ignoring");
										}
									}
								}
							}
						}
					}

					boolean useRunAs = false;

					if (script.getRunAsId() != null) { // null check for
														// UserProfile as well
														// if its exists
						useRunAs = true;
					}

					Script returnScript = null;
					try {
						if (useRunAs) {
							// get Principal Collection

							// build Subject
							// bind ThreadState

							if (LOG.isInfoEnabled()) {
								LOG.info("Running script under with RunAs with Id:" + script.getRunAsId());
							}

							// get UserProfile from script runAsID

							// Add userprofile and realm in Session for obtained
							// Subject

							// Add RUN-SCRIPT action in Session
						} else {

							if (LOG.isWarnEnabled()) {
								LOG.warn(">> Running script with no associated userProfile, service calls will fail");

							}
						}

						JSRunner runner = new JSRunner();
						returnScript = runner.runJS(script, blackListedPackages);

					} finally {

						if (useRunAs) {
							// Logout Subject and clear SecureSession
						}

						// restore ThreadState

					}

					// set outputs
					if (returnScript.getType() != null && returnScript.getType().getOutputs() != null
							&& returnScript.getType().getOutputs().getAttributes() != null) {
						for (DynamicAttribute attribute : returnScript.getType().getOutputs().getAttributes()
								.values()) {
							String attributeName = attribute.getRefName();
							if (attributeName != null) {
								if ("body".equals(attributeName)) {
									exchange.getIn().setBody(attribute.getValue());
								} else if (attributeName.startsWith("header_")) {
									String headerKey = attributeName.substring("header_".length());
									exchange.getIn().setHeader(headerKey, attribute.getValue());
								} else if (attributeName.startsWith("property_")) {
									String propertyKey = attributeName.substring("property_".length());
									exchange.setProperty(propertyKey, attribute.getValue());
								}
							}
						}
					}
				} else {
					throw new IllegalStateException("An attempt call blacklisted script:" + script.getRefName()
							+ " from route:" + exchange.getFromRouteId() + " was made.");
				}

			} catch (ScriptException se) {

				String msg = "### >>>> !!!! There was a problem executing script:" + script.getRefName()
						+ " from route:" + exchange.getFromRouteId() + " problem was:" + se.getMessage();
				if (LOG.isErrorEnabled()) {
					LOG.error(msg);
				}

				script.setBlacklisted(true);
				script.setBlacklistReason(msg);
				exceptionOccurred = true;
				throw se;
			} finally {
				if (exceptionOccurred) {
					try {
						scriptDAO.update(script);

					} catch (Exception e) {
						if (LOG.isErrorEnabled()) {
							LOG.error("There was a problem saving the script:" + script.getRefName() + " exception:"
									+ e.getMessage());
						}
					}
				}
			}
		} else {
			throw new Exception("Script " + script + "was not found.");
		}
	}

	@Override
	public PreProcessorScriptEndpoint getEndpoint() {
		return (PreProcessorScriptEndpoint) super.getEndpoint();
	}
}
