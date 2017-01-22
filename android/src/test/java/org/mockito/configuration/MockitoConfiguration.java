package org.mockito.configuration;

/**
 * Created by reweber on 22/01/2017
 */
public class MockitoConfiguration extends DefaultMockitoConfiguration {

    @Override
    public boolean enableClassCache() {
        return false;
    }
}
