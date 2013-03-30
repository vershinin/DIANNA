package org.dianna.tests.common;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSelf implements Answer<Object> {
	private final Answer<Object> delegate = new ReturnsEmptyValues();

	public Object answer(InvocationOnMock invocation) throws Throwable {
		Class<?> returnType = invocation.getMethod().getReturnType();
		Class<?> mockClass = invocation.getMock().getClass();
		if (returnType == mockClass) {
			return invocation.getMock();
		} else {
			return delegate.answer(invocation);
		}
	}
}