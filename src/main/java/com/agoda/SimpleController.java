package com.agoda;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Andrey Kapitonov on 25.11.2015.
 */
@RestController
public class SimpleController {
    @RequestMapping
    public String index() {
        return "Hello booty";
    }
}
