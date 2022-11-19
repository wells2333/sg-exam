package com.github.tangyi.auth.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 *
 * @author tangyi
 * @date 2020/8/15 2:44 下午
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

	private final Map<String, String[]> params = new HashMap<>();

	public ParameterRequestWrapper(HttpServletRequest request) {
		super(request);
		this.params.putAll(request.getParameterMap());
	}

	public ParameterRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
		this(request);
		addAllParameters(extendParams);
	}

	@Override
	public String getParameter(String name) {
		String[] values = params.get(name);
		if (values == null || values.length == 0) {
			return null;
		}
		return values[0];
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return new Vector<String>(params.keySet()).elements();
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return params;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] result;

		Object v = params.get(name);
		if (v == null) {
			result = null;
		} else if (v instanceof String[]) {
			result = (String[]) v;
		} else if (v instanceof String) {
			result = new String[]{(String) v};
		} else {
			result = new String[]{v.toString()};
		}

		return result;
	}

	public void addAllParameters(Map<String, Object> otherParams) {
		for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
			addParameter(entry.getKey(), entry.getValue());
		}
	}

	public void addParameter(String name, Object value) {
		if (value != null) {
			if (value instanceof ArrayList) {
				String value1 = String.valueOf(value).substring(1);
				value = value1.substring(0, value1.length() - 1);
				params.put(name, new String[]{(String) value});
			} else if (value instanceof String[]) {
				params.put(name, (String[]) value);
			} else if (value instanceof String) {
				params.put(name, new String[]{(String) value});
			} else {
				params.put(name, new String[]{String.valueOf(value)});
			}
		}
	}
}
