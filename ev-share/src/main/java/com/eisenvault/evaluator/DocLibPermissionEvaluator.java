package com.eisenvault.evaluator;

import org.alfresco.web.evaluator.BaseEvaluator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.extensions.surf.RequestContext;
import org.springframework.extensions.surf.ServletUtil;
import org.springframework.extensions.surf.exception.ConnectorServiceException;
import org.springframework.extensions.surf.support.ThreadLocalRequestContext;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.connector.Connector;
import org.springframework.extensions.webscripts.connector.Response;



public class DocLibPermissionEvaluator extends BaseEvaluator {

	private static Log logger = LogFactory.getLog(DocLibPermissionEvaluator.class);

	@Override
	public boolean evaluate(JSONObject arg0) {
		boolean result = false;

		final RequestContext rc = ThreadLocalRequestContext.getRequestContext();
		final String userId = rc.getUserId();
		try {
			final JSONObject node = (JSONObject) arg0.get("node");
			final String nodeRef = (String) node.get("nodeRef");
			final Connector conn = rc.getServiceRegistry().getConnectorService().getConnector("alfresco", userId, ServletUtil.getSession());
			final Response response = conn.call("/ev/hasPermissions?nodeRef=" + nodeRef.replace("://", "/"));
			if (response.getStatus().getCode() == Status.STATUS_OK) {
				try {
					org.json.JSONObject json = new org.json.JSONObject(response.getResponse());
					result = Boolean.parseBoolean(((String) json.get("result")));
				} catch (JSONException je) {
					je.printStackTrace();
					return false;
				}
			} else {
				return false;
			}
		} catch (ConnectorServiceException cse) {
			cse.printStackTrace();
			return false;
		}
		return result;
	}

}
