package com.wtshop.plugin.localStorage;

import com.jfinal.core.JFinal;
import com.wtshop.plugin.StoragePlugin;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

/**
 * Plugin - 本地文件存储
 * 
 * 
 */
public class LocalStoragePlugin extends StoragePlugin {

	/** ServletContext */
	private ServletContext servletContext = JFinal.me().getServletContext();

	/**
	 * 设置ServletContext
	 * 
	 * @param servletContext
	 *            ServletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public String getName() {
		return "本地文件存储";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "Wangtiansoft";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.wangtiansoft.com";
	}

	@Override
	public String getInstallUrl() {
		return null;
	}

	@Override
	public String getUninstallUrl() {
		return null;
	}

	@Override
	public String getSettingUrl() {
		return "local_storage/setting.jhtml";
	}

	@Override
	public void upload(String path, File file, String contentType) {
		File destFile = new File(servletContext.getRealPath(path));
		try {
			FileUtils.moveFile(file, destFile);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void uploadReal(String path, File file, String contentType) {
		File destFile = new File(path);
		try {
			FileUtils.moveFile(file, destFile);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String getUrl(String path) {
		//Setting setting = SystemUtils.getSetting();
		//return setting.getSiteUrl() + path;
		return path;
	}

}