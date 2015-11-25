package com.agoda.controller;

import com.agoda.RateLimit;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Andrey Kapitonov on 11/26/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RateLimit.class)
public class TestHotelController {
}
