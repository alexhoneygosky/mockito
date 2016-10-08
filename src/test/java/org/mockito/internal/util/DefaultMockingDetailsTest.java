package org.mockito.internal.util;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.invocation.Stubbing;
import org.mockitousage.IMethods;

import java.util.Collection;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DefaultMockingDetailsTest {

    @Mock private Foo foo;
    @Mock private Bar bar;
    @Mock private IMethods mock;
    @Spy private Gork gork;

    @Before public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_check_that_a_mock_is_indeed_a_mock() throws Exception {
        assertEquals(true, mockingDetails(foo).isMock());
        assertEquals(true, mockingDetails(bar).isMock());
        assertEquals(true, mockingDetails(gork).isMock());
        assertEquals(false, mockingDetails("no a mock").isMock());
    }

    @Test
    public void should_check_that_a_spy_is_indeed_a_spy() throws Exception {
        assertEquals(true, mockingDetails(gork).isSpy());
        assertEquals(false, mockingDetails(foo).isSpy());
        assertEquals(false, mockingDetails(bar).isSpy());
        assertEquals(false, mockingDetails("not a spy").isSpy());
    }

    @Test
    public void should_check_that_a_spy_is_also_a_mock() throws Exception {
        assertEquals(true, mockingDetails(gork).isMock());
    }

    @Test
    public void provides_invocations() {
        //when
        mock.simpleMethod(10);
        mock.otherMethod();

        //then
        assertEquals(0, mockingDetails(foo).getInvocations().size());
        assertEquals("[mock.simpleMethod(10);, mock.otherMethod();]", mockingDetails(mock).getInvocations().toString());
    }

    @Test
    public void manipulating_invocations_is_safe() {
        mock.simpleMethod();

        //when we manipulate the invocations
        mockingDetails(mock).getInvocations().clear();

        //then we didn't actually changed the invocations
        assertEquals(1, mockingDetails(mock).getInvocations().size());
    }

    @Test
    public void provides_mock_creation_settings() {
        //smoke test some creation settings
        assertEquals(Foo.class, mockingDetails(foo).getMockCreationSettings().getTypeToMock());
        assertEquals(Bar.class, mockingDetails(bar).getMockCreationSettings().getTypeToMock());
        assertEquals(0, mockingDetails(mock).getMockCreationSettings().getExtraInterfaces().size());
    }

    @Test(expected = NotAMockException.class)
    public void fails_when_getting_creation_settings_for_incorrect_input() {
        mockingDetails(null).getMockCreationSettings();
    }

    @Test
    public void fails_when_getting_invocations_when_null() {
        try {
            //when
            mockingDetails(null).getInvocations();
            //then
            fail();
        } catch (NotAMockException e) {
            TestCase.assertEquals("Argument passed to Mockito.mockingDetails() should be a mock, but is null!", e.getMessage());
        }
    }

    @Test
    public void fails_when_getting_invocations_when_not_mock() {
        try {
            //when
            mockingDetails(new Object()).getInvocations();
            //then
            fail();
        } catch (NotAMockException e) {
            TestCase.assertEquals("Argument passed to Mockito.mockingDetails() should be a mock, but is an instance of class java.lang.Object!", e.getMessage());
        }
    }

    @Test
    public void fails_when_getting_stubbings_from_non_mock() {
        try {
            //when
            mockingDetails(new Object()).getStubbings();
            //then
            fail();
        } catch (NotAMockException e) {
            TestCase.assertEquals("Argument passed to Mockito.mockingDetails() should be a mock, but is an instance of class java.lang.Object!", e.getMessage());
        }
    }

    @Test
    public void mock_with_no_stubbings() {
        assertTrue(mockingDetails(mock).getStubbings().isEmpty());
    }

    @Test
    public void provides_stubbings_of_mock() {
        when(mock.simpleMethod(1)).thenReturn("1");
        when(mock.otherMethod()).thenReturn("2");

        //when
        Collection<Stubbing> stubbings = mockingDetails(mock).getStubbings();

        //then
        assertEquals(2, stubbings.size());
        assertEquals("[mock.simpleMethod(1); stubbed with: [Returns: 1], mock.otherMethod(); stubbed with: [Returns: 2]]", stubbings.toString());
    }

    @Test
    public void manipulating_stubbings_explicitly_is_safe() {
        when(mock.simpleMethod(1)).thenReturn("1");

        //when somebody manipulates stubbings directly
        mockingDetails(mock).getStubbings().clear();

        //then it does not affect stubbings of the mock
        assertEquals(1, mockingDetails(mock).getStubbings().size());
    }

    public class Foo { }
    public interface Bar { }
    public static class Gork { }
}
