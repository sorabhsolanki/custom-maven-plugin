package com.pract.sample.java.callable;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

public class CommonFileCallable implements Callable<Long>{

	private Long counter = 0L;
	private File prjctFile;


	public CommonFileCallable(File prjctFile){
		this.prjctFile = prjctFile;
	}

	public Long call() throws Exception {
		countOfLOC(prjctFile);
		return counter;
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
}
