package cc.shanruifeng.functions.udfs.scalar.array;


import com.facebook.presto.spi.PrestoException;
import com.facebook.presto.spi.StandardErrorCode;
import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.function.Description;
import com.facebook.presto.spi.function.OperatorDependency;
import com.facebook.presto.spi.function.ScalarFunction;
import com.facebook.presto.spi.function.SqlNullable;
import com.facebook.presto.spi.function.SqlType;
import com.facebook.presto.spi.function.TypeParameter;
import com.facebook.presto.spi.type.StandardTypes;
import com.facebook.presto.spi.type.Type;
import com.google.common.base.Throwables;

import io.airlift.slice.Slice;

import java.lang.invoke.MethodHandle;

import static com.facebook.presto.spi.function.OperatorType.EQUAL;


/**
 * @author ruifeng.shan
 * @date 2016-07-21
 * @time 11:05
 */
@ScalarFunction("value_count")
@Description("count numbers if value equals given value.")
public class ArrayValueCountFunction {
    @TypeParameter("T")
    @SqlType(StandardTypes.INTEGER)
    public static long valueCount(@TypeParameter("T") Type elementType,
                                  @OperatorDependency(operator = EQUAL, returnType = StandardTypes.BOOLEAN, argumentTypes = {"T", "T"}) MethodHandle equals,
                                  @SqlType("array(T)") Block arrayBlock,
                                  @SqlNullable @SqlType("T") Block value) {
        int count = 0;
        if (arrayBlock.getPositionCount() > 0) {
            for (int i = 0; i < arrayBlock.getPositionCount(); i++) {
                if (value == null || arrayBlock.isNull(i)) {
                    if (value == null && arrayBlock.isNull(i)) {
                        count++;
                    }
                    continue;
                }
                try {
                    if ((boolean) equals.invokeExact((Block) elementType.getObject(arrayBlock, i), value)) {
                        count++;
                    }
                } catch (Throwable t) {
                    Throwables.propagateIfInstanceOf(t, Error.class);
                    Throwables.propagateIfInstanceOf(t, PrestoException.class);

                    throw new PrestoException(StandardErrorCode.FUNCTION_IMPLEMENTATION_ERROR, t);
                }
            }
        }

        return count;
    }

    @TypeParameter("T")
    @SqlType(StandardTypes.INTEGER)
    public static long valueCount(@TypeParameter("T") Type elementType,
                                  @OperatorDependency(operator = EQUAL, returnType = StandardTypes.BOOLEAN, argumentTypes = {"T", "T"}) MethodHandle equals,
                                  @SqlType("array(T)") Block arrayBlock,
        @SqlNullable @SqlType("T") Slice value) {
        int count = 0;
        if (arrayBlock.getPositionCount() > 0) {
            for (int i = 0; i < arrayBlock.getPositionCount(); i++) {
                if (value == null || arrayBlock.isNull(i)) {
                    if (value == null && arrayBlock.isNull(i)) {
                        count++;
                    }
                    continue;
                }
                try {
                    if ((boolean) equals.invokeExact(elementType.getSlice(arrayBlock, i), value)) {
                        count++;
                    }
                } catch (Throwable t) {
                    Throwables.propagateIfInstanceOf(t, Error.class);
                    Throwables.propagateIfInstanceOf(t, PrestoException.class);

                    throw new PrestoException(StandardErrorCode.FUNCTION_IMPLEMENTATION_ERROR, t);
                }
            }
        }

        return count;
    }

    @TypeParameter("T")
    @SqlType(StandardTypes.INTEGER)
    public static long valueCount(@TypeParameter("T") Type elementType,
                                  @OperatorDependency(operator = EQUAL, returnType = StandardTypes.BOOLEAN, argumentTypes = {"T", "T"}) MethodHandle equals,
                                  @SqlType("array(T)") Block arrayBlock,
                                  @SqlNullable @SqlType("T") Long value) {
        int count = 0;
        if (arrayBlock.getPositionCount() > 0) {
            for (int i = 0; i < arrayBlock.getPositionCount(); i++) {
                if (value == null || arrayBlock.isNull(i)) {
                    if (value == null && arrayBlock.isNull(i)) {
                        count++;
                    }
                    continue;
                }
                try {
                    if ((boolean) equals.invokeExact(elementType.getLong(arrayBlock, i), value.longValue())) {
                        count++;
                    }
                } catch (Throwable t) {
                    Throwables.propagateIfInstanceOf(t, Error.class);
                    Throwables.propagateIfInstanceOf(t, PrestoException.class);

                    throw new PrestoException(StandardErrorCode.FUNCTION_IMPLEMENTATION_ERROR, t);
                }
            }
        }

        return count;
    }

    @TypeParameter("T")
    @SqlType(StandardTypes.INTEGER)
    public static long valueCount(@TypeParameter("T") Type elementType,
                                  @OperatorDependency(operator = EQUAL, returnType = StandardTypes.BOOLEAN, argumentTypes = {"T", "T"}) MethodHandle equals,
                                  @SqlType("array(T)") Block arrayBlock,
                                  @SqlNullable @SqlType("T") Boolean value) {
        int count = 0;
        if (arrayBlock.getPositionCount() > 0) {
            for (int i = 0; i < arrayBlock.getPositionCount(); i++) {
                if (value == null || arrayBlock.isNull(i)) {
                    if (value == null && arrayBlock.isNull(i)) {
                        count++;
                    }
                    continue;
                }
                try {
                    if ((boolean) equals.invokeExact(elementType.getBoolean(arrayBlock, i), value.booleanValue())) {
                        count++;
                    }
                } catch (Throwable t) {
                    Throwables.propagateIfInstanceOf(t, Error.class);
                    Throwables.propagateIfInstanceOf(t, PrestoException.class);

                    throw new PrestoException(StandardErrorCode.FUNCTION_IMPLEMENTATION_ERROR, t);
                }
            }
        }

        return count;
    }

    @TypeParameter("T")
    @SqlType(StandardTypes.INTEGER)
    public static long valueCount(@TypeParameter("T") Type elementType,
                                  @OperatorDependency(operator = EQUAL, returnType = StandardTypes.BOOLEAN, argumentTypes = {"T", "T"}) MethodHandle equals,
                                  @SqlType("array(T)") Block arrayBlock,
                                  @SqlNullable @SqlType("T") Double value) {
        int count = 0;
        if (arrayBlock.getPositionCount() > 0) {
            for (int i = 0; i < arrayBlock.getPositionCount(); i++) {
                if (value == null || arrayBlock.isNull(i)) {
                    if (value == null && arrayBlock.isNull(i)) {
                        count++;
                    }
                    continue;
                }
                try {
                    if ((boolean) equals.invokeExact(elementType.getDouble(arrayBlock, i), value.doubleValue())) {
                        count++;
                    }
                } catch (Throwable t) {
                    Throwables.propagateIfInstanceOf(t, Error.class);
                    Throwables.propagateIfInstanceOf(t, PrestoException.class);

                    throw new PrestoException(StandardErrorCode.FUNCTION_IMPLEMENTATION_ERROR, t);
                }
            }
        }

        return count;
    }
}
