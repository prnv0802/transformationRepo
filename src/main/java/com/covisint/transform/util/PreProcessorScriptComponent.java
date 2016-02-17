package com.covisint.transform.util;


import org.apache.camel.Endpoint;
import org.apache.camel.ResolveEndpointFailedException;
import org.apache.camel.impl.DefaultComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.covisint.transform.dao.CorrelationDAO;
import com.covisint.transform.dao.ScriptDAO;
import com.covisint.transform.model.Script;

import java.util.Map;

/**
 * Camel component for Script
 *
 * @author PJ00452307 - Pranav Jha
 */
public class PreProcessorScriptComponent extends DefaultComponent {

    private static final Logger LOG = LoggerFactory.getLogger(PreProcessorScriptComponent.class);

    /**
     * ScriptDAO - injected by camel
     */
    @Autowired
    protected ScriptDAO scriptDAO;

    /**
     * CorrelationDAO - injected by camel
     */
    @Autowired
    protected CorrelationDAO correlationDAO;

    @Override
    protected void validateURI(String uri, String path, Map parameters)
            throws ResolveEndpointFailedException {
        super.validateURI(uri, path, parameters);
        if (!path.contains("/")) {
            throw new ResolveEndpointFailedException("Realm and script name must be in the uri");
        }
        String realm = path.substring(0, path.indexOf('/'));
        String scriptName = path.substring(path.indexOf('/')+1);

        try {
            Script script = scriptDAO.getByRefName(scriptName, realm);
            if (script == null) {
                throw new ResolveEndpointFailedException("Unable to resolve script with realm " +
                        realm + " and script name " + scriptName);
            }
        } catch (Exception b2BTransactionFailed) {
            throw new ResolveEndpointFailedException("Unable to resolve script with data domain " +
                    realm + " and script name " + scriptName);
        }
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating ScriptEndpoint with uri :: " + uri);
        }
        PreProcessorScriptEndpoint endpoint = new PreProcessorScriptEndpoint(uri, this);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting parameters in the properties Parameter Map :: " + parameters);
        }
        //instead of using setProperties, Script endpoint gets the list of parameters to pull from for inputParameters
        endpoint.setParameters(parameters);

        endpoint.setScriptDAO(scriptDAO);
        endpoint.setCorrelationDAO(correlationDAO);
        endpoint.setRealm(remaining.substring(0, remaining.indexOf('/')));
        endpoint.setScriptName(remaining.substring(remaining.indexOf('/')+1));
        return endpoint;
    }
}
