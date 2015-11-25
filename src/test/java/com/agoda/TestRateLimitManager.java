package com.agoda;

import com.agoda.exception.InvalidApiKeyException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * Created by Andrey Kapitonov on 11/26/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RateLimit.class)
public class TestRateLimitManager {

    @Value("${rate.limit.delay}")
    private long minimumDelay;

    @Value("${rate.limit.postpone.delay}")
    private long postponeDelay;

    @Autowired
    private RateLimitManager rateLimitManager;

    private String sessionId;
    private String apiKey;

    @Before
    public void setUp() throws Exception {
        sessionId = "sssddd";
        apiKey = "wow_such_api_key_" + minimumDelay + "_much_security_oO";
    }

    @Test
    public void testUpdateClientStateWithProperDelay() throws Exception {

        //add new client
        rateLimitManager.updateClientState(sessionId, apiKey);
        RateLimitManager.ClientState clientState;
        for (int i = 0; i < 5; i++) {
            //wait proper time
            Thread.sleep(minimumDelay + 100);
            //check if client is not postponed
            clientState = rateLimitManager.updateClientState(sessionId, apiKey);
            Assert.assertFalse(clientState.isPostponed());
        }
    }

    @Test
    public void testBecomePostponedAndBecomeNormalAgain() throws Exception {
        rateLimitManager.updateClientState(sessionId, apiKey);
        //become postponed
        RateLimitManager.ClientState clientState = rateLimitManager.updateClientState(sessionId, apiKey);
        Assert.assertTrue(clientState.isPostponed());
        //wait some time but less than postpone delay and check is still postponed
        Thread.sleep(postponeDelay - 200);
        clientState = rateLimitManager.updateClientState(sessionId, apiKey);
        Assert.assertTrue(clientState.isPostponed());
        //wait a bit more and check is postpone is over
        Thread.sleep(1000);
        clientState = rateLimitManager.updateClientState(sessionId, apiKey);
        Assert.assertFalse(clientState.isPostponed());
    }

    @Test(expected = InvalidApiKeyException.class)
    public void testBadApiKey() throws Exception {
        apiKey = "123";

        rateLimitManager.updateClientState(sessionId, apiKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadSessionId() throws Exception {
        rateLimitManager.updateClientState(null, apiKey);
    }
}
