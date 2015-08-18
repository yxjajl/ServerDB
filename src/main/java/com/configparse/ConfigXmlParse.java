package com.configparse;

import java.io.File;
import java.net.URL;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.commons.digester3.binder.DigesterLoader;

import com.configparse.vo.ConfigProject;

public class ConfigXmlParse {

	public static ConfigProject annotationParse(String filename) throws Exception {
		DigesterLoader digesterLoader = DigesterLoader.newLoader(new FromAnnotationsRuleModule() {
			@Override
			protected void configureRules() {
				bindRulesFrom(ConfigProject.class);
			}

		});

		Digester digester = digesterLoader.newDigester();
		try {
			ConfigProject root = digester.parse(new File(filename));
			System.out.println(root.toString());
			return root;
		} catch (Exception e) {
			// do something
			e.printStackTrace();
		} finally {
			digester.clear();
		}
		return null;
	}

	public static void annotation() throws Exception {
		DigesterLoader digesterLoader = DigesterLoader.newLoader(new FromAnnotationsRuleModule() {

			@Override
			protected void configureRules() {
				bindRulesFrom(ConfigProject.class);
			}

		});

		Digester digester = digesterLoader.newDigester();
		try {
			URL u = ConfigProject.class.getClassLoader().getResource("dbconfig.xml");
			System.out.println(u.getPath().replaceAll("%20", " "));
			ConfigProject root = digester.parse(u.openStream());
			System.out.println(root.toString());
		} catch (Exception e) {
			// do something
			e.printStackTrace();
		} finally {
			digester.clear();
		}
	}

	public static void main(String[] args) throws Exception {
		annotationParse("E:\\rick_workspace\\ServerDB\\src\\main\\resources\\dbconfig.xml");
	}

}
