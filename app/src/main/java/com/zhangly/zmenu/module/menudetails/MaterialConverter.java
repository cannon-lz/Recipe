package com.zhangly.zmenu.module.menudetails;

import com.zhangly.zmenu.api.bean.Menus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangly on 16/8/19.
 */
public class MaterialConverter {

    public static class MaterialPair {

        String key;
        String value;

        public MaterialPair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static List<MaterialPair> convert(Menus.Menu menu) {
        List<MaterialPair> materials = new ArrayList<>();
        String allMaterial = String.format("%s;%s", menu.ingredients, menu.burden);
        String[] materialPairArray = allMaterial.split(";");
        for (String value : materialPairArray) {
            final String[] keyValue = value.split(",");
            materials.add(new MaterialPair(keyValue[0], keyValue[1]));
        }
        return materials;
    }
}
