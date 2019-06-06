package com.eshore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MockPrintWriter extends PrintWriter {

	public MockPrintWriter(File file) throws FileNotFoundException {
		super(file);
		// TODO Auto-generated constructor stub
	}
	public MockPrintWriter() throws FileNotFoundException {
		super(System.out);
	}

}
