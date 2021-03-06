package com.wtshop.model;

import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;

/**
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
 */
public class _WtShopGenerator {
	
	public static DataSource getDataSource() {
//		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/rxm_shop_db?useUnicode=true&characterEncoding=UTF-8";
//		String user = "test";
//		String password = "test";
	/*	String jdbcUrl = "jdbc:mysql://59.110.18.65:3306/rxm_shop_db?useUnicode=true&characterEncoding=UTF-8";
		String user = "rxm_db";
		String password = "2x1CpGT2C5URxfSE";*/
	String jdbcUrl = "jdbc:mysql://114.116.111.212:3306/rxm_shop_db?useUnicode=true&characterEncoding=UTF-8";
		String user = "root";
		String password = "root";
		DruidPlugin druidPlugin = new DruidPlugin(jdbcUrl, user, password);
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}
	
	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "com.wtshop.model.base";
		// base model 文件保存路径
		String baseModelOutputDir = "D:\\work\\svnWork\\shop_20171218\\wtshop-model\\src\\main\\java\\com\\wtshop\\model\\base";
		
		
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "com.wtshop.model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";
		
		// 创建生成器
		Generator gernerator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		// 添加不需要生成的表名
		gernerator.addExcludedTable("order_coupon");
		// 设置是否在 Model 中生成 dao 对象
		gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		gernerator.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		//gernerator.setRemovedTableNamePrefixes("t_");
		// 生成
		gernerator.generate();
	}
}




