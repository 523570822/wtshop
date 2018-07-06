package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.upload.UploadFile;
import com.wtshop.FileType;
import com.wtshop.Message;
import com.wtshop.service.FileService;
import com.wtshop.util.ReadProper;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller - 文件
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/file")
public class FileController extends BaseController {

	private FileService fileService = enhance(FileService.class);

	/**
	 * 上传
	 */
	public void upload() {
		UploadFile file = getFile();
		FileType fileType = FileType.valueOf(getPara("fileType", "image"));
		
		Map<String, Object> data = new HashMap<String, Object>();
		if (fileType == null || file == null || file.getFile().length() <= 0) {
			data.put("message", ERROR_MESSAGE);
			data.put("state", message("admin.message.error"));
			renderJson(data);
			return;
		}
		if (!fileService.isValid(fileType, file)) {
			data.put("message", Message.warn("admin.upload.invalid"));
			data.put("state", message("admin.upload.invalid"));
			renderJson(data);
			return;
		}
		String url = fileService.upload(fileType, file, false);
		if (StringUtils.isEmpty(url)) {
			data.put("message", Message.warn("admin.upload.error"));
			data.put("state", message("admin.upload.error"));
			renderJson(data);
			return;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("state", "SUCCESS");
		String fullpath =getPara("fullpath");
		if (fullpath !=null&&fullpath.equals("1")){
			data.put("url", ReadProper.getResourceValue("fileServer")+ url);
		}else {
			data.put("url", url);
		}

		renderJson(data);
	}

}