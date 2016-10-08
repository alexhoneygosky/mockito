/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.invocation.Stubbing;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.MockitoHint;

import java.util.Collection;

/**
 * Provides mocking information.
 * For example, you can identify whether a particular object is either a mock or a spy.
 *
 * @since 1.9.5
 */
public interface MockingDetails {
    
    /**
     * Informs if the object is a mock. isMock() for null input returns false.
     * @return true if the object is a mock or a spy.
     *
     * @since 1.9.5
     */
    boolean isMock();

    /**
     * Informs if the object is a spy. isSpy() for null input returns false.
     * @return true if the object is a spy.
     *
     * @since 1.9.5
     */
    boolean isSpy();
    
    /**
     * All method invocations on this mock.
     * Can be empty - it means there were no interactions with the mock.
     * <p>
     * This method is useful for framework integrators and for certain edge cases.
     * <p>
     * Manipulating the collection (e.g. by removing, adding elements) is safe and has no effect on the mock.
     * <p>
     * Throws meaningful exception when object wrapped by MockingDetails is not a mock.
     *
     * @since 1.10.0
     */
    Collection<Invocation> getInvocations();

    /**
     * Returns various mock settings provided when the mock was created, for example:
     *  mocked class, mock name (if any), any extra interfaces (if any), etc.
     * See also {@link MockCreationSettings}.
     * <p>
     * This method is useful for framework integrators and for certain edge cases.
     * <p>
     * If <code>null</code> or non-mock was passed to {@link Mockito#mockingDetails(Object)}
     * then this method will throw with an appropriate exception.
     * After all, non-mock objects do not have any mock creation settings.
     * @since 2.1.0
     */
    MockCreationSettings<?> getMockCreationSettings();

    /**
     * Returns stubbings declared on this mock object.
     * <p>
     * What is 'stubbing'?
     * Stubbing is your when(x).then(y) declaration, e.g. configuring the mock to behave in a specific way,
     * when specific method with specific arguments is invoked on a mock.
     * Typically stubbing is configuring mock to return X when method Y is invoked.
     * <p>
     * Why do you need to access stubbings of a mock?
     * In a normal workflow of creation clean tests, there is no need for this API.
     * However, it is useful for advanced users, edge cases or framework integrators.
     * For example, Mockito internally uses this API to report and detect unused stubbings
     * that should be removed from test. Unused stubbings are dead code that needs to be removed
     * (see {@link MockitoHint}).
     * <p>
     * Manipulating the collection (e.g. by removing, adding elements) is safe and has no effect on the mock.
     * <p>
     * This method throws meaningful exception when object wrapped by MockingDetails is not a mock.
     *
     * @since 2.2.0
     */
    Collection<Stubbing> getStubbings();
}
