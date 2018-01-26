package org.smart.framework.helper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.bean.FileParam;
import org.smart.framework.bean.FormParam;
import org.smart.framework.bean.Param;
import org.smart.framework.util.CollectionUtil;
import org.smart.framework.util.FileUtil;
import org.smart.framework.util.StreamUtil;
import org.smart.framework.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UploadHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    private static ServletFileUpload servletFileUpload;

    public static void init(ServletContext servletContext) {
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if (uploadLimit != 0) {
            servletFileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);
        }
    }

    public static boolean isMultipart(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    public static Param createParam(HttpServletRequest request) {
        List<FormParam> formParams = new ArrayList<>();
        List<FileParam> fileParams = new ArrayList<>();

        try {
            Map<String, List<FileItem>> fileParamMap = servletFileUpload.parseParameterMap(request);
            if (CollectionUtil.isNotEmpty(fileParamMap)) {
                for (Map.Entry<String, List<FileItem>> fileEntry : fileParamMap.entrySet()) {
                    String fieldName = fileEntry.getKey();
                    List<FileItem> fileItems = fileEntry.getValue();
                    for (FileItem it : fileItems) {
                        if (it.isFormField()) {
                            String value = it.getString("UTF-8");
                            formParams.add(new FormParam(fieldName, value));
                        } else {
                            String fileName = FileUtil.getRealFileName(new String(it.getName().getBytes(), "UTF-8"));
                            if (StringUtil.isNotEmpty(fileName)) {
                                long fileSize = it.getSize();
                                String contextType = it.getContentType();
                                InputStream inputStream = it.getInputStream();
                                fileParams.add(new FileParam(fieldName, fileName, fileSize, contextType, inputStream));
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Param(formParams, fileParams);
    }

    public static void uploadFile(String basePath, FileParam fileParam) {
        if (fileParam != null) {
            try {
                String filePath = basePath + fileParam.getFileName();
                FileUtil.createFile(filePath);
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(inputStream, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LOGGER.error("upload file failure");
            }
        }
    }

    public static void uploadFile(String basePath, List<FileParam> fileParams) {
        if (CollectionUtil.isNotEmpty(fileParams)) {
            for (FileParam fileParam : fileParams) {
                uploadFile(basePath, fileParam);
            }
        }

    }

}
