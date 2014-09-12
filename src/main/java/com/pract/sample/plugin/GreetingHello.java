package com.pract.sample.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "sayhi",defaultPhase = LifecyclePhase.COMPILE)
public class GreetingHello extends AbstractMojo{

	@Parameter(property="greeting.message",defaultValue="default hello!")
	String greetingMessage;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info(greetingMessage);
	}

}
