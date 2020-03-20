import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


public class MockTest {
    @Test
    public void createMockObjectTest() {
        List<String> mock = mock(List.class);

        assertThat(mock.get(0), is(nullValue()));
        assertThat(mock.contains("Test"), is(false));
    }

    @Test
    public void defineStubTest() {
        List<String> stub = mock(List.class);
        when(stub.get(0)).thenReturn("Test");
        when(stub.contains("Test")).thenReturn(true);

        assertThat(stub.get(0), is("Test"));
        assertThat(stub.contains("Test"), is(true));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void stubThrowingExceptionTest() {
        List<String> stub = mock(List.class);
        when(stub.get(0)).thenThrow(new IndexOutOfBoundsException());

        stub.get(0);
    }

    @Test
    public void OtherStubThrowingExceptionTest() {
        List<String> stub = mock(List.class);
        when(stub.get(0)).thenThrow(new IndexOutOfBoundsException());

        try {
            stub.get(0);
            fail("No exception is raised");
        } catch (IndexOutOfBoundsException e) {
            assertThat(e.getMessage(), is(nullValue()));
        }
    }

    @Test(expected = RuntimeException.class)
    public void definitionStubReturnsVoid() {
        List<String> stub = mock(List.class);
        doThrow(new RuntimeException()).when(stub).clear();
        stub.clear();
    }

    @Test
    public void definingStubForArbitraryArguments() {
        List<String> stub = mock(List.class);
        when(stub.get(anyInt())).thenReturn("Test");

        assertThat(stub.get(0), is("Test"));
        assertThat(stub.get(1), is("Test"));
        assertThat(stub.get(3), is("Test"));
        assertThat(stub.get(6), is("Test"));
        assertThat(stub.get(999), is("Test"));
    }

    @Test
    public void createPartialMockTest() {
        List<String> list = new ArrayList();
        List<String> spy = spy(list);
        when(spy.size()).thenReturn(100);
        spy.add("Hello");

        assertThat(spy.get(0), is("Hello"));
        assertThat(spy.size(), is(100));
    }

    @Test
    public void createSpyTest() {
        List<String> list = new ArrayList();
        List<String> spy = spy(list);
        doReturn("Study").when(spy).get(1);
        spy.add("Hello");
        spy.add("World");

        // addメソッドの呼び出し回数を検証
        verify(spy).add("Hello");
        verify(spy).add("World");

        assertThat(spy.get(0), is("Hello"));
        // spy.getした結果がdoReturnで設定した値であること
        assertThat(spy.get(1), is("Study"));
    }
}
