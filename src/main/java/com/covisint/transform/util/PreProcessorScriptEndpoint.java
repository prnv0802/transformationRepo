package com.covisint.transform.util;

import java.util.Map;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultPollingEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.covisint.transform.dao.CorrelationDAO;
import com.covisint.transform.dao.ScriptDAO;

/**
 * Camel endpoint for Script
 * @author aaldredge
 */
public class PreProcessorScriptEndpoint extends DefaultPollingEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(PreProcessorScriptEndpoint.class);
    private String scriptName;
    private String realm;
    private ScriptDAO scriptDAO;
    private CorrelationDAO correlationDAO;

    private Map<String, Object> parameters;

    /**
     * Contructor
     * @param endpointUri endpoint uri
     * @param component component
     */
    public PreProcessorScriptEndpoint(String endpointUri, PreProcessorScriptComponent component) {
        super(endpointUri, component);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        PreProcessorScriptConsumer consumer = new PreProcessorScriptConsumer(this, processor);

        // ScheduledPollConsumer default delay is 500 millis, override with a new default value.
        // End user can override this value by providing a consumer.delay parameter
        consumer.setDelay(consumer.DEFAULT_CONSUMER_DELAY);
        configureConsumer(consumer);
        return consumer;
    }

    @Override
    public Producer createProducer() throws Exception {
        return new PreProcessorScriptProducer(this);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public boolean isLenientProperties() {
        return true;
    }

    /**
	 * @return the realm
	 */
	public String getRealm() {
		return realm;
	}

	/**
	 * @param realm the realm to set
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}

	/**
     * Get the script name
     * @return script ref name
     */
    public String getScriptName() {
        return scriptName;
    }

    /**
     * Set the script name
     * @param scriptName script ref name
     */
    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    /**
     * Get the scriptDAO
     * @return ScriptDAO
     */
    public ScriptDAO getScriptDAO() {
        return scriptDAO;
    }

    /**
     * Set the scriptDAO
     * @param scriptDAO scriptDAO to set
     */
    public void setScriptDAO(ScriptDAO scriptDAO) {
        this.scriptDAO = scriptDAO;
    }

    /**
     * get the correlationDAO
     * @return correlationDAO
     */
    public CorrelationDAO getCorrelationDAO() {
        return correlationDAO;
    }

    /**
     * Set the correlationDAO
     * @param correlationDAO correlationDAO to set
     */
    public void setCorrelationDAO(CorrelationDAO correlationDAO) {
        this.correlationDAO = correlationDAO;
    }

    /**
     * Get the parameters from the endpoint.  These are used to populate matching script input parameters
     * @return parameters
     */
    public Map<String, Object> getParameters(){
        return parameters;
    }

    /**
     * Set the parameters from the endpoint.  These are used to populate matching script input parameters
     * @param parameters parameters from endpoint
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

}
