package com.gabriel.orders.core.application;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("com.gabriel.orders.core.application")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.gabriel.orders.core.application")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "classpath:resources/features")
public class RunCucumberTest {
}
