package org.wadl.model.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class BasicPathResolver implements IPathResolver {
	
	private File root;

	public BasicPathResolver(File root) {
		this.root = root;
	}

	public String getContent(String path) {
		
		File file = new File(root, path);
		InputStream in;
		try {
			in = new FileInputStream(file);
			String content = IOUtils.toString(in);
			return content;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
