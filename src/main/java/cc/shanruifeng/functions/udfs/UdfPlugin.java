package cc.shanruifeng.functions.udfs;

import com.facebook.presto.spi.Plugin;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

import cc.shanruifeng.functions.udfs.scalar.IsNullFunctions;
import cc.shanruifeng.functions.udfs.scalar.array.ArrayUnionFunction;
import cc.shanruifeng.functions.udfs.scalar.array.ArrayValueCountFunction;

/**
 * @author ruifeng.shan
 * @date 2016-07-06
 * @time 18:39
 */
public class UdfPlugin implements Plugin {

  @Override
  public Set<Class<?>> getFunctions() {

    return ImmutableSet.<Class<?>>builder()
        //.add(JsonArrayExtractFunction.class)
        .add(IsNullFunctions.class)
        .add(ArrayUnionFunction.class)
        .add(ArrayValueCountFunction.class)
        .build();
  }

}
