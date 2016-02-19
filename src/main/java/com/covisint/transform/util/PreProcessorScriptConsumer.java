package com.covisint.transform.util;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.covisint.transform.dao.CorrelationDAO;
import com.covisint.transform.dao.ScriptDAO;
import com.covisint.transform.model.Correlation;
import com.covisint.transform.model.DynamicAttribute;
import com.covisint.transform.model.Script;

/**
 * Consumer for scripts - kicks off a script to run and populate exchange.
 * Batching is not supported; a script can populate at most one exchange per run.
 * No success or failure callback is called on route completion.
 *
 * @author PJ00452307 - Pranav Jha
 */
public class PreProcessorScriptConsumer extends ScheduledPollConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(PreProcessorScriptConsumer.class);

    //package visibility, default delay if none set
    static final long DEFAULT_CONSUMER_DELAY = 60 * 1000L;


    /**
     * @param endpoint  endpoint object
     * @param processor processor
     */
    public PreProcessorScriptConsumer(PreProcessorScriptEndpoint endpoint, Processor processor) {

        super(endpoint, processor);
    }

    @Override
    protected int poll() throws Exception {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling  PreProcessorScriptConsumer   poll() method ");
        }
        ScriptDAO scriptDAO = getEndpoint().getScriptDAO();
        if (scriptDAO == null) {
            throw new IllegalArgumentException("scriptDAO is not being initialized  ");
        }
        CorrelationDAO correlationDAO = getEndpoint().getCorrelationDAO();
        if (correlationDAO == null) {
            throw new IllegalArgumentException("correlationDAO is not being initialized  ");
        }

        // code to check if UserProfile is associated with the endpoint url



        Script script = scriptDAO.getByRefName(getEndpoint().getScriptName(), getEndpoint().getRealm());
        if (script != null) {
            boolean populatedExchange = false;
            Exchange exchange = getEndpoint().createExchange();

            boolean exceptionOccurred = false;
            try {
                if (!script.isBlacklisted()) {
                    //I guess should do this?
                    List<String> blackListedPackages = new ArrayList<String>();
                    Correlation correlation = correlationDAO.getByRefName("BlackListScriptPackages",
                            Constants.FORD_REALM);
                    if (correlation != null) {
                        blackListedPackages = (List<String>) correlation.getValue();
                    }
                    //set inputs
                    if (script.getType() != null && script.getType().getInputs() != null
                            && script.getType().getInputs().getAttributes() != null) {
                        for (DynamicAttribute attribute : script.getType().getInputs().getAttributes().values()) {
                            String attributeName = attribute.getRefName();
                            boolean required = attribute.isRequired();

                            if (attributeName != null) {
                                if (getEndpoint().getParameters() != null) {
                                    if (getEndpoint().getParameters().get(attributeName) != null) {
                                        attribute.setValue(getEndpoint().getParameters().get(attributeName));
                                    }
                                    else if (required) {
                                        throw new IllegalStateException("Variable:"
                                                + attributeName
                                                + " is required by Script:"
                                                + script.getRefName()
                                                + " but variable was not found in route context");
                                    }
                                }
                            }
                        }
                    }

                    boolean useRunAs = false;
                    
                    if (script.getRunAsId() != null) { // check for userProfile as well. Ommiting for Demo purposes
                        useRunAs = true;
                    }

                    Script returnScript = null;
                    try {
                        if (useRunAs) {

                            if (LOG.isInfoEnabled()) {
                                LOG.info("Running script under with RunAs with Id:" + script.getRunAsId());
                            }

                            // In case of Multi Tenancy bind script to ThreadState
                            // add Session variables etc
                        }
                        else {


                            if (LOG.isWarnEnabled()) {
                                LOG.warn(">> Running script with no associated userProfile, service calls will fail");

                            }


                        }

                        JSRunner runner = new JSRunner();
                        returnScript = runner.runJS(script, blackListedPackages);

                    } finally {


                        if (useRunAs) {
                          // log out Subject and clear Session
                        }

                      // restore thread state

                    }


                    //set outputs
                    if (returnScript.getType() != null && returnScript.getType().getOutputs() != null
                            && returnScript.getType().getOutputs().getAttributes() != null) {

                        for (DynamicAttribute attribute : returnScript.getType().getOutputs().getAttributes().values
                                ()) {

                            String attributeName = attribute.getRefName();

                            if (attributeName != null) {

                                if ("body".equals(attributeName)) {
                                    exchange.getIn().setBody(attribute.getValue());
                                    populatedExchange = true;
                                }
                                else if (attributeName.startsWith("header_")) {
                                    String headerKey = attributeName.substring("header_".length());
                                    exchange.getIn().setHeader(headerKey, attribute.getValue());
                                    populatedExchange = true;
                                }
                                else if (attributeName.startsWith("property_")) {
                                    String propertyKey = attributeName.substring("property_".length());
                                    exchange.setProperty(propertyKey, attribute.getValue());
                                    populatedExchange = true;
                                }
                                else {
                                    if (LOG.isWarnEnabled()) {
                                        LOG.warn(">>> ### Could not find attributeName:" + attributeName
                                                + "attribute does not the body, or start with header_, or property_");
                                    }

                                    if (attribute.isRequired()) {
                                        throw new IllegalStateException("Required attribute:" + attributeName +
                                                " does start header_, property_, or named body check attribute naming");
                                    }
                                }
                            }
                        }
                    }

                    if (populatedExchange) {
                        //TODO: look at adding success and failure callbacks as endpoint parameters; can use consumer.<>
                        getProcessor().process(exchange);
                        return 1;
                    }
                    else {
                        return 0;
                    }

                }
                else {
                    throw new IllegalStateException("An attempt call blacklisted script:" + script.getRefName() +
                            " from route:" + exchange.getFromRouteId() + " was made.");
                }

            } catch (ScriptException se) {
                String msg = "There was a problem executing script:" + script.getRefName() + " from route:" +
                        exchange.getFromRouteId() +
                        " problem was:" + se.getMessage();
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
                            LOG.error("There was a problem saving the script:" + script.getRefName() + " exception:" +
                                    e.getMessage());
                        }
                    } 
                }
            }
        }
        else {
            throw new Exception("Script " + script + "was not found.");
        }
    }

    @Override
    public PreProcessorScriptEndpoint getEndpoint() {

        return (PreProcessorScriptEndpoint) super.getEndpoint();
    }

    @Override
    public String toString() {

        return "PreProcessorScriptConsumer";
    }
}

