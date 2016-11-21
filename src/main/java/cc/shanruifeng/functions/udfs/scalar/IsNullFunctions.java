package cc.shanruifeng.functions.udfs.scalar;


import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.function.Description;
import com.facebook.presto.spi.function.ScalarFunction;
import com.facebook.presto.spi.function.SqlNullable;
import com.facebook.presto.spi.function.SqlType;
import com.facebook.presto.spi.function.TypeParameter;
import com.facebook.presto.spi.type.StandardTypes;

import io.airlift.slice.Slice;

/**
 * @author ruifeng.shan
 * @date 2016-07-12
 * @time 15:39
 */
@ScalarFunction("is_null")
@Description("Returns TRUE if the argument is NULL")
public class IsNullFunctions {
    @TypeParameter("T")
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean isNullSlice(@SqlNullable @SqlType("T") Slice value) {
        return (value == null);
    }

    @TypeParameter("T")
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean isNullLong(@SqlNullable @SqlType("T") Long value) {
        return (value == null);
    }

    @TypeParameter("T")
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean isNullDouble(@SqlNullable @SqlType("T") Double value) {
        return (value == null);
    }

    @TypeParameter("T")
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean isNullBoolean(@SqlNullable @SqlType("T") Boolean value) {
        return (value == null);
    }

    @TypeParameter("T")
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean isNullBlock(@SqlNullable @SqlType("T") Block value) {
        return (value == null);
    }
}
