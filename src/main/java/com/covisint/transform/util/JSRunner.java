package com.covisint.transform.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;
import org.mozilla.javascript.WrappedException;
import org.mozilla.javascript.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.covisint.transform.model.Script;
import com.covisint.transform.model.ScriptSecurityPolicy;

/**
 * Class used to run javascript using Rhino.
 * <p>
 * Date: 12/13/13 Time: 4:29 PM To change this template use File | Settings |
 * File Templates.
 */
public class JSRunner {
	private static final Logger LOG = LoggerFactory.getLogger(JSRunner.class);

	/**
	 * Constructor.
	 */
	public JSRunner() {
		if (!ContextFactory.hasExplicitGlobal()) {
			ContextFactory.initGlobal(new CovisintContextFactory());
		}
	}

	/**
	 * Runs javascript and returns a result.
	 *
	 * @param script
	 *            - Script object containing script and inputs
	 * @param blPackages
	 *            - List<String> of packages that are not allowed by the script
	 *            runner
	 * @return Script containing the results
	 * @throws javax.script.ScriptException
	 *             - if script execution fails
	 */
	public Script runJS(Script script, List<String> blPackages) throws ScriptException {

		Context cx = ContextFactory.getGlobal().enterContext();
		cx.getWrapFactory().setJavaPrimitiveWrap(false);
		Scriptable scope = cx.initStandardObjects();
		Script returnScript = null;
		try {
			cx.setClassShutter(new CovisintClassShutter(script.getScriptSecurityPolicy(), blPackages));
		} catch (SecurityException e) {
			// will get this security exception when a script calls a script.
			// Ignore exception, stick with original shutter
			if (LOG.isDebugEnabled()) {
				LOG.debug("Received security exception when setting shutter - "
						+ "not the only one on this thread using script", e);
			}
		}
		try {
			// if (checkPermissions(script.getScriptSecurityPolicy())) {
			prepareContext(script, scope);
			convertInputsToScope(scope, script.getInputs());
			Object obj = cx.evaluateString(scope, script.getScript(), "script", 1, null);
			returnScript = convertResult(script, scope, obj);

		} catch (EcmaError e) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Caught EmcaError.  Typically this points to a problem in the script. " + e.details(), e);
			}
			throw new ScriptException(e.getMessage());
		} catch (WrappedException e) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Caught WrappedException.  Typically this points to a problem encountered executing "
						+ "code from the script. " + e.getMessage() + " " + e.getScriptStackTrace(), e);
			}
			if (e.getWrappedException() instanceof RuntimeException) {
				throw (RuntimeException) e.getWrappedException();
			} else {
				// don't want script b/c script exceptions count toward
				// blacklisting...this is a runtime or data problem
				throw new RuntimeException(e.getWrappedException().getMessage(), e);
			}

		} finally {
			cx.exit();
		}
		return returnScript;
	}

	/**
	 * Runs javascript and returns a result.
	 *
	 * @param script
	 *            - Script object containing script and inputs
	 * @param blPackages
	 *            - List<String> of packages that are not allowed by the script
	 *            runner
	 * @return Script containing the results
	 * @throws javax.script.ScriptException
	 *             - if script execution fails
	 * @throws com.eis.core.api.v1.exception.B2BNotAuthenticatedException
	 *             - if the user not authenticated
	 * @throws com.eis.core.api.v1.exception.B2BNotAuthorizedException
	 *             - if the user is not authorized
	 * @throws com.eis.core.api.v1.exception.B2BTransactionFailed
	 *             - if there is a problem running the script
	 * @throws com.eis.core.api.v1.exception.B2BNotFoundException
	 *             - if data objects are not found
	 */
	public Script testJS(Script script, List<String> blPackages) throws ScriptException {
		Context cx = ContextFactory.getGlobal().enterContext();
		cx.getWrapFactory().setJavaPrimitiveWrap(false);
		Scriptable scope = cx.initStandardObjects();
		Script returnScript = null;
		try {
			cx.setClassShutter(new CovisintClassShutter(script.getScriptSecurityPolicy(), blPackages));
		} catch (SecurityException e) {
			// will get this security exception when a script calls a script.
			// Ignore exception, stick with original shutter
			if (LOG.isDebugEnabled()) {
				LOG.debug("Received security exception when setting shutter - "
						+ "not the only one on this thread using script", e);
			}
		}
		try {
			// if (checkPermissions(script.getScriptSecurityPolicy())) {
			prepareContext(script, scope);
			Object obj = cx.evaluateString(scope, script.getScript(), "script", 1, null);
			returnScript = convertResult(script, scope, obj);
			// } else {
			// throw new ScriptException("There is an error with your script,
			// user does not have required permission" +
			// " to run this script");
			// }

		} finally {
			cx.exit();
		}
		return returnScript;
	}

	private void prepareContext(Script script, Scriptable scope) throws ScriptException {
		// this would prepare the context based on contextVars and
		// DynamicAttributes if we want to add that capability

	}

	private boolean checkPermissions(ScriptSecurityPolicy scriptSecurityPolicy) {
		// this would check for permissions whether the user running the script
		// is authorized or not.
		return true;

	}

	/**
	 * Converts the result of the executed script.
	 *
	 * @param script
	 *            - the javascript
	 * @param scope
	 *            - scope to use
	 * @param result
	 *            - the rhino result
	 * @return Script object with the result
	 */
	private Script convertResult(Script script, Scriptable scope, Object result) {

		// *** Have some questions about how things are returned.
		if (result instanceof Wrapper) {
			result = ((Wrapper) result).unwrap();
			script.setResult(result);
		} else if (result instanceof NativeObject) {
			script.setResult(objectToMap((NativeObject) result));
		} else if (result instanceof NativeArray) {
			script.setResult(objectToList((NativeArray) result));
		} else {
			script.setResult(result);
		}


		Object builder = scope.get("builder", scope);
		if (builder != null) {
			if (builder instanceof Wrapper) {
				builder = ((Wrapper) builder).unwrap();
				if (builder instanceof StringBuilder) {
					StringBuilder tracer = (StringBuilder) builder;
					script.setScriptTracer(builder.toString());
				}
			}
		}

		return script;

	}

	/**
	 * Adds the object passed in to the JS Rhino scope under the name passed in.
	 *
	 * @param scope
	 *            - scope to populate
	 * @param name
	 *            - name to reference in the context
	 * @param object
	 *            - the object
	 */
	protected void addToContext(Scriptable scope, String name, Object object) {
		ScriptableObject.putProperty(scope, name, object);
	}

	/**
	 * Adds the script inputs defined as DynamicAttirbutesSet and puts them in
	 * the scope to be run by the script.
	 *
	 * @param scope
	 *            - scope to populate
	 * @param inputs
	 *            - DynamicAttributeSet that contains the input variable.
	 */
	private void convertInputsToScope(Scriptable scope, LinkedHashMap<String, Object> inputs) {

		for (Map.Entry<String, Object> entry : inputs.entrySet()) {
			ScriptableObject.putProperty(scope, entry.getKey(), entry.getValue());
		}
	}

	private Map<String, Object> objectToMap(NativeObject obj) {

		HashMap<String, Object> map = new HashMap<>();

		for (Object id : obj.getIds()) {
			String key;
			Object value;
			if (id instanceof String) {
				key = (String) id;
				value = obj.get(key, obj);
				if (value instanceof NativeArray) {
					List<Object> list = objectToList((NativeArray) value);
					map.put(key, list);
				} else {
					map.put(key, value);
				}

			} else if (id instanceof Integer) {
				key = id.toString();
				value = obj.get(((Integer) id).intValue(), obj);
				if (value instanceof NativeArray) {
					List<Object> list = objectToList((NativeArray) value);
					map.put(key, list);
				} else {
					map.put(key, value);
				}
			} else {
				throw new IllegalArgumentException();
			}
		}

		return map;
	}

	private List<Object> objectToList(NativeArray arr) {

		List<Object> list = new ArrayList<Object>();

		Object[] array = new Object[(int) arr.getLength()];
		for (Object o : arr.getIds()) {
			int index = (Integer) o;
			Object result = arr.get(index, null);
			if (result instanceof Wrapper) {
				result = ((Wrapper) result).unwrap();
			}

			list.add(result);
		}

		return list;
	}

	class CovisintClassShutter implements ClassShutter {
		private ScriptSecurityPolicy policy;
		private List<String> blackListedClasses;

		public CovisintClassShutter(ScriptSecurityPolicy policy, List<String> blackListedClasses) {
			this.policy = policy;
			this.blackListedClasses = blackListedClasses;
		}

		public boolean visibleToScripts(String className) {
			for (String packageName : policy.getAllowedPackages().values()) {
				// TODO: Allow everything for now.
				blackListedClasses = new ArrayList<String>();
				if (className.startsWith(packageName) && !blackListedClasses.contains(className)) {

					return true;
				}
			}
			return false;
		}
	}

	class CovisintNativeJavaObject extends NativeJavaObject {
		public CovisintNativeJavaObject(Scriptable scpe, Object javaObject, Class staticType) {
			super(scpe, javaObject, staticType);
		}

		@Override
		public Object get(String name, Scriptable start) {
			if (name.equals("getClass")) {
				return NOT_FOUND;
			}

			return super.get(name, start);
		}
	}

	class CovisintWrapFactory extends WrapFactory {
		@Override
		public Scriptable wrapAsJavaObject(Context context, Scriptable scpe, Object javaObject, Class staticType) {
			return new CovisintNativeJavaObject(scpe, javaObject, staticType);
		}
	}

	class CovisintContextFactory extends ContextFactory {

		@Override
		protected Context makeContext() {
			Context context = super.makeContext();
			context.setWrapFactory(new CovisintWrapFactory());
			return context;
		}

		@Override
		protected void onContextReleased(Context cx) {
			super.onContextReleased(cx);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Context released");
			}

		}
	}

}
