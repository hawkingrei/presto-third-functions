package cc.shanruifeng.functions.udfs;

import com.facebook.presto.spi.Plugin;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

import cc.shanruifeng.functions.udfs.scalar.json.JsonArrayExtractFunction;

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
      //      .add(HashFunctions.class)
//          .add(IsNullFunctions.class)
 //           .add(MapValueCountFunction.class)
 //           .add(ArrayUnionFunction.class)
 //           .add(ArrayValueCountFunction.class)
  //          .add(ChinaTypeOfDayFunction.class)
    //        .add(DayOfWeekFunctions.class)
 //           .add(ZodiacSignFunctions.class)
 //           .add(GeographicFunctions.class)
 //           .add(ChinaIdCardFunctions.class)
 //           .add(ChineseToPinYinFunctions.class)
            .build();
    }

}
