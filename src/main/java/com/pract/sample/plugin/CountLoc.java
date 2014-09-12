package com.pract.sample.plugin;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.pract.plugin.util.InvalidProjectException;
import com.pract.sample.plugin.exector.service.JavaExecutorService;
import com.pract.sample.plugin.exector.service.XmlExecutorService;

@Mojo(name = "loc", defaultPhase = LifecyclePhase.COMPILE)
public class CountLoc extends AbstractMojo {

	private static final String MANDATORY = "mendatory";
	private static final String SIMPLE = "linear";
	private static final String CONCURRENT = "concurrent";

	@Parameter(property = "project.dir", defaultValue = MANDATORY)
	private String projectDir;

	@Parameter(property = "project.mode", defaultValue = SIMPLE)
	private String projectMode;


	private String patternString = ".*.java";
	private Pattern pattern = Pattern.compile(patternString);

	private String patternStringForJava = ".*.java";
	private Pattern javaPattern = Pattern.compile(patternStringForJava);

	private String patternStringForXml = ".*.xml";
	private Pattern xmlPattern = Pattern.compile(patternStringForXml);



	private long counter;

	public void execute() throws MojoExecutionException, MojoFailureException {

		try {
			doValidate();
		} catch (InvalidProjectException e) {
			e.printStackTrace();
		}

		// now start the main logic
		try {
			generateReport();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doValidate() throws InvalidProjectException {
		if (projectDir.equals(MANDATORY))
			throw new InvalidProjectException("Not a Valid Project!");

		File file = new File(projectDir);
		if (!file.isDirectory())
			throw new InvalidProjectException("Not a Valid Project!");
	}

	private void generateReport() throws IOException {
		File file = new File(projectDir);

		if (projectMode.equals(CONCURRENT)){
			getLog().info("Starting in : " + CONCURRENT + " mode");
			submitTaskToExecutorAccordingToTheFileExtension(file.listFiles());
			displayAllDetailsRegardingLoc();
		}else{
			getLog().info("Starting in : " + SIMPLE + " mode");
		    recuriveWay(file.listFiles());
		    getLog().info("LOC : " + counter);
		}

	}

	private void recuriveWay(File[] files) throws IOException {
		for (File prjctFile : files) {
			if (prjctFile.isFile()) {
				Matcher matcher = pattern.matcher(prjctFile.getName());
				if (matcher.matches())
					countOfLOC(prjctFile);
			}else if(prjctFile.isDirectory())
				recuriveWay(prjctFile.listFiles());
		}
	}

	private void submitTaskToExecutorAccordingToTheFileExtension(File[] files){
		for (File prjctFile : files) {
			if (prjctFile.isFile()) {
				if (javaPattern.matcher(prjctFile.getName()).matches())
					JavaExecutorService.submitNewTask(prjctFile);
				else if(xmlPattern.matcher(prjctFile.getName()).matches())
					XmlExecutorService.submitNewTask(prjctFile);
			}else if(prjctFile.isDirectory())
				submitTaskToExecutorAccordingToTheFileExtension(prjctFile.listFiles());
		}
	}

	private void countOfLOC(File prjctFile) throws IOException {

		// opening a file channel
		// RandomAccessFile file = new RandomAccessFile(prjctFile, "r");
		FileInputStream file = new FileInputStream(prjctFile);
		FileChannel fileChannel = file.getChannel();

		// reading data from a file channel
		ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
		fileChannel.read(buffer);
		buffer.flip();
		CharBuffer charBuffer = Charset.defaultCharset().decode(buffer);
		BufferedReader br = new BufferedReader(new CharArrayReader(
				charBuffer.array()));
		while (br.readLine() != null) {
			counter++;
		}
		br.close();
		fileChannel.close();
		file.close();
	}

	private void displayAllDetailsRegardingLoc(){
		 getLog().info("Java LOC : " + JavaExecutorService.returnLoc());
		 getLog().info("Xml LOC : " + XmlExecutorService.returnLoc());
	}

}
