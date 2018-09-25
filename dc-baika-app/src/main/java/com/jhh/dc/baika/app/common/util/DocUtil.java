package com.jhh.dc.baika.app.common.util;

import io.github.yedaxia.apidocs.Docs;

/**
 * 使用JApiDocs 生成接口文档
 * @author xingmin
 */
public class DocUtil {

    public static void main(String[] args) {
        Docs.DocsConfig config = new Docs.DocsConfig();
        config.setProjectPath("D:\\IdeaProjects\\dc-loan\\dc-loan-app");
        Docs.buildHtmlDocs(config);
    }

}
