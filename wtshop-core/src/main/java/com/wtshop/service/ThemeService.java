package com.wtshop.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.jfinal.core.JFinal;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import com.wtshop.CommonAttributes;
import com.wtshop.Theme;
import com.wtshop.util.CompressUtils;

/**
 * Service - 主题
 * 
 * 
 */
public class ThemeService {

	/** ServletContext */
	private ServletContext servletContext = JFinal.me().getServletContext();

	Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
	
	private String themeTemplatePath = prop.get("theme.template_path");
	private String themeResourcePath = prop.get("theme.resource_path");

	
	/**
	 * 获取所有主题
	 * 
	 * @return 所有主题
	 */
	public List<Theme> getAll() {
		File[] files = new File(servletContext.getRealPath(themeTemplatePath)).listFiles(new FileFilter() {
			public boolean accept(File file) {
				File themeXmlFile = new File(file, "theme.xml");
				return themeXmlFile.exists() && themeXmlFile.isFile();
			}
		});
		List<Theme> themes = new ArrayList<Theme>();
		for (File file : files) {
			File themeXmlFile = new File(file, "theme.xml");
			themes.add(get(themeXmlFile));
		}
		return themes;
	}

	/**
	 * 获取主题
	 * 
	 * @param id
	 *            ID
	 * @return 主题
	 */
	public Theme get(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		File themeXmlFile = new File(servletContext.getRealPath(themeTemplatePath + "/" + id), "theme.xml");
		if (themeXmlFile.exists() && themeXmlFile.isFile()) {
			return get(themeXmlFile);
		}
		return null;
	}

	/**
	 * 上传主题
	 * 
	 * @param uploadFile
	 *            上传文件
	 * @return 是否上传成功
	 */
	public boolean upload(UploadFile uploadFile) {
		if (uploadFile == null || uploadFile.getFile().length() <= 0) {
			return false;
		}
		File tempThemeFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");
		File tempThemeDir = new File(FileUtils.getTempDirectory(), UUID.randomUUID().toString());
		try {
			//uploadFile.transferTo(tempThemeFile);
			FileUtils.copyFile(uploadFile.getFile(), tempThemeFile);
			CompressUtils.extract(tempThemeFile, tempThemeDir);
			File themeXmlFile = new File(tempThemeDir, "/template/theme.xml");
			if (themeXmlFile.exists() && themeXmlFile.isFile()) {
				Theme theme = get(themeXmlFile);
				if (theme != null && StringUtils.isNotEmpty(theme.getId())) {
					FileUtils.moveDirectory(new File(tempThemeDir, "/template"), new File(servletContext.getRealPath(themeTemplatePath), theme.getId()));
					FileUtils.moveDirectory(new File(tempThemeDir, "/resources"), new File(servletContext.getRealPath(themeResourcePath), theme.getId()));
					return true;
				}
			}
		} catch (IllegalStateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			FileUtils.deleteQuietly(tempThemeFile);
			FileUtils.deleteQuietly(tempThemeDir);
		}
		return false;
	}

	/**
	 * 获取主题
	 * 
	 * @param themeXmlFile
	 *            主题配置文件
	 * @return 主题
	 */
	private Theme get(File themeXmlFile) {
		try {
			Document document = new SAXReader().read(themeXmlFile);
			Node idNode = document.selectSingleNode("/theme/id");
			Node nameNode = document.selectSingleNode("/theme/name");
			Node versionNode = document.selectSingleNode("/theme/version");
			Node authorNode = document.selectSingleNode("/theme/author");
			Node siteUrlNode = document.selectSingleNode("/theme/siteUrl");
			Node previewNode = document.selectSingleNode("/theme/preview");

			Theme theme = new Theme();
			theme.setId(idNode != null ? idNode.getText().trim() : null);
			theme.setName(nameNode != null ? nameNode.getText().trim() : null);
			theme.setVersion(versionNode != null ? versionNode.getText().trim() : null);
			theme.setAuthor(authorNode != null ? authorNode.getText().trim() : null);
			theme.setSiteUrl(siteUrlNode != null ? siteUrlNode.getText().trim() : null);
			theme.setPreview(previewNode != null ? previewNode.getText().trim() : null);
			return theme;
		} catch (DocumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}