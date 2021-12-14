package com.jrasp.module.admin;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jrasp.api.Information;
import com.jrasp.api.Module;
import com.jrasp.api.Resource;
import com.jrasp.api.annotation.Command;
import com.jrasp.api.log.Log;
import com.jrasp.api.model.RestResultUtils;
import org.kohsuke.MetaInfServices;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.instrument.Instrumentation;
import java.util.Map;

@MetaInfServices(Module.class)
@Information(id = "package", version = "0.0.1", author = "jrasp")
public class PackageModule implements Module {

    @Resource
    private Instrumentation instrumentation;

    @Resource
    private Log logger;

    @Command("version")
    public void version(final Map<String, String> parameterMap, final PrintWriter writer) throws IOException {
        String className = parameterMap.get("class");
        if (instrumentation != null) {
            Class[] classList = instrumentation.getAllLoadedClasses();
            for (int i = 0; i < classList.length; i++) {
                Class clazz = classList[i];
                String name = clazz.getName();
                if (name.equals(className) || name.contains(className)) {
                    Package aPackage = clazz.getPackage();
                    writer.println(JSONObject.toJSONString(RestResultUtils.success(aPackage), SerializerFeature.PrettyFormat));
                    writer.flush();
                }
            }
        } else {
            writer.println(JSONObject.toJSONString(RestResultUtils.success("instrumentation==null"), SerializerFeature.PrettyFormat));
            writer.flush();
        }
    }
}
