package org.smart.framework.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static String getRealFileName(String fileName){
        return FilenameUtils.getName(fileName);
    }


    public static File createFile(String filePath){
        File file = null;
        file = new File(filePath);
        File parentDir = file.getParentFile();
        if(!parentDir.exists()){
            try {
                FileUtils.forceMkdir(parentDir);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Create file failure");
            }
        }

        return file;
    }
}
