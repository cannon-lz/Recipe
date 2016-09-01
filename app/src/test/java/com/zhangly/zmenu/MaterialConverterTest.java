package com.zhangly.zmenu;

import com.zhangly.zmenu.api.bean.Menus;
import com.zhangly.zmenu.module.menudetails.MaterialConverter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangly on 16/8/19.
 */

public class MaterialConverterTest {

    @Test
    public void test_converter() {
        final Menus.Menu menu = new Menus.Menu();
        menu.ingredients = "豆腐,400g;香菇,10g;黑木耳,10g";
        menu.burden = "食油,1勺;盐,适量;老抽,适量;生抽,1勺;孜然粉,1勺;青椒,适量;洋葱,适量;红椒,适量";
        final List<MaterialConverter.MaterialPair> convert = MaterialConverter.convert(menu);
        final MaterialConverter.MaterialPair[] result = convert.toArray(new MaterialConverter.MaterialPair[convert.size()]);
        final MaterialConverter.MaterialPair[] complete = new MaterialConverter.MaterialPair[]{
                new MaterialConverter.MaterialPair("豆腐", "400g"),
                new MaterialConverter.MaterialPair("香菇", "10g"),
                new MaterialConverter.MaterialPair("黑木耳", "10g"),
                new MaterialConverter.MaterialPair("食油", "1勺"),
                new MaterialConverter.MaterialPair("盐", "适量"),
                new MaterialConverter.MaterialPair("老抽", "适量"),
                new MaterialConverter.MaterialPair("生抽", "1勺"),
                new MaterialConverter.MaterialPair("孜然粉", "1勺"),
                new MaterialConverter.MaterialPair("青椒", "适量"),
                new MaterialConverter.MaterialPair("洋葱", "适量"),
                new MaterialConverter.MaterialPair("红椒", "适量"),
        };
        Assert.assertEquals(result.length, complete.length);
    }
}
