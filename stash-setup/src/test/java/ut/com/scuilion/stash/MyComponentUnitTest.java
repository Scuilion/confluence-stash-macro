package ut.com.scuilion.stash;

import org.junit.Test;
import com.scuilion.stash.api.MyPluginComponent;
import com.scuilion.stash.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}