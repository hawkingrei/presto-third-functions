package cc.shanruifeng.functions.udfs;

import com.facebook.presto.spi.Plugin;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

import cc.shanruifeng.functions.udfs.scalar.IsNullFunctions;
import cc.shanruifeng.functions.udfs.scalar.array.ArrayUnionFunction;
import cc.shanruifeng.functions.udfs.scalar.array.ArrayValueCountFunction;
import cc.shanruifeng.functions.udfs.scalar.date.ChinaTypeOfDayFunction;
import cc.shanruifeng.functions.udfs.scalar.date.DayOfWeekFunctions;
import cc.shanruifeng.functions.udfs.scalar.date.ZodiacSignFunctions;
import cc.shanruifeng.functions.udfs.scalar.geographic.GeographicFunctions;
import cc.shanruifeng.functions.udfs.scalar.json.JsonArrayExtractFunction;
import cc.shanruifeng.functions.udfs.scalar.map.MapValueCountFunction;
import cc.shanruifeng.functions.udfs.scalar.string.ChinaIdCardFunctions;
import cc.shanruifeng.functions.udfs.scalar.string.ChineseToPinYinFunctions;
import cc.shanruifeng.functions.udfs.scalar.string.HashFunctions;

/**
 * @author ruifeng.shan
 * @date 2016-07-06
 * @time 18:39
 */
public class UdfPlugin implements Plugin {

    @Override
    public Set<Class<?>> getFunctions(){

        return ImmutableSet.<Class<?>>builder()
            .add(JsonArrayExtractFunction.class)
            .add(HashFunctions.class)
            .add(IsNullFunctions.class)
            .add(MapValueCountFunction.class)
            .add(ArrayUnionFunction.class)
            .add(ArrayValueCountFunction.class)
            .add(ChinaTypeOfDayFunction.class)
            .add(DayOfWeekFunctions.class)
            .add(ZodiacSignFunctions.class)
            .add(GeographicFunctions.class)
            .add(ChinaIdCardFunctions.class)
            .add(ChineseToPinYinFunctions.class)
            .build();
    }

}
